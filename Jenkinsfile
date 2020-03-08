pipeline {
 agent any
 environment {
  // This can be nexus3 or nexus2
  NEXUS_VERSION = "nexus3"
  // This can be http or https
  NEXUS_PROTOCOL = "http"
  // Where your Nexus is running. In my case:
  NEXUS_URL = "nexus:8081"
  // Repository where we will upload the artifact
  NEXUS_REPOSITORY = "maven-snapshots"
  // Jenkins credential id to authenticate to Nexus OSS (Created in Jenkins Credentials Management)
  NEXUS_CREDENTIAL_ID = "nexus-credentials"
  /*
    Windows: set the ip address of docker host. In my case 192.168.43.22.
    to obtains this address : $ docker-machine ip or
    ifconfig | grep "inet " | grep -v 127.0.0.1 => inet 192.168.43.22 netmask 0xffffff00 broadcast 192.168.43.255
    Linux: set localhost to SONARQUBE_URL
  */
  SONARQUBE_URL = "http://192.168.43.22"
  SONARQUBE_PORT = "9000"
 }
 options {
  skipDefaultCheckout()
 }
 stages {
  stage('SCM') {
   steps {
    checkout scm
   }
  }

  stage('Unit Tests') {
   when {
    anyOf { branch 'master'; branch 'dev' }
   }
   agent {
    docker {
     image 'maven:3.6.0-jdk-8-alpine'
     args '-v $HOME/.m2:/root/.m2'
     reuseNode true
    }
   }
   steps {
    sh 'mvn test'
   }
   post {
    always {
     junit '**/target/surefire-reports/**/*.xml'
    }
   }
  }
  stage('Build Package') {
   when {
    anyOf { branch 'master'; branch 'dev' }
   }
   agent {
    docker {
     image 'maven:3.6.0-jdk-8-alpine'
     args '-v $HOME/.m2:/root/.m2'
     reuseNode true
    }
   }
   steps {
    sh ' mvn package -DskipTests=true'
   }
   post {
    success {
     stash(name: 'artifact', includes: '**/target/*.jar')
     stash(name: 'pom', includes: 'pom.xml')
     // to add artifacts in jenkins pipeline tab (UI)
     archiveArtifacts '**/target/*.jar'
    }
   }
  }

stage('Sonarqube') {
  agent {
        docker {
        image 'maven:3.6.0-jdk-8-alpine'
        args '-v $HOME/.m2:/root/.m2'
        // to use the same node and workdir defined on top-level pipeline for all docker agents
        reuseNode true
        }
  }
  steps {
      withSonarQubeEnv('SonarCele') {
          sh 'mvn clean install -U -DskipTests sonar:sonar'
      }
      //timeout(time: 3, unit: 'MINUTES') {
        //  waitForQualityGate abortPipeline: true
      //}
  }
}
  stage('Deploy Artifact To Nexus') {
   when {
    anyOf { branch 'master'; branch 'dev' }
   }
   steps {
    script {
     unstash 'pom'
     unstash 'artifact'
     // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
     pom = readMavenPom file: "immo-service/pom.xml";
     // Find built artifact under target folder
     filesByGlob = findFiles(glob: "immo-service/target/*.${pom.packaging}");
     // Print some info from the artifact found
     echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
     // Extract the path from the File found
     artifactPath = filesByGlob[0].path;
     // Assign to a boolean response verifying If the artifact name exists
     artifactExists = fileExists artifactPath;
     if (artifactExists) {
      nexusArtifactUploader(
       nexusVersion: NEXUS_VERSION,
       protocol: NEXUS_PROTOCOL,
       nexusUrl: NEXUS_URL,
       groupId: pom.groupId,
       version: pom.version,
       repository: NEXUS_REPOSITORY,
       credentialsId: NEXUS_CREDENTIAL_ID,
       artifacts: [
        // Artifact generated such as .jar, .ear and .war files.
        [artifactId: pom.artifactId,
         classifier: '',
         file: artifactPath,
         type: pom.packaging
        ],
        // Lets upload the pom.xml file for additional information for Transitive dependencies
        [artifactId: pom.artifactId,
         classifier: '',
         file: "pom.xml",
         type: "pom"
        ]
       ]
      )
     } else {
      error "*** File: ${artifactPath}, could not be found";
     }
    }
   }
  }
  stage('Deploy to Staging Servers') {
   when {
    anyOf { branch 'master'; branch 'dev' }
   }
   agent {
    docker {
     image 'celeduc/microservice-docker-immo-service'
     reuseNode true
    }
   }
   steps {
    script {

     pom = readMavenPom file: "pom.xml"
     repoPath = "${pom.groupId}".replace(".", "/") + "/${pom.artifactId}"
     version = pom.version
     artifactId = pom.artifactId
     withEnv(["ANSIBLE_HOST_KEY_CHECKING=False", "APP_NAME=${artifactId}", "repoPath=${repoPath}", "version=${version}"]) {
      sh '''

        curl --silent "http://$NEXUS_URL/repository/maven-snapshots/${repoPath}/${version}/maven-metadata.xml" > tmp &&
        egrep '<value>+([0-9\\-\\.]*)' tmp > tmp2 &&
        tail -n 1 tmp2 > tmp3 &&
        tr -d "</value>[:space:]" < tmp3 > tmp4 &&
        REPO_VERSION=$(cat tmp4) &&

        export APP_SRC_URL="http://${NEXUS_URL}/repository/maven-snapshots/${repoPath}/${version}/${APP_NAME}-${REPO_VERSION}.war" &&
        ansible-playbook -v -i ./ansible_provisioning/hosts --extra-vars "host=staging" ./ansible_provisioning/playbook.yml

       '''
     }
    }
   }
  }
   stage('Deploy to Production Servers') {
   when {
    branch 'master'
   }
   agent {
    docker {
     image 'celeduc/microservice-docker-immo-service'
     reuseNode true
    }
   }
   steps {
    script {

     pom = readMavenPom file: "pom.xml"
     repoPath = "${pom.groupId}".replace(".", "/") + "/${pom.artifactId}"
     version = pom.version
     artifactId = pom.artifactId
     withEnv(["ANSIBLE_HOST_KEY_CHECKING=False", "APP_NAME=${artifactId}", "repoPath=${repoPath}", "version=${version}"]) {
      sh '''

        curl --silent "$NEXUS_URL/repository/maven-snapshots/${repoPath}/${version}/maven-metadata.xml" > tmp &&
        egrep '<value>+([0-9\\-\\.]*)' tmp > tmp2 &&
        tail -n 1 tmp2 > tmp3 &&
        tr -d "</value>[:space:]" < tmp3 > tmp4 &&
        REPO_VERSION=$(cat tmp4) &&

        export APP_SRC_URL="http://${NEXUS_URL}/repository/maven-snapshots/${repoPath}/${version}/${APP_NAME}-${REPO_VERSION}.war" &&
        ansible-playbook -v -i ./ansible_provisioning/hosts --extra-vars "host=production" ./ansible_provisioning/playbook.yml

       '''
     }
    }
   }
  }
 }
}

