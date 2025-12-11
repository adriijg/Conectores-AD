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

    @Override
    public List<Nota> listarNotas() {
        return notas;
    }

    @Override
    public List<String> listarAlumnosConNotas() {
        List<String> salida = new ArrayList<>();
        for (Alumno a : alumnos) {
            for (Nota n : notas) {
                if (n.getNombreAlumno().equals(a.getNombre())
                        && n.getApellidoAlumno().equals(a.getApellido())) {
                    salida.add(a + " | " + n);
                }
            }
        }
        return salida;
    }

    @Override
    public int borrarAlumno(Alumno a) {
        alumnos.remove(a);
        return 1;
    }

    @Override
    public int borrarNota(Nota n) {
        notas.remove(n);
        return 1;
    }

    @Override
    public void consultarAlumno(int edad) {
        for (Alumno a : alumnos) {
            if (a.getEdad() == edad) {
                System.out.println(a);
            }
        }
    }
}