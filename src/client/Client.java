package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",8188);
            System.out.println("Подключился");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String response = ois.readObject().toString(); //Ждем сообщение от  серевера
            System.out.println(response);

            Scanner scanner = new Scanner(System.in);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            /*String response = in.readUTF();
                            System.out.println( response);*/
                            Object responseObject = ois.readObject();
                            //System.out.println(responseObject.getClass().toString());
                            if(String.class == responseObject.getClass()){
                                System.out.println(responseObject + "\n");

                            } else if (ArrayList.class == responseObject.getClass()){

                                System.out.println("Получили список пользователей");
                                ArrayList<String> usersName= new ArrayList<>();
                                usersName = (ArrayList<String>) responseObject;

                                for (String userName:usersName){
                                    System.out.print(userName +" | ");
                                }
                                System.out.print("\n");
                            } else {
                              /*  Platform.runLater(() -> textAreaUserList.appendText(
                                        responseObject.getClass().toString()+"\n"));*/
                                System.out.println(responseObject.toString());
                                System.out.println("Ответ не распознан");
                            }
                        } catch (IOException | ClassNotFoundException e) {
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
