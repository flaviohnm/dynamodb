#!/bin/bash -x

LOCALSTACK_HOST=localhost
AWS_REGION=sa-east-1

create_dynamodb(){
  awslocal dynamodb create-table --table-name costumer --cli-input-json file:///docker-entrypoint-initaws.d/costumer.json
  echo "create dynamodbTable funcionario successfully"
}

echo "Criando DynamoDB"
echo "==================="
create_dynamodb