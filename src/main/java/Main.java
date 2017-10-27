
import controller.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modellers.FlowModeller;
import modellers.interfaces.FlowModelInterface;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(loader.load());
        primaryStage.setTitle("Woosh Deployment");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
