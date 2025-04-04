package co.edu.unicauca.servidor.controladores;


import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControladorServidorChatImpl extends UnicastRemoteObject implements ControladorServidorChatInt {
    //private final List<UsuarioCllbckInt> usuarios;//lista que almacena la referencia remota de los clientes
    private final HashMap<UsuarioCllbckInt, String> usuariosConectados;//almacena el nombre de los usuarios conectados
    public ControladorServidorChatImpl() throws RemoteException
    {
        super();//asignamos el puerto 
        usuariosConectados = new HashMap<>();
    }
    
    @Override
    public synchronized boolean  registrarReferenciaUsuario(UsuarioCllbckInt usuario, String nickname) throws RemoteException 
    {
       //método que unicamente puede ser accedido por un hilo
	    System.out.println("Invocando al método registrar usuario desde el servidor");
        if (!(usuariosConectados.containsValue(nickname) &&usuariosConectados.containsKey(usuario)))
        {
            return (null!=usuariosConectados.put(usuario, nickname));  
        }          
        return false;
    }

    @Override
    public void enviarMensaje(String mensaje, String origen)throws RemoteException 
    {        
        notificarUsuarios("-"+origen+": " + mensaje);
    }
    
    private void notificarUsuarios(String mensaje) throws RemoteException 
    {
        System.out.println("Invocando al método notificar usuarios desde el servidor");
        for (UsuarioCllbckInt usuario : usuariosConectados.keySet()) {
            usuario.notificar(mensaje, usuariosConectados.size());
        }
    }

    @Override
    public List<String> listarUsuariosActivos() throws RemoteException {
        List<String> nicknamesList = new ArrayList<>();
        for (String nickname : usuariosConectados.values()) {
            nicknamesList.add(nickname);
        }
        return nicknamesList;
    }

    @Override
    public void enviarMensajePrivado(String mensaje, String origen, String destinatario) throws RemoteException {
        System.out.println("Invocando al método enviar mensaje privado desde el servidor");
        for (UsuarioCllbckInt usuario : usuariosConectados.keySet()) {
            if (usuariosConectados.get(usuario).equals(destinatario)) {
                usuario.notificar("-"+origen+"(privado): " + mensaje, usuariosConectados.size());
            }
        }
    }

    @Override
    public boolean desconectarse(String nickname) throws RemoteException {
        System.out.println("Invocando al método desconectarse desde el servidor");
        for(UsuarioCllbckInt usuario : usuariosConectados.keySet()) {
            if (usuariosConectados.get(usuario).equals(nickname)) {
                usuariosConectados.remove(usuario);
                notificarUsuarios(nickname + " se ha desconectado.");
                return true;
            }
        }
        return true;
    }
}
