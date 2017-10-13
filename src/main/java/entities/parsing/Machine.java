package entities.parsing;

import java.util.Observable;

abstract public class Machine extends Observable {

    private String name,username,ip,password, pathCompressed, bashScript;
    private int SSHPort, port;

    public String getBashScript() {
        return bashScript;
    }

    public void setBashScript(String bashScript) {
        this.bashScript = bashScript;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    final public String getIp() {
        return ip;
    }

    final public void setIp(String ip) {
        this.ip = ip;
    }

    final public String getPassword() {
        return password;
    }

    final public void setPassword(String password) {
        this.password = password;
    }

    final public int getSSHPort() {
        return SSHPort;

    }

    final public void setSSHPort(int SSHPort) {
        this.SSHPort = SSHPort;
    }




}
