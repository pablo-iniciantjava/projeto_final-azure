# 🔧 Solução de Problemas - MongoDB Atlas

## Erro: "Failed looking up SRV record" ou "Timed out"

Este erro indica que a aplicação não consegue conectar ao MongoDB Atlas. Siga os passos abaixo:

### 1. ✅ Verificar String de Conexão

A string de conexão deve seguir este formato:
```
mongodb+srv://USUARIO:SENHA@NOME_DO_CLUSTER.mongodb.net/
```

**IMPORTANTE:**
- Substitua `USUARIO` pelo seu usuário do MongoDB Atlas
- Substitua `SENHA` pela sua senha (use `%40` para `@`, `%23` para `#`, etc.)
- Substitua `NOME_DO_CLUSTER` pelo nome real do seu cluster (não use "cluster")

**Exemplo correto:**
```
mongodb+srv://gustavoflopes:Senai2025%40@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes
```

### 2. ✅ Verificar Configuração no IntelliJ IDEA

1. Vá em **Run** → **Edit Configurations...**
2. Verifique se a variável `MONGODB_URI` está configurada
3. Certifique-se de que a string está completa e correta
4. Use **aspas** ao redor do valor se contiver caracteres especiais

### 3. ✅ Verificar Conexão com Internet

- Teste se você consegue acessar https://cloud.mongodb.com
- Verifique se não há firewall bloqueando a conexão
- Tente usar uma rede diferente (ex: mobile hotspot)

### 4. ✅ Verificar Network Access no MongoDB Atlas

1. Acesse https://cloud.mongodb.com
2. Vá em **Network Access** (no menu lateral)
3. Verifique se seu IP está na lista de IPs permitidos
4. Se não estiver, clique em **Add IP Address**
5. Para testes, pode usar `0.0.0.0/0` (permite qualquer IP) - **não recomendado para produção**

### 5. ✅ Verificar Database Access

1. Vá em **Database Access** (no menu lateral)
2. Verifique se o usuário existe e está ativo
3. Verifique se a senha está correta
4. Se necessário, resete a senha do usuário

### 6. ✅ Obter Nova String de Conexão

1. No MongoDB Atlas, clique em **Connect** no seu cluster
2. Escolha **Connect your application**
3. Selecione **Java** e a versão mais recente
4. Copie a string de conexão completa
5. Substitua `<password>` pela sua senha real
6. Se a senha contém `@`, substitua por `%40`

### 7. ✅ Testar Conexão

Execute no PowerShell (na mesma sessão onde vai rodar a aplicação):

```powershell
# Configure a variável
$env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025%40@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes"

# Verifique se foi configurada
echo $env:MONGODB_URI
```

### 8. ✅ Problemas Comuns

**Problema:** String de conexão não está sendo lida
- **Solução:** Configure no IntelliJ IDEA (Run → Edit Configurations → Environment variables)

**Problema:** Senha com caracteres especiais não funciona
- **Solução:** Use URL encoding: `@` = `%40`, `#` = `%23`, `$` = `%24`, etc.

**Problema:** Timeout mesmo com conexão correta
- **Solução:** Verifique o Network Access no MongoDB Atlas e adicione seu IP

**Problema:** DNS não resolve
- **Solução:** Verifique se consegue fazer ping para o domínio do MongoDB, ou use DNS do Google (8.8.8.8)

## 📞 Ainda com Problemas?

Se após seguir todos os passos o problema persistir:

1. Verifique os logs completos do erro
2. Teste a conexão usando o MongoDB Compass (ferramenta gráfica)
3. Verifique se o cluster do MongoDB Atlas está ativo e não está em pausa
4. Tente criar um novo usuário e usar uma nova string de conexão

## 🔍 Logs Úteis

A aplicação agora mostra mais informações de debug:
- ✅ Mostra a string de conexão (sem a senha) ao iniciar
- ✅ Informa quando a conexão é estabelecida
- ✅ Mostra qual banco e coleção está sendo usado
- ✅ Exibe erros detalhados com sugestões de solução


