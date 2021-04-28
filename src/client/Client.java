package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.70.70",8188);
            System.out.println("Подключился");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String response = in.readUTF(); //Ждем сообщение от  серевера
            System.out.println(response);

            Scanner scanner = new Scanner(System.in);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String response = in.readUTF();
                            System.out.println( response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
            while (true) {

                String request = scanner.nextLine();
                out.writeUTF(request);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
