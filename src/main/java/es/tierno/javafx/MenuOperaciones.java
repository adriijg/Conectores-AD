package es.tierno.javafx;

import es.tierno.Modo;
import es.tierno.dao.InstitutoDAO;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuOperaciones {

    private InstitutoDAO dao;
    private Modo modo;

    public MenuOperaciones(InstitutoDAO dao, Modo modo) {
        this.dao = dao;
        this.modo = modo;
    }

    public void mostrar(Stage stage) {

        Button crear = new Button("Crear tablas");
        Button insertar1 = new Button("Insertar Alumno");
        Button insertar2 = new Button("Insertar Nota");
        Button actualizar1 = new Button("Actualizar Alumno");
        Button actualizar2 = new Button("Actualizar Nota");
        Button listarAlumnos = new Button("Listar Alumnos");
        Button listarNotas = new Button("Listar Notas");
        Button listarJoin = new Button("Listar Alumnos + Notas");
        Button borrarAlumno = new Button("Borrar Alumno");
        Button borrarNota = new Button("Borrar Nota");
        Button volver = new Button("Volver");

        crear.setMinWidth(250);
        insertar1.setMinWidth(250);
        insertar2.setMinWidth(250);
        actualizar1.setMinWidth(250);
        actualizar2.setMinWidth(250);
        listarAlumnos.setMinWidth(250);
        listarNotas.setMinWidth(250);
        listarJoin.setMinWidth(250);
        borrarAlumno.setMinWidth(250);
        borrarNota.setMinWidth(250);
        volver.setMinWidth(250);

        crear.setOnAction(e -> new VentanaCrear(dao).mostrar());
        // insertar1.setOnAction(e -> new VentanaInsertarAlumno(dao).mostrar());
        insertar2.setOnAction(e -> new VentanaInsertarNota(dao).mostrar());
        // actualizar1.setOnAction(e -> new VentanaActualizarAlumno(dao).mostrar());
        // actualizar2.setOnAction(e -> new VentanaActualizarNota(dao).mostrar());
        // listarAlumnos.setOnAction(e -> new VentanaListarAlumnos(dao).mostrar());
        // listarNotas.setOnAction(e -> new VentanaListarNotas(dao).mostrar());
        // listarJoin.setOnAction(e -> new VentanaListarJoin(dao).mostrar());
        // borrarAlumno.setOnAction(e -> new VentanaBorrarAlumno(dao).mostrar());
        // borrarNota.setOnAction(e -> new VentanaBorrarNota(dao).mostrar());
        volver.setOnAction(e -> new MenuPrincipal().mostrar(stage));

        VBox root = new VBox(12, crear, insertar1, insertar2, actualizar1, actualizar2,
                listarAlumnos, listarNotas, listarJoin, borrarAlumno, borrarNota, volver);
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 450, 600));
        stage.setTitle("Operaciones (" + modo + ")");
        stage.show();
    }
}
