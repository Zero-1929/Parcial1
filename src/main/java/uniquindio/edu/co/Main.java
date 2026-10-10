package uniquindio.edu.co;

import uniquindio.edu.co.PuntoB.AnalisisRecursivo;
import uniquindio.edu.co.PuntoB.BusquedaBinaria;
import uniquindio.edu.co.PuntoB.ComparadoresPaquete;
import uniquindio.edu.co.PuntoB.DivideYVenceras;
import uniquindio.edu.co.PuntoB.ListaEnlazadaSimple;
import uniquindio.edu.co.PuntoB.Paquete;
import uniquindio.edu.co.PuntoB.Repartidor;
import uniquindio.edu.co.PuntoB.SistemaQuindioExpress;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Demostración de integración de QuindíoExpress: modelos, colecciones
 * propias, sistema de gestión y algoritmos (recursividad, divide y
 * vencerás y búsqueda binaria).
 */
public class Main {

    public static void main(String[] args) {

        SistemaQuindioExpress sistema = new SistemaQuindioExpress();

        registrarRepartidores(sistema);
        registrarPaquetes(sistema);

        System.out.println("\n===== 1. REGISTRO Y CONSULTA POR CÓDIGO =====");
        System.out.println("Paquetes registrados: " + sistema.cantidadPaquetes());
        System.out.println("¿Existe PQ701?  -> " + sistema.existePaquete("PQ701"));
        System.out.println("¿Existe PQ000?  -> " + sistema.existePaquete("PQ000"));
        System.out.println("Consulta PQ606  -> " + sistema.consultarPorCodigo("PQ606"));

        boolean duplicado = sistema.registrarPaquete(
                new Paquete("PQ701", "Salento", 9.9, 5, 15));
        System.out.println("Registrar PQ701 otra vez (duplicado) -> " + duplicado);

        System.out.println("\n===== 2. MUNICIPIOS (SIN REPETIR Y ORDENADOS) =====");
        Set<String> municipios = sistema.municipiosConPaquetes();
        System.out.println("Municipios con paquetes: " + municipios);
        System.out.println("Ordenados alfabéticamente: "
                + sistema.municipiosOrdenadosAlfabeticamente());
        System.out.println("Paquetes en Armenia: "
                + sistema.paquetesPorMunicipio("Armenia").size());

        System.out.println("\n===== 3. MODOS DE ATENCIÓN =====");
        System.out.println("Cola FIFO (siguiente por orden de llegada): "
                + sistema.siguientePorOrdenDeLlegada().getCodigo());
        System.out.println("Cola de despacho (siguiente prioritario): "
                + sistema.siguientePrioritario().getCodigo()
                + " (mayor prioridad, menor tiempo y menor código)");

        System.out.println("\n===== 4. ALGORITMOS =====");
        ListaEnlazadaSimple<Paquete> todos = coleccionOrdenadaPorCodigo(sistema);
        System.out.println("Peso total en Armenia: "
                + AnalisisRecursivo.pesoTotalPorMunicipio(todos, "Armenia", 0)
                + " kg");
        System.out.println("Paquetes con prioridad >= 4: "
                + AnalisisRecursivo.contarPorPrioridad(todos, 4, 0));
        System.out.println("Paquete más pesado (divide y vencerás): "
                + DivideYVenceras.encontrarMasPesado(todos));
        System.out.println("Búsqueda binaria PQ606: "
                + BusquedaBinaria.buscarPorCodigo(todos, "PQ606").getDestino());
        System.out.println("Búsqueda binaria PQ000: "
                + BusquedaBinaria.buscarPorCodigo(todos, "PQ000"));

        System.out.println("\n===== 5. ASIGNACIÓN A REPARTIDORES =====");
        System.out.println("Asignar PQ318 (Calarcá) a R1 (Armenia) -> "
                + sistema.asignarPaquete("PQ318", "R1")
                + " (zonas distintas)");
        System.out.println("Asignar PQ105 (Salento) a R2 (Salento) -> "
                + sistema.asignarPaquete("PQ105", "R2"));
        System.out.println("R2 disponible: "
                + sistema.consultarRepartidor("R2").isDisponible());
        System.out.println("Asignar PQ567 (Salento) a R2 (ocupado) -> "
                + sistema.asignarPaquete("PQ567", "R2"));

        System.out.println("\n===== 6. ENTREGA E HISTORIAL =====");
        entregar(sistema, "PQ105");
        System.out.println("R2 disponible tras entrega: "
                + sistema.consultarRepartidor("R2").isDisponible());
        sistema.asignarPaquete("PQ942", "R1");
        entregar(sistema, "PQ942");
        sistema.asignarPaquete("PQ731", "R1");
        entregar(sistema, "PQ731");
        System.out.println("Entregados: " + sistema.cantidadEntregados());
        System.out.println("Historial (orden de entrega): "
                + sistema.historialCodigos());
        System.out.println("Paquetes pendientes: "
                + sistema.paquetesPendientes().size());
        System.out.println("Atendidos por peso (desc): "
                + sistema.paquetesOrdenadosPor(ComparadoresPaquete.POR_PESO)
                        .subList(0, 3).stream()
                        .map(Paquete::getCodigo).toList());
    }

