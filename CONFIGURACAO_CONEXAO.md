# 🔌 Configuração da String de Conexão MongoDB

## ⚠️ IMPORTANTE: Adicionar Credenciais

A string de conexão fornecida precisa incluir **usuário e senha**. 

### String Atual (incompleta):
```
mongodb://atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin
```

### String Completa (formato correto):
```
mongodb://USUARIO:SENHA@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin
```

## 📝 Como Configurar

### 1. Obter Credenciais do MongoDB Atlas

1. Acesse https://cloud.mongodb.com
2. Vá em **Database Access** (menu lateral)
3. Copie o **usuário** e a **senha** do seu banco de dados

### 2. Formatar a String de Conexão

Substitua `USUARIO` e `SENHA` na string:

**Exemplo:**
```
mongodb://gustavoflopes:Senai2025@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin
```

### 3. Tratar Caracteres Especiais na Senha

Se sua senha contém caracteres especiais, use **URL encoding**:

| Caractere | Código URL |
|-----------|------------|
| `@`       | `%40`      |
| `#`       | `%23`      |
| `$`       | `%24`      |
| `&`       | `%26`      |
| `+`       | `%2B`      |
| `=`       | `%3D`      |

**Exemplo:** Se sua senha é `Senha@2025`, use:
```
mongodb://gustavoflopes:Senha%402025@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin
```

## 🔧 Configurar no IntelliJ IDEA

1. **Run** → **Edit Configurations...**
2. Selecione sua configuração
3. Em **Environment variables**, adicione:
   - **Name:** `MONGODB_URI`
   - **Value:** A string completa com usuário e senha
4. Clique em **OK**

## 🖥️ Configurar no Terminal

### PowerShell:
```powershell
$env:MONGODB_URI = "mongodb://USUARIO:SENHA@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin"
```

### CMD:
```cmd
set MONGODB_URI=mongodb://USUARIO:SENHA@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin
```

### Linux/Mac:
```bash
export MONGODB_URI="mongodb://USUARIO:SENHA@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin"
```

## ✅ Verificar Configuração

Para verificar se a string está configurada corretamente, a aplicação mostrará:

```
Conectando ao MongoDB: mongodb://USUARIO:***@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin
✅ Conectado ao MongoDB com sucesso!
```

## 🔍 Informações da Conexão

- **Host:** `atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net`
- **Database:** `cursosenai`
- **SSL:** Habilitado
- **Auth Source:** `admin`

## 📌 Observações

- Esta é uma conexão SQL do MongoDB Atlas (Data Federation)
- O banco de dados padrão é `cursosenai`
- A autenticação usa o source `admin`
- SSL está habilitado por padrão


