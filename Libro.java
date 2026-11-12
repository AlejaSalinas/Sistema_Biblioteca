/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.java;

public class Libro extends Material {

    private String editorial;
    private String isbn;
    private int numeroPaginas;

    public Libro(String codigo, String titulo, int año, String editorial, String isbn, int numeroPaginas) {
        super(codigo, titulo, año);
        this.editorial = editorial;
        this.isbn = isbn;
        this.numeroPaginas = numeroPaginas;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getNumPaginas() {
        return numeroPaginas;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("LIBRO");
        System.out.println("  Codigo: " + getCodigo());
        System.out.println("  Titulo: " + getTitulo());
        System.out.println("  Anio: " + getAño());
        System.out.println("  Editorial: " + editorial);
        System.out.println("  ISBN: " + isbn);
        System.out.println("  Paginas: " + numeroPaginas);
        System.out.println("  Disponible: " + getDisponible());
    }
}