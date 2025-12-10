#!/bin/bash
# Script de configuração para Linux/Mac
# Configure a variável de ambiente MONGODB_URI antes de executar a aplicação

# ============================================================================
# CONFIGURAÇÃO DA STRING DE CONEXÃO MONGODB ATLAS
# ============================================================================
# IMPORTANTE: Substitua os placeholders pela sua configuração real:
#   - <username> = seu usuário do MongoDB Atlas
#   - <password> = sua senha (se contém @, use %40; se contém #, use %23, etc.)
#   - xxxxx = código real do seu cluster (ex: abc123)
#
# Para obter a string correta, veja o arquivo COMO_OBTER_STRING_CONEXAO.md
# ============================================================================

# String de conexão MongoDB Atlas (formato SRV recomendado)
# SUBSTITUA os placeholders abaixo pela sua configuração real:
export MONGODB_URI="mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority"

# Exemplo completo (descomente e ajuste com seus dados reais):
# export MONGODB_URI="mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority"

echo "Variável MONGODB_URI configurada!"
echo ""
echo "Para executar a aplicação, use:"
echo "mvn exec:java -Dexec.mainClass=\"com.controlefinanceiro.Servidor\""
echo ""
echo "Ou compile e execute:"
echo "mvn clean package"
echo "java -jar target/controle-financeiro-1.0.0.jar"


