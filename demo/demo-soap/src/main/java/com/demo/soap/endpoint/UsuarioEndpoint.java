package com.demo.soap.endpoint;

import com.demo.soap.usuarios.GetUsuarioRequest;
import com.demo.soap.usuarios.GetUsuarioResponse;
import com.demo.soap.usuarios.UsuarioSoap;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@Endpoint
public class UsuarioEndpoint {

    private static final String NAMESPACE_URI = "http://demo.com/soap/usuarios";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsuarioRequest")
    @ResponsePayload
    public GetUsuarioResponse getUsuario(@RequestPayload GetUsuarioRequest request) {
        if (request.getId() != 1L) {
            throw new UsuarioNotFoundException("Usuario no encontrado con ID: " + request.getId());
        }

        UsuarioSoap usuario = new UsuarioSoap();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@example.com");

        GetUsuarioResponse response = new GetUsuarioResponse();
        response.setUsuario(usuario);
        return response;
    }
}

@SoapFault(faultCode = FaultCode.CLIENT)
class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
