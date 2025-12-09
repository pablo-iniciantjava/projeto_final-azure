package com.controlefinanceiro;

import com.controlefinanceiro.dao.GastoDAO;
import com.controlefinanceiro.modelo.Gasto;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Servidor HTTP simples para a aplicação de controle financeiro
 */
public class Servidor {
    private static final int PORTA = 8080;
    private static GastoDAO gastoDAO;

    public static void main(String[] args) {
        // Verifica se a URI do MongoDB foi configurada
        String mongoUri = System.getenv("MONGODB_URI");
        if (mongoUri == null || mongoUri.isEmpty()) {
            System.err.println("ERRO: Variável de ambiente MONGODB_URI não configurada!");
            System.err.println("Configure a variável de ambiente com a string de conexão do MongoDB Atlas.");
            System.err.println("Exemplo: export MONGODB_URI=\"mongodb+srv://usuario:senha@cluster.mongodb.net/\"");
            System.exit(1);
        }

        try {
            // Inicializa o DAO
            gastoDAO = new GastoDAO(mongoUri);
            System.out.println("✅ Conectado ao MongoDB com sucesso!");

            // Cria o servidor HTTP
            HttpServer server = HttpServer.create(new InetSocketAddress(PORTA), 0);
            
            // Define os endpoints
            server.createContext("/", new HomeHandler());
            server.createContext("/api/gastos", new GastosHandler());
            server.createContext("/api/gastos/mes", new GastosPorMesHandler());
            
            server.setExecutor(null);
            server.start();
            
            System.out.println("Servidor iniciado na porta " + PORTA);
            System.out.println("Acesse: http://localhost:" + PORTA);
            
        } catch (Exception e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Handler para a página inicial (HTML)
     */
    static class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String html = lerArquivoHTML();
                enviarResposta(exchange, 200, html, "text/html");
            } else {
                enviarResposta(exchange, 405, "Método não permitido", "text/plain");
            }
        }

        private String lerArquivoHTML() {
            try (InputStream is = Servidor.class.getResourceAsStream("/index.html");
                 BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                return gerarHTMLPadrao();
            }
        }

