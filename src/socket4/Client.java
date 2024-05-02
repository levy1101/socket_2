package socket4;

import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private static boolean hasEnteredName = false; // Biến cờ để kiểm tra đã nhập tên hay chưa

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            hasEnteredName = true;
            if (!hasEnteredName){
                System.out.print("Nhập tên của bạn: ");
                String username = userInput.readLine();
                out.println(username);
            }




            System.out.println("Kết nối đến server thành công.");

            Thread serverListener = new Thread(() -> {
                String message;
                try {
                    while ((message = serverInput.readLine()) != null) {
                        if (hasEnteredName) { // Kiểm tra biến cờ trước khi in tin nhắn
                            System.out.println(message);
                        } else {
                            System.out.print(message); // In tin nhắn của server khi chờ nhập tên
                        }
                    }
                } catch (IOException ex) {
                    System.err.println("Lỗi khi đọc tin nhắn từ server: " + ex.getMessage());
                }
            });
            serverListener.start();

            String input;
            while ((input = userInput.readLine()) != null) {
                out.println(input);
            }
        } catch (IOException ex) {
            System.err.println("Lỗi khi kết nối đến server: " + ex.getMessage());
        }
    }
}