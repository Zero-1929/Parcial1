
package uniquindio.edu.co.PuntoB;

/**
 * Realiza análisis recursivos sobre los paquetes.
 */
public class AnalisisRecursivo {

    /**
     * Calcula el peso total de los paquetes de un municipio.
     */
    public static double pesoTotalPorMunicipio(
            ColeccionGenerica<Paquete> paquetes,
            String municipio,
            int indice) {

        validarColeccion(paquetes);

        if (municipio == null || municipio.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El municipio no puede estar vacío.");
        }

        if (indice < 0 || indice > paquetes.tamano()) {
            throw new IndexOutOfBoundsException(
                    "El índice está fuera de la colección.");
        }

        // Caso base: ya se revisaron todos los paquetes.
        if (indice == paquetes.tamano()) {
            return 0;
        }

        Paquete paquete = paquetes.obtener(indice);
        double pesoActual = 0;

        // Solo suma el peso si coincide el destino.
        if (paquete.getDestino().equalsIgnoreCase(municipio.trim())) {
            pesoActual = paquete.getPeso();
        }

        // Continúa con el siguiente paquete.
        return pesoActual + pesoTotalPorMunicipio(
                paquetes, municipio, indice + 1);
    }

    /**
     * Cuenta los paquetes cuya prioridad es igual o superior
     * a la prioridad mínima indicada.
     */
    public static int contarPorPrioridad(
            ColeccionGenerica<Paquete> paquetes,
            int prioridadMinima,
            int indice) {

        validarColeccion(paquetes);

        if (indice < 0 || indice > paquetes.tamano()) {
            throw new IndexOutOfBoundsException(
                    "El índice está fuera de la colección.");
        }

        // Caso base: ya se revisaron todos los paquetes.
        if (indice == paquetes.tamano()) {
            return 0;
        }

        Paquete paquete = paquetes.obtener(indice);
        int cantidadActual = 0;

        if (paquete.getPrioridad() >= prioridadMinima) {
            cantidadActual = 1;
        }

        // Suma el resultado de revisar el resto de la colección.
        return cantidadActual + contarPorPrioridad(
                paquetes, prioridadMinima, indice + 1);
    }

    /**
     * Comprueba que la colección exista.
     */
    private static void validarColeccion(
            ColeccionGenerica<Paquete> paquetes) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La colección de paquetes no puede ser null.");
        }
    }
}
