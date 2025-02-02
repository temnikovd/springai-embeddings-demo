package dev.temnikov.aiembeddings;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmbeddingsService {

    private static List<Product> productList = new ArrayList<>();
    private static Map<Integer, float[]> embeddings = new HashMap<>();

    @Autowired
    private EmbeddingModel embeddingModel;
    @Autowired
    private SimilarityCalculator similarityCalculator;

    @PostConstruct
    public void initProducts() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("samples.json");
        if (inputStream != null) {
            // map JSON into List<Product>
            productList = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            System.out.println("Products loaded: List size = " + productList.size());

        } else {
            System.out.println("File samples.json not found in resources.");
        }
        embeddings = loadEmbeddingsFromFile();
    }

    public Map<Integer, float[]> loadEmbeddingsFromFile() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("embeddings.json");
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (Exception e) {
            System.err.println("Error loading embeddings from file: " + e.getMessage());
            return null;
        }
    }

    public void getSimilarProducts(String description) {
        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(description));
        List<ProductSimilarity> topSimilarProducts = similarityCalculator.findTopSimilarProducts(embeddingResponse.getResult().getOutput(),
                embeddings,
                productList,
                5);
        for (ProductSimilarity ps : topSimilarProducts) {
            System.out.printf("Product ID: %d, Name: %s, Description: %s, Similarity: %.4f%n",
                    ps.getProduct().getId(),
                    ps.getProduct().getName(),
                    ps.getProduct().getDescription(),
                    ps.getSimilarity());
        }
    }
}