/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.java;

public class Usuario {

    private String carnet;
    private String nombre;
    private String carrera;

    public Usuario(String carnet, String nombre, String carrera) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.carrera = carrera;
    }

    public String getCarnet() {
        return carnet;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void mostrarInfo() {
        System.out.println("USUARIO");
        System.out.println("  Carnet: " + carnet);
        System.out.println("  Nombre: " + nombre);
        System.out.println("  Carrera: " + carrera);
    }
}
