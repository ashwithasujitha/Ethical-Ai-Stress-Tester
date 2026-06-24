package DSA.InventoryManagementSystem;

import java.util.HashMap;

public class Inventory {
    private HashMap<Integer, Product> products=new HashMap<>();
    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
        System.out.println("Product added: " );
    }
    public void removeProduct(int productId) {
        products.remove(productId);
        System.out.println("Product removed: " );
    }
     public void updateProduct(int id,
                              int quantity,
                              double price) {

        Product product = products.get(id);

        if (product != null) {

            product.setQuantity(quantity);
            product.setPrice(price);

            System.out.println("Product Updated");
        }
        else {
            System.out.println("Product Not Found");
        }
    }
     public void displayProducts() {

        for (Product p : products.values()) {
            System.out.println(p);
        }
    }

    
}
