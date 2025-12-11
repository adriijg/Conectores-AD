package es.tierno.javafx;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        new MenuPrincipal().mostrar(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
