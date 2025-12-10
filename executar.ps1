# Script para configurar e executar a aplicação
# Este script configura a variável de ambiente MONGODB_URI e executa a aplicação

Write-Host "=== Configurando variável de ambiente MONGODB_URI ===" -ForegroundColor Cyan

# ============================================================================
# CONFIGURAÇÃO DA STRING DE CONEXÃO MONGODB ATLAS
# ============================================================================
# IMPORTANTE: Substitua os placeholders pela sua configuração real:
#   - <username> = seu usuário do MongoDB Atlas
#   - <password> = sua senha (se contém @, use %40; se contém #, use %23, etc.)
#   - xxxxx = código real do seu cluster (ex: abc123)
#
# Para obter a string correta:
# 1. Acesse https://cloud.mongodb.com
# 2. Clique em "Connect" no seu cluster
# 3. Escolha "Connect your application"
# 4. Copie a string e substitua <password> pela senha real
#
# ============================================================================

# String de conexão MongoDB Atlas (formato SRV recomendado)
# SUBSTITUA os placeholders abaixo pela sua configuração real:
$env:MONGODB_URI = "mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority"

# Exemplo completo (descomente e ajuste com seus dados reais):
# $env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority"

Write-Host "Variável MONGODB_URI configurada!" -ForegroundColor Green
Write-Host ""

# Verifica se o Maven está instalado
Write-Host "Compilando o projeto..." -ForegroundColor Yellow
mvn clean compile

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilação concluída com sucesso!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Iniciando servidor..." -ForegroundColor Yellow
    Write-Host ""
    
    # Executa a aplicação
    mvn exec:java -Dexec.mainClass="com.controlefinanceiro.Servidor"
} else {
    Write-Host "Erro na compilação. Verifique os erros acima." -ForegroundColor Red
    exit 1
}

