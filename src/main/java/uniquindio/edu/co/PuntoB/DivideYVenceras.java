
package uniquindio.edu.co.PuntoB;

import java.util.ArrayList;

/**
 * Encuentra el paquete de mayor peso mediante divide y vencerás.
 */
public class DivideYVenceras {

    /**
     * Encuentra el paquete más pesado de toda la colección.
     *
     * Devuelve null si la colección está vacía.
     */
    public static Paquete encontrarMasPesado(
            ColeccionGenerica<Paquete> paquetes) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La colección de paquetes no puede ser null.");
        }

        if (paquetes.estaVacia()) {
            return null;
        }

        ArrayList<Paquete> copia = new ArrayList<>();

        for (Paquete paquete : paquetes) {
            copia.add(paquete);
        }

        return encontrarMasPesado(
                copia, 0, copia.size() - 1);
    }

    /**
     * Divide el arreglo en dos partes y compara sus resultados.
     */
    private static Paquete encontrarMasPesado(
            ArrayList<Paquete> paquetes,
            int inicio,
            int fin) {

        // Caso base: queda un solo paquete.
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        int medio = inicio + (fin - inicio) / 2;

        // Busca el paquete más pesado de cada mitad.
        Paquete izquierdo = encontrarMasPesado(
                paquetes, inicio, medio);

        Paquete derecho = encontrarMasPesado(
                paquetes, medio + 1, fin);

        // Devuelve el más pesado de los dos resultados.
        if (izquierdo.getPeso() >= derecho.getPeso()) {
            return izquierdo;
        }

        return derecho;
    }
}
