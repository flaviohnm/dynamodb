# 🚀 Guia de Implementação: CI/CD Automático (feature → develop → main → Release)

## Resumo do Fluxo

```
feature/* ──[push]──> PR automático na develop
                            ↓
                         [merge]
                            ↓
develop ──[push]──> PR automático na main
                            ↓
                         [merge]
                            ↓
main ──[push]──> Análise de commits (Conventional Commits)
                      ↓
                   [auto-bump version]
                      ↓
                   [gera CHANGELOG.md]
                      ↓
                   [cria tag v1.x.x]
                      ↓
                   [publica Release]
```

---

## 📋 Passo a Passo de Implementação

### 1. Copiar os Workflows

No seu repositório, crie a pasta `../.github/workflows` (se não existir) e coloque estes 3 arquivos:

```bash
.github/workflows/
├── feature-to-develop.yml       # feature/* → develop
├── develop-to-main.yml          # develop → main
└── release-tag.yml              # main → tag + release
```

### 2. Pré-requisitos

#### a) Versão inicial no `../pom.xml`
Seu projeto já tem uma versão definida. Exemplo:
```xml
<version>0.1.0</version>
```

O workflow de release vai manter isso, mas a **tag no Git** será criada como `v0.1.0`, `v0.2.0`, etc.

#### b) Histórico de Commits (Conventional Commits)
Seus commits precisam seguir este padrão para o bump automático funcionar:

```
feat: adiciona nova funcionalidade    → MINOR (v0.1.0 → v0.2.0)
fix: corrige bug crítico              → PATCH (v0.1.0 → v0.1.1)
BREAKING CHANGE: muda API             → MAJOR (v0.1.0 → v1.0.0)
chore: atualiza deps                  → [sem bump, se usar squash]
```

Se seus commits atuais não seguem esse padrão, o workflow vai fazer um bump conservador (PATCH) na primeira vez.

---

## 🎯 Fluxo de Uso

### Cenário 1: Desenvolver uma Feature

```bash
# 1. Cria branch de feature
git checkout -b feature/novo-endpoint

# 2. Faz commits (seguindo Conventional Commits)
git commit -m "feat: adiciona endpoint GET /customers"
git commit -m "feat: valida payload no controller"

# 3. Faz push
git push origin feature/novo-endpoint
```

**O que acontece:**
- ✅ Workflow `../.github/workflows/feature-to-develop.yml` dispara
- ✅ Abre PR automático `feature/novo-endpoint → develop`
- ✅ Você revisa, aprova e faz merge na UI do GitHub

---

### Cenário 2: Prepare para Release (Merge na develop)

Quando todas as features estão prontas e mergeadas na `develop`:

```bash
# (Isso acontece via GitHub UI ao fazer merge)
# develop recebe push dos commits da feature → develop
```

**O que acontece:**
- ✅ Workflow `../.github/workflows/develop-to-main.yml` dispara
- ✅ Abre PR automático `develop → main`
- ✅ Você revisa a release, aprova e faz merge

---

### Cenário 3: Release Automática (Merge na main)

Quando o PR develop→main é mergeado:

```bash
# (Merge acontece via GitHub UI)
# main recebe push dos commits da develop
```

**O que acontece:**
- ✅ Workflow `../.github/workflows/release-tag.yml` dispara
- ✅ Analisa todos os commits desde a última tag
- ✅ Calcula versão automática (Conventional Commits):
  - `feat:` + qualquer coisa = MINOR
  - `fix:` ou `BREAKING CHANGE:` = PATCH ou MAJOR
- ✅ Gera/atualiza `CHANGELOG.md`
- ✅ Cria tag anotada (ex: `v0.2.0`)
- ✅ Faz push da tag pra o repositório
- ✅ Cria Release automática no GitHub (aba "Releases")

---

## 🔍 Monitorando os Workflows

