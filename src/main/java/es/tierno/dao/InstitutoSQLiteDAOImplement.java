package es.tierno.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.tierno.modelo.Alumno;
import es.tierno.modelo.Nota;

public class InstitutoSQLiteDAOImplement implements InstitutoDAO {

    private static final String URL = "jdbc:sqlite:src/data/database.db";

    private Connection conn;

    public InstitutoSQLiteDAOImplement() throws Exception {
        this.conn = DriverManager.getConnection(URL);
    }

        @Override
    public void crearTablaAlumno() throws Exception {
        String q = """
            CREATE TABLE IF NOT EXISTS ALUMNO(
                id INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                edad INTEGER
            )
        """;
        conn.prepareStatement(q).execute();
    }

    @Override
    public void crearTablaNotas() throws Exception {
        String q = """
            CREATE TABLE IF NOT EXISTS NOTA(
                id INTEGER PRIMARY KEY,
                asignatura TEXT NOT NULL,
                nota INTEGER,
                alumno_id INTEGER,
                FOREIGN KEY (alumno_id) REFERENCES ALUMNO(id)
            )
        """;
        conn.prepareStatement(q).execute();
    }

    @Override
    public int insertarAlumno(Alumno a) throws SQLException {
        String q = "INSERT INTO ALUMNO VALUES (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, a.getId());
        ps.setString(2, a.getNombre());
        ps.setString(3, a.getApellido());
        ps.setInt(4, a.getEdad());
        return ps.executeUpdate();
    }

    @Override
    public int insertarNota(Nota n) throws SQLException {
        String q = "INSERT INTO NOTA VALUES (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, n.getId());
        ps.setString(2, n.getAsignatura());
        ps.setInt(3, n.getNota());
        ps.setInt(4, n.getAlumnoId());
        return ps.executeUpdate();
    }

    @Override
    public int actualizarAlumno(Alumno a) throws SQLException {
        String q = "UPDATE ALUMNO SET edad=? WHERE nombre=? AND apellido=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, a.getEdad());
        ps.setString(2, a.getNombre());
        ps.setString(3, a.getApellido());
        return ps.executeUpdate();
    }

    @Override
    public int actualizarNota(Nota n) throws SQLException {
        String q = "UPDATE NOTAS SET nota=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, n.getNota());
        ps.setInt(2, n.getId());
        return ps.executeUpdate();
    }

    @Override
    public List<Alumno> listarAlumnos() throws SQLException {
        List<Alumno> out = new ArrayList<>();
        ResultSet rs = conn.prepareStatement("SELECT * FROM ALUMNO").executeQuery();
        while (rs.next()) {
            out.add(new Alumno(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getInt("edad")
            ));
        }
        return out;
    }

    @Override
    public List<Nota> listarNotas() throws SQLException {
        List<Nota> out = new ArrayList<>();
        ResultSet rs = conn.prepareStatement("SELECT * FROM NOTAS").executeQuery();
        while (rs.next()) {
            out.add(new Nota(
                    rs.getInt("id"),
                    rs.getString("asignatura"),
                    rs.getInt("nota"),
                    rs.getInt("alumno_id")
            ));
        }
        return out;
    }

    @Override
    public List<String> listarAlumnosConNotas() throws SQLException {
        List<String> out = new ArrayList<>();
        String q = """
        SELECT a.nombre,a.apellido,a.edad,n.asignatura,n.nota
        FROM ALUMNO a LEFT JOIN NOTAS n
        ON a.nombre=n.nombre_alumno AND a.apellido=n.apellido_alumno
        """;
        ResultSet rs = conn.prepareStatement(q).executeQuery();
        while (rs.next()) {
            out.add(
                    rs.getString("nombre") + " " +
                    rs.getString("apellido") + " | " +
                    rs.getString("asignatura") + " = " +
                    rs.getInt("nota")
            );
        }
        return out;
    }

    @Override
    public int borrarAlumno(Alumno a) throws SQLException {
        String q = "DELETE FROM ALUMNO WHERE nombre=? AND apellido=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1, a.getNombre());
        ps.setString(2, a.getApellido());
        return ps.executeUpdate();
    }

    @Override
    public int borrarNota(Nota n) throws SQLException {
        String q = "DELETE FROM NOTAS WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, n.getId());
        return ps.executeUpdate();
    }

    @Override
    public void consultarAlumno(int edad) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM ALUMNO WHERE edad=?"
        );
        ps.setInt(1, edad);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(
                    rs.getString("nombre") + " " +
                    rs.getString("apellido")
            );
        }
    }
}
