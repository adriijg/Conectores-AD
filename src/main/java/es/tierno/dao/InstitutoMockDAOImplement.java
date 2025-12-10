package es.tierno.dao;

import java.sql.SQLException;
import java.util.List;

import es.tierno.modelo.Alumno;

public class InstitutoMockDAOImplement implements  InstitutoDAO {

    @Override
    public void crearTablaAlumno() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void crearTablaNotas() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eliminarTablaAlumno() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Alumno> listarAlumnos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Alumno> listarAlumnos(int edad) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int insertar(Alumno a) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int insertar(List<Alumno> alumnos) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int actualizar(Alumno a) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int borrar(Alumno a) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
