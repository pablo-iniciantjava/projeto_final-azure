# Como Configurar MONGODB_URI no IntelliJ IDEA

## Opção 1: Configurar nas Run Configurations (Recomendado)

1. No IntelliJ IDEA, vá em **Run** → **Edit Configurations...**
2. Selecione sua configuração de execução (ou crie uma nova)
3. Na seção **Environment variables**, clique no ícone de pasta para adicionar variáveis
4. Adicione:
   - **Name**: `MONGODB_URI`
   - **Value**: `mongodb+srv://gustavoflopes:Senai2025@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes`
5. Clique em **OK** e execute novamente

## Opção 2: Configurar no Terminal Integrado

Antes de executar pelo IntelliJ, configure a variável no terminal integrado:

**PowerShell:**
```powershell
$env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes"
```

**CMD:**
```cmd
set MONGODB_URI=mongodb+srv://gustavoflopes:Senai2025@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes
```

## Opção 3: Usar o Script executar.ps1

Execute o script `executar.ps1` que está na raiz do projeto:

```powershell
.\executar.ps1
```

Este script configura automaticamente a variável e executa a aplicação.

