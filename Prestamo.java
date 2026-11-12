/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.java;

public class Prestamo {

    private Material material;
    private Usuario usuario;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private boolean activo;

    public Prestamo(Material material, Usuario usuario, String fechaPrestamo) {
        this.material = material;
        this.usuario = usuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = "";
        this.activo = true;
        this.material.setDisponible(false);
    }

    public Material getMaterial() {
        return material;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean getActivo() {
        return activo;
    }

    public void devolver(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
        this.activo = false;
        this.material.setDisponible(true);
    }

    public void mostrarInfo() {
        System.out.println("PRESTAMO");
        System.out.println("  Material: " + material.getTitulo());
        System.out.println("  Usuario: " + usuario.getNombre());
        System.out.println("  Fecha prestamo: " + fechaPrestamo);
        System.out.println("  Fecha devolucion: " + fechaDevolucion);
        System.out.println("  Activo: " + activo);
    }
}
