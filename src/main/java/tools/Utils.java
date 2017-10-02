package tools;

import java.io.File;
import java.io.IOException;

public class Utils {

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
}
