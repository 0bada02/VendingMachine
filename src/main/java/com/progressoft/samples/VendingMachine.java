package com.progressoft.samples;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class VendingMachine {
    private static Integer maxSizeMachine = 500;
    private ArrayList<Item> items = new ArrayList<>();
    private TreeMap<Double, Integer> moneys = new TreeMap<>();
    private Scanner scanner = new Scanner(System.in);

    public VendingMachine() {
        moneys.put(Money.OnePiaster.amount(), 0);
        moneys.put(Money.FivePiasters.amount(), 0);
        moneys.put(Money.TenPiasters.amount(), 0);
        moneys.put(Money.TwentyFivePiasters.amount(), 0);
        moneys.put(Money.FiftyPiasters.amount(), 0);
        moneys.put(Money.OneDinar.amount(), 0);
        moneys.put(Money.FiveDinars.amount(), 0);
        moneys.put(Money.TenDinars.amount(), 0);
        moneys.put(Money.TwentyDinars.amount(), 0);
        moneys.put(Money.FiftyDinars.amount(), 0);
    }

    public Item getItemById(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        System.out.printf("Item not found by id %d.\n", id);
        return null;
    }

    public boolean existItemById(int id) {
        return items.stream().noneMatch(item -> item.getId() == id);
    }

    public int getMachineSize() {
        int size = 0;
        for (Item i : items)
            size += i.getQuantity();

        return size;
    }

    public void checkQuantityAddition(Item item) {
        int size = getMachineSize();

        if (size == maxSizeMachine) {
            System.out.println("Item cannot be added; maximum number exceeded.");
        } else if (existItemById(item.getId())) {
            item.setQuantity(item.getQuantity() + size <= maxSizeMachine ? item.getQuantity() : maxSizeMachine - size);
            items.add(item);
        } else {
            Item i = getItemById(item.getId());
            item.setQuantity(i.getQuantity() + item.getQuantity() <= maxSizeMachine ? item.getQuantity() : maxSizeMachine - size);
            i.setQuantity(i.getQuantity() + item.getQuantity());
        }
    }

    public boolean checkQuantityToRemove(Item item, int quantity) {
        if (item.getQuantity() > quantity) {
            System.out.println("This item is available.");
            return true;
        } else {
            System.out.println("This item has " + item.getQuantity() + " times.");
            return false;
        }
    }

    public void addItem(Item item) {
        if (items.isEmpty()) {
            item.setQuantity(Math.min(item.getQuantity(), maxSizeMachine)); // Ensure no exceeded machine size
            items.add(item);
        } else {
            checkQuantityAddition(item);
        }
        int cnt = item.getQuantity();
        System.out.printf("Added %d %s item.\n", cnt, item.getName());
    }

    public void removeItem(int id) {
        Item item = getItemById(id);
        if (item != null) {
            items.remove(item);
            System.out.printf("Removed %s from the machine.\n", item.getName());
        } else {
            System.out.println("Item not found.");
        }
    }

    public void showItemsInMachine() {
        if (items.isEmpty()) {
            System.out.println("No items added yet.");
        } else {
            StringBuilder allItems = new StringBuilder();
            allItems.append(String.format("\n%-3s | %-10s | %-7s | %-7s", "ID", "Name", "Price", "Quantity"));
            allItems.append("\n--------------------------------------");
            for (Item item : items) {
                allItems.append(String.format("\n%-3d | %-10s | %-7.2f | %-7d", item.getId(), item.getName(), item.getPrice(), item.getQuantity()));
            }
            System.out.println(allItems);
        }
    }

    public void showMoneysInMachine() {
        double total = 0;
        StringBuilder allMoneys = new StringBuilder();

        allMoneys.append(String.format("\n%-10s | %-5s", "Amount", "Count"));
        allMoneys.append("\n-------------------");
        for (Map.Entry<Double, Integer> m : moneys.entrySet()) {
            allMoneys.append(String.format("\n%05.2f JD   | %-7d", m.getKey(), m.getValue()));
            total += m.getKey() * m.getValue();
        }
        allMoneys.append("\n__________________");
        allMoneys.append(String.format("\nTotal: %-10.2f\n", total));
        System.out.println(allMoneys);
    }

    public void fillingMachine() {
        char finish;
        do {
            System.out.print("Enter the amount to fill: ");
            double amount = scanner.nextDouble();

            if (!moneys.containsKey(amount)) {
                System.out.println("Invalid denomination.");
            } else {
                System.out.print("Enter the count: ");
                int count = scanner.nextInt();
                moneys.put(amount, moneys.get(amount) + count);
                System.out.printf("Added %d units of %.2f JD to the machine.\n", count, amount);
            }

            System.out.print("\nDo you want to complete the purchase? (Yes/No): ");
            finish = Character.toUpperCase(scanner.next().charAt(0));

        } while (finish == 'Y');
    }

    public void buyItem() {
        char finish;
        if (items.isEmpty()) {
            System.out.println("No items are available in the machine.");
            return;
        }
        do {
            boolean remove;
            double price;

            showItemsInMachine();

            System.out.print("Enter the item ID you want to buy: ");
            int id = scanner.nextInt();
            if (existItemById(id)) {
                System.out.printf("Item not found with ID %d.\n", id);
                System.out.print("\nDo you want to complete the purchase? (Yes/No): ");
                finish = Character.toUpperCase(scanner.next().charAt(0));
                continue;
            }

            System.out.print("How many do you want: ");
            int quantity = scanner.nextInt();

            Item item = getItemById(id);

            remove = checkQuantityToRemove(item, quantity);

            price = item.getPrice() * quantity;


            TreeMap<Double, Integer> tempMoneys = new TreeMap<>(moneys);
            if (Payment.cash(price, moneys)) {
                if (!remove) {
                    removeItem(id);
                } else {
                    item.setQuantity(item.getQuantity() - quantity);
                }
            } else {
                moneys = tempMoneys;
            }

            System.out.print("\nDo you want to complete the purchase? (Yes/No): ");
            finish = Character.toUpperCase(scanner.next().charAt(0));
        } while (finish == 'Y');
    }
}