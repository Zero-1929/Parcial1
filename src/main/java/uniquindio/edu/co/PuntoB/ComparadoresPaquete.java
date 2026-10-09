package uniquindio.edu.co.PuntoB;
import java.util.Comparator;

/**
 * Comparadores reutilizables para ordenar paquetes.
 *
 * El orden natural de Paquete continúa siendo el código
 * ascendente. Estos comparadores sirven para otros criterios.
 */
public final class ComparadoresPaquete {


    private ComparadoresPaquete() {
        // Evita instanciar esta clase de utilidades.
    }

    /**
     * Prioridad de mayor a menor.
     */
    public static final Comparator<Paquete> POR_PRIORIDAD =
            Comparator.comparingInt(Paquete::getPrioridad)
                    .reversed();

    /**
     * Peso de mayor a menor.
     */
    public static final Comparator<Paquete> POR_PESO =
            Comparator.comparingDouble(Paquete::getPeso)
                    .reversed();

    /**
     * Tiempo estimado de menor a mayor.
     */
    public static final Comparator<Paquete> POR_TIEMPO =
            Comparator.comparingInt(Paquete::getTiempoEstimado);

    /**
     * Orden de despacho:
     * 1. Prioridad mayor primero.
     * 2. Menor tiempo estimado.
     * 3. Código alfabéticamente ascendente.
     */
    public static final Comparator<Paquete> POR_DESPACHO =
            Comparator.comparingInt(Paquete::getPrioridad)
                    .reversed()
                    .thenComparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);
}

