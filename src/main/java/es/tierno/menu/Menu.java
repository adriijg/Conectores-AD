package es.tierno.menu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import es.tierno.Modo;
import es.tierno.dao.InstitutoDAO;
import es.tierno.dao.InstitutoDAOFactory;
import es.tierno.dao.InstitutoMockDAOImplement;
import es.tierno.modelo.Alumno;
import es.tierno.modelo.Nota;

public class Menu {
    
    // ==========================================================
    // INICIAR MENÚ
    public void iniciar() throws Exception {
        Modo modo = menuPrincipal();
        InstitutoDAO dao = InstitutoDAOFactory.obtenerDAO(modo);
        menuSecundario(dao, getConnection(modo));
    }

    // ==========================================================
    // CONEXIÓN
    public static Connection getConnection(Modo modo) throws SQLException {
        switch (modo) {
            case SQLITE:
                return DriverManager.getConnection("jdbc:sqlite:src/data/database.db");
            case ORACLE:
                String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
                String user = "usuario";
                String password = "usuario";
                return DriverManager.getConnection(url, user, password);
            case MOCK:
                return null;
            default:
                throw new IllegalArgumentException("Modo no soportado");
        }
    }

    // ==========================================================
    // MENÚ PRINCIPAL
    private Modo menuPrincipal() throws Exception {
        String input = JOptionPane.showInputDialog(null, mostrarOpciones(TipoMenu.PRINCIPAL), "Menú Principal", JOptionPane.QUESTION_MESSAGE);

        if (input == null) {
            System.exit(0);
        }

        int opcion = Integer.parseInt(input.trim());
        Modo modo;

        switch (opcion) {
            case 1 ->
                modo = Modo.MOCK;
            case 2 ->
                modo = Modo.SQLITE;
            case 3 ->
                modo = Modo.ORACLE;
            default ->
                throw new AssertionError();
        }

        return modo;
    }

    // ==========================================================
    // MENÚ SECUNDARIO
    private void menuSecundario(InstitutoDAO dao, Connection conn) throws Exception {
        if (conn != null) {
            conn = null;
        }

        boolean salir = false;

        while (!salir) {
            String input = JOptionPane.showInputDialog(null, mostrarOpciones(TipoMenu.SECUNDARIO), "Menú Secundario", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                salir = true;
                break;
            }
            int opcion = Integer.parseInt(input.trim());

            switch (opcion) {
                case 1 ->
                    crearTablas(dao);
                case 2 ->
                    insertarAlumno(dao, conn);
                case 3 ->
                    insertarNota(dao, conn);
                case 4 ->
                    listarAlumnos(dao);
                case 5 ->
                    listarNotasAlumno(dao);
                case 6 ->
                    actualizarAlumno(dao, conn);
                case 7 ->
                    actualizarNota(dao, conn);
                case 8 ->
                    consultarAlumnosPorEdad(dao);
                case 9 ->
                    borrarAlumno(dao);
                case 10 ->
                    borrarNota(dao);
                case 0 -> {
                    JOptionPane.showMessageDialog(null, "Cerrando programa...");
                    salir = true;
                }
                default ->
                    throw new AssertionError();
            }
        }
    }

