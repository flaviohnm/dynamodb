#!/bin/bash -x

LOCALSTACK_HOST=localhost
AWS_REGION=sa-east-1

create_dynamodb(){
  awslocal dynamodb create-table --table-name costumer --cli-input-json file:///docker-entrypoint-initaws.d/table/costumerTable.json
  echo "create dynamodbTable costumer successfully"
}

putItems_dynamodb(){
  awslocal dynamodb batch-write-item --request-items file:///docker-entrypoint-initaws.d/item/putCostumers.json
  echo "add costumers in costumerTable successfully"
}

echo "Criando DynamoDB"
echo "==================="
create_dynamodb

echo "Add Costumers in Costumer Table"
echo "==================="
putItems_dynamodb