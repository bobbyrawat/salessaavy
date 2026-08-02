package com.example.salessaavy.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {

    private List<CartItemResponseDto> items;

    private Integer totalQuantity;

    private Double totalAmount;
}