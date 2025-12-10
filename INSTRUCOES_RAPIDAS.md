# Instruções Rápidas - Configurar MONGODB_URI

## ⚡ Solução Rápida para IntelliJ IDEA

1. **Abra as configurações de execução:**
   - Clique na seta ao lado do botão ▶️ (Run)
   - Selecione **"Edit Configurations..."**

2. **Configure a variável de ambiente:**
   - Selecione sua configuração (ou crie uma nova)
   - Procure a seção **"Environment variables"**
   - Clique no ícone 📁 (pasta) para adicionar
   - Adicione:
     ```
     Name:  MONGODB_URI
     Value: mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority
     
     ⚠️ Substitua os placeholders pelos valores reais!
     Exemplo: mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority
     ```
   - Clique em **OK**

3. **Execute novamente!**

## 🔄 Solução Alternativa - Terminal PowerShell

Abra o PowerShell no diretório do projeto e execute:

```powershell
$env:MONGODB_URI = "mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority"

# Exemplo:
# $env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority"
```

Depois execute a aplicação normalmente pelo IntelliJ.

**Nota:** Esta configuração é temporária e válida apenas para a sessão atual do PowerShell.

## 📝 Usar o Script Automático

Execute o script `executar.ps1` que está na raiz do projeto:

```powershell
.\executar.ps1
```

Este script configura automaticamente a variável e executa a aplicação via Maven.

