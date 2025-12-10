# Script para configurar e executar a aplicação
# Este script configura a variável de ambiente MONGODB_URI e executa a aplicação

Write-Host "=== Configurando variável de ambiente MONGODB_URI ===" -ForegroundColor Cyan

# Configure aqui sua string de conexão do MongoDB Atlas
# ATENÇÃO: Substitua USUARIO e SENHA pelas suas credenciais do MongoDB Atlas
# Se a senha contém caracteres especiais, use URL encoding: @ = %40, # = %23, etc.
$env:MONGODB_URI = "mongodb://USUARIO:SENHA@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin"

# String alternativa (conexão SRV):
# $env:MONGODB_URI = "mongodb+srv://gustavoflopes:Senai2025%40@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes"

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

