package ru.gpm.example.flux.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserInfoOrders {

    private User user;
    private List<Order> orders = new ArrayList<>();

}
