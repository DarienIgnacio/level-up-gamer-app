package com.example.demo.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String email;
    private String rut;
    private String password;
    private boolean esAdmin;
}
