package iohelpers;

public class ConfigChecker {

    private static ConfigChecker instance;

    public static ConfigChecker getInstance() {
        if(instance == null){
            instance = new ConfigChecker();
        }
        return instance;
    }
}
