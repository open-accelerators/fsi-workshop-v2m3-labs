Deploying the transaction microservice built on Quarkus
=====


```
oc new-project banking

cat src/main/docker/Dockerfile.native | oc new-build --name transaction --strategy=docker --dockerfile -

oc start-build transaction --from-dir .

oc new-app --name=postgresql --template=postgresql-ephemeral \
    -p DATABASE_SERVICE_NAME=transaction-database -p POSTGRESQL_USER=transaction \
    -p POSTGRESQL_PASSWORD=mysecretpassword -p POSTGRESQL_DATABASE=transaction \
    -p MEMORY_LIMIT=128Mi

oc label dc transaction-database app.kubernetes.io/part-of=transaction

oc new-app transaction --name transaction

oc expose svc/transaction

oc label deployment transaction app.kubernetes.io/part-of=transaction
```