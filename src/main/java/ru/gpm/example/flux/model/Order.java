package ru.gpm.example.flux.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Order {

    private Integer number;
    private String sku;
    private int qty;
}
