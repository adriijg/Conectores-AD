package es.tierno;

import es.tierno.dao.InstitutoDAO;
import es.tierno.dao.InstitutoOracleXeDaoImp;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private InstitutoDAO daoSeleccionado;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Gestión de Alumnos - Conectores");
        mostrarMenuConexion();
        stage.show();
    }

    private void mostrarMenuConexion() {
        Label titulo = new Label("Menú 1 - Tipo de conexión");
        titulo.setFont(new Font("Arial", 18));
        
        // Botones de Conexión [cite: 12, 13, 14]
        Button btnMock = new Button("a) Mock");
        Button btnSQLite = new Button("b) SQLite");
        Button btnOracle = new Button("c) Oracle");

        // --- Manejadores de Eventos ---
        btnMock.setOnAction(e -> seleccionarDAO("Mock"));
        btnSQLite.setOnAction(e -> seleccionarDAO("SQLite"));
        btnOracle.setOnAction(e -> seleccionarDAO("Oracle"));

        // Diseño de la escena
        VBox layout = new VBox(15, titulo, btnMock, btnSQLite, btnOracle);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
    }

    private void seleccionarDAO(String tipo) {
        try {
            switch (tipo) {
                case "Mock":
                    // daoSeleccionado = new InstitutoMockDaoImp();
                    System.out.println("Conexión Mock seleccionada.");
                    break;
                case "SQLite":
                    // daoSeleccionado = new InstitutoSQLiteDaoImp();
                    System.out.println("Conexión SQLite seleccionada.");
                    break;
                case "Oracle":
                    daoSeleccionado = new InstitutoOracleXeDaoImp();
                    System.out.println("Conexión Oracle seleccionada.");
                    break;
                default:
                    return;
            }
            mostrarMenuOperaciones(tipo); // Si la conexión es exitosa, vamos al Menú 2
        } catch (Exception ex) {
            // Manejar errores de conexión (ej: base de datos no disponible)
            System.err.println("Error al conectar con la base de datos " + tipo + ": " + ex.getMessage());
            daoSeleccionado = null; // Reinicia el DAO
        }
    }

    // --- MENÚ 2: Operaciones CRUD ---
    private void mostrarMenuOperaciones(String tipoConexion) {
        Label titulo = new Label("Menú 2 - Operaciones (" + tipoConexion + ")");
        titulo.setFont(new Font("Arial", 18));

        // Botones de Operaciones [cite: 16, 17, 18, 19, 20, 21, 22, 23]
        Button btnCrearTabla = new Button("a) Crear tablas (Alumno y Notas)");
        Button btnInsertar1 = new Button("b) Insertar en tabla ALUMNO");
        Button btnInsertar2 = new Button("c) Insertar en tabla NOTAS");
        Button btnActualizar1 = new Button("d) Actualizar en tabla ALUMNO");
        Button btnActualizar2 = new Button("e) Actualizar en tabla NOTAS");
        Button btnListar1 = new Button("f) Listar todos los ALUMNOS");
        Button btnListarRelacion = new Button("g) Listar ALUMNO con NOTAS (Relacionados)");
        Button btnConsultaParam = new Button("h) Consulta enviando un parámetro (Listar por edad)");
        Button btnVolver = new Button("<- Volver al Menú de Conexión");

        // --- Manejadores de Eventos ---
        // Aquí llamas a los métodos del daoSeleccionado
        btnCrearTabla.setOnAction(e -> ejecutarOperacion(() -> {
            daoSeleccionado.crearTablaAlumno();
            daoSeleccionado.crearTablaNotas();
            return "Tablas creadas correctamente.";
        }));
        // Otros manejadores... (e.g., para listar o insertar, que necesitarán input adicional)
        btnListar1.setOnAction(e -> ejecutarOperacion(() -> {
            return "Alumnos listados: " + daoSeleccionado.listarAlumnos().toString();
        }));
        btnListarRelacion.setOnAction(e -> ejecutarOperacion(() -> {
             // Este método fue el propuesto en la respuesta anterior
             // return "Registros relacionados: " + daoSeleccionado.listarAlumnosConNotas().toString();
             return "PENDIENTE: Lógica de Listar relacionados.";
        }));
        
        btnVolver.setOnAction(e -> mostrarMenuConexion());

        // Diseño de la escena
        VBox layout = new VBox(10, titulo, btnCrearTabla, btnInsertar1, btnInsertar2, 
                                btnActualizar1, btnActualizar2, btnListar1, 
                                btnListarRelacion, btnConsultaParam, new Label(""), btnVolver);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
    }
    
    // Función auxiliar para ejecutar operaciones y manejar excepciones
    private void ejecutarOperacion(OperacionDAO op) {
        try {
            String resultado = op.ejecutar();
            // Mostrar el resultado en una ventana de diálogo o en un label de estado
            System.out.println("Resultado de la operación: " + resultado);
        } catch (Exception ex) {
            System.err.println("Error en la operación: " + ex.getMessage());
        }
    }

    @FunctionalInterface
    interface OperacionDAO {
        String ejecutar() throws Exception;
    }
}
