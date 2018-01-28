import java.awt.*;
import java.text.SimpleDateFormat;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.layout.HBox;

public class Controller
{
    private File theFile;
    private Stage myStage;
    private ArrayList<File> fileList;
    private ArrayList<FileData> nameList;
    private File singleFile;
    private FileUtil fileUtil;


    public Controller()
    {
        this.fileUtil = new FileUtil();
        this.theFile = null;
        this.fileList = new ArrayList<File>();

    }

    @FXML
    public void initialize()
    {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        ".java",
                        ".cs",
                        ".c",
                        ".cpp"
                );

        this.extSelector.setItems(options);
        this.extSelector.getSelectionModel().selectFirst();

        this.nameCol.setCellValueFactory(
                new PropertyValueFactory<FileData, String>("fileName"));

        this.modCol.setCellValueFactory(
                new PropertyValueFactory<FileData, String>("lastMod"));

        this.pathCol.setCellValueFactory(
                new PropertyValueFactory<FileData, String>("path"));

        this.sizeCol.setCellValueFactory(
                new PropertyValueFactory<FileData, String>("size"));
    }

    @FXML
    private TableView<?> tableView;

    @FXML
    private TableColumn<FileData, String> nameCol;

    @FXML
    private TableColumn<FileData, String> modCol;

    @FXML
    private TableColumn<FileData, String> pathCol;

    @FXML
    private TableColumn<FileData, String> sizeCol;

    @FXML
    private Button onOpenBtn;

    @FXML
    private Label fileSize;

    @FXML
    private Label fileName;

    @FXML
    private Label codeLines;

    @FXML
    private Label lastMod;

    @FXML
    private ListView listView;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private ComboBox extSelector;

    @FXML
    private Label workingDirectory;

    @FXML
    private Label numFiles;

    @FXML
    private Label numLines;

    //this is for the custom Directory Warning Box
    @FXML
    private Button countBtn;

    @FXML
    private Label messageLabel;

    @FXML
    private Label detailsLabel;

    @FXML
    private HBox actionParent;

    @FXML
    private Button actionButton;

    @FXML
    private Button cancelButton;

    @FXML
    private HBox okParent;
    @FXML

    void onAbout(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
        alert.setTitle("About") ;
        alert.setHeaderText("Mike P Gilge, CSCD 316 Homework 3, Winter 2017") ;
        alert.setContentText(
                "General Usage Instructions:\n" +
                        "Select a type of source file, and then a directory\n" +
                        "Press the Count Lines button \n" +
                        "The total number of files and lines will be displayed\n" +
                        "Clicking a file in the list will give specific file info\n" +
                        "Double clicking in the list opens that file\n"+
                        "Right clicking in the list will copy that files path\n\n" +
                        "Line counting only fully verified with java files....\n" +
                        "Use others at your own risk!"
        );
        alert.showAndWait() ;
    }

    @FXML
    void onCount(ActionEvent event) throws Exception
    {
        if(theFile == null)
        {

            Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
            alert.setTitle("Enter Directory!") ;
            alert.setHeaderText("Select a Valid Directory First!") ;
            alert.setContentText("You must first select a directory using the Choose Directory button.");
            alert.showAndWait() ;

        }
        else
        {
            this.fileList = new ArrayList<File>();//did this to ensure each new run starts with an empty list/ie results dont add to old results
            this.genFileList(this.theFile.getAbsolutePath());
            int count = this.countLinesOfCode((this.fileList));
            this.printFilesToList();

            this.numFiles.setText("Number of Files Counted: " + this.fileList.size());
            this.numLines.setText("Lines of Code Found: " + count);
            ObservableList<FileData> list = FXCollections.observableArrayList(nameList);
            this.tableView.setItems((ObservableList)list);
        }
    }

    @FXML
    void onExtSelect(ActionEvent event)
    {
        //currently I am just getting the file extension from the combobox directly, this could be used to provide additional functionality
    }

    @FXML
    void onFileClicked(MouseEvent event)
    {
        try
        {
            FileData fileData = (FileData) this.tableView.getSelectionModel().getSelectedItem();  //I fixed this after switching to tableview by using this http://stackoverflow.com/questions/17388866/getting-selected-item-from-a-javafx-tableview

            this.singleFile = new File(fileData.getPath());
            ArrayList<File> list = new ArrayList<File>();
            list.add(this.singleFile);
            int count = this.countLinesOfCode(list);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy mm:HH:ss");

            this.fileName.setText("Filename: " + this.singleFile.getName());
            this.codeLines.setText("Code Lines: " + count);
            this.lastMod.setText("Last Modified: " + sdf.format(this.singleFile.lastModified()));
            this.fileSize.setText(("File Size (Bytes): " + this.singleFile.length()));

            ActionEvent event1 = new ActionEvent();
            if(event.getClickCount() == 2)
            {
                this.onOpenFile(event1);
            }
            if(event.getButton() == MouseButton.SECONDARY)
            {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(this.singleFile.getPath());
                clipboard.setContent(content);
            }
        }
        catch(Exception e)
        {
            //reason for this is no harm is caused by clicking the box when it isnt populated
            //a better way to do this would be to disable the control/handler too its populated
        }

        /* alternatively could pop up a dialog of the individual file information, I used this in this instance to parse the info I wanted(debugging)
        Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
        alert.setTitle("Handler Workikg!") ;
        alert.setHeaderText(this.listView.getSelectionModel().getSelectedItem().toString() + " " + count
         + " " + sdf.format(this.singleFile.lastModified())) ;
        alert.showAndWait() ;
        */

    }

    @FXML
    void onFileChosen(ActionEvent event)
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a Directory");
        directoryChooser.setInitialDirectory(new File(".")); //investigete what this . does exactly......

        this.theFile = directoryChooser.showDialog(this.myStage);
        if(this.theFile != null)
        {
            this.workingDirectory.setText(this.theFile.getPath());
        }
    }

    @FXML
    void onOpenFile(ActionEvent event)
    {
        try
        {
            Desktop.getDesktop().open(this.singleFile); //this will open the file in the default app for the system
        }
        catch(Exception e)
        {

            Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
            alert.setTitle("Select a File!") ;
            alert.setHeaderText("Select a file from the list first!");
            alert.setContentText("If no list is present, run a count on a directory first!");
            alert.showAndWait() ;

        }
    }

    @FXML
    void onMenuExit(ActionEvent event)
    {
        Platform.exit();
    }

    public void setStage(Stage stage)
    {
        this.myStage = stage;
    }



    public void genFileList(String fileName)  //build a list of all files with a directory and subdirectories, should implement FileFilter here passed in as parameter, could also set up helper functions here depending
    {
        File file = new File(fileName);
        File [] list = file.listFiles();
        try
        {
            for(File f: list)
            {
                if(f.isDirectory())
                {
                    genFileList(f.getAbsolutePath());
                }
                else
                {
                    if(f.getName().toLowerCase().endsWith(this.extSelector.getSelectionModel().getSelectedItem().toString()))  //lazy I was going to use file filters, if I didnt change this I obviously became lazy
                    {
                        this.fileList.add(f);
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
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

    private void printFilesToList()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy mm:HH:ss");
        this.nameList = new ArrayList<FileData>();
        for(File f: this.fileList)
        {
            String fileName = f.getName();
            String lastMod = sdf.format(f.lastModified());
            String path = f.getPath();
            String size = Long.toString(f.length());
            FileData dataToAdd = new FileData(fileName, lastMod, path, size);
           this.nameList.add(dataToAdd);
        }
    }


}