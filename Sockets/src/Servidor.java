/**
 * Servidor que procesa el texto enviado por el cliente
 */

// Librerias necesarias para la conexion TCP
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Librerias para flujos I/O
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

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
        // Construir los flujos de entrada y salida con autoFlush
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            while (true) {
                System.out.print(" > ");
                String linea = in.readLine();

                if (linea == null) {
                    break;
                }

                if ("exit".equalsIgnoreCase(linea.trim())) {
                    out.print("Saliendo.");
                    break;
                }
                out.print("Servidor: " + linea.toUpperCase());
            }
        }
    }

    public static void main(String[] args) {
        Servidor server = new Servidor(8080);
        server.iniciar();
    }
}