#!/bin/bash -x

FOLDER_PATH=/etc/localstack/init/ready.d

create_dynamodb(){
  awslocal dynamodb --endpoint http://localhost:4566 create-table --table-name customer --cli-input-json file://${FOLDER_PATH}/table/customerTable.json
  echo "create dynamodbTable customer successfully"
}

putItems_dynamodb(){
  awslocal dynamodb --endpoint http://localhost:4566 --region us-east-1 batch-write-item --request-items file://${FOLDER_PATH}/item/putCustomers.json
  echo " 3 costumers add in customerTable successfully"
}

#update_dynamodb(){
#  awslocal dynamodb update-time-to-live --table-name customer \
#    --time-to-live-specification "Enable=true, AttributeName=expiration_date"
#  echo "update dynamodb with ttl attribute successfully"
#}

create_dynamodb

putItems_dynamodb

#echo "Adding TTL attribute in Customer Table"
#echo "==================="
#update_dynamodb