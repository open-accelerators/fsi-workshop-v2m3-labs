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
oc delete deployment,dc,bc,build,svc,route,pod,is --all

echo "Waiting 30 seconds to fininalize deletion of resources..."
sleep 30

oc new-app --as-deployment-config -e POSTGRESQL_USER=transaction \
  -e POSTGRESQL_PASSWORD=mysecretpassword \
  -e POSTGRESQL_DATABASE=transaction openshift/postgresql:10 \
  --name=transaction-database

mvn clean package -DskipTests -f $CHE_PROJECTS_ROOT/fsi-workshop-v2m3-labs/transaction

## uncomment if you don't want to expose the service
# oc delete route transaction

oc label dc/transaction-database app.openshift.io/runtime=postgresql --overwrite && \
oc label dc/transaction app.kubernetes.io/part-of=transaction --overwrite && \
oc label dc/transaction-database app.kubernetes.io/part-of=transaction --overwrite && \
oc annotate dc/transaction app.openshift.io/connects-to=transaction-database --overwrite && \
oc annotate dc/transaction app.openshift.io/vcs-ref=ocp-4.5 --overwrite
