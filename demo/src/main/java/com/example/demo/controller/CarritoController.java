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
@CrossOrigin(origins = "*")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }

    @PostMapping("/agregar")
    public ResponseEntity<CarritoItem> agregar(@RequestBody CarritoAgregarRequest req) {
        CarritoItem item = carritoService.agregarProducto(req.getUsuarioId(), req.getProductoId(), req.getCantidad());
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CarritoItem> actualizarCantidad(
            @PathVariable Long itemId,
            @RequestBody CarritoActualizarCantidadRequest req
    ) {
        CarritoItem item = carritoService.actualizarCantidad(itemId, req.getCantidad());
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long itemId) {
        carritoService.eliminarItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> vaciar(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}

