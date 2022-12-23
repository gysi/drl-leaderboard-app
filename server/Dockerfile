FROM python:3.11-bullseye
WORKDIR /

RUN apt-get update

#Install JQ
RUN apt-get install -y jq

# INSTALL ANSIBLE
#RUN apt-get install -y curl
#RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
#RUN python get-pip.py --user

# INSTALL TERRAFORM
ARG terraformversion_arg
RUN wget --quiet https://releases.hashicorp.com/terraform/${terraformversion_arg}/terraform_${terraformversion_arg}_linux_amd64.zip \
  && unzip terraform_${terraformversion_arg}_linux_amd64.zip \
  && mv terraform /usr/bin \
  && rm terraform_${terraformversion_arg}_linux_amd64.zip

# Adds user which should mostly match the host users id and group id
# This is needed to avoid permission issues when mounting volumes
RUN groupadd -g 1000 mygroup
RUN useradd -u 1000 -g 1000 -G root myuser
USER myuser

# INIT TERRAFORM
COPY --chown=myuser:mygroup terraform terraform
RUN cd terraform && \
    terraform init && \
    chmod +x *.sh

# COPY SSH KEYS
COPY --chown=myuser:mygroup .ssh .ssh

ARG sshpassword_arg
# Removes password from private_key_file to avoid passwordprompt when for example using git commands or using ansible
RUN ssh-keygen -p -P $sshpassword_arg -N '' -f .ssh/id_ed25519

# COPY ENTRYPOINT
COPY --chown=myuser:mygroup entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
CMD ["deploy"]
