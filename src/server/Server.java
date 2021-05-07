package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(8188); // Подняли сокет на порту

            while (true){
                Socket socket = serverSocket.accept(); //Ожилаем подключения
                System.out.println("Клиент подключится c IP: " + socket.getInetAddress().toString());
                User userCurrent = new User(socket);
                users.add(userCurrent);
                //users.add(socket);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //поток вывода на сокете
                            DataInputStream in = new DataInputStream(socket.getInputStream());

                            out.writeUTF("Добро пожаловать на сервер!");
                            String name="";
                            while (name.isBlank()) { //простая проверка имя не должно быть пустым
                                out.writeUTF("Введите Ваше имя:");
                                name = in.readUTF();
                            }

                            userCurrent.setName(name);
                            out.writeUTF("Добро пожаловать на сервер! " + userCurrent.getUserName());
                            for (User user:users){
                                //System.out.println(user.getUserSocket());
                                if (user.equals(userCurrent)) continue;
                                    DataOutputStream userOut = new DataOutputStream(user.getUserSocket().getOutputStream());
                                    userOut.writeUTF("К нам присоединился: "+userCurrent.getUserName());


                            }
                            while (true) {
                                String request = in.readUTF();
                                for(User user:users){
                                    //System.out.println(user);
                                    //System.out.println(request);
                                    if (user.getUserSocket()!= userCurrent.getUserSocket()){
                                        DataOutputStream userOut = new DataOutputStream(user.getUserSocket().getOutputStream());
                                        userOut.writeUTF(userCurrent.getUserName()+" : "+request);
                                    }

                                }

                            }
                        }catch (IOException e){
                            System.out.println(userCurrent.getUserName()+" покинул чат");
                            users.remove(userCurrent);
                            for (User user:users) {
                                try {
                                    DataOutputStream userOut = new DataOutputStream(user.getUserSocket().getOutputStream());
                                    userOut.writeUTF(userCurrent.getUserName()+" покинул чат");
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
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
