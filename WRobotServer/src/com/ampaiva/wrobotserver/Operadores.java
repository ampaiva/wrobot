package com.ampaiva.wrobotserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.quickserver.net.server.ClientHandler;

import com.ampaiva.wrobotserver.data.OperadorData;

public class Operadores implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2888285235962288485L;

    public Collection<OperadorData> getClientes() {
        Collection<OperadorData> operadores = new ArrayList<OperadorData>();
        for (ClientHandler clientHandler : Proxy.operadores.values()) {
            operadores.add((OperadorData) clientHandler.getClientData());
        }
        return operadores;
    }
}
