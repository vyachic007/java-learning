package by.slava_borisov.hoteladmin.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static void ensureFileExists(String filePath) throws IOException {
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException(Messages.ERROR_DIRECTORY_CREATE);
            }
        }

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException(Messages.ERROR_FILE_CREATE);
            }
        }
    }
}
