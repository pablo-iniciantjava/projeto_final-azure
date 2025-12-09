@echo off
REM Script de configuração para Windows CMD
REM Configure a variável de ambiente MONGODB_URI antes de executar a aplicação

set MONGODB_URI=mongodb+srv://gustavoflopes:Senai2025@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes

echo Variável MONGODB_URI configurada!
echo.
echo Para executar a aplicação, use:
echo mvn exec:java -Dexec.mainClass="com.controlefinanceiro.Servidor"
echo.
echo Ou compile e execute:
echo mvn clean package
echo java -jar target/controle-financeiro-1.0.0.jar


