package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ControladorServidorChatInt extends Remote
{
    public boolean registrarReferenciaUsuario(UsuarioCllbckInt  usuario, String nickname) throws RemoteException;
    public void enviarMensajePrivado(String mensaje, String origen, String destinatario )throws RemoteException;
    public void enviarMensaje(String mensaje, String origen )throws RemoteException;
    public List<String> listarUsuariosActivos() throws RemoteException;
    public boolean desconectarse() throws RemoteException;
}


