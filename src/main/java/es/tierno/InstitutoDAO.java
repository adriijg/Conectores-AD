package es.tierno;

import java.sql.SQLException;
import java.util.List;

public interface InstitutoDAO {

    public void crearTablaAlumno() throws Exception;
    public void eliminarTablaAlumno()throws Exception;
    public List<Alumno> listarAlumnos() throws SQLException;
    public List<Alumno> listarAlumnos(int edad) throws SQLException;
    public int insertar(Alumno a) throws SQLException;
    public int insertar(List<Alumno> alumnos) throws SQLException;
    public int actualizar(Alumno a) throws SQLException;
    public int borrar(Alumno a) throws SQLException;
}
