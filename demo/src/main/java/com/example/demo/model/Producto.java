package com.example.levelupgamerapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // En Android es Int, lo puedes mapear sin problema

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private int precio; // En CLP

    private String imagen; // nombre de archivo o URL

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private int stock;
}
