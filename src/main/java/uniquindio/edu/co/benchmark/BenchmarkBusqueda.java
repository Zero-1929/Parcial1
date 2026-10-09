package uniquindio.edu.co.benchmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Benchmark de búsqueda por código: ArrayList<Paquete> vs HashMap<String, Paquete>.
 *
 * A.2 - Tiempo frente a memoria (Caso 1): la operación más frecuente es buscar un
 * paquete por su código y la empresa espera crecer hasta ~500.000 paquetes.
 *
 * Mide el tiempo por búsqueda para distintos tamaños de n y contrasta la teoría:
 *   ArrayList -> O(n)   (recorrido lineal)
 *   HashMap   -> O(1)   (acceso por hash, caso promedio)
 *
 * Los tiempos absolutos dependen del hardware/JVM; lo relevante es la tendencia
 * (lineal vs constante).
 */
public class BenchmarkBusqueda {

    /** Representación mínima de un paquete para el benchmark. */
    static final class Paquete {
        final String codigo;
        final String destino;
        final double peso;
        final int prioridad;
        final int tiempoEstimado;

        Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
            this.codigo = codigo;
            this.destino = destino;
            this.peso = peso;
            this.prioridad = prioridad;
            this.tiempoEstimado = tiempoEstimado;
        }
    }

    private static final int[] TAMANOS = {30, 1_000, 100_000, 500_000};

    /** Número de consultas por tamaño, ajustado para mantener un tiempo de ejecución razonable. */
    private static int consultasPara(int n) {
        if (n <= 1_000) {
            return 200_000;
        }
        if (n <= 100_000) {
            return 2_000;
        }
        return 1_000;
    }

    public static void main(String[] args) {
        System.out.printf("%-10s %-26s %-26s%n", "n", "ArrayList ns/busqueda", "HashMap ns/busqueda");

        for (int n : TAMANOS) {
            ArrayList<Paquete> lista = new ArrayList<>(n);
            HashMap<String, Paquete> mapa = new HashMap<>(n * 2);

            for (int i = 0; i < n; i++) {
                String codigo = String.format("PQ%07d", i * 3 + 1);
                Paquete p = new Paquete(codigo, "Armenia", 1.0 + (i % 50), (i % 5) + 1, (i % 120) + 1);
                lista.add(p);
                mapa.put(codigo, p);
            }

            int q = consultasPara(n);
            String[] consultas = new String[q];
            Random random = new Random(42);
            for (int i = 0; i < q; i++) {
                int indice = random.nextInt(n + (n / 10)); // ~9% de códigos inexistentes
                consultas[i] = String.format("PQ%07d", indice * 3 + 1);
            }

            // Calentamiento para que el JIT compile los caminos críticos.
            for (int w = 0; w < 3; w++) {
                buscarEnLista(lista, consultas);
                buscarEnMapa(mapa, consultas);
            }

            long tiempoLista = 0;
            long tiempoMapa = 0;
            int repeticiones = 3;
            for (int rep = 0; rep < repeticiones; rep++) {
                long inicio = System.nanoTime();
                buscarEnLista(lista, consultas);
                tiempoLista += System.nanoTime() - inicio;

                inicio = System.nanoTime();
                buscarEnMapa(mapa, consultas);
                tiempoMapa += System.nanoTime() - inicio;
            }

            double nsLista = (double) tiempoLista / (q * repeticiones);
            double nsMapa = (double) tiempoMapa / (q * repeticiones);

            System.out.printf("%-10d %-26s %-26s%n",
                    n,
                    String.format("%,.2f", nsLista),
                    String.format("%,.3f", nsMapa));
        }
    }

    /** Búsqueda lineal O(n) sobre la lista, comparando por código. */
    private static long buscarEnLista(ArrayList<Paquete> lista, String[] consultas) {
        long encontrados = 0;
        for (String codigo : consultas) {
            for (Paquete p : lista) {
                if (p.codigo.equals(codigo)) {
                    encontrados++;
                    break;
                }
            }
        }
        return encontrados;
    }

    /** Búsqueda por hash O(1) promedio sobre el mapa. */
    private static long buscarEnMapa(HashMap<String, Paquete> mapa, String[] consultas) {
        long encontrados = 0;
        for (String codigo : consultas) {
            if (mapa.get(codigo) != null) {
                encontrados++;
            }
        }
        return encontrados;
    }
}
