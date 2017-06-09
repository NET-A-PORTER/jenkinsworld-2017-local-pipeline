#!/bin/bash

function expand {
   cat $JENKINS_REF/$1.template | envsubst '
   $DOCKER_IP
   $DOCKER_HOSTNAME
   $GIT_SERVER_URL
   $JENKINS_URL
' > $JENKINS_REF/$1
}

expand config.xml
expand jobs/sas-pipeline/config.xml
expand jenkins.model.JenkinsLocationConfiguration.xml

cp -R $JENKINS_REF/.ssh ~/.
chmod 600 ~/.ssh/id_rsa
