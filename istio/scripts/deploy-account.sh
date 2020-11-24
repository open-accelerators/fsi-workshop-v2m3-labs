#!/bin/bash

USERXX=$1
DELAY=$2

if [ -z "$USERXX" -o "$USERXX" = "userXX" ]
  then
    echo "Usage: Input your username like deploy-account.sh user1"
    exit;
fi

echo Your username is $USERXX
echo Deploy Account service........

oc project $USERXX-account || oc new-project $USERXX-account
oc delete deployment,dc,bc,build,svc,route,pod,is --all

echo "Waiting 30 seconds to finialize deletion of resources..."
sleep 30

sed -i "s/userXX/${USERXX}/g" $CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/account/src/main/resources/application-default.properties
mvn clean package spring-boot:repackage -DskipTests -f $CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/account

oc new-app -e POSTGRESQL_USER=account \
             -e POSTGRESQL_PASSWORD=mysecretpassword \
             -e POSTGRESQL_DATABASE=account \
             openshift/postgresql:10 \
             --name=account-database

oc new-build registry.access.redhat.com/ubi8/openjdk-11:1.3 --binary --name=account-springboot -l app=account-springboot

if [ ! -z $DELAY ]
  then 
    echo Delay is $DELAY
    sleep $DELAY
fi

oc start-build account-springboot --from-file=target/account-1.0.0-SNAPSHOT.jar --follow
oc new-app account-springboot --as-deployment-config -e JAVA_OPTS_APPEND='-Dspring.profiles.active=openshift'

oc label dc/catalog-database app.openshift.io/runtime=postgresql --overwrite && \
oc label dc/catalog-springboot app.openshift.io/runtime=spring --overwrite && \
oc label dc/catalog-springboot app.kubernetes.io/part-of=catalog --overwrite && \
oc label dc/catalog-database app.kubernetes.io/part-of=catalog --overwrite && \
oc annotate dc/catalog-springboot app.openshift.io/connects-to=catalog-database --overwrite && \
oc annotate dc/catalog-springboot app.openshift.io/vcs-uri=https://github.com/RedHat-Middleware-Workshops/cloud-native-workshop-v2m3-labs.git --overwrite && \
oc annotate dc/catalog-springboot app.openshift.io/vcs-ref=ocp-4.5 --overwrite
