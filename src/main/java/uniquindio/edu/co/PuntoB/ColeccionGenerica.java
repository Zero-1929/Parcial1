package uniquindio.edu.co.PuntoB;

/**
 * Define las operaciones básicas de una colección genérica.
 *
 * @param <T> tipo de elementos almacenados
 */
public interface ColeccionGenerica<T> extends Iterable<T> {

    /**
     *
     * Agrega un elemento al final de la colección.
     */
    void agregar(T elemento);

    /**
     * Elimina la primera aparición del elemento.
     *
     * @return true si se eliminó; false si no se encontró
     */
    boolean eliminar(T elemento);

    /**
     * Obtiene un elemento usando un índice que comienza en cero.
     */
    T obtener(int indice);

    int tamano();

    boolean estaVacia();
}

