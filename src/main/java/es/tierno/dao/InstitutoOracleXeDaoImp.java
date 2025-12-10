package es.tierno.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.tierno.modelo.Alumno;

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
        final String query = "CREATE TABLE ALUMNO(nombre VARCHAR2(50) NOT NULL, apellido VARCHAR2(50) NOT NULL, edad INTEGER, PRIMARY KEY (nombre, apellido))";

        PreparedStatement ps = conn.prepareStatement(query);
        int r = ps.executeUpdate();
        System.out.println(r);
        ps.close();
        System.out.println("Tabla ALUMNO creada correctamente.");
    }

    @Override
    public void crearTablaNotas() throws Exception {
        final String query = "CREATE TABLE NOTAS(id INTEGER PRIMARY KEY, nombre_alumno VARCHAR2(50) NOT NULL, apellido_alumno VARCHAR2(50) NOT NULL, asignatura VARCHAR2(50) NOT NULL, nota INTEGER, FOREIGN KEY (nombre_alumno, apellido_alumno) REFERENCES ALUMNO(nombre, apellido))";

        PreparedStatement ps = conn.prepareStatement(query);
        int r = ps.executeUpdate();
        System.out.println(r);
        ps.close();
        System.out.println("Tabla NOTAS creada correctamente.");
    }

    @Override
    public void eliminarTablaAlumno() throws Exception {
        final String query = "DROP TABLE ALUMNO";

        PreparedStatement ps = conn.prepareStatement(query);
        int result = ps.executeUpdate();
        System.out.println(result);
        ps.close();
        System.out.println("Tabla ALUMNO eliminada correctamente.");
    }

        @Override
    public List<Alumno> listarAlumnos() throws SQLException {

        final String query = "SELECT nombre, apellido, edad FROM alumno";

        List<Alumno> alumnos = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            int edad = rs.getInt("edad");

            Alumno a = new Alumno(nombre, apellido, edad);
            alumnos.add(a);
        }
        rs.close();
        ps.close();

        return alumnos;
    }

    @Override
    public List<Alumno> listarAlumnos(int edad) throws SQLException {
        final String query = "SELECT nombre, apellido, edad FROM alumno WHERE edad >= ?";

        
        List<Alumno> alumnos = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setInt(1, edad);
        
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            int e = rs.getInt("edad");

            Alumno a = new Alumno(nombre, apellido, e);
            alumnos.add(a);
        }
        rs.close();
        ps.close();

        return alumnos;
    }

    @Override
    public int insertar(Alumno a) throws SQLException {
        final String query = "INSERT INTO Alumno (nombre, apellido, edad) VALUES (?,?,?)";

        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, a.getNombre());
        ps.setString(2, a.getApellido());
        ps.setInt(3, a.getEdad());
        ps.executeUpdate();

        ps.close();

        return 1;
    }

    @Override
    public int insertar(List<Alumno> alumnos) throws SQLException {
        final String query = "INSERT INTO Alumno (nombre, apellido, edad) VALUES (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(query);
        int alumnInserts = 0;

        for (Alumno a : alumnos) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setInt(3, a.getEdad());
            alumnInserts = ps.executeUpdate();
        }

        ps.close();

        return alumnInserts;
    }

    @Override
    public int actualizar(Alumno a) throws SQLException {
        final String query = "UPDATE Alumno SET Edad = 25 WHERE nombre =  ? AND apellido = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, a.getNombre());
        ps.setString(2, a.getApellido());

        return ps.executeUpdate();
    }

    @Override
    public int borrar(Alumno a) throws SQLException {
        final String query = "DELETE FROM ALUMNO WHERE nombre = ? AND apellido = ?";

        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, a.getNombre());
        ps.setString(2, a.getApellido());

        return ps.executeUpdate();
    }
    
}
