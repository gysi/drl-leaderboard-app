FROM eclipse-temurin:21-jdk-alpine as backend-builder
WORKDIR /app
COPY backend/.mvn .mvn
COPY backend/mvnw backend/pom.xml ./
RUN ./mvnw dependency:resolve dependency:resolve-plugins

# This we want to do before because proto will download a binary on this phase and we want to cache that behavior
COPY backend/src/main/proto ./src/main/proto
RUN ./mvnw protobuf:compile

COPY backend/src/main ./src/main
COPY backend/src/test ./src/test
RUN ./mvnw package


#FROM alpine:3 as native-image-backend-builder
#WORKDIR /app
#RUN wget https://download.bell-sw.com/vm/22.3.0/bellsoft-liberica-vm-core-openjdk17.0.5+8-22.3.0+2-linux-x64-musl.apk
#RUN wget -P /etc/apk/keys/ https://apk.bell-sw.com/info@bell-sw.com-5fea454e.rsa.pub
#RUN apk add ./bellsoft-liberica-vm-core-openjdk17.0.5+8-22.3.0+2-linux-x64-musl.apk
#ENV PATH="/opt/bellsoft/liberica-vm-core-22.3.0-openjdk17/bin:${PATH}"
#RUN apk add build-base zlib-dev fontconfig-dev freetype-dev
#COPY backend/.mvn .mvn
#COPY backend/mvnw backend/pom.xml ./
#RUN ./mvnw dependency:resolve
#COPY backend/src/main ./src/main
#COPY backend/src/test ./src/test
#RUN ./mvnw -Pnative native:compile


FROM node:18-alpine as frontend-builder
WORKDIR /app
COPY frontend/.yarn .yarn/
COPY frontend/.yarnrc.yml frontend/package.json frontend/yarn.lock ./
RUN yarn install
COPY frontend/public public/
COPY frontend/src src/
COPY frontend/env/ env/
COPY frontend/.eslintignore frontend/.eslintrc.cjs frontend/.npmrc \
    frontend/.pnp.cjs frontend/.pnp.loader.mjs frontend/index.html frontend/jsconfig.json \
    frontend/postcss.config.cjs frontend/quasar.config.js frontend/purgecss.config.cjs ./


FROM frontend-builder as frontend-builder-staging
WORKDIR /app
# TODO deactivate purge for now, because it removes too much
#RUN yarn quasar build && yarn purgecss --config ./purgecss.config.cjs
RUN rm env/.env.prod && mv env/.env.staging env/.env.prod
RUN DLAPP_ENV=STAGING yarn quasar build

FROM frontend-builder as frontend-builder-prod
WORKDIR /app
# TODO deactivate purge for now, because it removes too much
#RUN yarn quasar build && yarn purgecss --config ./purgecss.config.cjs
RUN DLAPP_ENV=PROD yarn quasar build

FROM python:3.11-bullseye as runner
WORKDIR /

RUN apt-get update

#Install JQ, RYSNC
RUN apt-get install -y jq rsync

# INSTALL TERRAFORM
ARG terraformversion_arg
RUN wget --quiet https://releases.hashicorp.com/terraform/${terraformversion_arg}/terraform_${terraformversion_arg}_linux_amd64.zip \
  && unzip terraform_${terraformversion_arg}_linux_amd64.zip \
  && mv terraform /usr/bin \
  && rm terraform_${terraformversion_arg}_linux_amd64.zip

# Adds user which should mostly match the host users id and group id
# This is needed to avoid permission issues when mounting volumes
RUN groupadd -g 1000 mygroup
RUN useradd -u 1000 -g 1000 -G root -m myuser
USER myuser
WORKDIR /home/myuser

# INSTALL ANSIBLE
ENV PATH="/home/myuser/.local/bin:${PATH}"
ARG ansibleversion_arg
RUN python3 -m pip install --user ansible==${ansibleversion_arg} && \
    python3 -m pip install --user "ansible-lint"


# INIT TERRAFORM
COPY --chown=myuser:mygroup server/terraform terraform
RUN cd terraform && \
    terraform init && \
    chmod +x *.sh

# COPY SSH KEYS
COPY --chown=myuser:mygroup server/.ssh .ssh

ARG sshpassword_arg
# Removes password from private_key_file to avoid passwordprompt when for example using git commands or using ansible
RUN ssh-keygen -p -P $sshpassword_arg -N '' -f .ssh/id_ed25519

COPY --chown=myuser:mygroup server/ansible ansible

COPY --from=backend-builder --chown=myuser:mygroup /app/target/*.jar /home/myuser/ansible/app/backend/app.jar
#COPY --from=native-image-backend-builder --chown=myuser:mygroup /app/target/DRLLeaderboardBackend /home/myuser/ansible/app/backend/native/DRLLeaderboardBackend
COPY --from=frontend-builder-staging --chown=myuser:mygroup /app/dist/spa /home/myuser/ansible/app/frontend/staging/leaderboard
COPY --from=frontend-builder-prod --chown=myuser:mygroup /app/dist/spa /home/myuser/ansible/app/frontend/prod/leaderboard

# COPY ENTRYPOINT
COPY --chown=myuser:mygroup server/entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

ENTRYPOINT ["/home/myuser/entrypoint.sh"]
CMD ["deploy"]
