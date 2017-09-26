package entities.parsing;

abstract public class System {

    private String name, ip, password;
    private int port;

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
