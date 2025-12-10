# Script de configuração para Windows PowerShell
# Configure a variável de ambiente MONGODB_URI antes de executar a aplicação

# ============================================================================
# CONFIGURAÇÃO DA STRING DE CONEXÃO MONGODB ATLAS
# ============================================================================
# IMPORTANTE: Substitua os placeholders pela sua configuração real:
#   - <username> = seu usuário do MongoDB Atlas
#   - <password> = sua senha (se contém @, use %40; se contém #, use %23, etc.)
#   - xxxxx = código real do seu cluster (ex: abc123)
#
# Exemplo CORRETO (formato):
# mongodb+srv://meuUsuario:minhaSenha%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority
#
# ============================================================================

# String de conexão MongoDB Atlas (formato SRV recomendado)
# SUBSTITUA os placeholders abaixo pela sua configuração real:
$env:MONGODB_URI = "mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority"

# Exemplo completo (descomente e ajuste com seus dados reais):
# $env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority"

Write-Host "Variável MONGODB_URI configurada!" -ForegroundColor Green
Write-Host ""
Write-Host "Para executar a aplicação, use:" -ForegroundColor Yellow
Write-Host "mvn exec:java -Dexec.mainClass=`"com.controlefinanceiro.Servidor`"" -ForegroundColor Cyan
Write-Host ""
Write-Host "Ou compile e execute:" -ForegroundColor Yellow
Write-Host "mvn clean package" -ForegroundColor Cyan
Write-Host "java -jar target/controle-financeiro-1.0.0.jar" -ForegroundColor Cyan


