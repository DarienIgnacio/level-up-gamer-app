package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CarritoItem;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CarritoItemRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CarritoService {

    private final CarritoItemRepository carritoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;

    public CarritoService(CarritoItemRepository carritoRepo,
                          UsuarioRepository usuarioRepo,
                          ProductoRepository productoRepo) {
        this.carritoRepo = carritoRepo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
    }

    public List<CarritoItem> obtenerCarrito(Long usuarioId) {
        return carritoRepo.findByUsuarioId(usuarioId);
    }

    public CarritoItem agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        // Si ya existe el item, sumamos cantidad
        CarritoItem item = carritoRepo.findByUsuarioIdAndProductoId(usuarioId, productoId)
                .orElse(CarritoItem.builder()
                        .usuario(usuario)
                        .producto(producto)
                        .cantidad(0)
                        .build());

        item.setCantidad(item.getCantidad() + cantidad);

        if (item.getCantidad() <= 0) {
            carritoRepo.delete(item);
            throw new RuntimeException("Cantidad no puede ser <= 0");
        }

        return carritoRepo.save(item);
    }

    public CarritoItem actualizarCantidad(Long itemId, int cantidad) {
        CarritoItem item = carritoRepo.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado"));

        if (cantidad <= 0) {
            carritoRepo.delete(item);
            throw new RuntimeException("Cantidad no puede ser <= 0");
        }

        item.setCantidad(cantidad);
        return carritoRepo.save(item);
    }

    public void eliminarItem(Long itemId) {
        if (!carritoRepo.existsById(itemId)) {
            throw new ResourceNotFoundException("Item no encontrado");
        }
        carritoRepo.deleteById(itemId);
    }

    public void vaciarCarrito(Long usuarioId) {
        List<CarritoItem> items = carritoRepo.findByUsuarioId(usuarioId);
        carritoRepo.deleteAll(items);
    }
}

