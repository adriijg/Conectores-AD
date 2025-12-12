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
import es.tierno.modelo.Alumno;
import es.tierno.modelo.Nota;

public class Menu {

    // ==========================================================
    // MAIN
    public static void main(String[] args) throws SQLException {
        Menu menu = new Menu();
        try {
            InstitutoDAO dao = menu.menuPrincipal();
            menu.menuSecundario(dao, getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================================
    // CONEXIÓN
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:src/data/database.db");
    }

    // ==========================================================
    // MENÚ PRINCIPAL
    private InstitutoDAO menuPrincipal() throws Exception {
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

        return InstitutoDAOFactory.obtenerDAO(modo);
    }

    // ==========================================================
    // MENÚ SECUNDARIO
    private void menuSecundario(InstitutoDAO dao, Connection conn) throws Exception {
        boolean salir = false;

        while (!salir) {
            String input = JOptionPane.showInputDialog(null, mostrarOpciones(TipoMenu.SECUNDARIO), "Menú Secundario", JOptionPane.QUESTION_MESSAGE);
            if (input == null) { // Si cierra la ventana
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
        String nombre = JOptionPane.showInputDialog("Nombre:");
        if (nombre == null) {
            return;
        }

        String apellido = JOptionPane.showInputDialog("Apellido:");
        if (apellido == null) {
            return;
        }

        String edadStr = JOptionPane.showInputDialog("Edad:");
        if (edadStr == null) {
            return;
        }
        int edad = Integer.parseInt(edadStr.trim());

        int id = getMaxTableID(conn, Tablas.ALUMNO) + 1;
        Alumno a = new Alumno(id, nombre, apellido, edad);
        dao.insertarAlumno(a);

        JOptionPane.showMessageDialog(null, "Alumno insertado correctamente.");
    }

    // ==========================================================
    // INSERTAR NOTA
    private void insertarNota(InstitutoDAO dao, Connection conn) throws SQLException {
        String asig = JOptionPane.showInputDialog("Asignatura:");
        if (asig == null) {
            return;
        }

        String notaStr = JOptionPane.showInputDialog("Nota (0-10):");
        if (notaStr == null) {
            return;
        }
        int nota = Integer.parseInt(notaStr.trim());
        if (!notaValida(nota)) {
            JOptionPane.showMessageDialog(null, "La nota debe estar entre 0 y 10.");
            return;
        }

        String alumnoIdStr = JOptionPane.showInputDialog("ID Alumno:");
        if (alumnoIdStr == null) {
            return;
        }
        int alumnoId = Integer.parseInt(alumnoIdStr.trim());

        if (!existAlumno(conn, alumnoId)) {
            JOptionPane.showMessageDialog(null, "El alumno con ID " + alumnoId + " no existe.");
            return;
        }

        Nota n = new Nota(getMaxTableID(conn, Tablas.NOTA) + 1, asig, nota, alumnoId);
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
        // 1. Listar alumnos
        StringBuilder sb = new StringBuilder();
        sb.append("ALUMNOS:\n");
        ResultSet rs = conn.prepareStatement("SELECT * FROM ALUMNO").executeQuery();
        while (rs.next()) {
            sb.append(rs.getInt("id")).append(": ")
                    .append(rs.getString("nombre")).append(" ")
                    .append(rs.getString("apellido")).append(" | Edad: ")
                    .append(rs.getInt("edad")).append("\n");
        }
        rs.close();

        // 2. Pedir ID del alumno a editar
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

        // 3. Comprobar si existe el alumno
        if (!existAlumno(conn, id)) {
            JOptionPane.showMessageDialog(null, "El alumno con ID " + id + " no existe.");
            return;
        }

        // 4. Pedir nuevos datos
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

        // 5. Crear objeto Alumno manteniendo el mismo ID y actualizarlo
        Alumno a = new Alumno(id, nombre, apellido, edad);
        int filas = dao.actualizarAlumno(a);

        JOptionPane.showMessageDialog(null, filas + " alumno(s) actualizado(s) correctamente.");
    }

    // ==========================================================
    // ACTUALIZAR NOTA
    private void actualizarNota(InstitutoDAO dao, Connection conn) throws SQLException {
        // 1. Listar alumnos
        StringBuilder sb = new StringBuilder();
        sb.append("ALUMNOS:\n");
        ResultSet rsAlumnos = conn.prepareStatement("SELECT * FROM ALUMNO").executeQuery();
        while (rsAlumnos.next()) {
            sb.append(rsAlumnos.getInt("id")).append(": ")
                    .append(rsAlumnos.getString("nombre")).append(" ")
                    .append(rsAlumnos.getString("apellido")).append("\n");
        }
        rsAlumnos.close();

        String idAlumnoStr = JOptionPane.showInputDialog(null, sb.toString() + "\nIntroduce el ID del alumno cuya nota quieres actualizar:");
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

        if (!existAlumno(conn, idAlumno)) {
            JOptionPane.showMessageDialog(null, "El alumno con ID " + idAlumno + " no existe.");
            return;
        }

        // 2. Listar notas del alumno
        StringBuilder sbNotas = new StringBuilder();
        sbNotas.append("NOTAS DEL ALUMNO:\n");
        PreparedStatement psNotas = conn.prepareStatement("SELECT * FROM NOTA WHERE alumno_id = ?");
        psNotas.setInt(1, idAlumno);
        ResultSet rsNotas = psNotas.executeQuery();

        boolean tieneNotas = false;
        while (rsNotas.next()) {
            tieneNotas = true;
            sbNotas.append(rsNotas.getInt("id")).append(": ")
                    .append(rsNotas.getString("asignatura")).append(" = ")
                    .append(rsNotas.getInt("nota")).append("\n");
        }
        rsNotas.close();
        psNotas.close();

        if (!tieneNotas) {
            JOptionPane.showMessageDialog(null, "Este alumno no tiene notas registradas.");
            return;
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

        // 3. Pedir nueva nota
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

        // 4. Crear objeto Nota con id, asignatura y alumnoId no modificados (asignatura se puede consultar si quieres)
        // Aquí simplemente pasamos null para asignatura, ya que dao.actualizarNota solo necesita id y nota
        Nota n = new Nota(idNota, null, nuevaNota, idAlumno);
        int filas = dao.actualizarNota(n);

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
                JOptionPane.showMessageDialog(null, "No hay alumnos con " + edad + " años.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
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
            // Obtenemos la lista de alumnos
            List<Alumno> alumnos = dao.listarAlumnos();
            if (alumnos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay alumnos para borrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Construimos un String con todos los alumnos
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
                return; // Cancelar

                        }int id;
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscamos el alumno por ID
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

            // Llamamos al DAO para borrar
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
            // Primero pedimos el ID del alumno
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

            // Ahora obtenemos las notas de ese alumno
            List<Nota> notas = dao.listarNotas(); // Suponemos que listarNotas devuelve todas las notas
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

            // Mostramos las notas del alumno
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

            // Borramos la nota
            dao.borrarNota(notaABorrar);
            JOptionPane.showMessageDialog(null, "Nota borrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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

    // ==========================================================
    private enum TipoMenu {
        PRINCIPAL,
        SECUNDARIO
    }

    public enum Tablas {
        ALUMNO, NOTA
    }
}
