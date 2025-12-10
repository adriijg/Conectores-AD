package es.tierno;

import es.tierno.dao.InstitutoOracleXeDaoImp;
import es.tierno.modelo.Alumno;

public class Main {
    public static void main(String[] args) {
        
        try {

            // InstitutoSQLiteDAOImplement dao = new InstitutoSQLiteDAOImplement();
            InstitutoOracleXeDaoImp dao = new InstitutoOracleXeDaoImp();

            // Crear tabla
            dao.crearTablaAlumno();
            dao.crearTablaNotas();

            // Eliminar tabla
            // dao.eliminarTablaAlumno();

            // Listar alumnos
            // List<Alumno> alumnos = dao.listarAlumnos();
            // for (Alumno a : alumnos) {
            //     System.out.println(a.toString());
            // }

            // Listar alumnos condicion
            // List<Alumno> alumnos2 = dao.listarAlumnos(20);
            // for (Alumno a : alumnos2) {
            //     System.out.println(a.toString());
            // }

            // Insertar alumno
            Alumno a = new Alumno("Toni", "Sancha", 17);
            // dao.insertar(a);

            // Insertar lista alumnos
            // ...

            // Actualizar tabla
            // dao.actualizar(a);

            // Borrar alumno
            // dao.borrar(a);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}