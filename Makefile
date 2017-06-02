DOCKER_IP=$(shell docker network inspect bridge | jq -r '.[0].IPAM.Config[0].Gateway')

ifeq ($(DOCKER_IP),null)
	$(error Cannot detect Docker network IP, please start Docker daemon first)
endif

HOST_NAME ?= $(shell hostname)
DOCKER_HOSTNAME?=$(shell expr "`ping -c 1 $(HOST_NAME)`" : '.*(\(.*\)).*')
DOCKER_SERVER_URL=unix:///var/run/docker.sock

run:
	DOCKER_IP=${DOCKER_IP} DOCKER_HOSTNAME=${DOCKER_HOSTNAME} DOCKER_SERVER_URL=${DOCKER_SERVER_URL} && docker-compose up

start:
	DOCKER_IP=${DOCKER_IP} DOCKER_HOSTNAME=${DOCKER_HOSTNAME} DOCKER_SERVER_URL=${DOCKER_SERVER_URL} && docker-compose up -d

build:
	docker-compose build
	cd slave-sbt && docker build -t slave-sbt .

stop:
	-docker-compose down

clean:	stop
	for dockid in $$(docker ps -a -q); do docker rm -f $$dockid; done
	for volid in $$(docker volume ls -q); do docker volume rm $$volid; done

restart: stop start

.PHONY: run build clean stop clean
