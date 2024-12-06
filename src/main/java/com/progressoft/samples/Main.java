package com.progressoft.samples;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        Scanner scanner = new Scanner(System.in);

        boolean finish = false;
        while (!finish) {
            System.out.println("Who are you?");
            System.out.println("1. Buyer.");
            System.out.println("2. Owner.");
            System.out.println("3. Exit.");
            System.out.print("Enter your choice: ");
            int you = scanner.nextInt();
            System.out.println();

            if (you == 1) {
                boolean exit = false;
                while (!exit) {
                    System.out.println("What you want?");
                    System.out.println("1. Show Items.");
                    System.out.println("2. Buy Item.");
                    System.out.println("3. Exit.");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            if (vm.showItemsInMachine() != null)
                                System.out.println(vm.showItemsInMachine());
                            break;
                        case 2:
                            vm.buyItem();
                            break;
                        case 3:
                            exit = true;
                            break;
                    }
                    System.out.println();
                }
            } else if (you == 2) {
                boolean exit = false;
                while (!exit) {
                    System.out.println("What you want?");
                    System.out.println("1. Add Item.");
                    System.out.println("2. Remove Item.");
                    System.out.println("3. Fill Machine.");
                    System.out.println("4. Show Items.");
                    System.out.println("5. Show Moneys.");
                    System.out.println("6. Exit.");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.print("Enter Item ID: ");
                            int id = scanner.nextInt();
                            System.out.print("Enter Item Name: ");
                            String name = scanner.next();
                            System.out.print("Enter Item Price: ");
                            double price = scanner.nextDouble();
                            System.out.print("Enter Quantity: ");
                            int quantity = scanner.nextInt();
                            vm.addItem(new Item(id, name, price, quantity));
                            break;
                        case 2:
                            System.out.print("Enter Item ID you want to remove: ");
                            int idx = scanner.nextInt();
                            System.out.println();
                            vm.removeItem(idx);
                            break;
                        case 3:
                            vm.fillingMachine();
                            break;
                        case 4:
                            if (vm.showItemsInMachine() != null)
                                System.out.println(vm.showItemsInMachine());
                            break;
                        case 5:
                            vm.showMoneysInMachine();
                            break;
                        case 6:
                            exit = true;
                            break;
                    }
                    System.out.println();
                }
            } else {
                finish = true;
            }
        }
    }
}
