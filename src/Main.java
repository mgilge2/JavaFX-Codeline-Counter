import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    private Controller mController;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("myLayout.fxml"));
        Parent root = loader.load();
        this.mController = loader.getController();
        this.mController.setStage(primaryStage);
        primaryStage.setTitle("Code Line Counter");
        primaryStage.setScene(new Scene(root, 923, 652));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
