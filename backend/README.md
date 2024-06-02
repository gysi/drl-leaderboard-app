# DRL leaderboard backend
**Tech/Framework/Libs:**
* Java 17
* Spring Boot
* Spring Data JPA
* Spring Web
* Hibernate
* cache2k
* Spring Doc / OpenAPI
* GraalVM Native Image support
* Maven
* PostgreSQL
* Docker
* Docker Compose

## Local Setup
### Generate your own api token
Create a token (ask gysi if you don't know how) and paste it into src/main/resources/application.yaml file under 'app.drl-api.token'

### Run the application locally
#### Run the postgres docker container:
```bash
cd src/docker/local && docker-compose up
```
#### Build & Run the application
```bash
mvn clean package spring-boot:run -Dspring-boot.run.profiles=local
```
If the db is empty it will initialize the database with the latest data from the drl api on startup.

## Open API
You can access the open api documentation at http://localhost:8080/swagger-ui.html  
Or as a json file at http://localhost:8080/v3/api-docs

## GraalVM Native Image META-INF configuration creation
Install GRAALVM using sdkman.
use 23.1.2.r21-nik or graalvm (sdk use)
Make sure to have native-image installed (gu install native-image) only need when using graalvm not liberica
```bash
/home/gregord/.sdkman/candidates/java/22.3.r17-grl/bin/gu install native-image 
```
Build jar file, go to backend folder and run:
```bash
java -Dspring.profiles.active=local -agentlib:native-image-agent=config-output-dir=META-INF/native-image -jar target/DRLLeaderboardBackend-1.4.0.jar
```

Now test all features of the app as best as you can, by clicking through the app.

After that you should see the generated META-INF config in backend/META-INF/native-image
You can copy that to the resources folder
I only copied jni-config and serialization-config to the resources folder and it worked fine.
If I copied all the files it didn't work. So for now I only copied those two files and use MyCustomRuntimeHints for the rest.
