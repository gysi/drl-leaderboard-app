FROM eclipse-temurin:17.0.5_8-jdk-alpine as backend-builder
WORKDIR /app
COPY backend/.mvn .mvn
COPY backend/mvnw backend/pom.xml ./
RUN ./mvnw dependency:resolve
COPY backend/src ./src
RUN ./mvnw install

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
RUN python3 -m pip install --user ansible && \
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

COPY --from=backend-builder --chown=myuser:mygroup /app/target/*.jar /home/myuser/backend/

# COPY ENTRYPOINT
COPY --chown=myuser:mygroup server/entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

ENTRYPOINT ["/home/myuser/entrypoint.sh"]
CMD ["deploy"]
