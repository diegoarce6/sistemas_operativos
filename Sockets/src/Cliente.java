
/**
 * Cliente TCP que manda mensajes al servidor eco y recibe respuestas formateadas utilizando sockets
 */

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class Cliente {
    private final String host;
    private final int puerto;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Cliente(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    private void conectar() throws IOException {
        socket = new Socket(host, puerto);
        // PrintWriter escribe texto formateado en el flujo de salida
        out = new PrintWriter(socket.getOutputStream(), true);
        // BufferedReader lee un flujo de caracteres de entrada
        // InputStreamReader lee un flujo de entradas de bytes y lo decodifica a un conjunto especifico de caracteres
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.printf("Conectado al servidor %s en el puerto %d%n", host, puerto);
    }

    private void enviarMensaje(String mensaje) {
        out.println(mensaje);
    }

    private String recibirMensaje() throws IOException {
        return in.readLine();
    }

    private void desconectar() {
        try {
            // Si el flujo de entrada se creo, se cierra
            if (in != null) {
                in.close();
            }
            // Si el flujo de salida se creo, se cierra
            if (out != null) {
                out.close();
            }
            // Si el socket se abrio, se cierra
            if (socket != null) {
                socket.close();
            }
            System.out.println("Desconectado del servidor");
        } catch (IOException ex) {
            System.err.println("Error al liberar recursos del cliente:" + ex.getMessage());
        }
    }

    private void iniciarInteraccion() {
        try (Scanner consola = new Scanner(System.in)) {

            conectar();

            System.out.println("Escribe un mensaje ('exit' para terminar):");

            while (true) {
                System.out.print("$ ");

                String entradaConsola = consola.nextLine();
                enviarMensaje(entradaConsola);

                String respuesta = recibirMensaje();
                // Si no recibe respuesta del servidor termina la conexion
                if (respuesta == null) {
                    System.out.println("El servidor cerro la conexion.");
                    break;
                }

                System.out.println("Servidor: " + respuesta);

                if ("exit".equalsIgnoreCase(entradaConsola.trim())) {
                    break;
                }
            }

        } catch (UnknownHostException ex) {
            System.err.println("No se encontro el host: \"" + host + "\" " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error al conectar: " + ex.getMessage());
        } finally {
            desconectar();
        }
    }

    public static void main(String[] args) {
        Cliente client = new Cliente("localhost", 8080);

        client.iniciarInteraccion();
    }
}
