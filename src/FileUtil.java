import javafx.scene.control.Alert;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mgilge on 2/9/17.
 */
public class FileUtil
{
    public FileUtil()
    {
        //any specific stuff for the logic of my program that needs to be initialized goes here
    }

    public int countLinesOfCode(ArrayList<File> fileList)
    {
        int count = 0;

        try
        {
            for(int x = 0; x < fileList.size(); x ++)
            {
                File f = fileList.get(x);
                Scanner fin = new Scanner(f);
                while(fin.hasNextLine())
                {
                    String line = fin.nextLine();
                    if(line.matches(".*[{};)]$"))
                    {
                        count = count + 1;
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
            alert.setTitle("Enter Directory!") ;
            alert.setHeaderText("Select a Valid Directory First!") ;
            alert.showAndWait() ;
        }

        return count;
    }

    public ArrayList<String> printFilesToList(ArrayList<File> fileList)
    {
        ArrayList<String> nameList = new ArrayList<String>();
        for(File f: fileList)
        {
            nameList.add(f.toString());
        }
        return nameList;
    }

    private ArrayList<File> genFileList(String fileName, String ext)  //build a list of all files with a directory and subdirectories, should implement FileFilter here passed in as parameter, could also set up helper functions here depending
    {
        ArrayList<File> fileList = new ArrayList<File>();
        File file = new File(fileName);
        File [] list = file.listFiles();

        try
        {
            for(File f: list)
            {
                if(f.isDirectory())
                {
                    genFileList(f.getAbsolutePath(), ext);
                }
                else
                {
                    if(f.getName().toLowerCase().endsWith(ext))  //lazy I was going to use file filters, if I didnt change this I obviously became lazy
                    {
                        fileList.add(f);
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return fileList;
    }

}
