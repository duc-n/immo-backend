
## Project Structure
mvn clean install sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=4f07d4d15d7774a6b360783d0da931e31cd6172b

https://medium.com/@amitvermaa93/jenkins-sonarqube-integration-129f5c49c4ca

https://dzone.com/articles/dockerizing-jenkins-2-setup-and-using-it-along-wit  install manually jenkins

SonarQube Plugin : Checkstyle, PMD, SonarJava
In administration Tab: => Configuration => Webhooks, create a new wekhooks :
Name : jenkins
Url : http://localhost:8080/sonarqube-webhook/


Jenkins Plugin Installation : SonarQube Scanner, CheckStyles

Configuration :

SonarQube Scanner in Jenkins : In Global Config : 

SonarQube Scanner : 
Name : SonarQubeScanner

Start docker compose : docker-compose up
Get ip address of SonarQube from Mac Os : 
ifconfig | grep "inet " | grep -v 127.0.0.1 => inet 192.168.43.22 netmask 0xffffff00 broadcast 192.168.43.255

In Jenkins : Install SonarQubeScanner plugin, checkstyles, 
SonarQube servers :
Namev: SonarCele
URL du serveur : http://192.168.43.22:9000