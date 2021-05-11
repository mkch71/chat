package server;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.UUID;

public class User implements Serializable {
    private Socket userSocket;
    private String name;
    private UUID uuid;
    private ObjectOutputStream oos;

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

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }
}
