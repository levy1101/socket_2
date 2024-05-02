package socket3;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Địa chỉ IP của server
        int serverPort = 8080; // Cổng của server

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String number;
            while ((number = in.readLine()) != null) {
                System.out.println("Số từ server: " + number);
            }
        } catch (IOException ex) {
            System.out.println("Lỗi: " + ex.getMessage());
        }
    }
}

