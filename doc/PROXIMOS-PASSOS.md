# 📦 Arquivos Gerados - CI/CD Automático

## Todos os Arquivos Estão Prontos

```
✅ feature-to-develop.yml        → Abre PR automático feature/* → develop
✅ develop-to-main.yml           → Abre PR automático develop → main  
✅ release-tag.yml               → Cria tag + release ao mergear main
✅ IMPLEMENTACAO-CI-CD.md         → Guia completo de uso
```

---

## 🚀 Próximos Passos (3 minutos)

### 1. Copie os workflows para seu repositório

```bash
# No seu repositório local:
mkdir -p .github/workflows

# Copie os 3 arquivos .yml para essa pasta
cp feature-to-develop.yml .github/workflows/
cp develop-to-main.yml .github/workflows/
cp release-tag.yml .github/workflows/

git add .github/workflows/
git commit -m "ci: adiciona workflows automáticos de release"
git push origin main
```

### 2. Crie a branch `develop` (se ainda não existir)

```bash
git checkout -b develop
git push -u origin develop
```

### 3. Teste com uma feature

```bash
git checkout -b feature/test-workflow
echo "# Test" >> README.md
git commit -m "feat: test workflow"
git push origin feature/test-workflow
```

Vá ao GitHub → Pull requests → verá um PR aberto automaticamente de `feature/test-workflow` para `develop`. ✨

---

## 📊 O Fluxo em Ação

```
You push feature/novo-endpoint
         ↓
🤖 feature-to-develop dispara
         ↓
📋 PR automático criado: feature/novo-endpoint → develop
         ↓
👤 Você revisa e faz merge
         ↓
develop recebe o push
         ↓
🤖 develop-to-main dispara
         ↓
📋 PR automático criado: develop → main
         ↓
👤 Você revisa e faz merge
         ↓
main recebe o push
         ↓
🤖 release-tag dispara
         ↓
🏷️ Analisa commits (Conventional Commits)
         ↓
📈 Calcula versão (feat=MINOR, fix=PATCH, BREAKING=MAJOR)
         ↓
📝 Gera CHANGELOG.md
         ↓
✅ Cria tag v0.2.0
         ↓
🎉 Publica Release no GitHub
```

---

## ⚙️ Configurações Opcionais (Recomendado)

### Proteger branches com regras

No GitHub → Settings → Branches → Add rule:

```
Branch name pattern: develop
✅ Require pull requests
✅ Require approvals (1+)
✅ Dismiss stale PR reviews
✅ Require branches to be up to date before merging

Branch name pattern: main
✅ Require pull requests
✅ Require approvals (2+)  ← mais restritivo
✅ Dismiss stale PR reviews
✅ Require status checks (CI/CD)
✅ Require branches to be up to date before merging
```

---

## 🔍 Monitorar os Workflows

### No GitHub:
1. Vá para **Actions** (aba do repositório)
2. Filtre por workflow (`feature-to-develop`, `develop-to-main`, `release-tag`)
3. Clique em um run para ver logs completos

### Dicas:
- Se algum workflow falhar, os logs mostram o erro
- O workflow não quebra o fluxo de PR — apenas não cria a tag/release

---

## 📝 Exemplo de Commit Conventional (importante!)

```bash
# ❌ Errado:
git commit -m "atualizou o endpoint"

# ✅ Certo:
git commit -m "feat: adiciona endpoint GET /customers"
git commit -m "fix: corrige validação de email"
git commit -m "chore: atualiza dependências"
git commit -m "BREAKING CHANGE: remove endpoint legado /v1/users"
```

Isso determina se é MAJOR, MINOR ou PATCH na próxima release.

---

## 🎯 Resumo do Que Vai Automatizar

| Antes | Depois |
|-------|--------|
| Manual: criar PR feature→develop | ✅ Automático |
| Manual: criar PR develop→main | ✅ Automático |
| Manual: decisão de versão | ✅ Automático (via commits) |
| Manual: criar tag | ✅ Automático |
| Manual: escrever release notes | ✅ Automático (CHANGELOG.md) |
| Manual: publicar release no GitHub | ✅ Automático |

---

## ❓ Dúvida?

Consulte `IMPLEMENTACAO-CI-CD.md` para:
- Fluxo visual completo
- Troubleshooting
- Customizações avançadas
- Checklist antes de começar

---

Tudo pronto! Agora é só dar push nos workflows e começar a usar. 🚀