### No GitHub UI:
1. Vá para **Actions** (aba do repositório)
2. Veja os 3 workflows rodando:
   - `Auto PR - feature -> develop`
   - `Auto PR - develop -> main`
   - `Release - Tag & GitHub Release`

### Cada workflow tem:
- ✅ Status (sucesso, falha)
- 📋 Logs detalhados
- ⏱️ Tempo de execução

---

## 📝 Arquivos Gerados

### `CHANGELOG.md` (gerado automaticamente)
Exemplo de conteúdo:
```markdown
# Changelog

## [0.2.0] - 2025-01-15

### Added
- New endpoint GET /customers (#123)

### Fixed
- Bug in customer validation (#125)

## [0.1.0] - 2025-01-01

### Added
- Initial release
```

### Tags no Git
```bash
$ git tag
v0.1.0
v0.2.0  ← criada automaticamente
```

### Releases no GitHub
Vá para **Releases** no repositório → verá `v0.2.0` listada com:
- Nome: "Release 0.2.0"
- Descrição (changelog)
- Link para download

---

## ⚠️ Pontos Importantes

### 1. Conventional Commits são obrigatórios
Se um commit não segue o padrão, o workflow ignora (trata como `chore`). Se todos forem ignorados, faz PATCH.

### 2. Não faça merge direto de `feature/*` para `main`
Sempre passe por `develop` primeiro, pois é onde a `develop → main` PR é criada.

### 3. CHANGELOG.md será commitado na `main`
O workflow faz:
```bash
git commit -m "chore: bump version to v0.2.0"
git push origin main
```

Isso é automático e seguro — apenas atualiza versão e changelog.

### 4. Se precisar corrigir uma release
Simplesmente faça merge de uma nova feature (ou hotfix), e a próxima release recalcula tudo.

---

## 🛠️ Customizações Comuns

### A. Não gerar CHANGELOG
Edite `../.github/workflows/release-tag.yml`, remova a seção:
```yaml
- name: Generate Release Notes
```

### B. Usar versão do Maven ao invés de tags Git
Adicione um step que lê `../pom.xml` antes de criar a tag (role mais abaixo para exemplo).

### C. Publicar artefato (JAR/WAR) na release
Adicione ao final de `../.github/workflows/release-tag.yml`:
```yaml
- name: Build artifact
  run: mvn clean package -DskipTests

- name: Upload JAR to release
  uses: actions/upload-release-asset@v1
  with:
    upload_url: ${{ steps.create_release.outputs.upload_url }}
    asset_path: target/dynamodb-0.1.0.jar
    asset_name: dynamodb.jar
    asset_content_type: application/java-archive
```

---

## ✅ Checklist Antes de Começar

- [ ] Versão no `../pom.xml` está definida (ex: `0.1.0`)
- [ ] Copiei os 3 `.yml` para `../.github/workflows`
- [ ] Meus commits seguem Conventional Commits (ou vou começar a partir de agora)
- [ ] Criei a branch `develop` (se ainda não existe)
- [ ] Protegi as branches `develop` e `main` com regras (opcional, mas recomendado)
- [ ] Testei o workflow localmente com um dummy commit em `feature/*`

---

## 📞 Dúvidas Comuns

**P: E se eu fizer push direto na `main` sem passar por `develop`?**  
A: O workflow de release vai rodar normalmente. Recomendo proteger a `main` com branch rules que exigem PR + aprovação.

**P: Posso fazer cherry-pick entre branches?**  
A: Pode, mas quebra o fluxo de versioning. Evite — use PRs normais.

**P: Como desfaço uma release errada?**  
A: No GitHub, delete a tag e a release. Faça um novo commit e a próxima release recalcula tudo.

**P: E se meu commit não segue Conventional Commits?**  
A: É tratado como `chore` (sem bump). Se todos forem assim, a primeira release faz PATCH.

---

Agora é só dar push e ver a magia acontecer! 🎉
