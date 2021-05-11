package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<User> users = new ArrayList<>();
    public static void main(String[] args) {

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
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); //поток вывода на сокете
                            DataInputStream in = new DataInputStream(socket.getInputStream());
                            userCurrent.setOos(oos);
                            userCurrent.getOos().writeObject("Добро пожаловать на сервер!");
                            String name="";
                            while (name.isBlank()) { //простая проверка имя не должно быть пустым
                                userCurrent.getOos().writeObject("Введите Ваше имя:");
                                name = in.readUTF();
                            }

                            userCurrent.setName(name);
                            userCurrent.getOos().writeObject("Добро пожаловать на сервер! " + userCurrent.getUserName());
                            sendUserList();
                            //sendCurrentUser(userCurrent);
                            for (User user:users){
                                //System.out.println(user.getUserSocket());
                                if (user.equals(userCurrent)) continue;
                                    //DataOutputStream userOut = new DataOutputStream(user.getUserSocket().getOutputStream());
                                    user.getOos().writeObject("К нам присоединился: "+userCurrent.getUserName());
                            }
                            while (true) {
                                String request = in.readUTF();
                                for(User user:users){
                                    //System.out.println(user);
                                    //System.out.println(request);
                                    if (user.getUserSocket()!= userCurrent.getUserSocket()){
                                        //DataOutputStream userOut = new DataOutputStream(user.getUserSocket().getOutputStream());
                                        user.getOos().writeObject(userCurrent.getUserName()+" : "+request);
                                    }
                                }
                            }
                        }catch (IOException e){
                            System.out.println(userCurrent.getUserName()+" покинул чат");
                            users.remove(userCurrent);
                            sendUserList();
                            for (User user:users) {
                                try {
                                    //DataOutputStream userOut = new DataOutputStream(user.getUserSocket().getOutputStream());
                                    user.getOos().writeObject(userCurrent.getUserName()+" покинул чат");
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
    private static void sendUserList(){
        try {

            ArrayList<String> usersName = new ArrayList<>();
            for (User user:users){

                usersName.add(user.getUserName());

            }
            for (User user:users){
                user.getOos().writeObject(usersName);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void sendCurrentUser(User user){
        ArrayList<String> userName = new ArrayList<>();
        try {
            userName.add(user.getUserName());
            user.getOos().writeObject(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




