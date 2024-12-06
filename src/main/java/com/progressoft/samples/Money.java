package com.progressoft.samples;

public class Money {

    private final double amount;

    public static final Money Zero = new Money(0.00);
    public static final Money OnePiaster = new Money(0.01);
    public static final Money FivePiasters = new Money(0.05);
    public static final Money TenPiasters = new Money(0.10);
    public static final Money TwentyFivePiasters = new Money(0.25);
    public static final Money FiftyPiasters = new Money(0.50);
    public static final Money OneDinar = new Money(1.00);
    public static final Money FiveDinars = new Money(5.00);
    public static final Money TenDinars = new Money(10.00);
    public static final Money TwentyDinars = new Money(20.00);
    public static final Money FiftyDinars = new Money(50.00);

    public Money(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative.");
        this.amount = amount;
    }

    public double amount() {
        return amount;
    }

    public Money times(int count) {
        if (count < 0)
            throw new IllegalArgumentException("Count cannot be negative.");
        return new Money(amount * count);
    }

    public static Money sum(Money... items) {
        double sum = 0;
        for (Money item : items)
            sum += item.amount;
        return new Money(sum);
    }

    public Money plus(Money other) {
        return new Money(amount + other.amount);
    }

    public Money minus(Money other) {
        if (amount < other.amount)
            throw new IllegalArgumentException("Cannot subtract " + other.amount + " from " + amount);
        return new Money(amount - other.amount);
    }

    @Override
    public String toString() {
        return String.format("%.2f", amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Money)
            return amount == ((Money) o).amount;
        else
            return false;
    }
}
