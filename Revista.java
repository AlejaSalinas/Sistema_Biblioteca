/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.java;

public class Revista extends Material {

    private int numeroEdicion;
    private String mes;
    private String periodicidad;

    public Revista(String codigo, String titulo, int año, int numeroEdicion, String mes, String periodicidad) {
        super(codigo, titulo, año);
        this.numeroEdicion = numeroEdicion;
        this.mes = mes;
        this.periodicidad = periodicidad;
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public String getMes() {
        return mes;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("REVISTA");
        System.out.println("  Codigo: " + getCodigo());
        System.out.println("  Titulo: " + getTitulo());
        System.out.println("  Anio: " + getAño());
        System.out.println("  Numero de edicion: " + numeroEdicion);
        System.out.println("  Mes: " + mes);
        System.out.println("  Periodicidad: " + periodicidad);
        System.out.println("  Disponible: " + getDisponible());
    }
}