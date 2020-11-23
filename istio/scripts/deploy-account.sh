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

oc delete dc,bc,build,svc,route,pod,is --all

echo "Waiting 30 seconds to finialize deletion of resources..."
sleep 30

rm -rf /projects/fsi-workshop-v2m3-labs/account/src/main/resources/application-default.properties
cp /projects/fsi-workshop-v2m3-labs/istio/scripts/application-default.properties /projects/fsi-workshop-v2m3-labs/account/src/main/resources/
sed -i "s/userXX/${USERXX}/g" /projects/fsi-workshop-v2m3-labs/account/src/main/resources/application-default.properties

cd /projects/fsi-workshop-v2m3-labs/account/

oc new-app -e POSTGRESQL_USER=account \
             -e POSTGRESQL_PASSWORD=mysecretpassword \
             -e POSTGRESQL_DATABASE=account \
             openshift/postgresql:10 \
             --name=account-database

mvn clean package spring-boot:repackage -DskipTests

oc new-build registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.5 --binary --name=account-springboot -l app=account-springboot

if [ ! -z $DELAY ]
  then 
    echo Delay is $DELAY
    sleep $DELAY
fi

oc start-build account-springboot --from-file=target/account-1.0.0-SNAPSHOT.jar --follow
oc new-app account-springboot
oc expose service account-springboot

clear
echo "Done! Verify by accessing in your browser:"
echo
echo "http://$(oc get route account-springboot -o=go-template --template='{{ .spec.host }}')"
echo
