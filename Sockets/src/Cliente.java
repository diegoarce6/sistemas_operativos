
/**
 * Cliente
 */

import java.net.Socket;
import java.net.UnknownHostException;
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

    public void conectar() throws IOException {
        socket = new Socket(host, puerto);
        // PrintWriter
        out = new PrintWriter(socket.getOutputStream());
        // BufferedReader lee un flujo de caracteres de entrada
        // InputStreamReader lee un flujo de entradas de bytes y lo decodifica a un
        // conjunto especifico de caracteres
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.printf("Cliente conectado al servidor %s en el puerto %d%n", host, puerto);


    }

    public void enviarMensaje() {}

    public void desconectar() {}

}
