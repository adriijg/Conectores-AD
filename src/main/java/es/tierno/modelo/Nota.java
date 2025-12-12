package es.tierno.modelo;

public class Nota {

    private int id;
    private String asignatura;
    private int nota;
    private int alumnoId;

    public Nota(int id, String asignatura, int nota, int alumnoId) {
        this.id = id;
        this.asignatura = asignatura;
        this.nota = nota;
        this.alumnoId = alumnoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public int getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(int alumnoId) {
        this.alumnoId = alumnoId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("N -> ");
        sb.append("ID: ").append(id);
        sb.append(", Asignatura: ").append(asignatura);
        sb.append(", Nota: ").append(nota);
        sb.append(", Alumno ID: ").append(alumnoId);
        sb.append(';');
        return sb.toString();
    }

}
