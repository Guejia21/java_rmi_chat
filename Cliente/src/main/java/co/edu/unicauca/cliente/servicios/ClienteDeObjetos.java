package co.edu.unicauca.cliente.servicios;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckImpl;
import co.edu.unicauca.cliente.utilidades.UtilidadesConsola;
import co.edu.unicauca.cliente.utilidades.UtilidadesRegistroC;
import co.edu.unicauca.servidor.controladores.ControladorServidorChatInt;
import java.util.List;

public class ClienteDeObjetos {

    public static void main(String[] args) {

        try {
            ControladorServidorChatInt servidor;
            int numPuertoRMIRegistry = 0;
            String direccionIpRMIRegistry = "";
            System.out.println("Cual es el la dirección ip donde se encuentra  el rmiregistry ");
            direccionIpRMIRegistry = UtilidadesConsola.leerCadena();
            System.out.println("Cual es el número de puerto por el cual escucha el rmiregistry ");
            numPuertoRMIRegistry = UtilidadesConsola.leerEntero();
            System.out.println("Digite su nickname: ");
            String nickname = UtilidadesConsola.leerCadena();
            //Se conecta al objeto remoto
            servidor = (ControladorServidorChatInt) UtilidadesRegistroC.obtenerObjRemoto(numPuertoRMIRegistry, direccionIpRMIRegistry, "ServidorChat");
            //Se registra un usuario en el servidor
            UsuarioCllbckImpl objNuevoUsuario = new UsuarioCllbckImpl();
            servidor.registrarReferenciaUsuario(objNuevoUsuario, nickname);
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
                        servidor.enviarMensajePrivado(mensaje, nickname,destinatario);
                    }
                    case 3 -> {
                        List<String> usuariosActivos = servidor.listarUsuariosActivos();
                        System.out.println("Usuarios activos");
                        for(String usuario: usuariosActivos){
                            System.out.println("Nickname: "+usuario);
                        }
                    }
                    case 4 -> {
                        //TODO!!
                        System.err.println("Opcion 4 no implementada");
                    }
                    default -> {
                        System.out.println("Opción inválida");
                    }
                }
            } while (opcion != 4);
        } catch (Exception e) {
            System.out.println("No se pudo realizar la conexion...");
            System.out.println(e.getMessage());
        }

    }

}