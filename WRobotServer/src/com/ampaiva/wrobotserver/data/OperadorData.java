package com.ampaiva.wrobotserver.data;

import org.quickserver.net.server.ClientData;

public class OperadorData implements ClientData {
    private static final long serialVersionUID = 6462747640816879763L;

    public enum TipoStatus {
        ESTADO_AGUARDANDO_NOME, ESTADO_CONECTADO
    };

    private TipoStatus operadorStatus;

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoStatus getOperadorStatus() {
        return operadorStatus;
    }

    public void setOperadorStatus(TipoStatus operadorStatus) {
        this.operadorStatus = operadorStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OperadorData [id=" + id + ", Nome=" + name + ", Status=" + operadorStatus + "]";
    }
}
