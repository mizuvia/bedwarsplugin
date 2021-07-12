package util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Logger;

public class DeleteDirectory{
    public File directory;

    public DeleteDirectory(@NotNull File file) {
        directory = file;
    }

    public void deleteStream() {
        if (directory.isDirectory()) {
            File[] list = directory.listFiles();
            if (list.length != 0) {
                for (File value : list) {
                    DeleteDirectory dir = new DeleteDirectory(value);
                    dir.deleteStream();
                }
            }
        }
        delete();
    }

    private void delete(){
        Logger.getLogger("").info("Deleting file: " + directory.getName());
        directory.delete();
    }

}
