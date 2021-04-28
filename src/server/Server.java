package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        ArrayList<Users> users = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(8188); // Подняли сокет на порту

            while (true){
                Socket socket = serverSocket.accept(); //Ожилаем подключения
                System.out.println("Клиент подключится c IP: " + socket.getInetAddress().toString());

                //users.add(socket);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //поток вывода на сокете
                            DataInputStream in = new DataInputStream(socket.getInputStream());

                            out.writeUTF("Добро пожаловать на сервер! Введите Ваше имя:");

                            String name = in.readUTF();
                            Users userCurrent = new Users(socket,name);
                            users.add(userCurrent);
                            out.writeUTF("Добро пожаловать на сервер!" + userCurrent.getName());
                            for (Users user:users){
                                System.out.println(user.getUserSocket());
                                DataOutputStream userOut = new DataOutputStream(user.getUserSocket().getOutputStream());
                                userOut.writeUTF("К нам присоденился: "+userCurrent.getName());
                            }
                            while (true) {
                                String request = in.readUTF();
                                for(Users user:users){
                                    //System.out.println(user);
                                    //System.out.println(request);
                                    if (user.getUserSocket()!= userCurrent.getUserSocket()){
                                        DataOutputStream userOut = new DataOutputStream(user.getUserSocket().getOutputStream());
                                        userOut.writeUTF(userCurrent.getName()+" : "+request);
                                    }

                                }

                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }





        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
class Users {
    private Socket userSocket;
    private String name;

    public Users(Socket userSocket, String name) {
        this.userSocket = userSocket;
        this.name = name;
    }

    public Socket getUserSocket() {
        return userSocket;
    }

    public String getName() {
        return name;
    }

    public void setUserSocket(Socket userSocket) {
        this.userSocket = userSocket;
    }

    public void setName(String name) {
        this.name = name;
    }
}