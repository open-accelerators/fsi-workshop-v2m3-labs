#!/bin/bash

USERXX=$1
DELAY=$2

if [ -z $USERXX ]
  then
    echo "Usage: Input your username like deploy-transaction.sh user1"
    exit;
fi

echo Your username is $USERXX
echo Deploy Transaction service........

oc project $USERXX-transaction || oc new-project $USERXX-transaction

oc delete dc,bc,build,svc,route,pod,is --all

cd $CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/transaction/

mvn clean package -DskipTests

oc new-app -e POSTGRESQL_USER=transaction \
  -e POSTGRESQL_PASSWORD=mysecretpassword \
  -e POSTGRESQL_DATABASE=transaction openshift/postgresql:10 \
  --name=transaction-database
  
oc new-build registry.redhat.io/ubi8/openjdk-11 --binary --name=transaction-quarkus -l app=transaction-quarkus

if [ ! -z $DELAY ]
  then 
    echo Delay is $DELAY
    sleep $DELAY
fi

rm -rf target/binary && mkdir -p target/binary && cp -r target/*runner.jar target/binary
oc start-build transaction-quarkus --from-dir=target/binary --follow
oc new-app transaction-quarkus -e QUARKUS_PROFILE=prod
oc expose service transaction-quarkus
