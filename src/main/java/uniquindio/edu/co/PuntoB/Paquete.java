package uniquindio.edu.co.PuntoB;

/**
 * Representa un paquete gestionado por QuindíoExpress.
 * El orden natural se establece por código ascendente.
 */
public class Paquete implements Comparable<Paquete> {

    /**
     * Atributos
     */
    private String codigo;
    private String destino;
    private double peso;
    private int prioridad;
    private int tiempoEstimado;

    /**
     * Prioridad predeterminada para paquetes que no la especifiquen.
     */
    public static final int PRIORIDAD_PREDETERMINADA = 3;

    /**
     * Crea un paquete con prioridad predeterminada.
     */
    public Paquete(String codigo, String destino,
                   double peso, int tiempoEstimado) {
        this(codigo, destino, peso,
                PRIORIDAD_PREDETERMINADA, tiempoEstimado);
    }

    /**
     * Crea un paquete con todos sus atributos.
     */
    public Paquete(String codigo, String destino, double peso,
                   int prioridad, int tiempoEstimado) {
        setCodigo(codigo);
        setDestino(destino);
        setPeso(peso);
        setPrioridad(prioridad);
        setTiempoEstimado(tiempoEstimado);
    }

    /**
     * Obtiene el codigo
     * @return
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Cambia el codigo
     * @param codigo
     */
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El código no puede ser nulo ni vacío."
            );
        }
        this.codigo = codigo.trim();
    }

    /**
     * Obtiene el destino
     * @return
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Cambia el destino
     * @param destino
     */
    public void setDestino(String destino) {
        if (destino == null || destino.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El destino no puede ser nulo ni vacío."
            );
        }
        this.destino = destino.trim();
    }

    /**
     * Obtiene el peso
     * @return
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Cambia el peso
     * @param peso
     */
    public void setPeso(double peso) {
        if (!Double.isFinite(peso) || peso <= 0) {
            throw new IllegalArgumentException(
                    "El peso debe ser positivo y finito."
            );
        }
        this.peso = peso;
    }

    /**
     * Obtiene la prioridad
     * @return
     */
    public int getPrioridad() {
        return prioridad;
    }

    /**
     * Cambia la prioridad
     * @param prioridad
     */
    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException(
                    "La prioridad debe estar entre 1 y 5."
            );
        }
        this.prioridad = prioridad;
    }

    /**
     * Obtiene el tiempo estimado
     * @return
     */
    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    /**
     * Cambia el tiempo estimado
     * @param tiempoEstimado
     */
    public void setTiempoEstimado(int tiempoEstimado) {
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException(
                    "El tiempo estimado debe ser positivo."
            );
        }
        this.tiempoEstimado = tiempoEstimado;
    }

    /**
     * Compara los paquetes por código ascendente.
     */
    @Override
    public int compareTo(Paquete otro) {
        if (otro == null) {
            throw new NullPointerException(
                    "No se puede comparar con un paquete nulo."
            );
        }
        return this.codigo.compareTo(otro.codigo);
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "codigo='" + codigo + '\'' +
                ", destino='" + destino + '\'' +
                ", peso=" + peso + " kg" +
                ", prioridad=" + prioridad +
                ", tiempoEstimado=" + tiempoEstimado + " min" +
                '}';
    }
}

