
package uniquindio.edu.co.PuntoB;

import java.util.ArrayList;

/**
 * Busca paquetes por código mediante búsqueda binaria.
 */
public class BusquedaBinaria {

    /**
     * Busca un paquete por su código en una colección ordenada.
     */
    public static Paquete buscarPorCodigo(
            ColeccionGenerica<Paquete> paquetes,
            String codigo) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La colección de paquetes no puede ser null.");
        }

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El código no puede estar vacío.");
        }

        ArrayList<Paquete> copia = copiarYValidarOrden(paquetes);

        return buscarPorCodigo(
                copia, codigo.trim(), 0, copia.size() - 1);
    }

    /**
     * Busca recursivamente un código dentro de un rango.
     */
    private static Paquete buscarPorCodigo(
            ArrayList<Paquete> paquetes,
            String codigo,
            int inicio,
            int fin) {

        // Caso base: el código no se encuentra.
        if (inicio > fin) {
            return null;
        }

        int medio = inicio + (fin - inicio) / 2;
        Paquete paqueteMedio = paquetes.get(medio);

        int comparacion =
                paqueteMedio.getCodigo().compareTo(codigo);

        // El código del centro coincide con el buscado.
        if (comparacion == 0) {
            return paqueteMedio;
        }

        // El código buscado debe estar en la mitad derecha.
        if (comparacion < 0) {
            return buscarPorCodigo(
                    paquetes, codigo, medio + 1, fin);
        }

        // El código buscado debe estar en la mitad izquierda.
        return buscarPorCodigo(
                paquetes, codigo, inicio, medio - 1);
    }

    /**
     * Copia los paquetes y comprueba que estén ordenados
     * por código ascendente.
     */
    private static ArrayList<Paquete> copiarYValidarOrden(
            ColeccionGenerica<Paquete> paquetes) {

        ArrayList<Paquete> copia = new ArrayList<>();

        for (Paquete paquete : paquetes) {
            copia.add(paquete);
        }

        for (int i = 1; i < copia.size(); i++) {
            String anterior = copia.get(i - 1).getCodigo();
            String actual = copia.get(i).getCodigo();

            if (anterior.compareTo(actual) > 0) {
                throw new IllegalArgumentException(
                        "La colección debe estar ordenada "
                                + "por código ascendente.");
            }
        }

        return copia;
    }
}

