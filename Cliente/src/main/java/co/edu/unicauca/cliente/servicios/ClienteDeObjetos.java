package co.edu.unicauca.cliente.servicios;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckImpl;
import co.edu.unicauca.cliente.utilidades.UtilidadesConsola;
import co.edu.unicauca.cliente.utilidades.UtilidadesRegistroC;
import co.edu.unicauca.servidor.controladores.ControladorServidorChatInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClienteDeObjetos {

    public static void main(String[] args) {
        ControladorServidorChatInt servidor;
        int numPuertoRMIRegistry = 0;
        String direccionIpRMIRegistry = "";
        System.out.println("Cual es el la dirección ip donde se encuentra  el rmiregistry ");
        direccionIpRMIRegistry = UtilidadesConsola.leerCadena();
        System.out.println("Cual es el número de puerto por el cual escucha el rmiregistry ");
        numPuertoRMIRegistry = UtilidadesConsola.leerEntero();
        boolean bandera = true;
        System.out.println("Digite su nickname: ");
        String nickname = UtilidadesConsola.leerCadena();
        try {
            //Se conecta al objeto remoto
            servidor = (ControladorServidorChatInt) UtilidadesRegistroC.obtenerObjRemoto(numPuertoRMIRegistry, direccionIpRMIRegistry, "ServidorChat");
            //Se registra un usuario en el servidor
            UsuarioCllbckImpl objNuevoUsuario = new UsuarioCllbckImpl();
            if(servidor.registrarReferenciaUsuario(objNuevoUsuario, nickname)){
                menu(nickname, servidor);
                bandera = false;
            }else{
                System.out.println("El nickname ya está en uso, por favor elija otro.");
            }
            //Esta linea desconecta el cliente del servidor
            UnicastRemoteObject.unexportObject(objNuevoUsuario, true);           
        } catch (Exception e) {
            System.out.println("No se pudo realizar la conexion...");
            System.out.println(e.getMessage());
        }
    }

    public static void menu(String nickname, ControladorServidorChatInt servidor) {
        try {
            int opcion;
            do {
                System.out.println("======Chat Grupal========");
                System.out.println("1. Enviar mensaje general");
                System.out.println("2. Enviar mensaje privado");
                System.out.println("3.  Listar usuarios activos");
                System.out.println("4. Desconectarse del chat");
                opcion = UtilidadesConsola.leerEntero();
                switch (opcion) {
                    case 1 -> {
                        System.out.println("Ingrese el mensaje: ");
                        String mensaje = UtilidadesConsola.leerCadena();
                        servidor.enviarMensaje(mensaje, nickname);
                    }
                    case 2 -> {
                        System.out.println("Ingrese el destinatario: ");
                        String destinatario = UtilidadesConsola.leerCadena();
                        System.out.println("Ingrese el mensaje: ");
                        String mensaje = UtilidadesConsola.leerCadena();
                        servidor.enviarMensajePrivado(mensaje, nickname, destinatario);
                    }
                    case 3 -> {
                        List<String> usuariosActivos = servidor.listarUsuariosActivos();
                        System.out.println("Usuarios activos");
                        for (String usuario : usuariosActivos) {
                            System.out.println("Nickname: " + usuario);
                        }
                    }
                    case 4 -> {
                        servidor.desconectarse(nickname);
                        System.out.println("Desconectado del chat ...");
                    }
                    default -> {
                        System.out.println("Opción inválida");
                    }
                }
            } while (opcion != 4);
        } catch (RemoteException e) {
            System.out.println("No se pudo realizar el método.");
            System.out.println(e.getMessage());
        }

    }

}
