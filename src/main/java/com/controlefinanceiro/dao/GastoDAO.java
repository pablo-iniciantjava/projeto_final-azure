package com.controlefinanceiro.dao;

import com.controlefinanceiro.modelo.Gasto;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com MongoDB
 */
public class GastoDAO {
    private static final String DATABASE_NAME = "controle_financeiro";
    private static final String COLLECTION_NAME = "gastos";
    
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public GastoDAO(String connectionString) {
        try {
            System.out.println("Inicializando conexão com MongoDB...");
            this.mongoClient = MongoClients.create(connectionString);
            
            // Testa a conexão tentando listar os bancos de dados
            try {
                mongoClient.listDatabaseNames().first();
                System.out.println("Conexão com MongoDB estabelecida com sucesso!");
            } catch (Exception e) {
                System.err.println("AVISO: Não foi possível validar a conexão: " + e.getMessage());
            }
            
            this.database = mongoClient.getDatabase(DATABASE_NAME);
            this.collection = database.getCollection(COLLECTION_NAME);
            
            System.out.println("Usando banco de dados: " + DATABASE_NAME);
            System.out.println("Usando coleção: " + COLLECTION_NAME);
        } catch (Exception e) {
            System.err.println("ERRO ao conectar ao MongoDB: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("SRV record")) {
                System.err.println("\n⚠️  PROBLEMA DE CONEXÃO DETECTADO:");
                System.err.println("   - Verifique se a string de conexão está correta");
                System.err.println("   - Verifique sua conexão com a internet");
                System.err.println("   - Verifique se o DNS está funcionando");
                System.err.println("   - Verifique se o IP está liberado no MongoDB Atlas (Network Access)");
                System.err.println("\n   String de conexão deve ser: mongodb+srv://usuario:senha@NOME_DO_CLUSTER.mongodb.net/");
            }
            throw new RuntimeException("Falha na conexão com MongoDB", e);
        }
    }

    /**
     * Insere um novo gasto no banco de dados
     */
    public String inserir(Gasto gasto) {
        try {
            Document doc = new Document()
                    .append("descricao", gasto.getDescricao())
                    .append("valor", gasto.getValor())
                    .append("categoria", gasto.getCategoria())
                    .append("data", gasto.getData().format(DateTimeFormatter.ISO_LOCAL_DATE))
                    .append("mes", gasto.getMes())
                    .append("ano", gasto.getAno());

            // Adiciona imagem da nota fiscal se houver
            if (gasto.getImagemNotaFiscal() != null && !gasto.getImagemNotaFiscal().trim().isEmpty()) {
                doc.append("imagemNotaFiscal", gasto.getImagemNotaFiscal());
            }

            collection.insertOne(doc);
            return doc.getObjectId("_id").toString();
        } catch (com.mongodb.MongoTimeoutException e) {
            System.err.println("\n⚠️  ERRO DE TIMEOUT AO INSERIR:");
            System.err.println("   A conexão com o MongoDB Atlas expirou.");
            System.err.println("   Possíveis causas:");
            System.err.println("   1. String de conexão incorreta ou incompleta");
            System.err.println("   2. IP não está liberado no Network Access do MongoDB Atlas");
            System.err.println("   3. Problema de conexão com a internet");
            System.err.println("   4. Cluster do MongoDB Atlas pode estar pausado");
            throw new RuntimeException("Erro ao inserir gasto: " + e.getMessage(), e);
        } catch (com.mongodb.MongoConfigurationException e) {
            System.err.println("\n⚠️  ERRO DE CONFIGURAÇÃO:");
            System.err.println("   Verifique se a string de conexão está correta.");
            System.err.println("   Deve ser: mongodb+srv://usuario:senha@NOME_DO_CLUSTER.mongodb.net/");
            throw new RuntimeException("Erro ao inserir gasto: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("\n⚠️  ERRO AO INSERIR GASTO:");
            System.err.println("   " + e.getClass().getSimpleName() + ": " + e.getMessage());
            throw new RuntimeException("Erro ao inserir gasto: " + e.getMessage(), e);
        }
    }

    /**
     * Lista todos os gastos
     */
    public List<Gasto> listarTodos() {
        List<Gasto> gastos = new ArrayList<>();
        
        for (Document doc : collection.find().sort(Sorts.descending("data"))) {
            gastos.add(documentToGasto(doc));
        }
        
        return gastos;
    }

    /**
     * Lista gastos por mês e ano
     */
    public List<Gasto> listarPorMesAno(int mes, int ano) {
        List<Gasto> gastos = new ArrayList<>();
        
        for (Document doc : collection.find(
                Filters.and(
                    Filters.eq("mes", mes),
                    Filters.eq("ano", ano)
                )
        ).sort(Sorts.descending("data"))) {
            gastos.add(documentToGasto(doc));
        }
        
        return gastos;
    }

    /**
     * Remove um gasto pelo ID
     */
    public boolean remover(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            var result = collection.deleteOne(Filters.eq("_id", objectId));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Erro ao remover gasto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calcula o total de gastos por mês e ano
     */
    public double calcularTotalPorMesAno(int mes, int ano) {
        double total = 0.0;
        
        for (Document doc : collection.find(
                Filters.and(
                    Filters.eq("mes", mes),
                    Filters.eq("ano", ano)
                )
        )) {
            total += doc.getDouble("valor");
        }
        
        return total;
    }

    /**
     * Converte Document do MongoDB para objeto Gasto
     */
    private Gasto documentToGasto(Document doc) {
        Gasto gasto = new Gasto();
        gasto.setId(doc.getObjectId("_id").toString());
        gasto.setDescricao(doc.getString("descricao"));
        gasto.setValor(doc.getDouble("valor"));
        gasto.setCategoria(doc.getString("categoria"));
        
        String dataStr = doc.getString("data");
        if (dataStr != null) {
            gasto.setData(LocalDate.parse(dataStr, DateTimeFormatter.ISO_LOCAL_DATE));
        }
        
        gasto.setMes(doc.getInteger("mes"));
        gasto.setAno(doc.getInteger("ano"));
        
        // Carrega imagem da nota fiscal se existir
        if (doc.containsKey("imagemNotaFiscal")) {
            gasto.setImagemNotaFiscal(doc.getString("imagemNotaFiscal"));
        }
        
        return gasto;
    }

    /**
     * Fecha a conexão com o MongoDB
     */
    public void fechar() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}


