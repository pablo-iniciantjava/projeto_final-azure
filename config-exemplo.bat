@echo off
REM Script de configuração para Windows CMD
REM Configure a variável de ambiente MONGODB_URI antes de executar a aplicação

REM ============================================================================
REM CONFIGURAÇÃO DA STRING DE CONEXÃO MONGODB ATLAS
REM ============================================================================
REM IMPORTANTE: Substitua os placeholders pela sua configuração real:
REM   - <username> = seu usuário do MongoDB Atlas
REM   - <password> = sua senha (se contém @, use %%40; se contém #, use %%23, etc.)
REM   - xxxxx = código real do seu cluster (ex: abc123)
REM
REM Para obter a string correta, veja o arquivo COMO_OBTER_STRING_CONEXAO.md
REM ============================================================================

REM String de conexão MongoDB Atlas (formato SRV recomendado)
REM SUBSTITUA os placeholders abaixo pela sua configuração real:
set MONGODB_URI=mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority

REM Exemplo completo (descomente e ajuste com seus dados reais):
REM set MONGODB_URI=mongodb+srv://gustavoflopes:Senai2025%%40@cluster0.abc123.mongodb.net/?retryWrites=true&w=majority

echo Variável MONGODB_URI configurada!
echo.
echo Para executar a aplicação, use:
echo mvn exec:java -Dexec.mainClass="com.controlefinanceiro.Servidor"
echo.
echo Ou compile e execute:
echo mvn clean package
echo java -jar target/controle-financeiro-1.0.0.jar


