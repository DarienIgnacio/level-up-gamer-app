package com.example.demo.dto;

import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private String rut;
    private boolean esAdmin;
}
