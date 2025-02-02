package dev.temnikov.aiembeddings;

public class ProductSimilarity {

    private final Product product;
    private final float similarity;

    public ProductSimilarity(Product product, float similarity) {
        this.product = product;
        this.similarity = similarity;
    }

    public Product getProduct() {
        return product;
    }

    public double getSimilarity() {
        return similarity;
    }
}
