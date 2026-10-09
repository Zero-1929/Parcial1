package uniquindio.edu.co.PuntoB;

/**
 * Representa un repartidor de QuindíoExpress.
 */
public class Repartidor {

    /**
     * Atributos
     */
    private String identificacion;
    private String nombre;
    private String zona;
    private boolean disponible;

    /**
     * Crea un repartidor con todos sus atributos.
     */
    public Repartidor(String identificacion, String nombre,
                      String zona, boolean disponible) {
        setIdentificacion(identificacion);
        setNombre(nombre);
        setZona(zona);
        this.disponible = disponible;
    }

    /**
     * Obtiene la idetificacion
     * @return
     */
    public String getIdentificacion() {
        return identificacion;
    }

    /**
     * Cambia la identificacion
     * @param identificacion
     */
    public void setIdentificacion(String identificacion) {
        if (identificacion == null
                || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La identificación no puede estar vacía."
            );
        }
        this.identificacion = identificacion.trim();
    }

    /**
     * Obtiene el nombre
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre
     * @param nombre
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre no puede estar vacío."
            );
        }
        this.nombre = nombre.trim();
    }

    /**
     * Obtiene la zona
     * @return
     */
    public String getZona() {
        return zona;
    }

    /**
     * Cambia la zona
     * @param zona
     */
    public void setZona(String zona) {
        if (zona == null || zona.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La zona no puede estar vacía."
            );
        }
        this.zona = zona.trim();
    }

    /**
     * Obtiene si es disponible
     * @return
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Cambia si esta disponible
     * @param disponible
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Comprueba si puede recibir el paquete.
     */
    public boolean puedeRecibir(Paquete paquete) {
        return paquete != null
                && disponible
                && zona.equalsIgnoreCase(paquete.getDestino());
    }

    @Override
    public String toString() {
        return "Repartidor{" +
                "identificacion='" + identificacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", zona='" + zona + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
