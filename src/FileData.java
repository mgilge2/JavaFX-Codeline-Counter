import javafx.beans.property.SimpleStringProperty;

/**
 * Created by mgilge on 2/9/17.
 */
public class FileData
{
    private String fileName;
    private String lastMod;
    private String path;
    private String size;

    public FileData(String fileName, String lastMod, String path, String size)
    {
        this.fileName = fileName;
        this.lastMod = lastMod;
        this.path = path;
        this.size = size;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLastMod() {
        return lastMod;
    }

    public void setLastMod(String lastMod) {
        this.lastMod = lastMod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
