package proyecto.java;

public class Revista extends Material {
    private int edicion;
    private int volumen;
    private String mesPublicacion;
    private String periodicidad;

    public Revista(String id, String titulo, int edicion, String mesPublicacion) {
        super(id, titulo);
        this.setTipo("Revista");
        this.edicion = edicion;
        this.mesPublicacion = mesPublicacion;
        this.volumen = 1;
        this.periodicidad = "Mensual";
    }

    public Revista(String id, String titulo, int anioPublicacion, int edicion, String mesPublicacion, String periodicidad) {
        super(id, titulo, "Revista", anioPublicacion, periodicidad, 1);
        this.edicion = edicion;
        this.volumen = 1;
        this.mesPublicacion = mesPublicacion;
        this.periodicidad = periodicidad;
    }

    public Revista(String id, String titulo, int anioPublicacion, String editorial, int cantidadDisponible, int edicion, int volumen, String mesPublicacion) {
        super(id, titulo, "Revista", anioPublicacion, editorial, cantidadDisponible);
        this.edicion = edicion;
        this.volumen = volumen;
        this.mesPublicacion = mesPublicacion;
        this.periodicidad = "Mensual";
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Revista [" + getId() + "]: " + getTitulo() + " | Edicion: " + edicion + " | Volumen: " + volumen);
    }

    @Override
    public String obtenerDetalleEspecifico() {
        return "Edición: " + edicion + " | Vol: " + volumen + " | Mes: " + mesPublicacion;
    }

    public int getEdicion() { return edicion; }
    public int getNumeroEdicion() { return edicion; }
    public void setEdicion(int edicion) { this.edicion = edicion; }

    public int getVolumen() { return volumen; }
    public void setVolumen(int volumen) { this.volumen = volumen; }

    public String getMesPublicacion() { return mesPublicacion; }
    public String getMes() { return mesPublicacion; }
    public void setMesPublicacion(String mesPublicacion) { this.mesPublicacion = mesPublicacion; }

    public String getPeriodicidad() { return periodicidad; }
    public void setPeriodicidad(String periodicidad) { this.periodicidad = periodicidad; }
}
