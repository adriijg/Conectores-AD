package es.tierno.modelo;

public class Nota {
    
    private int id;
    private String nombreAlumno;
    private String apellidoAlumno;
    private String asignatura;
    private int nota;

    public Nota(int id, String nombreAlumno, String apellidoAlumno, String asignatura, int nota) {
        this.id = id;
        this.nombreAlumno = nombreAlumno;
        this.apellidoAlumno = apellidoAlumno;
        this.asignatura = asignatura;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public String getApellidoAlumno() {
        return apellidoAlumno;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public int getNota() {
        return nota;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nota -> ");
        sb.append("ID: ").append(id);
        sb.append(", Nombre Alumno: ").append(nombreAlumno);
        sb.append(", Apellido Alumno: ").append(apellidoAlumno);
        sb.append(", Asignatura: ").append(asignatura);
        sb.append(", Nota: ").append(nota);
        sb.append(';');
        return sb.toString();
    }

}