    private static void registrarRepartidores(SistemaQuindioExpress sistema) {
        sistema.registrarRepartidor(new Repartidor("R1", "Ana Gómez", "Armenia", true));
        sistema.registrarRepartidor(new Repartidor("R2", "Luis Pérez", "Salento", true));
        sistema.registrarRepartidor(new Repartidor("R3", "Eva Ramos", "Calarcá", true));
        sistema.registrarRepartidor(new Repartidor("R4", "José Díaz", "Montenegro", true));
        sistema.registrarRepartidor(new Repartidor("R5", "María Ruiz", "Quimbaya", true));
        sistema.registrarRepartidor(new Repartidor("R6", "Pedro Silva", "Filandia", true));
    }

    private static void registrarPaquetes(SistemaQuindioExpress sistema) {
        sistema.registrarPaquete(new Paquete("PQ731", "Armenia", 4.5, 4, 45));
        sistema.registrarPaquete(new Paquete("PQ942", "Armenia", 7.2, 5, 30));
        sistema.registrarPaquete(new Paquete("PQ105", "Salento", 3.0, 3, 25));
        sistema.registrarPaquete(new Paquete("PQ318", "Calarcá", 2.3, 3, 30));
        sistema.registrarPaquete(new Paquete("PQ903", "Montenegro", 8.0, 2, 55));
        sistema.registrarPaquete(new Paquete("PQ385", "Quimbaya", 7.5, 3, 33));
        sistema.registrarPaquete(new Paquete("PQ777", "Filandia", 4.2, 5, 12));
        sistema.registrarPaquete(new Paquete("PQ829", "Armenia", 3.1, 2, 60));
        sistema.registrarPaquete(new Paquete("PQ567", "Salento", 6.5, 4, 40));
        sistema.registrarPaquete(new Paquete("PQ450", "Calarcá", 4.0, 1, 70));
        sistema.registrarPaquete(new Paquete("PQ140", "Montenegro", 2.5, 5, 18));
        sistema.registrarPaquete(new Paquete("PQ498", "Quimbaya", 5.5, 4, 27));
        sistema.registrarPaquete(new Paquete("PQ888", "Filandia", 1.5, 1, 65));
        sistema.registrarPaquete(new Paquete("PQ214", "Armenia", 5.0, 3, 35));
        sistema.registrarPaquete(new Paquete("PQ330", "Salento", 1.8, 2, 50));
        sistema.registrarPaquete(new Paquete("PQ612", "Calarcá", 3.3, 4, 22));
        sistema.registrarPaquete(new Paquete("PQ271", "Montenegro", 6.0, 1, 80));
        sistema.registrarPaquete(new Paquete("PQ650", "Quimbaya", 3.9, 2, 44));
        sistema.registrarPaquete(new Paquete("PQ999", "Filandia", 8.8, 3, 38));
        sistema.registrarPaquete(new Paquete("PQ560", "Armenia", 2.0, 1, 90));
        sistema.registrarPaquete(new Paquete("PQ701", "Salento", 9.9, 5, 15));
        sistema.registrarPaquete(new Paquete("PQ404", "Calarcá", 6.7, 5, 16));
        sistema.registrarPaquete(new Paquete("PQ505", "Montenegro", 4.4, 3, 29));
        sistema.registrarPaquete(new Paquete("PQ606", "Quimbaya", 9.1, 2, 41));
        sistema.registrarPaquete(new Paquete("PQ707", "Filandia", 2.9, 4, 24));
        sistema.registrarPaquete(new Paquete("PQ101", "Armenia", 2.2, 2, 48));
        sistema.registrarPaquete(new Paquete("PQ303", "Salento", 3.7, 1, 72));
        sistema.registrarPaquete(new Paquete("PQ111", "Montenegro", 5.2, 1, 58));
        sistema.registrarPaquete(new Paquete("PQ222", "Montenegro", 6.9, 4, 21));
        sistema.registrarPaquete(new Paquete("PQ202", "Armenia", 5.8, 4, 26));
        sistema.registrarPaquete(new Paquete("PQ909", "Salento", 7.7, 3, 37));
        sistema.registrarPaquete(new Paquete("PQ808", "Armenia", 3.6, 5, 19));
    }

    private static ListaEnlazadaSimple<Paquete> coleccionOrdenadaPorCodigo(
            SistemaQuindioExpress sistema) {

        ListaEnlazadaSimple<Paquete> coleccion = new ListaEnlazadaSimple<>();
        for (Paquete paquete : sistema.paquetesOrdenadosPor(Comparator.naturalOrder())) {
            coleccion.agregar(paquete);
        }
        return coleccion;
    }

    private static void entregar(SistemaQuindioExpress sistema, String codigo) {
        boolean entregado = sistema.entregarPaquete(codigo);
        System.out.println("Entregar " + codigo + " -> " + entregado);
        System.out.println("¿" + codigo + " entregado? "
                + sistema.estaEntregado(codigo));
    }
}
