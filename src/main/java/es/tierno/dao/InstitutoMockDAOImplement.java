package es.tierno.dao;

import java.util.ArrayList;
import java.util.List;

import es.tierno.modelo.Alumno;
import es.tierno.modelo.Nota;

public class InstitutoMockDAOImplement implements InstitutoDAO {

    private List<Alumno> alumnos = new ArrayList<>();
    private List<Nota> notas = new ArrayList<>();

    @Override
    public void crearTablaAlumno() {}
    @Override
    public void crearTablaNotas() {}

    @Override
    public int insertarAlumno(Alumno a) {
        alumnos.add(a);
        return 1;
    }

    @Override
    public int insertarNota(Nota n) {
        notas.add(n);
        return 1;
    }

    @Override
    public int actualizarAlumno(Alumno a) {
        return 1;
    }

    @Override
    public int actualizarNota(Nota n) {
        return 1;
    }

    @Override
    public List<Alumno> listarAlumnos() {
        return alumnos;
    }

        public List<Nota> listarNotas() {
        return notas;
    }

    @Override
    public List<String> listarAlumnosConNotas() {
        List<String> out = new ArrayList<>();
        return out;
    }

    @Override
    public int borrarAlumno(Alumno a) {
        return 1;
    }

    @Override
    public int borrarNota(Nota n) {
        return 1;
    }

    @Override
    public List<Alumno> consultarAlumno(int edad) {
        List<Alumno> out = new ArrayList<>();
        return out;
    }
}