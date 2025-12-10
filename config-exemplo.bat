@echo off
REM Script de configuração para Windows CMD
REM Configure a variável de ambiente MONGODB_URI antes de executar a aplicação

REM ATENÇÃO: Substitua USUARIO e SENHA pelas suas credenciais do MongoDB Atlas
REM Se a senha contém caracteres especiais, use URL encoding: @ = %%40, # = %%23, etc.
set MONGODB_URI=mongodb://USUARIO:SENHA@atlas-sql-69374f43db44607e5a5ab60e-rmm2m7.z.query.mongodb.net/cursosenai?ssl=true&authSource=admin

REM String alternativa (conexão SRV):
REM set MONGODB_URI=mongodb+srv://gustavoflopes:Senai2025%%40@gustavoflopes.7ohuzi9.mongodb.net/?appName=gustavoFLopes

echo Variável MONGODB_URI configurada!
echo.
echo Para executar a aplicação, use:
echo mvn exec:java -Dexec.mainClass="com.controlefinanceiro.Servidor"
echo.
echo Ou compile e execute:
echo mvn clean package
echo java -jar target/controle-financeiro-1.0.0.jar


