package com.progressoft.samples;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private int id;
    private String name;
    private double price;
    private int quantity;
}
