package DSA.ProductSearching;

public class LinearSearch {
    public static int linearSearch(Product[] products, String target) {
        for (int i = 0; i < products.length; i++) {
            if (products[i].productName.equalsIgnoreCase(target)) {
                return i; // Return the index of the found product
            }
        }
        return -1; // Return -1 if the product is not found
    }
}