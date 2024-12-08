import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Csvfilecreation {

    public static void main(String[] args) throws IOException {
        carVariantCreation();
        carAccessoriesCreation();
    }

    private static void carVariantCreation() throws IOException {
        Map<String, Map<String, List<String>>> carNames = new HashMap<>();

        carNames.put("Alto K10", Map.of("variants", Arrays.asList("STD", "LXI", "VXI", "VXI+"), "priceRange", Arrays.asList("3.99", "4.25", "4.75", "5.00", "5.96")));
        carNames.put("Wagon R", Map.of("variants", Arrays.asList("LXI", "VXI", "ZXI", "ZXI+"), "priceRange", Arrays.asList("5.54", "6.25", "6.75", "7.00", "7.38")));
        carNames.put("Celerio", Map.of("variants", Arrays.asList("LXI", "VXI", "ZXI", "ZXI+"), "priceRange", Arrays.asList("4.99", "6.00", "6.75", "7.04")));
        carNames.put("Fronx", Map.of("variants", Arrays.asList("Sigma", "Delta", "Zeta", "Alpha", "Alpha Dual Tone"), "priceRange", Arrays.asList("7.51", "10", "11", "12", "13.04")));
        carNames.put("Jimny", Map.of("variants", Arrays.asList("Zeta", "Alpha"), "priceRange", Arrays.asList("12.74", "14.95")));

        FileWriter write = new FileWriter("CarVariants.csv");

        for (Map.Entry<String, Map<String, List<String>>> car : carNames.entrySet()) {
            String carModel = car.getKey();
            List<String> variants = car.getValue().get("variants");
            List<String> priceRange = car.getValue().get("priceRange");

            for (int i = 0; i < variants.size(); i++) {
                String variant = variants.get(i);
                String price = priceRange.get(i);
                write.append(carModel).append(",").append(variant).append(",₹").append(price).append("\n");
            }
        }

        write.close();
    }

    private static void carAccessoriesCreation() throws IOException {
        Map<String, Map<String, String>> accessoriesPrices = new HashMap<>();
        accessoriesPrices.put("Exterior Accessories", Map.of("Alloy Wheels", "₹15,000 - ₹40,000", "Body Side Moulding", "₹1,500 - ₹3,000", "Door Visors", "₹1,000 - ₹2,500", "Mud Flaps", "₹500 - ₹1,200", "Spoilers", "₹3,000 - ₹8,000", "Chrome Garnish", "₹1,000 - ₹5,000", "Roof Rails", "₹2,500 - ₹6,000", "Side Skirts", "₹4,000 - ₹10,000"));

        accessoriesPrices.put("Interior Accessories", Map.of("Seat Covers", "₹2,000 - ₹10,000", "Floor Mats", "₹1,000 - ₹3,000", "Steering Wheel Covers", "₹500 - ₹1,500", "Sunshades", "₹500 - ₹2,000", "Dashboard Kits", "₹2,000 - ₹5,000", "Ambient Lighting", "₹2,000 - ₹6,000", "Car Perfumes", "₹200 - ₹1,000"));

        accessoriesPrices.put("Infotainment and Electronics", Map.of("Touchscreen Infotainment Systems", "₹10,000 - ₹50,000", "Speakers and Amplifiers", "₹5,000 - ₹20,000", "Reverse Parking Sensors", "₹2,000 - ₹5,000", "Rear View Cameras", "₹3,000 - ₹7,000", "GPS Navigation Systems", "₹5,000 - ₹15,000", "Car Chargers", "₹300 - ₹1,000"));

        accessoriesPrices.put("Safety and Security", Map.of("Car Alarms", "₹2,000 - ₹5,000", "Central Locking Systems", "₹3,000 - ₹8,000", "Fire Extinguishers", "₹500 - ₹2,000", "First Aid Kits", "₹300 - ₹1,000", "Child Safety Seats", "₹5,000 - ₹20,000"));

        accessoriesPrices.put("Car Care", Map.of("Car Covers", "₹1,000 - ₹5,000", "Cleaning Kits", "₹500 - ₹2,000", "Polish and Wax", "₹300 - ₹1,500", "Pressure Washers", "₹5,000 - ₹15,000"));
        FileWriter accessoriesWrite = new FileWriter("CarAccessories.csv");

        for (Map.Entry<String, Map<String, String>> categoryEntry : accessoriesPrices.entrySet()) {
            String category = categoryEntry.getKey();
            for (Map.Entry<String, String> accessoryEntry : categoryEntry.getValue().entrySet()) {
                String accessoryName = accessoryEntry.getKey();
                String priceRange = accessoryEntry.getValue();
                accessoriesWrite.append(category).append(",").append(accessoryName).append(",").append(priceRange).append("\n");
            }
        }

        accessoriesWrite.close();
    }
}
