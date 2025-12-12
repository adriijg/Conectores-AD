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

public class InstitutoOracleXeDaoImp implements InstitutoDAO {

    private Connection conn;
    private final String URL = "jdbc:oracle:thin:%s/%s@localhost:1521/XEPDB1";
    private final String DB_USER = "usuario";
    private final String DB_PASSWORD = "usuario";

    public InstitutoOracleXeDaoImp() throws Exception {
        conn = DriverManager.getConnection(String.format(URL, DB_USER, DB_PASSWORD));
    }

    @Override
    public void crearTablaAlumno() throws Exception {
        final String query = "CREATE TABLE ALUMNO(id INTEGER PRIMARY KEY, nombre VARCHAR2(50) NOT NULL, apellido VARCHAR2(50) NOT NULL, edad INTEGER)";

        PreparedStatement ps = conn.prepareStatement(query);
        int r = ps.executeUpdate();
        System.out.println(r);
        ps.close();
        System.out.println("Tabla ALUMNO creada correctamente.");
    }

    @Override
    public void crearTablaNotas() throws Exception {
        final String query = "CREATE TABLE NOTA(id INTEGER PRIMARY KEY, asignatura VARCHAR2(50) NOT NULL, nota INTEGER, id INTEGER FOREIGN KEY () REFERENCES ALUMNO(nombre, apellido))";

        PreparedStatement ps = conn.prepareStatement(query);
        int r = ps.executeUpdate();
        System.out.println(r);
        ps.close();
        System.out.println("Tabla NOTA creada correctamente.");
    }

    @Override
    public int insertarAlumno(Alumno a) throws SQLException {
        String query = "INSERT INTO ALUMNO (nombre, apellido, edad) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, a.getNombre());
        ps.setString(2, a.getApellido());
        ps.setInt(3, a.getEdad());
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }

    @Override
    public int insertarNota(Nota n) throws SQLException {
        String query = "INSERT INTO NOTA (id, nombre_alumno, apellido_alumno, asignatura, nota) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, n.getId());
        ps.setString(2, n.getNombreAlumno());
        ps.setString(3, n.getApellidoAlumno());
        ps.setString(4, n.getAsignatura());
        ps.setInt(5, n.getNota());
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }

    @Override
    public int actualizarAlumno(Alumno a) throws SQLException {
        String query = "UPDATE ALUMNO SET edad = ? WHERE nombre = ? AND apellido = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, a.getEdad());
        ps.setString(2, a.getNombre());
        ps.setString(3, a.getApellido());
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }

    @Override
    public int actualizarNota(Nota n) throws SQLException {
        String query = "UPDATE NOTA SET nota = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, n.getNota());
        ps.setInt(2, n.getId());
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }

    @Override
    public List<Alumno> listarAlumnos() throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        String query = "SELECT * FROM ALUMNO";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            int edad = rs.getInt("edad");
            alumnos.add(new Alumno(nombre, apellido, edad));
        }
        rs.close();
        ps.close();
        return alumnos;
    }

    @Override
    public List<String> listarAlumnosConNotas() throws SQLException {
        String query = "SELECT a.nombre, a.apellido, a.edad, n.asignatura, n.nota FROM ALUMNO a LEFT JOIN NOTAS n ON a.nombre = n.nombre_alumno AND a.apellido = n.apellido_alumno";

        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        List<String> salida = new ArrayList<>();

        while (rs.next()) {
            salida.add(
                    rs.getString("nombre") + " "
                    + rs.getString("apellido") + " | Edad: "
                    + rs.getInt("edad") + " | Asignatura: "
                    + rs.getString("asignatura") + " | Nota: "
                    + rs.getInt("nota")
            );
        }

        rs.close();
        ps.close();
        return salida;
    }

    @Override
    public int borrarAlumno(Alumno a) throws SQLException {
        String query = "DELETE FROM ALUMNO WHERE nombre = ? AND apellido = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, a.getNombre());
        ps.setString(2, a.getApellido());
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }

    @Override
    public int borrarNota(Nota n) throws SQLException {
        String query = "DELETE FROM NOTA WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, n.getId());
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }

    @Override
    public void consultarAlumno(int edad) throws SQLException {
        String query = "SELECT * FROM ALUMNO WHERE edad = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, edad);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            int edadAlumno = rs.getInt("edad");
            System.out.println("Alumno -> Nombre: " + nombre + ", Apellido: " + apellido + ", Edad: " + edadAlumno);
        }
        rs.close();
        ps.close();
    }
}
