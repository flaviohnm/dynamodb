#!/bin/bash -x

LOCALSTACK_HOST=localhost
AWS_REGION=sa-east-1

create_dynamodb(){
  awslocal dynamodb create-table --table-name customer --cli-input-json file:///docker-entrypoint-initaws.d/table/customerTable.json
  echo "create dynamodbTable customer successfully"
}

putItems_dynamodb(){
  awslocal dynamodb batch-write-item --request-items file:///docker-entrypoint-initaws.d/item/putCustomers.json
  echo " 3 costumers add in costumerTable successfully"
}

update_dynamodb(){
  awslocal dynamodb update-time-to-live --table-name customer \
    --time-to-live-specification "Enable=true, AttributeName=expiration_date"
  echo "update dynamodb with ttl attribute successfully"
}

echo "Criando DynamoDB"
echo "==================="
create_dynamodb

echo "Adding 3 Customers in Customer Table"
echo "==================="
putItems_dynamodb

echo "Adding TTL attribute in Customer Table"
echo "==================="
putItems_dynamodb