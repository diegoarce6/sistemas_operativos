/**
 * Servidor eco que procesa el texto enviado por un solo cliente
 * mediante sockets TCP
 */

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Servidor {

    private final int puerto;
    private boolean activo;

    public Servidor(int puerto) {
        this.puerto = puerto;
        this.activo = false;
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            activo = true; // Cambiar el estado del servidor

            System.out.printf("Servidor escuchando en el puerto %d...%n", puerto);

            while (activo) {
                escucharPeticion(serverSocket); // Esperar y atender las peticiones
            }

        } catch (IOException ex) {
            System.err.println("Error en el servidor: " + ex.getMessage());
        } finally {
            activo = false;
        }
    }

    private void escucharPeticion(ServerSocket serverSocket) {
        // Conectar cliente
        try (Socket clientSocket = serverSocket.accept()) {
            // mostrar la direccion ip del cliente conectado
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

            procesarMensaje(clientSocket);

        } catch (IOException ex) {
            System.err.println("Error con el cliente: " + ex.getMessage());
        }
    }

    // Metodo para procesar y mostrar en mayusculas el mensaje del cliente
    private void procesarMensaje(Socket clientSocket) throws IOException {
        // Construir los flujos de entrada y de salida con autoFlush
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            while (true) {

                String linea = in.readLine();

                
                if (linea == null) {
                    System.out.printf("Cliente [%s] desconectado%n", clientSocket.getInetAddress());
                    break;
                }
                
                System.out.printf("Cliente [%s]: %s%n",clientSocket.getInetAddress(), linea);

                if ("exit".equalsIgnoreCase(linea.trim())) {
                    out.println("Saliendo...");
                    break;
                }
                out.println(linea.toUpperCase());
            }
        } 
    }

    public static void main(String[] args) {
        Servidor server = new Servidor(8080);
        server.iniciar();
    }
}