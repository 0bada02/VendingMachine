package com.progressoft.samples;

import java.util.*;

public class Payment {

    public static boolean cash(double price, TreeMap<Double, Integer> moneys) {
        double total = 0;
        Scanner scanner = new Scanner(System.in);


        for (Map.Entry<Double, Integer> m : moneys.entrySet()) {
            total += m.getKey() * m.getValue();
        }

        double required;
        System.out.printf("Required amount %.2f JD: ", price);
        double paid = scanner.nextDouble();

        while (paid < price) {
            System.out.printf("Amount paid %.3f JD, Enter remaining amount %.3f JD: ", paid, (price - paid));
            paid += scanner.nextDouble();
        }

        if (paid == price) {
            System.out.println("Payment has been made successfully(:");
            return true;
        }

        if (total < (paid - price)) {
            System.out.println("Sorry, there is no money in the machine.");
            return false;
        } else {
            required = paid - price;
            double remain = 0;
            for (Map.Entry<Double, Integer> m : moneys.descendingMap().entrySet()) {
                while (true) {
                    if (remain != required && m.getValue() >= 1 && (remain + m.getKey()) <= required) {
                        remain += m.getKey();
                        moneys.put(m.getKey(), moneys.get(m.getKey()) - 1);
                        continue;
                    }
                    break;
                }
            }
            if (required != required) {
                System.out.println("Sorry, there is no money in the machine.");
                return false;
            } else {
                System.out.printf("The rest of the amount %.2f JD, Thank you for purchasing (:\n", required);
                return true;
            }
        }

    }
}
