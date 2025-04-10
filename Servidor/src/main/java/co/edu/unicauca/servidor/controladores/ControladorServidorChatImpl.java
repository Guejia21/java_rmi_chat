package co.edu.unicauca.servidor.controladores;


import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControladorServidorChatImpl extends UnicastRemoteObject implements ControladorServidorChatInt {
    //private final List<UsuarioCllbckInt> usuarios;//lista que almacena la referencia remota de los clientes
    private final HashMap<String, UsuarioCllbckInt> usuariosConectados;//almacena el nombre de los usuarios conectados
    public ControladorServidorChatImpl() throws RemoteException
    {
        super();//asignamos el puerto 
        usuariosConectados = new HashMap<>();
    }
    
    @Override
    public synchronized boolean registrarReferenciaUsuario(UsuarioCllbckInt usuario, String nickname) throws RemoteException 
    {
       //método que unicamente puede ser accedido por un hilo
	    System.out.println("Invocando al método registrar usuario desde el servidor");
        if (!(usuariosConectados.containsValue(usuario) &&usuariosConectados.containsKey(nickname)))
        {
            System.out.println("Verificando si existe el nickname");
            return (null==usuariosConectados.put(nickname,usuario));  
        }          
        return false;
    }

    @Override
    public void enviarMensaje(String mensaje, String origen)throws RemoteException 
    {        
        notificarUsuarios(origen,"-"+origen+": " + mensaje);
    }
    
    private void notificarUsuarios(String origen, String mensaje) throws RemoteException 
    {
        System.out.println("Invocando al método notificar usuarios desde el servidor");
        for (UsuarioCllbckInt usuario : usuariosConectados.values()) {
            //Enviamos a tdos los usuarios el mensaje, menos a si mismo
            if (usuario.equals(usuariosConectados.get(origen))) {
                continue;
            }
            usuario.notificar(mensaje, usuariosConectados.size());
        }
    }

    @Override
    public List<String> listarUsuariosActivos() throws RemoteException {
        List<String> nicknamesList = new ArrayList<>();
        for (String nickname : usuariosConectados.keySet()) {
            nicknamesList.add(nickname);
        }
        return nicknamesList;
    }

    @Override
    public void enviarMensajePrivado(String mensaje, String origen, String destinatario) throws RemoteException {
        System.out.println("Invocando al método enviar mensaje privado desde el servidor");
        if (estaConectado(destinatario)) {
            System.out.println("Enviando mensajes a usuario conectado");
            UsuarioCllbckInt usuarioDestinatario = usuariosConectados.get(destinatario);
            usuarioDestinatario.notificar("-"+origen+"(privado): " + mensaje, usuariosConectados.size());
        }else{
            System.out.println("El usuario "+destinatario+" no está conectado.");
        }
    }

    @Override
    public boolean desconectarse(String nickname) throws RemoteException {
        System.out.println("Invocando al método desconectarse desde el servidor");
        for(String usuario : usuariosConectados.keySet()) {
            if (nickname.equals(usuario)) {
                usuariosConectados.remove(nickname);
                notificarUsuarios(nickname, nickname + " se ha desconectado.");
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean estaConectado(String nickname) throws RemoteException {
        System.out.println("Invocando al método estaConectado desde el servidor");
        return usuariosConectados.containsKey(nickname);
    }

}    
