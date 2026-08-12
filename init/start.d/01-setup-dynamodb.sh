#!/bin/bash -x

# 1. Definição do comando AWS com o endpoint do Floci
AWS_CMD="aws --endpoint-url=http://localhost:4566 --region sa-east-1"

# 2. Configuração de diretório e parâmetros padrão
FOLDER_PATH=${FOLDER_PATH:-"/etc/files/database"}
TABLE_NAME=${1:-"customers"}
CREATE_FILE=${2:-"${FOLDER_PATH}/customerTable.json"}
PUT_FILE=${3:-"${FOLDER_PATH}/putCustomers.json"}

# ------------------------------------------------------------------------------
# Funções
# ------------------------------------------------------------------------------

create_dynamodb(){
  local table_name=$1
  local json_file=$2

  echo "🛠️ Criando tabela '${table_name}' a partir de: ${json_file}..."
  ${AWS_CMD} dynamodb create-table \
    --cli-input-json "file://${json_file}"

  echo "⏳ Aguardando a tabela '${table_name}' ficar pronta (status ACTIVE)..."
  ${AWS_CMD} dynamodb wait table-exists --table-name "${table_name}"

  echo "✅ Tabela '${table_name}' criada e ativa com sucesso!"
}

putItems_dynamodb(){
  local json_file=$1

  echo "📦 Inserindo itens na tabela a partir de: ${json_file}..."
  ${AWS_CMD} dynamodb batch-write-item \
    --request-items "file://${json_file}"
  
  echo "✅ Carga de dados finalizada com sucesso!"
}

update_ttl_dynamodb(){
  local table_name=$1
  local ttl_attribute=${2:-"expiration_date"}

  echo "⚙️ Configurando TTL no atributo '${ttl_attribute}' para a tabela '${table_name}'..."
  ${AWS_CMD} dynamodb update-time-to-live \
    --table-name "${table_name}" \
    --time-to-live-specification "Enabled=true, AttributeName=${ttl_attribute}"
  
  echo "✅ TTL configurado com sucesso!"
}

# ------------------------------------------------------------------------------
# Execução Principal
# ------------------------------------------------------------------------------

create_dynamodb "${TABLE_NAME}" "${CREATE_FILE}"

putItems_dynamodb "${PUT_FILE}"

update_ttl_dynamodb "${TABLE_NAME}" "expiration_date"