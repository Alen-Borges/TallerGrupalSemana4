package com.demo.soap.endpoint;

import com.demo.soap.usuarios.GetUsuarioRequest;
import com.demo.soap.usuarios.GetUsuarioResponse;
import com.demo.soap.usuarios.GetUsuariosRequest;
import com.demo.soap.usuarios.GetUsuariosResponse;
import com.demo.soap.usuarios.UsuarioSoap;
import com.demo.soap.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

import java.util.List;

@Endpoint
public class UsuarioEndpoint {

    private static final String NAMESPACE_URI = "http://demo.com/soap/usuarios";

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioEndpoint(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsuarioRequest")
    @ResponsePayload
    public GetUsuarioResponse getUsuario(@RequestPayload GetUsuarioRequest request) {
        UsuarioSoap usuario = usuarioService.findById(request.getId());
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuario no encontrado con ID: " + request.getId());
        }

        GetUsuarioResponse response = new GetUsuarioResponse();
        response.setUsuario(usuario);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsuariosRequest")
    @ResponsePayload
    public GetUsuariosResponse getUsuarios(@RequestPayload GetUsuariosRequest request) {
        List<UsuarioSoap> usuarios = usuarioService.findAll();
        
        GetUsuariosResponse response = new GetUsuariosResponse();
        response.getUsuarios().addAll(usuarios);
        return response;
    }
}

@SoapFault(faultCode = FaultCode.CLIENT)
class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
