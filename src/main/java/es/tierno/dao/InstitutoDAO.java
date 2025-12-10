package es.tierno.dao;

import java.sql.SQLException;
import java.util.List;

import es.tierno.modelo.Alumno;

public interface InstitutoDAO {

    public void crearTablaAlumno() throws Exception;
    public void crearTablaNotas() throws Exception;
    public void eliminarTablaAlumno()throws Exception;
    public List<Alumno> listarAlumnos() throws SQLException;
    public List<Alumno> listarAlumnos(int edad) throws SQLException;
    public int insertar(Alumno a) throws SQLException;
    public int insertar(List<Alumno> alumnos) throws SQLException;
    public int actualizar(Alumno a) throws SQLException;
    public int borrar(Alumno a) throws SQLException;
}
