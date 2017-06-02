FROM jenkins:latest

USER root

# Basic OS packages
RUN apt-get update && apt-get install -y \
      apt-utils \
      python-dev \
      python-pip \
      python-yaml \
      python-jenkins \
      autoconf \
      automake \
      gettext-base \
      zip && \
      rm -rf /var/lib/apt/lists/* 

RUN apt-get install -y openjdk-8-jdk && \
    update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y apt-transport-https

# Install Docker and allow Jenkins to use it
RUN apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D && \
    echo "deb https://apt.dockerproject.org/repo debian-jessie main" > /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-cache policy docker-engine && \
    apt-get install -y docker-engine=1.12.1-0~jessie && \
    usermod -g staff jenkins

# Version definitions (can be overridden by CLI arguments)
ARG JENKINS_URL=http://repo.jenkins-ci.org/public
ARG JENKINS_WAR_VER=2.58
ARG JENKINS_WAR_SHA=37a85821c5026396177a2b2487f14f9548034478
ARG JJB_VERSION=1.6.1

# Set Jenkins ENV
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false -Dhudson.model.ParametersAction.keepUndefinedParameters=true"
ENV JENKINS_REF="/usr/share/jenkins/ref/"
ENV WAIT_FOR_SERVICE_URL=""
ENV WAIT_FOR_SERVICE_TIMEOUT="300"
ENV BINTRAY_URL=https://dl.bintray.com/lucamilanesio
#ENV JENKINS_UC_DOWNLOAD=http://ftp-chi.osuosl.org/pub/jenkins

# Jenkins upgrade

# could use ADD but it wouldn't check the SHA1 of the downloaded war
RUN curl -fsSL ${JENKINS_URL}/org/jenkins-ci/main/jenkins-war/${JENKINS_WAR_VER}/jenkins-war-${JENKINS_WAR_VER}.war -o /usr/share/jenkins/jenkins.war \
  && echo "$JENKINS_WAR_SHA  /usr/share/jenkins/jenkins.war" | \
  sha1sum -c -

# Default Jenkins and Jobs configuration
COPY config.xml $JENKINS_REF/config.xml.template
COPY jenkins.model.JenkinsLocationConfiguration.xml $JENKINS_REF/jenkins.model.JenkinsLocationConfiguration.xml.template
COPY jobs $JENKINS_REF/jobs/
RUN chown -R jenkins:jenkins $JENKINS_REF
COPY scriptApproval.xml $JENKINS_REF

# Load Jenkins plugins
COPY plugins.txt $JENKINS_REF
RUN cat $JENKINS_REF/plugins.txt | xargs install-plugins.sh

# Snapshot build of the Jenkins Docker Plugin (includes unix:/// sockets fix)
ADD $BINTRAY_URL/jenkins-snapshots/com/nirima/docker-plugin/0.16.1-54-gc5c5be1/docker-plugin-0.16.1-54-gc5c5be1.hpi $JENKINS_REF/plugins/docker-plugin.jpi
RUN chown -R jenkins:jenkins $JENKINS_REF

# Initialise Jenkins SSH Keys and Git Config
RUN mkdir $JENKINS_REF/.ssh && \
    chown -R jenkins:jenkins $JENKINS_REF/.ssh && \
    chmod -R 700 $JENKINS_REF/.ssh
COPY id_rsa* $JENKINS_REF/.ssh/
COPY gitconfig $JENKINS_REF/.gitconfig
RUN chown jenkins:jenkins $JENKINS_REF/.ssh/id_rsa* && \
    chmod 600 $JENKINS_REF/.ssh/id_rsa

# Disable Jenkins SetUp Wizard
RUN echo "2.0" > $JENKINS_REF/jenkins.install.UpgradeWizard.state && \
    echo "2.0" > $JENKINS_REF/upgraded && \
    echo "2.0" > $JENKINS_REF/.last_exec_version

# Configure Jenkins start-up scripts
COPY run-jenkins.sh /usr/local/bin/run-jenkins.sh
COPY jenkins.sh /usr/local/bin/jenkins.sh
COPY config.sh /usr/local/bin/
COPY wait-for-it.sh /usr/local/bin/
COPY init-credentials.groovy $JENKINS_REF/init.groovy.d/

USER jenkins


