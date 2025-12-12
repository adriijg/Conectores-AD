package es.tierno.dao;

import java.sql.SQLException;
import java.util.List;

import es.tierno.modelo.Alumno;
import es.tierno.modelo.Nota;

public interface InstitutoDAO {

    public void crearTablaAlumno() throws Exception;
    public void crearTablaNotas() throws Exception;

    public int insertarAlumno(Alumno a) throws SQLException;
    public int insertarNota(Nota n) throws SQLException;

    public int actualizarAlumno(Alumno a) throws SQLException;
    public int actualizarNota(Nota n) throws SQLException;

    public List<Alumno> listarAlumnos() throws SQLException;
    public List<Nota> listarNotas() throws SQLException;
    public List<String> listarAlumnosConNotas() throws SQLException;
    
    public int borrarAlumno(Alumno a) throws SQLException;
    public int borrarNota(Nota n) throws SQLException;

    public List<Alumno> consultarAlumno(int edad) throws SQLException;
}
