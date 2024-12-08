import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;

public class Main {
    private static double selectedAccessoriescost = 0.0;
    private static double selectedCarprice = 0.0;
    private static String selectedCarmodel = "";
    private static String selectedCarvariant = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the budget for the car in rupees: ");
        double budget = scanner.nextDouble();
        scanner.nextLine();

        try {
            filterByBudget(budget, scanner);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static double onRoadPriceCalculate(double carPrice) {
            return carPrice * (1 + 0.07 + 0.12 + 0.02);
    }

    private static void filterByBudget(double budget, Scanner scanner) throws IOException {
        List<String[]> carData = Files.lines(Paths.get("CarVariants.csv")).map(line -> line.split(",")).filter(data -> {double price = Double.parseDouble(data[2].replace("₹", "").trim()) * 100000;
            return price <= budget;
                })
                .collect(Collectors.toList());

        if (carData.isEmpty()) {
            System.out.println("No cars found within your budget.");
            return;
        }

        Map<String, Double> availableCars = carData.stream().collect(Collectors.toMap(data -> data[0] + " - " + data[1], data -> Double.parseDouble(data[2].replace("₹", "").trim()) * 100000));

        System.out.println("Cars within your budget:");
        int i = 1;
        for (Map.Entry<String, Double> entry : availableCars.entrySet()) {
            System.out.println(i + ". " + entry.getKey() + " - ₹" + entry.getValue());
            i++;
        }

        System.out.print("Select the car by entering the number: ");
        int selectedCarIndex = scanner.nextInt();
        scanner.nextLine();

        if (selectedCarIndex < 1 || selectedCarIndex > availableCars.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String selectedCar = (String) availableCars.keySet().toArray()[selectedCarIndex - 1];
        String[] carDetails = selectedCar.split(" - ");
        selectedCarmodel = carDetails[0];
        selectedCarvariant = carDetails[1];
        selectedCarprice = availableCars.get(selectedCar);

        double onRoadPrice = onRoadPriceCalculate(selectedCarprice);
        System.out.println("\nOn-road price after adding registration insurance, and handling fees, which are 7%, 12%, and 2% respectively of " + selectedCarmodel + " " + selectedCarvariant + " (including taxes): ₹" + onRoadPrice);

        getAccessories(budget - onRoadPrice, scanner);
    }

    private static void getAccessories(double budget, Scanner scanner) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("CarAccessories.csv"));
        String line;
        reader.readLine();

        Map<String, Double> affordableAccessories = new HashMap<>();
        Map<String, String> accessoriesWithRange = new HashMap<>();

        while ((line = reader.readLine()) != null) {
            String[] accessoryData = line.split(",", 3);
            if (accessoryData.length != 3) {
                System.out.println("Invalid accessory entry: " + line);
                continue;
            }

            String accessoryType = accessoryData[0].trim();
            String accessoryName = accessoryData[1].trim();
            String priceRange = accessoryData[2].trim();

            priceRange = priceRange.replace("₹", "").replace(",", "").trim();

            String[] priceBounds = priceRange.split(" - ");
            try {
                if (priceBounds.length == 2) {
                    double minPrice = Double.parseDouble(priceBounds[0].trim());
                    double maxPrice = Double.parseDouble(priceBounds[1].trim());

                    if (minPrice <= budget) {
                        affordableAccessories.put(accessoryName, minPrice);
                        accessoriesWithRange.put(accessoryName, "₹" + minPrice + " - ₹" + maxPrice);
                        System.out.println(accessoryType + ", Accessory: " + accessoryName + ", Price Range: ₹" + minPrice + " - ₹" + maxPrice);
                    }
                } else {
                    double singlePrice = Double.parseDouble(priceBounds[0].trim());
                    if (singlePrice <= budget) {
                        affordableAccessories.put(accessoryName, singlePrice);
                        System.out.println(accessoryType + ", Accessory: " + accessoryName + ", Price: ₹" + singlePrice);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid accessory price range for accessory: " + accessoryName + " with price range: " + priceRange);
            }
        }

        if (affordableAccessories.isEmpty()) {
            System.out.println("No accessories available within your remaining budget.");
            return;
        }

        selectAccessories(budget, affordableAccessories, accessoriesWithRange, scanner);
        reader.close();
    }

    private static void selectAccessories(double budget, Map<String, Double> affordableAccessories, Map<String, String> accessoriesWithRange, Scanner scanner) {
        double remainingBudget = budget;

        while (remainingBudget > 0) {
            System.out.println("\nRemaining budget for accessories: ₹" + remainingBudget);
            System.out.print("Enter the name of the accessory you want to buy, or type 'done' to finish: ");
            String selectedAccessory = scanner.nextLine().trim();

            if (selectedAccessory.equalsIgnoreCase("done")) {
                break;
            }

            if (affordableAccessories.containsKey(selectedAccessory)) {
                double accessoryPrice = affordableAccessories.get(selectedAccessory);

                if (accessoriesWithRange.containsKey(selectedAccessory)) {
                    System.out.print("Enter the desired price for " + selectedAccessory + " within the range " + accessoriesWithRange.get(selectedAccessory) + ": ");
                    double chosenPrice = scanner.nextDouble();
                    scanner.nextLine();

                    if (chosenPrice >= accessoryPrice && chosenPrice <= Double.parseDouble(accessoriesWithRange.get(selectedAccessory).split(" - ")[1].replace("₹", "").trim())) {
                        accessoryPrice = chosenPrice;
                    } else {
                        System.out.println("Invalid price selection. Please try again.");
                        continue;
                    }
                }

                if (accessoryPrice <= remainingBudget) {
                    remainingBudget -= accessoryPrice;
                    selectedAccessoriescost += accessoryPrice;
                    System.out.println("You selected: " + selectedAccessory + " for ₹" + accessoryPrice + ". Remaining budget: ₹" + remainingBudget);
                } else {
                    System.out.println("You don't have enough budget for this accessory. Try again.");
                }
            } else {
                System.out.println("Accessory not found or not affordable. Please select from the list.");
            }

            if (remainingBudget <= 0) {
                System.out.println("Budget is ₹0. Do you want to finish? Press Y to finish or N to continue: ");
                String finishChoice = scanner.nextLine().trim();
                if (finishChoice.equalsIgnoreCase("Y")) {
                    break;
                } else {
                    System.out.println("You cannot buy more accessories. Budget is 0.");
                    break;
                }
            }
        }

        double onRoadPrice = onRoadPriceCalculate(selectedCarprice);
        double totalCarCost = onRoadPrice + selectedAccessoriescost;
        System.out.println("Total cost of the car with accessories (including taxes): ₹" + totalCarCost);
        System.out.println("Total cost of selected accessories: ₹" + selectedAccessoriescost);
        System.out.println("Remaining budget after accessories: ₹" + remainingBudget);
    }

}
