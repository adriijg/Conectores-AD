package es.tierno.javafx;

import es.tierno.Modo;
import es.tierno.dao.InstitutoDAO;
import es.tierno.dao.InstitutoDAOFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuPrincipal {

    public void mostrar(Stage stage) {
        Button mock = new Button("Conexión MOCK");
        Button sqlite = new Button("Conexión SQLite");
        Button oracle = new Button("Conexión Oracle");

        mock.setMinWidth(250);
        sqlite.setMinWidth(250);
        oracle.setMinWidth(250);

        mock.setOnAction(e -> abrirOperaciones(stage, Modo.MOCK));
        sqlite.setOnAction(e -> abrirOperaciones(stage, Modo.SQLITE));
        oracle.setOnAction(e -> abrirOperaciones(stage, Modo.ORACLE));

        VBox root = new VBox(20, mock, sqlite, oracle);
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("Gestión Instituto - Seleccione conexión");
        stage.show();
    }

    private void abrirOperaciones(Stage stage, Modo modo) {
        try {
            InstitutoDAO dao = InstitutoDAOFactory.obtenerDAO(modo);
            new MenuOperaciones(dao, modo).mostrar(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
