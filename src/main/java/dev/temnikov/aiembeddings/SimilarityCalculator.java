package dev.temnikov.aiembeddings;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SimilarityCalculator {

    private float calculateCosineSimilarity(float[] vectorA, float[] vectorB) {
        float dotProduct = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return (float) (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    public List<ProductSimilarity> findTopSimilarProducts(
            float[] queryEmbedding,
            Map<Integer, float[]> embeddings,
            List<Product> products,
            int topN) {

        List<ProductSimilarity> similarities = new ArrayList<>();

        for (Product product : products) {
            float[] productEmbedding = embeddings.get(product.getId());
            if (productEmbedding != null) {
                float similarity = calculateCosineSimilarity(queryEmbedding, productEmbedding);
                similarities.add(new ProductSimilarity(product, similarity));
            }
        }

        return similarities.stream()
                .sorted((p1, p2) -> Double.compare(p2.getSimilarity(), p1.getSimilarity()))
                .limit(topN)
                .toList();
    }
}
