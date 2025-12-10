# 📋 Como Obter e Configurar a String de Conexão MongoDB Atlas

## ⚠️ ERRO COMUM: "Failed looking up SRV record for '_mongodb._tcp.cluster.mongodb.net'"

Este erro ocorre quando a string de conexão contém **placeholders** (`<username>`, `<password>`, `xxxxx`) em vez dos valores reais.

## ✅ Solução Passo a Passo

### 1. Obter a String de Conexão Correta do MongoDB Atlas

1. **Acesse o MongoDB Atlas:**
   - Vá para https://cloud.mongodb.com
   - Faça login na sua conta

2. **Encontre seu Cluster:**
   - No painel, localize seu cluster (geralmente chamado `Cluster0`)
   - Clique no botão **"Connect"**

3. **Selecione o Método de Conexão:**
   - Escolha **"Connect your application"**
   - Selecione **"Java"** como driver
   - Escolha a versão mais recente (ex: 4.11 ou superior)

4. **Copie a String de Conexão:**
   - Você verá algo como:
     ```
     mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority
     ```

### 2. Substituir os Placeholders

**❌ String INCORRETA (com placeholders):**
```
mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority
```

**✅ String CORRETA (com valores reais):**
```
mongodb+srv://gustavoflopes:Senai2025@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority
```

#### O que substituir:

- **`<username>`** → Seu usuário do MongoDB Atlas
  - Encontre em: **Database Access** → Lista de usuários
  
- **`<password>`** → Sua senha
  - Se você esqueceu: **Database Access** → Clique no usuário → **Edit** → **Reset Password**
  - ⚠️ **IMPORTANTE:** Se sua senha contém caracteres especiais, use URL encoding:
    - `@` → `%40`
    - `#` → `%23`
    - `$` → `%24`
    - `&` → `%26`
    - `+` → `%2B`
    - `=` → `%3D`

- **`xxxxx`** → Código real do seu cluster
  - Está na própria string de conexão que você copiou
  - Exemplo: `abc123`, `xyz789`, etc.

### 3. Verificar Network Access

Antes de usar a conexão, certifique-se de que seu IP está liberado:

1. No MongoDB Atlas, vá em **Network Access** (menu lateral)
2. Clique em **Add IP Address**
3. Para testes locais, você pode:
   - Adicionar seu IP atual (clique em **"Add Current IP Address"**)
   - Ou adicionar `0.0.0.0/0` para permitir qualquer IP (⚠️ apenas para desenvolvimento)

### 4. Configurar no IntelliJ IDEA

1. No IntelliJ, vá em **Run** → **Edit Configurations...**
2. Selecione sua configuração de execução
3. Em **Environment variables**, adicione:
   - **Name:** `MONGODB_URI`
   - **Value:** A string completa com valores reais (sem placeholders)
   
**Exemplo:**
```
MONGODB_URI=mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority
```

4. Clique em **OK** e execute novamente

### 5. Configurar no Terminal (PowerShell)

```powershell
# Substitua pelos seus valores reais
$env:MONGODB_URI = "mongodb+srv://SEU_USUARIO:SUA_SENHA@cluster0.SEU_CODIGO.mongodb.net/?retryWrites=true&w=majority"

# Exemplo:
$env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority"
```

### 6. Verificar se Funcionou

Quando você executar a aplicação, deve ver:

```
Conectando ao MongoDB: mongodb+srv://gustavoflopes:***@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority
Inicializando conexão com MongoDB...
Conexão com MongoDB estabelecida com sucesso!
✅ Conectado ao MongoDB com sucesso!
```

## 🔍 Exemplo Completo

**String com placeholders (❌ NÃO FUNCIONA):**
```
mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority
```

**String correta (✅ FUNCIONA):**
```
mongodb+srv://gustavoflopes:Senai2025@cluster0.abc123def.mongodb.net/?retryWrites=true&w=majority
```

**String com senha especial (✅ FUNCIONA):**
```
# Se a senha é "Senai@2025", use %40 no lugar de @:
mongodb+srv://gustavoflopes:Senai%402025@cluster0.abc123def.mongodb.net/?retryWrites=true&w=majority
```

## ⚡ Checklist Rápido

- [ ] Copiou a string de conexão do MongoDB Atlas
- [ ] Substituiu `<username>` pelo usuário real
- [ ] Substituiu `<password>` pela senha real (com URL encoding se necessário)
- [ ] Substituiu `xxxxx` pelo código real do cluster
- [ ] Verificou que o IP está liberado no Network Access
- [ ] Configurou no IntelliJ IDEA ou terminal
- [ ] Testou a conexão e funcionou

## 🆘 Ainda com Problemas?

Consulte o arquivo `TROUBLESHOOTING.md` para mais soluções de problemas comuns.


