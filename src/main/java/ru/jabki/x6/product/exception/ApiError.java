package ru.jabki.x6.product.exception;

import lombok.Data;

@Data
public class ApiError {
    final boolean isSuccess;
    final String message;
}