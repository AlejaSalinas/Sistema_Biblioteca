package proyecto.java;

public class Libro extends Material {
    private String autor;
    private String isbn;
    private int numPaginas;

    public Libro(String id, String titulo, String autor, int numPaginas) {
        super(id, titulo);
        this.setTipo("Libro");
        this.autor = autor;
        this.numPaginas = numPaginas;
        this.isbn = "N/A";
    }

    public Libro(String id, String titulo, int anioPublicacion, String editorial, String isbn, int numPaginas) {
        super(id, titulo, "Libro", anioPublicacion, editorial, 1);
        this.autor = "Sin especificar";
        this.isbn = isbn;
        this.numPaginas = numPaginas;
    }

    public Libro(String id, String titulo, int anioPublicacion, String editorial, int cantidadDisponible, String autor, String isbn, int numPaginas) {
        super(id, titulo, "Libro", anioPublicacion, editorial, cantidadDisponible);
        this.autor = autor;
        this.isbn = isbn;
        this.numPaginas = numPaginas;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Libro [" + getId() + "]: " + getTitulo() + " | Autor: " + autor + " | ISBN: " + isbn);
    }

    @Override
    public String obtenerDetalleEspecifico() {
        return "Autor: " + autor + " | ISBN: " + isbn + " | Págs: " + numPaginas;
    }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }
}
