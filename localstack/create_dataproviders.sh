#!/bin/bash -x

LOCALSTACK_HOST=localhost
AWS_REGION=sa-east-1

create_dynamodb(){
  awslocal dynamodb create-table --table-name costumer --cli-input-json file:///docker-entrypoint-initaws.d/table/costumerTable.json
  echo "create dynamodbTable costumer successfully"
}

putItems_dynamodb(){
  awslocal dynamodb batch-write-item --request-items file:///docker-entrypoint-initaws.d/item/putCostumers.json
  echo " 3 costumers add in costumerTable successfully"
}

update_dynamodb(){
  awslocal dynamodb update-time-to-live --table-name costumer \
    --time-to-live-specification "Enable=true, AttributeName=expiration_date"
  echo "update dynamodb with ttl attribute successfully"
}

echo "Criando DynamoDB"
echo "==================="
create_dynamodb

echo "Adding 3 Costumers in Costumer Table"
echo "==================="
putItems_dynamodb

echo "Adding TTL attribute in Costumer Table"
echo "==================="
putItems_dynamodb