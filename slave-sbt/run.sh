#!/bin/bash

export DH_GID=$(ls -l /var/run/docker.sock | awk '{print $4}')
groupadd -g $DH_GID dockerhost
usermod -a -G $DH_GID jenkins

set-jenkins-profile.sh

/usr/sbin/sshd -e -D
