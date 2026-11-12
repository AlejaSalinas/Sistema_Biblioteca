/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.java;

import java.util.ArrayList;

public class Biblioteca {

    private ArrayList<Material> materiales;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Prestamo> prestamos;

    public Biblioteca() {
        this.materiales = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.prestamos = new ArrayList<>();
    }

    public void registrarMaterial(Material material) {
        materiales.add(material);
        System.out.println("Material registrado: " + material.getTitulo());
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("Usuario registrado: " + usuario.getNombre());
    }

    public void realizarPrestamo(Material material, Usuario usuario, String fecha) {
        if (material.getDisponible()) {
            Prestamo prestamo = new Prestamo(material, usuario, fecha);
            prestamos.add(prestamo);
            System.out.println("Prestamo realizado a " + usuario.getNombre());
        } else {
            System.out.println("El material no esta disponible.");
        }
    }

    public void registrarDevolucion(Material material, String fecha) {
        for (int i = 0; i < prestamos.size(); i++) {
            Prestamo p = prestamos.get(i);
            if (p.getMaterial() == material && p.getActivo()) {
                p.devolver(fecha);
                System.out.println("Devolucion registrada.");
                return;
            }
        }
        System.out.println("No se encontro prestamo activo para ese material.");
    }

    public void consultarDisponibles() {
        System.out.println("=== MATERIALES DISPONIBLES ===");
                boolean hayDisponibles = false;

        for (int i = 0; i < materiales.size(); i++) {
            if (materiales.get(i).getDisponible()) {
                System.out.println("  " + materiales.get(i).getTitulo());
                hayDisponibles = true;
            }
        }
        if (hayDisponibles == false) {
        System.out.println("  No hay materiales disponibles.");
        }
    }  
        
    

    public void consultarPrestamosActivos() {
        System.out.println("=== PRESTAMOS ACTIVOS ===");
        boolean HayActivos = false;
        for (int i = 0; i < prestamos.size(); i++) {
            if (prestamos.get(i).getActivo()) {
                prestamos.get(i).mostrarInfo();
                HayActivos = true;
            }
        }
        if (HayActivos == false){
            System.out.println(" No hay prestamos activos. ");
        }
        
    }

    public ArrayList<Material> getMateriales() {
        return materiales;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}
