package com.example.demo.controller;

import com.example.demo.dto.CarritoAgregarRequest;
import com.example.demo.dto.CarritoActualizarCantidadRequest;
import com.example.demo.model.CarritoItem;
import com.example.demo.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*") // Permitir acceso desde Android
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // =========================================================
    // Obtener carrito de un usuario
    // =========================================================
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }

    // =========================================================
    // Agregar producto al carrito
    // =========================================================
    @PostMapping("/agregar")
    public ResponseEntity<CarritoItem> agregarProducto(@RequestBody CarritoAgregarRequest req) {
        return ResponseEntity.ok(carritoService.agregarProducto(req));
    }

    // =========================================================
    // Actualizar cantidad de un item
    // =========================================================
    @PutMapping("/{itemId}/cantidad")
    public ResponseEntity<CarritoItem> actualizarCantidad(
            @PathVariable Long itemId,
            @RequestBody CarritoActualizarCantidadRequest req
    ) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(itemId, req));
    }

    // =========================================================
    // Eliminar item del carrito
    // =========================================================
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long itemId) {
        carritoService.eliminarItem(itemId);
        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // Vaciar carrito de un usuario
    // =========================================================
    @DeleteMapping("/vaciar/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