        private String gerarHTMLPadrao() {
            return "<!DOCTYPE html><html><head><title>Controle Financeiro</title></head>" +
                   "<body><h1>Erro ao carregar página</h1></body></html>";
        }
    }

    /**
     * Handler para operações CRUD de gastos
     */
    static class GastosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            
            switch (method) {
                case "GET":
                    listarGastos(exchange);
                    break;
                case "POST":
                    criarGasto(exchange);
                    break;
                case "DELETE":
                    removerGasto(exchange);
                    break;
                default:
                    enviarResposta(exchange, 405, "Método não permitido", "text/plain");
            }
        }

        private void listarGastos(HttpExchange exchange) throws IOException {
            var gastos = gastoDAO.listarTodos();
            String json = converterParaJSON(gastos);
            enviarResposta(exchange, 200, json, "application/json");
        }

        private void criarGasto(HttpExchange exchange) throws IOException {
            String body = lerBody(exchange);
            
            try {
                // Parse simples do JSON
                Gasto gasto = parseGastoFromJSON(body);
                
                // Validação básica
                if (gasto.getDescricao() == null || gasto.getDescricao().trim().isEmpty()) {
                    throw new IllegalArgumentException("Descrição é obrigatória");
                }
                if (gasto.getValor() <= 0) {
                    throw new IllegalArgumentException("Valor deve ser maior que zero");
                }
                
                String id = gastoDAO.inserir(gasto);
                System.out.println("Gasto inserido: " + gasto.toString() + " com ID: " + id);
                
                String resposta = "{\"sucesso\":true,\"id\":\"" + id + "\"}";
                enviarResposta(exchange, 201, resposta, "application/json");
            } catch (Exception e) {
                System.err.println("Erro ao criar gasto: " + e.getMessage());
                e.printStackTrace();
                String erroMsg = e.getMessage().replace("\"", "\\\"").replace("\n", " ");
                String resposta = "{\"sucesso\":false,\"erro\":\"" + erroMsg + "\"}";
                enviarResposta(exchange, 400, resposta, "application/json");
            }
        }

        private void removerGasto(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String id = null;
            
            if (query != null && query.startsWith("id=")) {
                id = query.substring(3);
            }
            
            if (id == null || id.isEmpty()) {
                enviarResposta(exchange, 400, "{\"sucesso\":false,\"erro\":\"ID não fornecido\"}", "application/json");
                return;
            }
            
            boolean removido = gastoDAO.remover(id);
            String resposta = "{\"sucesso\":" + removido + "}";
            enviarResposta(exchange, removido ? 200 : 404, resposta, "application/json");
        }
    }

    /**
     * Handler para listar gastos por mês e ano
     */
    static class GastosPorMesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                enviarResposta(exchange, 405, "Método não permitido", "text/plain");
                return;
            }
            
            String query = exchange.getRequestURI().getQuery();
            int mes = LocalDate.now().getMonthValue();
            int ano = LocalDate.now().getYear();
            
            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] kv = param.split("=");
                    if (kv.length == 2) {
                        if ("mes".equals(kv[0])) {
                            mes = Integer.parseInt(kv[1]);
                        } else if ("ano".equals(kv[0])) {
                            ano = Integer.parseInt(kv[1]);
                        }
                    }
                }
            }
            
            var gastos = gastoDAO.listarPorMesAno(mes, ano);
            double total = gastoDAO.calcularTotalPorMesAno(mes, ano);
            
            String json = "{\"gastos\":" + converterParaJSON(gastos) + 
                         ",\"total\":" + total + 
                         ",\"mes\":" + mes + 
                         ",\"ano\":" + ano + "}";
            
            enviarResposta(exchange, 200, json, "application/json");
        }
    }

    /**
     * Utilitários
     */
    private static String lerBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private static void enviarResposta(HttpExchange exchange, int statusCode, String resposta, String contentType) 
            throws IOException {
        byte[] respostaBytes = resposta.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, respostaBytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(respostaBytes);
        }
    }

    private static Gasto parseGastoFromJSON(String json) {
        // Parser JSON simples (sem biblioteca externa)
        Gasto gasto = new Gasto();
        
        try {
            // Remove chaves e espaços
            json = json.trim();
            if (json.startsWith("{")) {
                json = json.substring(1);
            }
            if (json.endsWith("}")) {
                json = json.substring(0, json.length() - 1);
            }
            
            // Divide por vírgulas, mas respeitando strings
            java.util.List<String> campos = new java.util.ArrayList<>();
            StringBuilder campoAtual = new StringBuilder();
            boolean dentroString = false;
            
            for (char c : json.toCharArray()) {
                if (c == '"') {
                    dentroString = !dentroString;
                    campoAtual.append(c);
                } else if (c == ',' && !dentroString) {
                    campos.add(campoAtual.toString().trim());
                    campoAtual = new StringBuilder();
                } else {
                    campoAtual.append(c);
                }
            }
            if (campoAtual.length() > 0) {
                campos.add(campoAtual.toString().trim());
            }
            
            // Processa cada campo
            for (String campo : campos) {
                int doisPontos = campo.indexOf(':');
                if (doisPontos > 0) {
                    String chave = campo.substring(0, doisPontos).trim().replaceAll("\"", "");
                    String valor = campo.substring(doisPontos + 1).trim().replaceAll("\"", "");
                    
                    switch (chave) {
                        case "descricao":
                            gasto.setDescricao(valor);
                            break;
                        case "valor":
                            gasto.setValor(Double.parseDouble(valor));
                            break;
                        case "categoria":
                            gasto.setCategoria(valor);
                            break;
                        case "data":
                            gasto.setData(LocalDate.parse(valor, DateTimeFormatter.ISO_LOCAL_DATE));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar JSON: " + e.getMessage(), e);
        }
        
        // Se data não foi fornecida, usa a data atual
        if (gasto.getData() == null) {
            gasto.setData(LocalDate.now());
        }
        
        return gasto;
    }

    private static String converterParaJSON(java.util.List<Gasto> gastos) {
        StringBuilder json = new StringBuilder("[");
        boolean primeiro = true;
        
        for (Gasto gasto : gastos) {
            if (!primeiro) {
                json.append(",");
            }
            primeiro = false;
            
            json.append("{")
                .append("\"id\":\"").append(gasto.getId()).append("\",")
                .append("\"descricao\":\"").append(gasto.getDescricao()).append("\",")
                .append("\"valor\":").append(gasto.getValor()).append(",")
                .append("\"categoria\":\"").append(gasto.getCategoria()).append("\",")
                .append("\"data\":\"").append(gasto.getData().format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\",")
                .append("\"mes\":").append(gasto.getMes()).append(",")
                .append("\"ano\":").append(gasto.getAno())
                .append("}");
        }
        
        json.append("]");
        return json.toString();
    }
}

