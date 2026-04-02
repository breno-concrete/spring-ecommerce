# 🔒 Guia de Segurança - Variáveis de Ambiente

## O que foi feito:

### 1. **application.yaml** - Agora usa variáveis de ambiente
```yaml
DB_HOST: localhost (default)
DB_PORT: 5332 (default)
DB_NAME: marketplace (default)
DB_USERNAME: seu_usuario
DB_PASSWORD: sua_senha_segura
```

### 2. **.env** - Arquivo local com credenciais REAIS
- ⚠️ **NÃO COMPARTILHE ESTE ARQUIVO**
- Edite com suas credenciais reais
- Será automaticamente ignorado pelo Git

### 3. **.env.example** - Template para documentação
- Use como referência
- Compartilhe este com o time
- Contém nomes das variáveis

---

## ✅ Como usar:

### Docker Compose:
```bash
docker-compose up -d
```
O arquivo `.env` será automaticamente carregado!

### Desenvolvedor Local:
```bash
# Adicione as variáveis ao seu shell/IDE:
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha
export DB_NAME=seu_database
export DB_HOST=localhost
export DB_PORT=5332
```

### IntelliJ/JetBrains IDE:
1. Vá em: `Run → Edit Configurations...`
2. Na aba "Environment", coloque:
```
DB_USERNAME=breno;DB_PASSWORD=password;DB_NAME=marketplace;DB_HOST=localhost;DB_PORT=5332
```

---

## 🛡️ Checklist de Segurança:

- ✅ Credenciais removidas do código-fonte
- ✅ Variáveis de ambiente configuradas
- ✅ `.env` será ignorado pelo Git
- ✅ `.env.example` documenta a estrutura
- ✅ `application.yaml` com valores padrão (para desenvolvimento)

---

## 📝 Atualizações feitas:

1. **application.yaml**: Agora lê variáveis com `${NOME:default}`
2. **docker-compose.yml**: Carrega arquivo `.env` automaticamente
3. **ddl-auto**: Mudado de `create-drop` para `update` (mais seguro)
4. **show-sql**: Desativado em produção (mais seguro)

---

## ⚠️ Não esqueça:

Editar o arquivo `.env` com suas credenciais REAIS antes de rodar!

