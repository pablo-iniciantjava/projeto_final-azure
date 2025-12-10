# Como Configurar MONGODB_URI no IntelliJ IDEA

## Opção 1: Configurar nas Run Configurations (Recomendado)

1. No IntelliJ IDEA, vá em **Run** → **Edit Configurations...**
2. Selecione sua configuração de execução (ou crie uma nova)
3. Na seção **Environment variables**, clique no ícone de pasta para adicionar variáveis
4. Adicione:
   - **Name**: `MONGODB_URI`
   - **Value**: `mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority`
   
   **⚠️ IMPORTANTE:** Substitua os placeholders:
   - `<username>` = seu usuário do MongoDB Atlas
   - `<password>` = sua senha (use `%40` para `@`, etc.)
   - `xxxxx` = código real do seu cluster
   
   **Exemplo:**
   - `mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority`
5. Clique em **OK** e execute novamente

## Opção 2: Configurar no Terminal Integrado

Antes de executar pelo IntelliJ, configure a variável no terminal integrado:

**PowerShell:**
```powershell
$env:MONGODB_URI = "mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority"

# Exemplo:
# $env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority"
```

**CMD:**
```cmd
set MONGODB_URI=mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority

REM Exemplo:
REM set MONGODB_URI=mongodb+srv://gustavoflopes:Senai2025%%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority
```

## Opção 3: Usar o Script executar.ps1

Execute o script `executar.ps1` que está na raiz do projeto:

```powershell
.\executar.ps1
```

Este script configura automaticamente a variável e executa a aplicação.

