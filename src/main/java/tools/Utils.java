package tools;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static boolean shouldPrintLog = true;

    public static File generateFile(String path) throws IOException {
        File file = new File(path);
        if (!file.isFile()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            file.createNewFile();
        }
        return file;
    }

    public static File generateSimpleFolder(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
                file.mkdirs();
        }
        return file;
    }

    public static void printLogs(String text){
        if(shouldPrintLog)
            System.out.println(text);
    }

    public static void delete(String path){
        File file = new File(path);
        if(file.isDirectory()){
            if(file.list().length==0){
                file.delete();
            }else{
                String files[] = file.list();
                for (String temp : files) {
                    File fileDelete = new File(file, temp);
                    delete(fileDelete.getAbsolutePath());
                }
                if(file.list().length==0){
                    file.delete();
                }
            }
        }else{
            file.delete();
        }
    }
}
