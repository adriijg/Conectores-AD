package es.tierno.modelo;

public class Alumno {

    private String nombre;
    private String apellido;
    private int edad;

    public Alumno(String nombre, String apellido, int edad) {
        this.nombre
                = nombre;
        this.apellido
                = apellido;
        this.edad
                = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre
                = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido
                = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad
                = edad;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("A -> ");
        sb.append("Nombre: ").append(nombre);
        sb.append(", Apellido: ").append(apellido);
        sb.append(", Edad: ").append(edad);
        sb.append(';');
        return sb.toString();
    }
}
