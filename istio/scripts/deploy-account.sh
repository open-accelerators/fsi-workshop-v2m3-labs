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

sed -i "s/userXX/${USERXX}/g" $CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/account/src/main/resources/application-openshift.properties
mvn clean install spring-boot:repackage -DskipTests -f $CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/account

oc new-app --as-deployment-config -e POSTGRESQL_USER=account \
             -e POSTGRESQL_PASSWORD=mysecretpassword \
             -e POSTGRESQL_DATABASE=account \
             openshift/postgresql:10 \
             --name=account-database

oc new-build registry.access.redhat.com/ubi8/openjdk-11 --binary --name=account-springboot -l app=account-springboot

if [ ! -z $DELAY ]
  then 
    echo Delay is $DELAY
    sleep $DELAY
fi

oc start-build account-springboot --from-file=$CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/account/target/account-1.0.0-SNAPSHOT.jar --follow
oc new-app account-springboot --as-deployment-config -e JAVA_OPTS_APPEND='-Dspring.profiles.active=openshift'

oc label dc/account-database app.openshift.io/runtime=postgresql --overwrite && \
oc label dc/account-springboot app.openshift.io/runtime=spring --overwrite && \
oc label dc/account-springboot app.kubernetes.io/part-of=account --overwrite && \
oc label dc/account-database app.kubernetes.io/part-of=account --overwrite && \
oc annotate dc/account-springboot app.openshift.io/connects-to=account-database --overwrite && \
oc annotate dc/account-springboot app.openshift.io/vcs-uri=https://github.com/rmarins/fsi-workshop-v2m3-labs.git --overwrite && \
oc annotate dc/account-springboot app.openshift.io/vcs-ref=ocp-4.5 --overwrite
