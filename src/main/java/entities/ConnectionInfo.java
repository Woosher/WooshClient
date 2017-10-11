package entities;

import entities.parsing.Machine;

import java.util.Observable;

public class ConnectionInfo {

    public ConnectionInfo(Machine machine, String info){
        this.machine = machine;
        this.info = info;
    }
    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private Machine machine;
    private String info;
}
