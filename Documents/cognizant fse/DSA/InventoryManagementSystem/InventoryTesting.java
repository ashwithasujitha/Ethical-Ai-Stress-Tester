package DSA.InventoryManagementSystem;
import java.util.Scanner;
public class InventoryTesting {
    public static void main(String[] args) {
    Inventory inventory = new Inventory();
    Scanner sc = new Scanner(System.in);
    int choice;
    do{
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Update Product");
        System.out.println("4. Display Products");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt();
        switch(choice) {

                case 1:
                    System.out.print("Enter Product ID: ");
                    int id = sc.nextInt();

                    sc.nextLine();

                    System.out.print("Enter Product Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Quantity: ");
                    int quantity = sc.nextInt();

                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();

                    Product p = new Product(id, name, quantity, price);

                    inventory.addProduct(p);
                    break;

                case 2:
                    System.out.print("Enter Product ID: ");
                    id = sc.nextInt();

                    System.out.print("Enter New Quantity: ");
                    quantity = sc.nextInt();

                    System.out.print("Enter New Price: ");
                    price = sc.nextDouble();

                    inventory.updateProduct(id, quantity, price);
                    break;

                case 3:
                    System.out.print("Enter Product ID: ");
                    id = sc.nextInt();

                    inventory.removeProduct(id);
                    break;

                case 4:
                    inventory.displayProducts();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid Choice");
            }

    }
    while(choice != 5);
}

}
