# DRL leaderboard backend

## Local Setup
### Generate your own api token
To create an api token you need to base64 encode the following json string:
```json
{"steamId":"[YOUR STEAM ID]","xbuid":null,"playstationId":null,"epicId":null,"ticket":"","os":"win","version":"4.0.d74d.rls-win"}
```
Replace '[YOUR STEAM ID]' with your actual steam id.  

To encode it run the following command in your terminal:
```bash
echo '{"steamId":"[YOUR STEAM ID]","xbuid":null,"playstationId":null,"epicId":null,"ticket":"","os":"win","version":"4.0.d74d.rls-win"}' | base64
```
Now paste your token into the src/main/resources/application.yaml file under 'app.drl-api.token'

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
