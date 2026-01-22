package ru.jabki.x6.product.model;

import lombok.Data;

@Data
public class ApiError {
    final boolean success;
    final String message;
}