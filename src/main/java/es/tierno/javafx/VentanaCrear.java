package es.tierno.javafx;

import es.tierno.dao.InstitutoDAO;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaCrear {
    private InstitutoDAO dao;

    public VentanaCrear(InstitutoDAO dao) {
        this.dao = dao;
    }

    public void mostrar() {
        Stage stage = new Stage();
        VBox root = new VBox(10, new Label("Ventana Crear - Pendiente de implementar"));
        root.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(root, 300, 200));
        stage.setTitle("Crear tablas");
        stage.show();
    }
}

