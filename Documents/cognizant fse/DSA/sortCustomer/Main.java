package DSA.sortCustomer;

public class Main {
    public static void main(String[] args) {
        Order[] orders = new Order[5];
        orders[0] = new Order(101, "Alice", 250.75);
        orders[1] = new Order(102, "Bob", 150.50);
        orders[2] = new Order(103, "Charlie", 300.00);
        orders[3] = new Order(104, "David", 200.25);
        orders[4] = new Order(105, "Eve", 175.80);

        System.out.println("Before Sorting:");
        for (Order order : orders) {
            System.out.println(order);
        }

        BubbleSort.bubbleSort(orders);

        System.out.println("\nAfter Sorting by Total Price(Bubble Sort):");
        for (Order order : orders) {
            System.out.println(order);
        }

        QuickSort.quickSort(orders, 0, orders.length - 1);
        System.out.println("\nAfter Sorting by Total Price(Quick Sort):");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

}
