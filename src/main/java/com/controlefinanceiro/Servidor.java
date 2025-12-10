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
        // ==========================
        // DADOS FIXOS DE CONEXÃO
        // ==========================
        String usuario = "pablovms_db_user";
        String senha   = "HgFbSwXyceLctl6j";
        String host    = "cluster0.e6lvmr8.mongodb.net";

        // Monta a URI
        String mongoUri = "mongodb+srv://" + usuario + ":" + senha + "@" + host + "/?appName=Cluster0";

        // Log sem exibir senha
        String uriDebug = "mongodb+srv://" + usuario + ":***@" + host + "/";
        System.out.println("Conectando ao MongoDB: " + uriDebug);

        try {
            // Inicializa o DAO
            gastoDAO = new GastoDAO(mongoUri);
            System.out.println("✅ Conectado ao MongoDB com sucesso!");

            // Cria o servidor HTTP
            HttpServer server = HttpServer.create(new InetSocketAddress(PORTA), 0);

            // Endpoints
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

    // ==========================
    // HOME
    // ==========================
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

    // ==========================
    // CRUD DE GASTOS
    // ==========================
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
                Gasto gasto = parseGastoFromJSON(body);

                if (gasto.getDescricao() == null || gasto.getDescricao().trim().isEmpty()) {
                    throw new IllegalArgumentException("Descrição é obrigatória");
                }
                if (gasto.getValor() <= 0) {
                    throw new IllegalArgumentException("Valor deve ser maior que zero");
                }

                String id = gastoDAO.inserir(gasto);

                String resposta = "{\"sucesso\":true,\"id\":\"" + id + "\"}";
                enviarResposta(exchange, 201, resposta, "application/json");
            } catch (Exception e) {
                String erroMsg = e.getMessage().replace("\"", "\\\"");
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

    // ==========================
    // GASTOS POR MÊS
    // ==========================
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

    // ==========================
    // UTILITÁRIOS
    // ==========================
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
        Gasto gasto = new Gasto();
        
        // Remove espaços e quebras de linha
        json = json.trim().replaceAll("\\s+", "");
        
        // Remove chaves externas
        if (json.startsWith("{")) {
            json = json.substring(1, json.length() - 1);
        }
        
        // Extrai os campos do JSON
        String descricao = extrairValorJSON(json, "descricao");
        String valorStr = extrairValorJSON(json, "valor");
        String categoria = extrairValorJSON(json, "categoria");
        String dataStr = extrairValorJSON(json, "data");
        
        // Define os valores no objeto Gasto
        if (descricao != null && !descricao.isEmpty()) {
            gasto.setDescricao(descricao);
        }
        
        if (valorStr != null && !valorStr.isEmpty()) {
            try {
                gasto.setValor(Double.parseDouble(valorStr));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Valor inválido: " + valorStr);
            }
        }
        
        if (categoria != null && !categoria.isEmpty()) {
            gasto.setCategoria(categoria);
        }
        
        if (dataStr != null && !dataStr.isEmpty()) {
            try {
                gasto.setData(LocalDate.parse(dataStr, DateTimeFormatter.ISO_LOCAL_DATE));
            } catch (Exception e) {
                throw new IllegalArgumentException("Data inválida: " + dataStr);
            }
        } else {
            gasto.setData(LocalDate.now());
        }
        
        return gasto;
    }
    
    private static String extrairValorJSON(String json, String campo) {
        String busca = "\"" + campo + "\":";
        int inicio = json.indexOf(busca);
        if (inicio == -1) {
            return null;
        }
        
        inicio += busca.length();
        
        // Verifica se é string (entre aspas) ou número
        if (inicio < json.length() && json.charAt(inicio) == '"') {
            // É uma string
            inicio++; // Pula a primeira aspas
            int fim = json.indexOf('"', inicio);
            if (fim == -1) {
                return null;
            }
            String valor = json.substring(inicio, fim);
            // Remove escape de aspas
            return valor.replace("\\\"", "\"").replace("\\\\", "\\");
        } else {
            // É um número ou boolean
            int fim = inicio;
            while (fim < json.length() && json.charAt(fim) != ',' && json.charAt(fim) != '}') {
                fim++;
            }
            return json.substring(inicio, fim).trim();
        }
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
                    .append("\"descricao\":\"").append(escapeJson(gasto.getDescricao())).append("\",")
                    .append("\"valor\":").append(gasto.getValor()).append(",")
                    .append("\"categoria\":\"").append(escapeJson(gasto.getCategoria())).append("\",")
                    .append("\"data\":\"").append(gasto.getData().format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\",")
                    .append("\"mes\":").append(gasto.getMes()).append(",")
                    .append("\"ano\":").append(gasto.getAno())
                    .append("}");
        }

        json.append("]");
        return json.toString();
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
