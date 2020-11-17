#!/bin/bash

USERXX=$1

if [ -z $USERXX ]
  then
    echo "Usage: Input your username like cleanup.sh user1"
    exit;
fi

echo Your username is $USERXX

echo Clean up Account service........

oc project $USERXX-account

oc delete policy/auth-policy

oc patch dc/account-database --type=json -p='[{"op":"remove", "path": "/spec/template/metadata/annotations"}]'
oc patch dc/account-springboot --type=json -p='[{"op":"remove", "path": "/spec/template/metadata/annotations"}]'

echo Complete to clean up.........
