package tools;

import exceptions.WooshException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static values.Constants.FULLTEMPPATH;

public class WooshLogger {

    StringBuilder stringBuilder;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
    DateTimeFormatter saveDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

    public WooshLogger(){
        stringBuilder = new StringBuilder();
    }

    public void appendLoggingWithTime(String text){
        LocalDateTime now = LocalDateTime.now();
        stringBuilder.append(dtf.format(now));
        stringBuilder.append("\t");
        append(text);
    }

    public void appendLoggingWithOutTime(String text){
        append(text);
    }

    private void append(String text){
        stringBuilder.append(text);
        stringBuilder.append(System.getProperty("line.separator"));
    }

    public void saveLogsAndClear(String name) throws WooshException {
        LocalDateTime now = LocalDateTime.now();
        String path = FULLTEMPPATH + saveDtf.format(now) + "-" + name + ".txt";
        File file = null;
        try {
            file = Utils.generateFile(path);
            FileWriter fooWriter = new FileWriter(file, false);
            fooWriter.write(stringBuilder.toString());
            fooWriter.close();
        } catch (IOException e) {
            throw new WooshException("Could not create log file" );
        }
    }

    public void printLog(){
        System.out.println(stringBuilder.toString());
    }

    public void clearLogging(){
        stringBuilder = new StringBuilder();
    }
}
