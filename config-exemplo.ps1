# Script de configuração para Windows PowerShell
# Configure a variável de ambiente MONGODB_URI antes de executar a aplicação

$env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025%40@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes"

Write-Host "Variável MONGODB_URI configurada!" -ForegroundColor Green
Write-Host ""
Write-Host "Para executar a aplicação, use:" -ForegroundColor Yellow
Write-Host "mvn exec:java -Dexec.mainClass=`"com.controlefinanceiro.Servidor`"" -ForegroundColor Cyan
Write-Host ""
Write-Host "Ou compile e execute:" -ForegroundColor Yellow
Write-Host "mvn clean package" -ForegroundColor Cyan
Write-Host "java -jar target/controle-financeiro-1.0.0.jar" -ForegroundColor Cyan