    // ==========================================================
    // MOSTRAR OPCIONES
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
                9. Borrar alumno
                10. Borrar nota
                0. Salir
                """;
        };
    }

    // ==========================================================
    // CREAR TABLAS
    private void crearTablas(InstitutoDAO dao) throws Exception {
        dao.crearTablaAlumno();
        dao.crearTablaNotas();
        JOptionPane.showMessageDialog(null, "Tablas creadas correctamente.");
    }

    // ==========================================================
    // INSERTAR ALUMNO
    private void insertarAlumno(InstitutoDAO dao, Connection conn) throws SQLException {
        String nombre = pedirTexto("Nombre:");
        if (nombre == null) {
            return;
        }

        String apellido = pedirTexto("Apellido:");
        if (apellido == null) {
            return;
        }

        String edadStr = pedirTexto("Edad:");
        if (edadStr == null) {
            return;
        }
        int edad = Integer.parseInt(edadStr.trim());

        int id = (conn == null) ? getNextId(conn, Tablas.ALUMNO, dao) : 0; // DB asigna ID
        Alumno a = new Alumno(id, nombre, apellido, edad);

        dao.insertarAlumno(a);
        JOptionPane.showMessageDialog(null, "Alumno insertado correctamente.");
    }

    // ==========================================================
    // INSERTAR NOTA
    private void insertarNota(InstitutoDAO dao, Connection conn) throws SQLException {
        String asig = pedirTexto("Asignatura:");
        if (asig == null) {
            return;
        }

        String notaStr = pedirTexto("Nota (0-10):");
        if (notaStr == null) {
            return;
        }
        int nota = Integer.parseInt(notaStr.trim());
        if (!notaValida(nota)) {
            JOptionPane.showMessageDialog(null, "La nota debe estar entre 0 y 10.");
            return;
        }

        String alumnoIdStr = pedirTexto("ID Alumno:");
        if (alumnoIdStr == null) {
            return;
        }
        int alumnoId = Integer.parseInt(alumnoIdStr.trim());

        if (conn != null && !existAlumno(conn, alumnoId, dao)) {
            JOptionPane.showMessageDialog(null, "El alumno con ID " + alumnoId + " no existe.");
            return;
        }

        int id = (conn == null) ? getNextId(conn, Tablas.NOTA, dao) : 0; // DB asigna ID
        Nota n = new Nota(id, asig, nota, alumnoId);

        dao.insertarNota(n);
        JOptionPane.showMessageDialog(null, "Nota insertada correctamente.");
    }

    // ==========================================================
    // LISTAR ALUMNOS
    private void listarAlumnos(InstitutoDAO dao) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (Alumno a : dao.listarAlumnos()) {
            sb.append(a.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Alumnos", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================================
    // LISTAR NOTAS
    private void listarNotasAlumno(InstitutoDAO dao) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (String s : dao.listarAlumnosConNotas()) {
            sb.append(s).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Notas + Alumnos", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================================
    // ACTUALIZAR ALUMNO
    private void actualizarAlumno(InstitutoDAO dao, Connection conn) throws SQLException {
        List<Alumno> alumnos;

        // Obtener alumnos según modo
        if (conn == null) { // MOCK
            alumnos = dao.listarAlumnos();
        } else { // BD real
            alumnos = new ArrayList<>();
            ResultSet rs = conn.prepareStatement("SELECT * FROM ALUMNO").executeQuery();
            while (rs.next()) {
                alumnos.add(new Alumno(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"), rs.getInt("edad")));
            }
            rs.close();
        }

        if (alumnos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay alumnos para actualizar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Mostrar alumnos
        StringBuilder sb = new StringBuilder("ALUMNOS:\n");
        for (Alumno a : alumnos) {
            sb.append(a.getId()).append(": ")
                    .append(a.getNombre()).append(" ")
                    .append(a.getApellido()).append(" | Edad: ")
                    .append(a.getEdad()).append("\n");
        }

        // Pedir ID del alumno a actualizar
        String idStr = JOptionPane.showInputDialog(null, sb.toString() + "\nIntroduce el ID del alumno a actualizar:");
        if (idStr == null) {
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.");
            return;
        }

        // Comprobar si existe
        boolean existe = conn == null
                ? alumnos.stream().anyMatch(a -> a.getId() == id)
                : existAlumno(conn, id, dao);

        if (!existe) {
            JOptionPane.showMessageDialog(null, "El alumno con ID " + id + " no existe.");
            return;
        }

        // Pedir nuevos datos
        String nombre = JOptionPane.showInputDialog(null, "Nuevo nombre:");
        if (nombre == null) {
            return;
        }
        String apellido = JOptionPane.showInputDialog(null, "Nuevo apellido:");
        if (apellido == null) {
            return;
        }
        String edadStr = JOptionPane.showInputDialog(null, "Nueva edad:");
        if (edadStr == null) {
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Edad inválida.");
            return;
        }

        // Actualizar alumno
        Alumno a = new Alumno(id, nombre, apellido, edad);
        int filas = dao.actualizarAlumno(a);

        JOptionPane.showMessageDialog(null, filas + " alumno(s) actualizado(s) correctamente.");
    }

    // ==========================================================
    // ACTUALIZAR NOTA
    private void actualizarNota(InstitutoDAO dao, Connection conn) throws SQLException {
        List<Alumno> alumnos;
        List<Nota> notas;

        // Obtener datos según modo
        if (conn == null) { // MOCK
            alumnos = dao.listarAlumnos();
            notas = ((InstitutoMockDAOImplement) dao).listarNotas();
        } else { // BD real
            alumnos = new ArrayList<>();
            ResultSet rsAlumnos = conn.prepareStatement("SELECT * FROM ALUMNO").executeQuery();
            while (rsAlumnos.next()) {
                alumnos.add(new Alumno(rsAlumnos.getInt("id"), rsAlumnos.getString("nombre"), rsAlumnos.getString("apellido"), rsAlumnos.getInt("edad")));
            }
            rsAlumnos.close();

            notas = new ArrayList<>();
            PreparedStatement psNotas = conn.prepareStatement("SELECT * FROM NOTA");
            ResultSet rsNotas = psNotas.executeQuery();
            while (rsNotas.next()) {
                notas.add(new Nota(rsNotas.getInt("id"), rsNotas.getString("asignatura"), rsNotas.getInt("nota"), rsNotas.getInt("alumno_id")));
            }
            rsNotas.close();
            psNotas.close();
        }

        if (alumnos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay alumnos registrados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Mostrar alumnos
        StringBuilder sbAlumnos = new StringBuilder("ALUMNOS:\n");
        for (Alumno a : alumnos) {
            sbAlumnos.append(a.getId()).append(": ")
                    .append(a.getNombre()).append(" ")
                    .append(a.getApellido()).append("\n");
        }

        String idAlumnoStr = JOptionPane.showInputDialog(null, sbAlumnos.toString() + "\nIntroduce el ID del alumno cuya nota quieres actualizar:");
        if (idAlumnoStr == null) {
            return;
        }

        int idAlumno;
        try {
            idAlumno = Integer.parseInt(idAlumnoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.");
            return;
        }

        boolean alumnoExiste = alumnos.stream().anyMatch(a -> a.getId() == idAlumno);
        if (!alumnoExiste) {
            JOptionPane.showMessageDialog(null, "El alumno con ID " + idAlumno + " no existe.");
            return;
        }

        // Filtrar notas del alumno
        List<Nota> notasAlumno = new ArrayList<>();
        for (Nota n : notas) {
            if (n.getAlumnoId() == idAlumno) {
                notasAlumno.add(n);
            }
        }

        if (notasAlumno.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Este alumno no tiene notas registradas.");
            return;
        }

        // Mostrar notas
        StringBuilder sbNotas = new StringBuilder("NOTAS DEL ALUMNO:\n");
        for (Nota n : notasAlumno) {
            sbNotas.append(n.getId()).append(": ")
                    .append(n.getAsignatura()).append(" = ")
                    .append(n.getNota()).append("\n");
        }

        String idNotaStr = JOptionPane.showInputDialog(null, sbNotas.toString() + "\nIntroduce el ID de la nota a actualizar:");
        if (idNotaStr == null) {
            return;
        }

        int idNota;
        try {
            idNota = Integer.parseInt(idNotaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.");
            return;
        }

        boolean notaExiste = notasAlumno.stream().anyMatch(n -> n.getId() == idNota);
        if (!notaExiste) {
            JOptionPane.showMessageDialog(null, "No existe una nota con ese ID para este alumno.");
            return;
        }

        // Pedir nueva nota
        String nuevaNotaStr = JOptionPane.showInputDialog(null, "Introduce la nueva nota (0-10):");
        if (nuevaNotaStr == null) {
            return;
        }

        int nuevaNota;
        try {
            nuevaNota = Integer.parseInt(nuevaNotaStr);
            if (nuevaNota < 0 || nuevaNota > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nota inválida, debe estar entre 0 y 10.");
            return;
        }

        // Actualizar nota
        Nota nActualizada = new Nota(idNota, null, nuevaNota, idAlumno);
        int filas = dao.actualizarNota(nActualizada);

        JOptionPane.showMessageDialog(null, filas + " nota(s) actualizada(s) correctamente.");
    }

    // ==========================================================
    // CONSULTA PARAMETRO
    private void consultarAlumnosPorEdad(InstitutoDAO dao) {
        try {
            String input = JOptionPane.showInputDialog(null, "Introduce la edad a consultar:", "Consultar alumnos", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return;

            }
            int edad;
            try {
                edad = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Edad inválida. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Alumno> alumnos = dao.consultarAlumno(edad);

            if (alumnos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay alumnos con " + edad + " años o menores.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder sb = new StringBuilder();
                for (Alumno a : alumnos) {
                    sb.append(a.getNombre()).append(" ").append(a.getApellido()).append(" (ID: ").append(a.getEdad()).append(")\n");
                }
                JOptionPane.showMessageDialog(null, sb.toString(), "Alumnos con " + edad + " años", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ==========================================================
    // BORRAR ALUMNO
    private void borrarAlumno(InstitutoDAO dao) {
        try {
            List<Alumno> alumnos = dao.listarAlumnos();
            if (alumnos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay alumnos para borrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (Alumno a : alumnos) {
                sb.append("ID: ").append(a.getId())
                        .append(" | ").append(a.getNombre())
                        .append(" ").append(a.getApellido())
                        .append(" | Edad: ").append(a.getEdad())
                        .append("\n");
            }

            String input = JOptionPane.showInputDialog(null, sb.toString() + "\nIntroduce el ID del alumno a borrar:", "Borrar Alumno", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return;

            }
            int id;
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Alumno alumnoABorrar = null;
            for (Alumno a : alumnos) {
                if (a.getId() == id) {
                    alumnoABorrar = a;
                    break;
                }
            }

            if (alumnoABorrar == null) {
                JOptionPane.showMessageDialog(null, "No existe un alumno con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dao.borrarAlumno(alumnoABorrar);
            JOptionPane.showMessageDialog(null, "Alumno borrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ==========================================================
    // BORRAR NOTA
    private void borrarNota(InstitutoDAO dao) {
        try {
            List<Alumno> alumnos = dao.listarAlumnos();
            if (alumnos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay alumnos para seleccionar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sbAlumnos = new StringBuilder();
            for (Alumno a : alumnos) {
                sbAlumnos.append("ID: ").append(a.getId())
                        .append(" | ").append(a.getNombre())
                        .append(" ").append(a.getApellido())
                        .append(" | Edad: ").append(a.getEdad())
                        .append("\n");
            }

            String inputAlumno = JOptionPane.showInputDialog(null, sbAlumnos.toString() + "\nIntroduce el ID del alumno cuyas notas quieres borrar:", "Seleccionar Alumno", JOptionPane.QUESTION_MESSAGE);
            if (inputAlumno == null) {
                return;
            }

            int alumnoId;
            try {
                alumnoId = Integer.parseInt(inputAlumno);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Alumno alumnoSeleccionado = null;
            for (Alumno a : alumnos) {
                if (a.getId() == alumnoId) {
                    alumnoSeleccionado = a;
                    break;
                }
            }

            if (alumnoSeleccionado == null) {
                JOptionPane.showMessageDialog(null, "No existe un alumno con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Nota> notas = dao.listarNotas();
            List<Nota> notasAlumno = new ArrayList<>();
            for (Nota n : notas) {
                if (n.getAlumnoId() == alumnoId) {
                    notasAlumno.add(n);
                }
            }

            if (notasAlumno.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El alumno no tiene notas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sbNotas = new StringBuilder();
            for (Nota n : notasAlumno) {
                sbNotas.append("ID Nota: ").append(n.getId())
                        .append(" | Asignatura: ").append(n.getAsignatura())
                        .append(" | Nota: ").append(n.getNota())
                        .append("\n");
            }

            String inputNota = JOptionPane.showInputDialog(null, sbNotas.toString() + "\nIntroduce el ID de la nota a borrar:", "Seleccionar Nota", JOptionPane.QUESTION_MESSAGE);
            if (inputNota == null) {
                return;
            }

            int notaId;
            try {
                notaId = Integer.parseInt(inputNota);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Nota notaABorrar = null;
            for (Nota n : notasAlumno) {
                if (n.getId() == notaId) {
                    notaABorrar = n;
                    break;
                }
            }

            if (notaABorrar == null) {
                JOptionPane.showMessageDialog(null, "No existe una nota con ese ID para este alumno.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dao.borrarNota(notaABorrar);
            JOptionPane.showMessageDialog(null, "Nota borrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ==========================================================
    // MÉTODO PARA OBTENER EL SIGUIENTE ID
    // Con este metodo arreglamos la implementación del DAO Mock para que asigne IDs correctamente
    private int getNextId(Connection conn, Tablas table, InstitutoDAO dao) throws SQLException {
        if (conn == null) { // Modo MOCK
            if (table == Tablas.ALUMNO) {
                return dao.listarAlumnos().stream().mapToInt(Alumno::getId).max().orElse(0) + 1;
            } else if (table == Tablas.NOTA) {
                return dao.listarNotas().stream().mapToInt(Nota::getId).max().orElse(0) + 1;
            } else {
                throw new IllegalArgumentException("Tabla desconocida");
            }
        } else { // Base de datos real
            return getMaxTableID(conn, table) + 1;
        }
    }

    // ==========================================================
    // MÉTODOS AUXILIARES
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

    private boolean existAlumno(Connection conn, int id, InstitutoDAO dao) throws SQLException {
        if (conn == null) {
            return dao.listarAlumnos().stream().anyMatch(a -> a.getId() == id);
        } else {
            try {
                String query = "SELECT id FROM ALUMNO WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                boolean exist = rs.next();
                rs.close();
                ps.close();
                return exist;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private boolean notaValida(int nota) {
        return nota >= 0 && nota <= 10;
    }

    private String pedirTexto(String mensaje) {
        String input = JOptionPane.showInputDialog(mensaje);
        if (input != null) {
            input = input.trim();
            if (input.isEmpty()) {
                return null;
            }
        }
        return input;
    }

    // ==========================================================
    private enum TipoMenu {
        PRINCIPAL,
        SECUNDARIO
    }

    public enum Tablas {
        ALUMNO, NOTA
    }
}
