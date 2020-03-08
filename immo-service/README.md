
## Project Structure
mvn clean install sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=4f07d4d15d7774a6b360783d0da931e31cd6172b

https://medium.com/@amitvermaa93/jenkins-sonarqube-integration-129f5c49c4ca

https://dzone.com/articles/dockerizing-jenkins-2-setup-and-using-it-along-wit  install manually jenkins


Configuration :
Start docker compose : docker-compose up
Get ip address of SonarQube : 
get container id : docker ps => e64ab815d2c6
docker inspect --format '{{ .NetworkSettings.IPAddress }}' 0ac86b6afe78 =>172.19.0.5

In Jenkins : Install SonarQubeScanner plugin, checkstyles, 
SonarQube servers :
Namev: SonarCele
URL du serveur : http://172.19.0.5:9000