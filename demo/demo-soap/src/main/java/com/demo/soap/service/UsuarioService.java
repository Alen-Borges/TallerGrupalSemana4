package com.demo.soap.service;

import com.demo.soap.usuarios.UsuarioSoap;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    private static final Map<Long, UsuarioSoap> usuarios = new HashMap<>();

    @PostConstruct
    public void initData() {
        UsuarioSoap juan = new UsuarioSoap();
        juan.setId(1L);
        juan.setNombre("Juan Pérez");
        juan.setEmail("juan.perez@example.com");
        usuarios.put(juan.getId(), juan);

        UsuarioSoap maria = new UsuarioSoap();
        maria.setId(2L);
        maria.setNombre("Maria Garcia");
        maria.setEmail("maria.garcia@example.com");
        usuarios.put(maria.getId(), maria);
        
        UsuarioSoap pedro = new UsuarioSoap();
        pedro.setId(3L);
        pedro.setNombre("Pedro Rodriguez");
        pedro.setEmail("pedro.rodriguez@example.com");
        usuarios.put(pedro.getId(), pedro);
    }

    public UsuarioSoap findById(Long id) {
        return usuarios.get(id);
    }

    public List<UsuarioSoap> findAll() {
        return new ArrayList<>(usuarios.values());
    }
}
