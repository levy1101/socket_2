package socket3;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        int port = 8080; // Port mặc định
        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang chạy trên cổng " + port + "...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client đã kết nối: " + socket);
                executor.submit(new ClientHandler(socket));
            }
        } catch (IOException ex) {
            System.out.println("Lỗi: " + ex.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                for (int i = 1; i <= 1000; i++) {
                    out.println(i);
                    Thread.sleep(1000); // Dừng 1 giây giữa mỗi số
                }
            } catch (IOException | InterruptedException ex) {
                System.out.println("Lỗi: " + ex.getMessage());
            }
        }
    }
}
