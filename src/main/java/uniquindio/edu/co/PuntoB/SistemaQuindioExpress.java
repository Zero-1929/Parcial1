package uniquindio.edu.co.PuntoB;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestiona la jornada de trabajo de QuindíoExpress.
 *
 * Primera parte: registro de paquetes sin códigos duplicados,
 * conservando el orden de llegada, y consulta de paquetes por código.
 *
 * Estructuras utilizadas:
 * - HashMap: índice para consultar por código en O(1) promedio y
 *   detectar duplicados sin recorrer toda la colección.
 * - ArrayDeque: conserva el orden exacto en que los paquetes fueron
 *   registrados (FIFO).
 */
public class SistemaQuindioExpress {

    /**
     * Índice de paquetes por código (búsqueda eficiente, sin duplicados).
     */
    private final Map<String, Paquete> indicePorCodigo;

    /**
     * Paquetes en el orden exacto en que fueron registrados.
     */
    private final Deque<Paquete> pendientesOrdenLlegada;

    /**
     * Crea un sistema de gestión vacío.
     */
    public SistemaQuindioExpress() {
        this.indicePorCodigo = new HashMap<>();
        this.pendientesOrdenLlegada = new ArrayDeque<>();
    }

    /**
     * Registra un paquete en la jornada.
     *
     * Verifica que su código no esté previamente registrado y conserva
     * el orden de llegada.
     *
     * @param paquete paquete a registrar
     * @return true si se registró; false si el código ya existía
     * @throws IllegalArgumentException si el paquete es nulo
     */
    public boolean registrarPaquete(Paquete paquete) {
        if (paquete == null) {
            throw new IllegalArgumentException(
                    "El paquete no puede ser nulo.");
        }

        String codigo = paquete.getCodigo();

        if (indicePorCodigo.containsKey(codigo)) {
            return false;
        }

        indicePorCodigo.put(codigo, paquete);
        pendientesOrdenLlegada.addLast(paquete);

        return true;
    }

    /**
     * Consulta un paquete por su código de forma eficiente.
     *
     * @param codigo código del paquete buscado
     * @return el paquete si existe; null en caso contrario
     * @throws IllegalArgumentException si el código es nulo o vacío
     */
    public Paquete consultarPorCodigo(String codigo) {
        validarCodigo(codigo);
        return indicePorCodigo.get(codigo.trim());
    }

    /**
     * Determina si existe un paquete con el código indicado.
     *
     * @param codigo código a verificar
     * @return true si el paquete existe
     * @throws IllegalArgumentException si el código es nulo o vacío
     */
    public boolean existePaquete(String codigo) {
        validarCodigo(codigo);
        return indicePorCodigo.containsKey(codigo.trim());
    }

    /**
     * Devuelve la cantidad de paquetes registrados.
     *
     * @return número de paquetes
     */
    public int cantidadPaquetes() {
        return indicePorCodigo.size();
    }

    /**
     * Indica si aún no se ha registrado ningún paquete.
     *
     * @return true si no hay paquetes registrados
     */
    public boolean estaVacio() {
        return indicePorCodigo.isEmpty();
    }

    /**
     * Comprueba que el código recibido sea válido.
     */
    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El código no puede ser nulo ni vacío.");
        }
    }
}
