package uniquindio.edu.co.PuntoB;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lista simplemente enlazada propia y genérica.
 * No utiliza colecciones del Java Collections Framework
 * para almacenar los elementos.
 *
 * @param <T> tipo de datos almacenados
 */
public class ListaEnlazadaSimple<T>
        implements ColeccionGenerica<T> {

    /**
     * Nodo interno: contiene el dato y el siguiente nodo.
     */
    private class Nodo {
        private T dato;
        private Nodo siguiente;

        private Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    /**
     * Referencia al primer nodo (cabeza) y al último nodo (cola).
     * También se mantiene la cantidad de elementos.
     */
    private Nodo cabeza;
    private Nodo cola;
    private int cantidad;

    /**
     * Inicializa una lista vacía.
     */
    public ListaEnlazadaSimple() {
        cabeza = null;
        cola = null;
        cantidad = 0;
    }

    /**
     * Agrega un elemento al final.
     * Complejidad: O(1).
     */
    @Override
    public void agregar(T elemento) {
        Nodo nuevo = new Nodo(elemento);

        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
        }

        cantidad++;
    }

    /**
     * Elimina la primera aparición del elemento.
     * Complejidad: O(n).
     */
    @Override
    public boolean eliminar(T elemento) {
        if (cabeza == null) {
            return false;
        }

        // Caso 1: el elemento está en la cabeza.
        if (Objects.equals(cabeza.dato, elemento)) {
            cabeza = cabeza.siguiente;
            cantidad--;

            // Si la lista quedó vacía, la cola también es nula.
            if (cabeza == null) {
                cola = null;
            }

            return true;
        }

        // Buscar el nodo anterior al elemento que se eliminará.
        Nodo anterior = cabeza;

        while (anterior.siguiente != null
                && !Objects.equals(
                anterior.siguiente.dato, elemento)) {
            anterior = anterior.siguiente;
        }

        // El elemento no existe.
        if (anterior.siguiente == null) {
            return false;
        }

        Nodo eliminado = anterior.siguiente;

        // Enlazar el anterior con el siguiente del eliminado.
        anterior.siguiente = eliminado.siguiente;

        // Si era el último nodo, actualizar la cola.
        if (eliminado == cola) {
            cola = anterior;
        }

        cantidad--;
        return true;
    }

    /**
     * Obtiene un elemento por su índice.
     * Complejidad: O(n) en el peor caso.
     */
    @Override
    public T obtener(int indice) {
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + indice
            );
        }

        Nodo actual = cabeza;

        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }

        return actual.dato;
    }

    /**
     * Devuelve la cantidad de elementos.
     * Complejidad: O(1).
     */
    @Override
    public int tamano() {
        return cantidad;
    }

    /**
     * Indica si la lista está vacía.
     * Complejidad: O(1).
     */
    @Override
    public boolean estaVacia() {
        return cantidad == 0;
    }

    /**
     * Permite recorrer la lista sin exponer sus nodos.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private Nodo actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException(
                            "No hay más elementos en la lista."
                    );
                }

                T elemento = actual.dato;
                actual = actual.siguiente;

                return elemento;
            }
        };
    }

    /**
     * Construye una representación textual de la lista.
     */
    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder("[");
        Iterator<T> iterador = iterator();

        while (iterador.hasNext()) {
            resultado.append(iterador.next());

            if (iterador.hasNext()) {
                resultado.append(" -> ");
            }
        }

        resultado.append("]");
        return resultado.toString();
    }
}

