package com.example.demo.dto;

import lombok.Data;

@Data
public class CarritoAgregarRequest {
    private Long usuarioId;
    private Long productoId;
    private int cantidad;
}
