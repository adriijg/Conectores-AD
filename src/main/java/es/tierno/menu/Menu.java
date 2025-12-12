package es.tierno.menu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import es.tierno.Modo;
import es.tierno.dao.InstitutoDAO;
import es.tierno.dao.InstitutoDAOFactory;
import es.tierno.modelo.Alumno;
import es.tierno.modelo.Nota;

public class Menu {

    private Scanner scanner = new Scanner(System.in);

    // TESTING MAIN METHOD
    public static void main(String[] args) throws SQLException {

        Menu menu = new Menu();

        // menu.getAlumnoID(menu.getConnection());
        try {
            InstitutoDAO dao = menu.menuPrincipal();
            menu.menuSecundario(dao, getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CONNECTION TEST METHOD
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:src/data/database.db");
    }

    // ==========================================================
    private InstitutoDAO menuPrincipal() throws Exception {

        String menuPrincipal = """
            ====== MENÚ PRINCIPAL ======
            1. Mock
            2. SQLite
            3. Oracle XE
                """;

        System.out.println(menuPrincipal);
        int opcion = scanner.nextInt();
        Modo modo;

        switch (opcion) {
            case 1:
                modo = Modo.MOCK;
                break;
            case 2:
                modo = Modo.SQLITE;
                break;
            case 3:
                modo = Modo.ORACLE;
                break;
            default:
                throw new AssertionError();
        }

        InstitutoDAO dao = InstitutoDAOFactory.obtenerDAO(modo);

        return dao;
    }

    private void menuSecundario(InstitutoDAO dao, Connection conn) throws Exception {

        boolean salir = false;

        while (!(salir == true)) {

            System.out.println(mostrarOpciones(TipoMenu.SECUNDARIO));
            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    crearTablas(dao);
                    break;
                case 2:
                    insertarAlumno(dao, conn);
                    break;
                case 3:
                    // Nuevo modelo de tabla, mas sencillo
                    insertarNota(dao, conn);
                    break;
                case 0:
                    System.out.println("Cerrando programa...");
                    salir = true;
                    scanner.close();
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }

    private String mostrarOpciones(TipoMenu tipo) {

        return switch (tipo) {
            case PRINCIPAL ->
                """
            ====== MENÚ PRINCIPAL ======
            1. Mock
            2. SQLite
            3. Oracle XE
            """;

            case SECUNDARIO ->
                """
            ====== MENÚ SECUNDARIO ======
            1. Crear tablas Alumno y Nota
            2. Insertar alumno
            3. Insertar nota
            4. Listar alumnos
            5. Listar notas + alumnos
            6. Actualizar alumno
            7. Actualizar nota
            8. Consultar alumnos por edad
            0. Salir
            """;
        };
    }

    private void crearTablas(InstitutoDAO dao) throws Exception {
        System.out.println("Creando tablas...");
        dao.crearTablaAlumno();
        dao.crearTablaNotas();
        System.out.println("Tablas creadas correctamente.");
    }

    private void insertarAlumno(InstitutoDAO dao, Connection conn) throws SQLException {
        scanner.nextLine(); // Limpiar buffer
        System.out.println("Nombre: ");
        String nombre = scanner.next();
        System.out.println("Apellido: ");
        String apellido = scanner.next();
        System.out.println("Edad: ");
        int edad = scanner.nextInt();

        int id = getMaxTableID(conn, Tablas.ALUMNO) + 1;

        Alumno a = new Alumno(id, nombre, apellido, edad);
        dao.insertarAlumno(a);
        System.out.println("Alumno insertado correctamente.");
    }

    private void insertarNota(InstitutoDAO dao, Connection conn) throws SQLException {
        scanner.nextLine(); // Limpiar buffer
        System.out.println("Asignatura: ");
        String asig = scanner.nextLine();
        System.out.println("Nota: ");
        int nota = scanner.nextInt();
        if (!notaValida(nota)) {
            System.out.println("La nota debe estar entre 0 y 10.");
            return;
        }
        System.out.println("ID Alumno: ");
        int alumnoId = scanner.nextInt();
        if (!existAlumno(conn, alumnoId)) {
            System.out.println("El alumno con ID " + alumnoId + " no existe.");
            return;
        } else {
            Nota n = new Nota(getMaxTableID(conn, Tablas.NOTA) + 1, asig, nota, alumnoId);
            dao.insertarNota(n);
            System.out.println("Nota insertada correctamente.");
        }
    }

    private int getMaxTableID(Connection conn, Tablas table) throws SQLException {
        String query = String.format("SELECT MAX(id) FROM %s", table.toString());
        PreparedStatement ps = conn.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        rs.close();
        ps.close();

        return id;
    }

    private boolean existAlumno(Connection conn, int id) throws SQLException {
        String query = "SELECT id FROM ALUMNO WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        boolean exist = rs.next();

        rs.close();
        ps.close();

        return exist;
    }

    private boolean notaValida(int nota) {
        return nota >= 0 && nota <= 10;
    }

    private enum TipoMenu {
        PRINCIPAL,
        SECUNDARIO
    }
}
