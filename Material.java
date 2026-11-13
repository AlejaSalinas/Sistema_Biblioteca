package proyecto.java;

public abstract class Material {
    private String id;
    private String titulo;
    private String tipo;
    private int anioPublicacion;
    private String editorial;
    private int cantidadDisponible;
    private boolean disponible;

    public Material(String id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.disponible = true;
        this.cantidadDisponible = 1;
    }

    public Material(String id, String titulo, int anioPublicacion) {
        this(id, titulo);
        this.anioPublicacion = anioPublicacion;
    }

    public Material(String id, String titulo, String tipo, int anioPublicacion, String editorial, int cantidadDisponible) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.anioPublicacion = anioPublicacion;
        this.editorial = editorial;
        this.cantidadDisponible = cantidadDisponible;
        this.disponible = cantidadDisponible > 0;
    }

    public abstract void mostrarDetalles();
    public abstract String obtenerDetalleEspecifico();

    public void mostrarInfo() {
        mostrarDetalles();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCodigo() { return id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public boolean isDisponible() { return disponible; }
    public boolean getDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getAnioPublicacion() { return anioPublicacion; }
    public int getAño() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
        this.disponible = cantidadDisponible > 0;
    }
}
