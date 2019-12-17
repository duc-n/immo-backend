
## Project Structure

* ci-vagrant
 * from immo-backend
    ./mvnw clean install
    
 * execute docker-compose up -d
 
 http://localhost:8761/
 
 docker-compose down
 docker-compose up -d
 
 You will also need to give yourself admin privileges on the cluster:
 kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value core/account)
 
 
 docker tag celeduc/microservice-docker-immo-service:0.0.1-SNAPSHOT eu.gcr.io/celeduc/microservice-docker-immo-service:0.0.1-SNAPSHOT;  
 docker push eu.gcr.io/celeduc/microservice-docker-immo-service:0.0.1-SNAPSHOT;
 
 docker tag celeduc/microservice-docker-api-gateway:0.0.1-SNAPSHOT eu.gcr.io/celeduc/microservice-docker-api-gateway:0.0.1-SNAPSHOT;  
 docker push eu.gcr.io/celeduc/microservice-docker-immo-service:0.0.1-SNAPSHOT;
  
 docker tag celeduc/microservice-docker-discovery-service:0.0.1-SNAPSHOT eu.gcr.io/celeduc/microservice-docker-discovery-service:0.0.1-SNAPSHOT;  
 docker push eu.gcr.io/celeduc/microservice-docker-discovery-service:0.0.1-SNAPSHOT;
   
 docker tag celeduc/microservice-docker-config:0.0.1-SNAPSHOT eu.gcr.io/celeduc/microservice-docker-config:0.0.1-SNAPSHOT;  
 docker push eu.gcr.io/celeduc/microservice-docker-config:0.0.1-SNAPSHOT;
 
 docker build -t celeduc/immo-front:0.0.1-SNAPSHOT .
 
  docker tag celeduc/immo-front:0.0.1-SNAPSHOT eu.gcr.io/celeduc/immo-front:0.0.1-SNAPSHOT; 
 
  docker push eu.gcr.io/celeduc/immo-front:0.0.1-SNAPSHOT;
  
    docker push eu.gcr.io/celeduc/microservice-docker-config:0.0.1-SNAPSHOT;
 
 docker push [HOSTNAME]/[PROJECT-ID]/[IMAGE]:[TAG]
 
 gcloud container images list-tags eu.gcr.io/celeduc/microservice-docker-immo-service
 
  gcloud container images list-tags eu.gcr.io/celeduc/immo-front