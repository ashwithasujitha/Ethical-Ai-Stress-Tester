package DSA.ProductSearching;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
       Product[] products = {
    new Product(3, "Headphones", "Electronics"),
    new Product(1, "Laptop", "Electronics"),
    new Product(4, "Shoes", "Fashion"),
    new Product(2, "Smartphone", "Electronics"),
    new Product(5, "T-shirt", "Fashion")
};
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product name to search: ");
        String targetProduct = scanner.nextLine();
        

        // Perform binary search
        BinarySearch binarySearch = new BinarySearch();
        int binarySearchResult = binarySearch.binarySearch(products, targetProduct);
       if (binarySearchResult != -1) {
    System.out.println("Binary Search: " +
            products[binarySearchResult]);
} else {
            System.out.println("Binary Search: Product not found.");
        }

        // Perform linear search
        int linearSearchResult = LinearSearch.linearSearch(products, targetProduct);
        if (linearSearchResult != -1) {
            System.out.println("Linear Search: Product found at index: " + linearSearchResult);
        } else {
            System.out.println("Linear Search: Product not found.");
        }
    }
}
