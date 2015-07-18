package com.ampaiva.wrobotserver.handler;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quickserver.net.server.ClientHandler;

import com.ampaiva.wrobotserver.Proxy;
import com.ampaiva.wrobotserver.data.OperadorData;
import com.ampaiva.wrobotserver.data.OperadorData.TipoStatus;

public class OperadorHandler extends CommonHandler {

    private static final Log LOG = LogFactory.getLog(OperadorHandler.class);

    @Override
    public void gotConnected(ClientHandler handler) throws SocketTimeoutException, IOException {
        sendClientMsgln(handler, "[1]");
        OperadorData data = (OperadorData) handler.getClientData();
        data.setId(handler.toString());
        Proxy.operadores.put(data.getId(), handler);
        data.setOperadorStatus(TipoStatus.ESTADO_AGUARDANDO_NOME);
    }

    private void connectionClosed(ClientHandler handler) throws IOException {
        OperadorData data = (OperadorData) handler.getClientData();
        Proxy.operadores.remove(data.getId());
    }

    @Override
    public void lostConnection(ClientHandler handler) throws IOException {
        handler.sendSystemMsg("Connection lost : " + handler.getSocket().getInetAddress());
        connectionClosed(handler);
    }

    @Override
    public void closingConnection(ClientHandler handler) throws IOException {
        handler.sendSystemMsg("Closing connection : " + handler.getSocket().getInetAddress());
        connectionClosed(handler);
    }

    @Override
    public void handleCommand(ClientHandler handler, String command) throws SocketTimeoutException, IOException {
        OperadorData data = (OperadorData) handler.getClientData();
        if (LOG.isDebugEnabled()) {
            LOG.debug("handleCommand(" + handler + "," + command + "," + data.getOperadorStatus() + ")");
        }

        if (data.getOperadorStatus() == OperadorData.TipoStatus.ESTADO_AGUARDANDO_NOME) {
            data.setName(command);
            data.setOperadorStatus(TipoStatus.ESTADO_CONECTADO);
        } else if (data.getOperadorStatus() == OperadorData.TipoStatus.ESTADO_CONECTADO) {
        } else {
            sendClientMsgln(handler, "");
            sendClientMsgln(handler, "Logged out.");
            handler.closeConnection();
        }
    }
}
