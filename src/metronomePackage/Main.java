package metronomePackage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
        Scene scene = new Scene(root, 616, 485);
        scene.getStylesheets().add(getClass().getResource("project.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Metronome");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.jpg")));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }



    public static void main(String[] args) {
        launch(args);

    }
}
