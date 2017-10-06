package entities.parsing;

import java.util.Observable;

abstract public class Machine extends Observable {

    private String name,username,ip,password, pathCompressed;
    private int SSHPort, port;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setChanged();
        notifyObservers();
    }

    public String getPathCompressed() {
        return pathCompressed;
    }

    public void setPathCompressed(String pathCompressed) {
        this.pathCompressed = pathCompressed;
    }

    final public String getName() {
        return name;
    }

    final public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        setChanged();
        notifyObservers();
    }

    final public String getIp() {
        return ip;
    }

    final public void setIp(String ip) {
        this.ip = ip;
        setChanged();
        notifyObservers();
    }

    final public String getPassword() {
        return password;
    }

    final public void setPassword(String password) {
        this.password = password;
        setChanged();
        notifyObservers();
    }

    final public int getSSHPort() {
        return SSHPort;

    }

    final public void setSSHPort(int SSHPort) {
        this.SSHPort = SSHPort;
        setChanged();
        notifyObservers();
    }




}
