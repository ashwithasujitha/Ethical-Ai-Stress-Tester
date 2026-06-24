package DSA.ProductSearching;

public class BinarySearch {
    int binarySearch(Product[] products, String target) {
        int left = 0;
        int right = products.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

           if(products[mid].productName.equalsIgnoreCase(target)) {
                return mid; // Return the index of the found product
            }

            if (products[mid].productName.compareToIgnoreCase(target) < 0) {
                left = mid + 1; // Search in the right half
            } else {
                right = mid - 1; // Search in the left half
            }
        }

        return -1; // Return -1 if the product is not found
    }
}
