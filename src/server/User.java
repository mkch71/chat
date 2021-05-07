package server;

import java.net.Socket;
import java.util.UUID;

public class User {
    private Socket userSocket;
    private String name;
    private UUID uuid;

    public User(Socket userSocket) {
        this.userSocket = userSocket;
        this.name = "гость";
        this.uuid = UUID.randomUUID();
    }
    public void setName(String name) {this.name = name;}

    public Socket getUserSocket() {return userSocket;}
    public String getUserName() {return name;}
    public void setUserSocket(Socket userSocket) {this.userSocket = userSocket;}
    public UUID getUuid() {return this.uuid;}
    public boolean equals(User user){

        return (this.getUuid().toString().equals(user.getUuid().toString()));
    }
}
