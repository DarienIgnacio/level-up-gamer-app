package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public UsuarioResponse registrar(RegisterRequest req) {
        // Podrías validar rut, email, etc. aquí
        Usuario user = Usuario.builder()
                .nombre(req.getNombre())
                .email(req.getEmail())
                .rut(req.getRut())
                .password(req.getPassword()) // En producción: encriptar
                .esAdmin(req.isEsAdmin())
                .build();

        Usuario saved = repo.save(user);
        return toResponse(saved);
    }

    public UsuarioResponse login(String email, String password) {
        Usuario user = repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return toResponse(user);
    }

    public UsuarioResponse obtenerPorId(Long id) {
        Usuario user = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        return toResponse(user);
    }

    private UsuarioResponse toResponse(Usuario user) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.setId(user.getId());
        dto.setNombre(user.getNombre());
        dto.setEmail(user.getEmail());
        dto.setRut(user.getRut());
        dto.setEsAdmin(user.isEsAdmin());
        return dto;
    }
}

