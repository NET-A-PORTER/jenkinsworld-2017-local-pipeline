FROM jenkinsci/ssh-slave

USER root

# Install Docker and allow Jenkins to use it
RUN apt-get update && apt-get install -y apt-transport-https && \
    apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D && \
    echo "deb https://apt.dockerproject.org/repo debian-jessie main" > /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-cache policy docker-engine && \
    apt-get install -y docker-engine=1.12.1-0~jessie && \
    apt-get install sudo && \
    usermod -g staff jenkins

# SBT Setup
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823 && \
    apt-get update && \
    apt-get -y install --no-install-recommends sbt && \
    rm -rf /etc/apt/sources.list.d/* && \
    rm -rf /var/lib/apt/lists/*

# Trigger SBT warm-up
COPY hello /tmp/hello
RUN chmod -R a+rwx /tmp/hello && cd /tmp/hello && sudo -u jenkins sbt package test
ENV JENKINS_SLAVE_SSH_PUBKEY="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDqOEaNdIXmMep8iDZFcJfpJomc/TlMBKD/hYdb7J97sTG/j9G1JnvUt9W/qAFwkhEKibN+1e8nBufAThTaLEfZW0pbKTSMX8XsWdBy1qT/W98cJy/XPTp+i0bF+OSNs4IhiN+99vdHFTE1Y0JZWE3X+XKBhFWHUZ3dCX2FYxQXnT9+qgMc5GapO2+/3TJuugIstr8C8LT4BTbtvwdFDMiKOA7i3JrL3kqIPZRWRZG3ou26x7SW/8auZ1HUA/EzbRnyN0b+zt4oBVKqTcaJq+omXHSQJpfbVDO9caOX5FFmXImyyx/+eRXlw2/aAtZX3ABdjjSVOgRQ/BQ/5pRLfTj9 lucamilanesio@skywalker-mac-2.local"

USER root
