package uniquindio.edu.co.PuntoB;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Gestiona la jornada de trabajo de QuindíoExpress.
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
     * Paquetes pendientes ordenados por el criterio de despacho.
     */
    private final PriorityQueue<Paquete> colaDespacho;

    /**
     * Repartidores por identificación.
     */
    private final Map<String, Repartidor> repartidores;

    /**
     * Repartidores agrupados por zona.
     */
    private final Map<String, List<Repartidor>> repartidoresPorZona;

    /**
     * Asignaciones vigentes: código de paquete -> identificación de repartidor.
     */
    private final Map<String, String> asignaciones;

    /**
     * Códigos de paquetes que ya fueron entregados.
     */
    private final Set<String> codigosEntregados;

    /**
     * Historial de entregas en el orden real en que ocurrieron.
     */
    private final ListaEnlazadaSimple<Paquete> historial;

    /**
     * Crea un sistema de gestión vacío.
     */
    public SistemaQuindioExpress() {
        this.indicePorCodigo = new HashMap<>();
        this.pendientesOrdenLlegada = new ArrayDeque<>();
        this.indicePorMunicipio = new HashMap<>();
        this.colaDespacho =
                new PriorityQueue<>(ComparadoresPaquete.POR_DESPACHO);
        this.repartidores = new HashMap<>();
        this.repartidoresPorZona = new HashMap<>();
        this.asignaciones = new HashMap<>();
        this.codigosEntregados = new HashSet<>();
        this.historial = new ListaEnlazadaSimple<>();
    }

    // ------------------------------------------------------------------
    // Registro de paquetes y consultas
    // ------------------------------------------------------------------

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
        colaDespacho.add(paquete);
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

    // ------------------------------------------------------------------
    // Municipios
    // ------------------------------------------------------------------

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

    // ------------------------------------------------------------------
    // Modos de atención
    // ------------------------------------------------------------------

    /**
     * Indica si no quedan paquetes pendientes por atender.
     *
     * @return true si no hay paquetes pendientes
     */
    public boolean sinPendientes() {
        return pendientesOrdenLlegada.isEmpty();
    }

    /**
     * Devuelve el siguiente paquete según el orden de llegada, sin
     * retirarlo de los pendientes.
     *
     * @return el paquete más antiguo registrado; null si no hay
     */
    public Paquete siguientePorOrdenDeLlegada() {
        return pendientesOrdenLlegada.peekFirst();
    }

    /**
     * Atiende el siguiente paquete según el orden de llegada (FIFO),
     * retirándolo de los pendientes.
     *
     * @return el paquete atendido; null si no hay pendientes
     */
    public Paquete atenderPorOrdenDeLlegada() {
        Paquete paquete = pendientesOrdenLlegada.pollFirst();

        if (paquete != null) {
            colaDespacho.remove(paquete);
        }

        return paquete;
    }

    /**
     * Devuelve el siguiente paquete a despachar según las reglas de
     * prioridad, sin retirarlo de los pendientes.
     *
     * @return el paquete de mayor prioridad; null si no hay pendientes
     */
    public Paquete siguientePrioritario() {
        return colaDespacho.peek();
    }

    /**
     * Despacha el siguiente paquete según las reglas de prioridad,
     * retirándolo de los pendientes.
     *
     * @return el paquete despachado; null si no hay pendientes
     */
    public Paquete despacharPrioritario() {
        Paquete paquete = colaDespacho.poll();

        if (paquete != null) {
            pendientesOrdenLlegada.remove(paquete);
        }

        return paquete;
    }

    /**
     * Devuelve los paquetes pendientes en el orden de llegada.
     *
     * @return lista de paquetes pendientes
     */
    public List<Paquete> paquetesPendientes() {
        return new ArrayList<>(pendientesOrdenLlegada);
    }

    /**
     * Devuelve todos los paquetes registrados ordenados según el
     * criterio indicado, sin alterar el orden natural del paquete.
     *
     * @param criterio comparador a aplicar
     * @return lista ordenada con los paquetes registrados
     * @throws IllegalArgumentException si el criterio es nulo
     */
    public List<Paquete> paquetesOrdenadosPor(
            Comparator<Paquete> criterio) {

        if (criterio == null) {
            throw new IllegalArgumentException(
                    "El criterio de ordenamiento no puede ser nulo.");
        }

        List<Paquete> copia =
                new ArrayList<>(indicePorCodigo.values());
        copia.sort(criterio);

        return copia;
    }

    // ------------------------------------------------------------------
    // Repartidores
    // ------------------------------------------------------------------

    /**
     * Registra un repartidor.
     *
     * Verifica que su identificación no esté previamente registrada.
     *
     * @param repartidor repartidor a registrar
     * @return true si se registró; false si la identificación ya existía
     * @throws IllegalArgumentException si el repartidor es nulo
     */
    public boolean registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException(
                    "El repartidor no puede ser nulo.");
        }

        String id = repartidor.getIdentificacion();

        if (repartidores.containsKey(id)) {
            return false;
        }

        repartidores.put(id, repartidor);
        repartidoresPorZona
                .computeIfAbsent(repartidor.getZona(),
                        zona -> new ArrayList<>())
                .add(repartidor);

        return true;
    }

    /**
     * Consulta un repartidor por su identificación.
     *
     * @param identificacion identificación del repartidor
     * @return el repartidor si existe; null en caso contrario
     * @throws IllegalArgumentException si la identificación es nula o vacía
     */
    public Repartidor consultarRepartidor(String identificacion) {
        validarIdentificacion(identificacion);
        return repartidores.get(identificacion.trim());
    }

    /**
     * Devuelve la cantidad de repartidores registrados.
     *
     * @return número de repartidores
     */
    public int cantidadRepartidores() {
        return repartidores.size();
    }

    // ------------------------------------------------------------------
    // Asignación y entrega
    // ------------------------------------------------------------------

    /**
     * Asigna un paquete a un repartidor.
     *
     * Reglas: el paquete y el repartidor deben existir; el paquete no
     * debe estar entregado; el repartidor debe estar disponible y su
     * zona debe coincidir con el destino del paquete.
     *
     * @param codigoPaquete código del paquete a asignar
     * @param identificacion identificación del repartidor
     * @return true si la asignación se realizó; false en caso contrario
     * @throws IllegalArgumentException si el código o la identificación
     *                                  son nulos o vacíos
     */
    public boolean asignarPaquete(String codigoPaquete,
                                  String identificacion) {
        validarCodigo(codigoPaquete);
        validarIdentificacion(identificacion);

        Paquete paquete = indicePorCodigo.get(codigoPaquete.trim());
        Repartidor repartidor =
                repartidores.get(identificacion.trim());

        if (paquete == null || repartidor == null) {
            return false;
        }

        if (codigosEntregados.contains(paquete.getCodigo())) {
            return false;
        }

        if (!repartidor.puedeRecibir(paquete)) {
            return false;
        }

        asignaciones.put(paquete.getCodigo(),
                repartidor.getIdentificacion());
        repartidor.setDisponible(false);

        return true;
    }

    /**
     * Registra la entrega satisfactoria de un paquete.
     *
     * El paquete deja de estar pendiente, se agrega al historial y el
     * repartidor asignado vuelve a quedar disponible.
     *
     * @param codigoPaquete código del paquete entregado
     * @return true si se registró la entrega; false si el paquete no
     *         existe o ya había sido entregado
     * @throws IllegalArgumentException si el código es nulo o vacío
     */
    public boolean entregarPaquete(String codigoPaquete) {
        validarCodigo(codigoPaquete);

        String codigo = codigoPaquete.trim();
        Paquete paquete = indicePorCodigo.get(codigo);

        if (paquete == null || codigosEntregados.contains(codigo)) {
            return false;
        }

        pendientesOrdenLlegada.remove(paquete);
        colaDespacho.remove(paquete);
        historial.agregar(paquete);
        codigosEntregados.add(codigo);

        liberarRepartidor(codigo);

        return true;
    }

    /**
     * Determina si un paquete ya fue entregado.
     *
     * @param codigoPaquete código del paquete
     * @return true si el paquete ya fue entregado
     * @throws IllegalArgumentException si el código es nulo o vacío
     */
    public boolean estaEntregado(String codigoPaquete) {
        validarCodigo(codigoPaquete);
        return codigosEntregados.contains(codigoPaquete.trim());
    }

    // ------------------------------------------------------------------
    // Historial
    // ------------------------------------------------------------------

    /**
     * Devuelve el historial de entregas en el orden real en que
     * ocurrieron. La colección es recorrible con su propio iterador.
     *
     * @return historial de paquetes entregados
     */
    public ListaEnlazadaSimple<Paquete> getHistorial() {
        return historial;
    }

    /**
     * Devuelve la cantidad de paquetes entregados.
     *
     * @return número de entregas registradas
     */
    public int cantidadEntregados() {
        return historial.tamano();
    }

    /**
     * Devuelve los códigos del historial en el orden de entrega.
     *
     * @return lista de códigos entregados
     */
    public List<String> historialCodigos() {
        List<String> codigos = new ArrayList<>();

        for (Paquete paquete : historial) {
            codigos.add(paquete.getCodigo());
        }

        return codigos;
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------

    /**
     * Libera al repartidor asignado a un paquete, si lo hubiera.
     */
    private void liberarRepartidor(String codigo) {
        String id = asignaciones.remove(codigo);

        if (id == null) {
            return;
        }

        Repartidor repartidor = repartidores.get(id);

        if (repartidor != null) {
            repartidor.setDisponible(true);
        }
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

    /**
     * Comprueba que la identificación recibida sea válida.
     */
    private void validarIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La identificación no puede ser nula ni vacía.");
        }
    }
}
