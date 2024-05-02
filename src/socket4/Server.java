package socket4;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 8080;
    private static final Map<Socket, String> clientMap = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy trên cổng " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client đã kết nối: " + clientSocket);

                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException ex) {
            System.err.println("Lỗi: " + ex.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            out.println("Nhập tên của bạn:");
            String username = in.readLine();
            clientMap.put(clientSocket, username);

            String message;
            while ((message = in.readLine()) != null) {
                broadcastMessage(username, message);
            }
        } catch (IOException ex) {
            System.err.println("Lỗi: " + ex.getMessage());
        } finally {
            clientMap.remove(clientSocket);
        }
    }

    private static void broadcastMessage(String sender, String message) {
        String formattedMessage = "[" + sender + "]: " + message;
        for (Socket clientSocket : clientMap.keySet()) {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(formattedMessage);
            } catch (IOException ex) {
                System.err.println("Lỗi khi gửi tin nhắn tới " + clientMap.get(clientSocket) + ": " + ex.getMessage());
            }
        }
    }
}
