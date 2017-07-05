FROM jenkins-pipeline:latest

USER root

# Default Jenkins and Jobs configuration
COPY config.xml $JENKINS_REF/config.xml.template
COPY jenkins.model.JenkinsLocationConfiguration.xml $JENKINS_REF/jenkins.model.JenkinsLocationConfiguration.xml.template
COPY org.codefirst.SimpleThemeDecorator.xml $JENKINS_REF/org.codefirst.SimpleThemeDecorator.xml

COPY jobs $JENKINS_REF/jobs/
RUN chown -R jenkins:jenkins $JENKINS_REF
COPY scriptApproval.xml $JENKINS_REF

# Load Jenkins plugins
COPY plugins.txt $JENKINS_REF
RUN cat $JENKINS_REF/plugins.txt | xargs install-plugins.sh

# Load custom layout
COPY layout $JENKINS_REF/userContent/layout

RUN chown -R jenkins:jenkins $JENKINS_REF

# Initialise Jenkins SSH Keys and Git Config
RUN mkdir $JENKINS_REF/.ssh && \
    chown -R jenkins:jenkins $JENKINS_REF/.ssh && \
    chmod -R 700 $JENKINS_REF/.ssh
COPY id_rsa* $JENKINS_REF/.ssh/
COPY gitconfig $JENKINS_REF/.gitconfig
RUN chown jenkins:jenkins $JENKINS_REF/.ssh/id_rsa* && \
    chmod 600 $JENKINS_REF/.ssh/id_rsa

USER jenkins


