#!/bin/bash

USERXX=$1
DELAY=$2

if [ -z $USERXX ]
  then
    echo "Usage: Input your username like deploy-transaction.sh user1"
    exit;
fi

echo Your username is $USERXX
echo Deploy transaction service........

oc project $USERXX-transaction

mvn clean -f $CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/transaction package -DskipTests

oc new-app -e POSTGRESQL_USER=transaction \
  -e POSTGRESQL_PASSWORD=mysecretpassword \
  -e POSTGRESQL_DATABASE=transaction openshift/postgresql:latest \
  --name=transaction-database

oc new-build registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.5 --binary --name=transaction-quarkus -l app=transaction-quarkus

if [ ! -z $DELAY ]
  then
    echo Delay is $DELAY
    sleep $DELAY
fi

oc start-build transaction-quarkus --from-file $CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/transaction/target/*-runner.jar --follow
oc new-app transaction-quarkus -e QUARKUS_PROFILE=prod