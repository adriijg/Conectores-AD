package es.tierno.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.tierno.modelo.Alumno;
import es.tierno.modelo.Nota;

public class InstitutoMockDAOImplement implements InstitutoDAO {

    private List<Alumno> alumnos = new ArrayList<>();
    private List<Nota> notas = new ArrayList<>();

    @Override
    public void crearTablaAlumno() {
        alumnos.clear();
    }

    @Override
    public void crearTablaNotas() {
        notas.clear();
    }

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
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getId() == a.getId()) {
                alumnos.set(i, a);
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int actualizarNota(Nota n) {
        for (int i = 0; i < notas.size(); i++) {
            if (notas.get(i).getId() == n.getId()) {
                // Solo actualizamos la nota, no la asignatura ni el alumnoId
                Nota notaExistente = notas.get(i);
                notas.set(i, new Nota(n.getId(), notaExistente.getAsignatura(), n.getNota(), notaExistente.getAlumnoId()));
                return 1;
            }
        }
        return 0;
    }

    @Override
    public List<Alumno> listarAlumnos() {
        return new ArrayList<>(alumnos);
    }

    public List<Nota> listarNotas() {
        return new ArrayList<>(notas);
    }

    @Override
    public List<String> listarAlumnosConNotas() {
        List<String> out = new ArrayList<>();
        for (Alumno a : alumnos) {
            List<Nota> notasAlumno = notas.stream()
                                          .filter(n -> n.getAlumnoId() == a.getId())
                                          .toList();
            for (Nota n : notasAlumno) {
                out.add(a.getNombre() + " " + a.getApellido() + " | " + n.getAsignatura() + " = " + n.getNota());
            }
        }
        return out;
    }

    @Override
    public int borrarAlumno(Alumno a) {
        boolean removed = alumnos.removeIf(al -> al.getId() == a.getId());
        // También borramos sus notas
        notas.removeIf(n -> n.getAlumnoId() == a.getId());
        return removed ? 1 : 0;
    }

    @Override
    public int borrarNota(Nota n) {
        boolean removed = notas.removeIf(nota -> nota.getId() == n.getId());
        return removed ? 1 : 0;
    }

    @Override
    public List<Alumno> consultarAlumno(int edad) {
        return alumnos.stream()
                      .filter(a -> a.getEdad() <= edad)
                      .collect(Collectors.toList());
    }
}