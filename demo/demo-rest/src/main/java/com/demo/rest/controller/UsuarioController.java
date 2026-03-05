package com.demo.rest.controller;

import com.demo.rest.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuario", description = "Endpoints para gestionar usuarios y demostrar códigos de estado HTTP")
public class UsuarioController {

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID", description = "Retorna 200 si existe, 404 si no.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        if (id == 1L) {
            Usuario usuario = new Usuario(1L, "Juan Pérez", "juan.perez@example.com");
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear un usuario", description = "Demuestra el código 201 Created.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        // Simulación de creación
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping("/status/100")
    @Operation(summary = "Demostrar código 100", description = "Retorna 100 Continue (simulado vía HttpStatus).")
    @ApiResponse(responseCode = "100", description = "Continue")
    public ResponseEntity<String> getStatus100() {
        return ResponseEntity.status(HttpStatus.CONTINUE).body("Continue...");
    }

    @GetMapping("/status/300")
    @Operation(summary = "Demostrar código 302", description = "Retorna 302 Found (Redirección).")
    @ApiResponse(responseCode = "302", description = "Redirección a Google")
    public ResponseEntity<Void> getStatus300() {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://www.google.com")).build();
    }

    @GetMapping("/status/400")
    @Operation(summary = "Demostrar código 400", description = "Retorna 400 Bad Request.")
    @ApiResponse(responseCode = "400", description = "Petición incorrecta")
    public ResponseEntity<String> getStatus400() {
        return ResponseEntity.badRequest().body("Solicitud mal formada.");
    }

    @GetMapping("/status/500")
    @Operation(summary = "Demostrar código 500", description = "Retorna 500 Internal Server Error.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<String> getStatus500() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado en el servidor.");
    }
}
