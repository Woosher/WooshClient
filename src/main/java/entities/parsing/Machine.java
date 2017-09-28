package entities.parsing;

abstract public class Machine {

    private String name,ip,password,pathBash, pathCompressed;
    private int port;

    public String getPathBash() {
        return pathBash;
    }

    public void setPathBash(String pathBash) {
        this.pathBash = pathBash;
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

    final public int getPort() {
        return port;
    }

    final public void setPort(int port) {
        this.port = port;
    }


}
