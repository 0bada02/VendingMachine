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
        initializeMoney();
    }

    public void initializeMoney() {
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
        return items.stream().anyMatch(item -> item.getId() == id);
    }

    public void addItem(Item item) {
        int cnt;
        if (items.isEmpty()) {
            items.add(item);
            cnt = item.getQuantity();
        } else {
            int size = 0;
            for (Item i : items)
                size += i.getQuantity();

            if (size == maxSizeMachine) {
                System.out.println("Item cannot be added; maximum number exceeded.");
                return;
            } else if (!existItemById(item.getId())) {
                item.setQuantity(item.getQuantity() + size <= maxSizeMachine ? item.getQuantity() : maxSizeMachine - size);
                cnt = item.getQuantity();
                items.add(item);
            } else {
                Item i = getItemById(item.getId());
                i.setQuantity(i.getQuantity() + item.getQuantity() <= maxSizeMachine ? i.getQuantity() + item.getQuantity() : maxSizeMachine - size);
                cnt = i.getQuantity();
            }
        }
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

    public String showItemsInMachine() {
        if (items.isEmpty()) {
            System.out.println("No items added yet.");
            return null;
        }
        StringBuilder allItems = new StringBuilder();
        allItems.append(String.format("\n%-3s | %-10s | %-7s | %-7s", "ID", "Name", "Price", "Quantity"));
        allItems.append("\n--------------------------------------");
        for (Item item : items) {
            allItems.append(String.format("\n%-3d | %-10s | %-7.2f | %-7d", item.getId(), item.getName(), item.getPrice(), item.getQuantity()));
        }
        return allItems.toString();
    }

    public void showMoneysInMachine() {
        double total = 0;
        StringBuilder allMoneys = new StringBuilder();

        allMoneys.append(String.format("\n%-10s | %-7s", "Amount", "Count"));
        allMoneys.append("\n-------------------");
        for (Map.Entry<Double, Integer> m : moneys.entrySet()) {
            allMoneys.append(String.format("\n%-2.2f JD       | %-7d", m.getKey(), m.getValue()));
            total += m.getKey() * m.getValue();
        }
        allMoneys.append("\n___________________");
        allMoneys.append(String.format("\nTotal: %-10.2f\n", total));
        System.out.println(allMoneys);
    }

    public void fillingMachine() {
        System.out.print("Enter the amount to fill: ");
        double amount = scanner.nextDouble();

        if (!moneys.containsKey(amount)) {
            System.out.println("Invalid denomination.");
            return;
        }

        System.out.print("Enter the count: ");
        int count = scanner.nextInt();

        moneys.put(amount, moneys.get(amount) + count);
        System.out.printf("Added %d units of %.2f JD to the machine.\n", count, amount);
    }

    public void buyItem() {
        char finish;
        if (items.isEmpty()) {
            System.out.println("No items are available in the machine.");
            return;
        }
        do {
            boolean remove = true;
            double price;
            System.out.println(showItemsInMachine());
            System.out.print("Enter the item ID you want to buy: ");
            int id = scanner.nextInt();

            System.out.print("How many do you want: ");
            int quantity = scanner.nextInt();
            Item item = getItemById(id);

            if (item.getQuantity() == quantity) {
                System.out.println("This item is available.");
            } else if (item.getQuantity() < quantity) {
                System.out.println("This item has " + item.getQuantity() + " times.");
                quantity = item.getQuantity();
            } else {
                System.out.println("This item is available.");
                remove = false;
            }

            price = item.getPrice() * quantity;

            if (cashPayment(price)) {
                if (remove) {
                    removeItem(id);
                } else {
                    item.setQuantity(item.getQuantity() - quantity);
                }
            }

            System.out.print("\nDo you want to complete the purchase? (Yes/No):");
            finish = Character.toUpperCase(scanner.next().charAt(0));
        } while (finish != 'Y');
    }

    public boolean cashPayment(double price) {
        double total = 0;
        for (Map.Entry<Double, Integer> m : moneys.entrySet()) {
            total += m.getKey() * m.getValue();
        }

        double remain;
        System.out.print("Required amount " + price + " JD: ");
        double cash = scanner.nextDouble();

        while (cash < price) {
            System.out.print("Remaining amount" + (price - cash) + "JD, Amount paid " + cash + " JD: ");
            cash += scanner.nextDouble();
        }

        if (total < cash) {
            System.out.println("Sorry, there is no money in the machine.");
            return false;
        }

        if (cash == price) {
            System.out.println("Payment has been made successfully(:");
            return true;
        } else {
            remain = cash - price;
            double req = 0;
            for (Map.Entry<Double, Integer> m : moneys.descendingMap().entrySet()) {
                do {
                    if (req != remain && m.getValue() >= 1) {
                        if (req + m.getKey() > remain) {
                            break;
                        } else {
                            req += m.getKey();
                            moneys.put(m.getKey(), moneys.get(m.getKey()) - 1);
                        }
                    } else {
                        break;
                    }
                } while (true);
                if (req == remain)
                    break;
            }
            if (req != remain) {
                System.out.println("Sorry, there is no money in the machine.");
                return false;
            } else {
                System.out.println("The rest of the amount " + remain + " JD, Thank you for purchasing (:");
                return true;
            }
        }

    }
}
