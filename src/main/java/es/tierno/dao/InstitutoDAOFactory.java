package es.tierno.dao;

import es.tierno.Modo;

public class InstitutoDAOFactory {

    public static InstitutoDAO obtenerDAO (Modo modo) throws Exception {
        InstitutoDAO salida;
        switch (modo) {
            case SQLITE:
                salida = (InstitutoDAO) new InstitutoSQLiteDAOImplement();
                break;
            case MOCK:
                salida = (InstitutoDAO) new InstitutoMockDAOImplement();
                break;
            default:
                throw new IllegalArgumentException("Modo no válido");
        }
        return salida;
    }
}
