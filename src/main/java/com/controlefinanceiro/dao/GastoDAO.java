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
            this.mongoClient = MongoClients.create(connectionString);
            this.database = mongoClient.getDatabase(DATABASE_NAME);
            this.collection = database.getCollection(COLLECTION_NAME);
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao MongoDB: " + e.getMessage());
            throw new RuntimeException("Falha na conexão com MongoDB", e);
        }
    }

    /**
     * Insere um novo gasto no banco de dados
     */
    public String inserir(Gasto gasto) {
        Document doc = new Document()
                .append("descricao", gasto.getDescricao())
                .append("valor", gasto.getValor())
                .append("categoria", gasto.getCategoria())
                .append("data", gasto.getData().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .append("mes", gasto.getMes())
                .append("ano", gasto.getAno());

        collection.insertOne(doc);
        return doc.getObjectId("_id").toString();
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

