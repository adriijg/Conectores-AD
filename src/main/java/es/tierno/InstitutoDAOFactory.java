package es.tierno;

public class InstitutoDAOFactory {

    public static InstitutoDAO obtenerDAO (Modo modo) throws Exception {
        InstitutoDAO salida;
        switch (modo) {
            case SQLITE:
                salida = new InstitutoSQLiteDAOImplement();
                break;
            case MOCK:
                salida = new InstitutoMockDAOImplement();
                break;
            default:
                throw new IllegalArgumentException("Modo no válido");
        }
        return salida;
    }
}
