package uniquindio.edu.co.PuntoB;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Gestiona la jornada de trabajo de QuindíoExpress.
 *
 * Partes implementadas:
 * - Registro de paquetes sin códigos duplicados, conservando el orden
 *   de llegada, y consulta de paquetes por código.
 * - Municipios con al menos un paquete (sin repetidos y ordenados
 *   alfabéticamente) y relación municipio -> paquetes.
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
     * Relación municipio de destino -> paquetes destinados a ese municipio.
     */
    private final Map<String, List<Paquete>> indicePorMunicipio;

    /**
     * Crea un sistema de gestión vacío.
     */
    public SistemaQuindioExpress() {
        this.indicePorCodigo = new HashMap<>();
        this.pendientesOrdenLlegada = new ArrayDeque<>();
        this.indicePorMunicipio = new HashMap<>();
    }

    /**
     * Registra un paquete en la jornada.
     *
     * Verifica que su código no esté previamente registrado, conserva
     * el orden de llegada y lo asocia a su municipio de destino.
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
        indicePorMunicipio
                .computeIfAbsent(paquete.getDestino(),
                        municipio -> new ArrayList<>())
                .add(paquete);

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
     * Devuelve los municipios que tienen al menos un paquete registrado,
     * sin elementos repetidos (sin orden definido).
     *
     * @return conjunto de municipios con entregas
     */
    public Set<String> municipiosConPaquetes() {
        return new HashSet<>(indicePorMunicipio.keySet());
    }

    /**
     * Devuelve los municipios con al menos un paquete, ordenados
     * alfabéticamente y sin elementos repetidos.
     *
     * @return lista de municipios ordenada alfabéticamente
     */
    public List<String> municipiosOrdenadosAlfabeticamente() {
        return new ArrayList<>(
                new TreeSet<>(indicePorMunicipio.keySet()));
    }

    /**
     * Devuelve los paquetes destinados a un municipio específico.
     *
     * @param municipio municipio de destino consultado
     * @return lista con los paquetes de ese municipio; vacía si no hay
     * @throws IllegalArgumentException si el municipio es nulo o vacío
     */
    public List<Paquete> paquetesPorMunicipio(String municipio) {
        validarMunicipio(municipio);

        List<Paquete> paquetes =
                indicePorMunicipio.get(municipio.trim());

        if (paquetes == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(paquetes);
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

    /**
     * Comprueba que el municipio recibido sea válido.
     */
    private void validarMunicipio(String municipio) {
        if (municipio == null || municipio.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El municipio no puede ser nulo ni vacío.");
        }
    }
}
