package proyecto.java;

import java.util.Scanner;

public class ProyectoJava {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();
        int opcion = 0;

        do {
            System.out.println("========================================");
            System.out.println("   SISTEMA DE GESTION DE BIBLIOTECA");
            System.out.println("========================================");
            System.out.println("1. Registrar material");
            System.out.println("2. Registrar usuario");
            System.out.println("3. Realizar prestamo");
            System.out.println("4. Registrar devolucion");
            System.out.println("5. Consultar materiales disponibles");
            System.out.println("6. Consultar prestamos activos");
            System.out.println("7. Salir");
            System.out.println("========================================");
            System.out.print("Seleccione una opcion: ");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("--- REGISTRAR MATERIAL ---");
                    System.out.print("Codigo: ");
                    String codigo = sc.nextLine();
                    System.out.print("Titulo: ");
                    String titulo = sc.nextLine();
                    System.out.print("Año: ");
                    int año = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Tipo (1=Libro, 2=Revista): ");
                    int tipo = sc.nextInt();
                    sc.nextLine();
                    if (tipo == 1) {
                        System.out.print("Editorial: ");
                        String editorial = sc.nextLine();
                        System.out.print("ISBN: ");
                        String isbn = sc.nextLine();
                        System.out.print("Numero de paginas: ");
                        int paginas = sc.nextInt();
                        sc.nextLine();
                        Libro libro = new Libro(codigo, titulo, año, editorial, isbn, paginas);
                        biblioteca.registrarMaterial(libro);
                    } else if (tipo == 2) {
                        System.out.print("Numero de edicion: ");
                        int edicion = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Mes: ");
                        String mes = sc.nextLine();
                        System.out.print("Periodicidad: ");
                        String periodicidad = sc.nextLine();
                        Revista revista = new Revista(codigo, titulo, año, edicion, mes, periodicidad);
                        biblioteca.registrarMaterial(revista);
                    } else {
                        System.out.println("Tipo no valido.");
                    }
                    break;

                case 2:
                    System.out.println("--- REGISTRAR USUARIO ---");
                    System.out.print("Carnet: ");
                    String carnet = sc.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Carrera: ");
                    String carrera = sc.nextLine();
                    Usuario usuario = new Usuario(carnet, nombre, carrera);
                    biblioteca.registrarUsuario(usuario);
                    break;

                case 3:
                    System.out.println("--- REALIZAR PRESTAMO ---");
                    System.out.print("Titulo del material: ");
                    String tituloPrestamo = sc.nextLine();
                    Material materialEncontrado = null;
                    for (int i = 0; i < biblioteca.getMateriales().size(); i++) {
                        if (biblioteca.getMateriales().get(i).getTitulo().equals(tituloPrestamo)) {
                            materialEncontrado = biblioteca.getMateriales().get(i);
                        }
                    }
                    if (materialEncontrado != null) {
                        System.out.print("Carnet del usuario: ");
                        String carnetPrestamo = sc.nextLine();
                        Usuario usuarioEncontrado = null;
                        for (int i = 0; i < biblioteca.getUsuarios().size(); i++) {
                            if (biblioteca.getUsuarios().get(i).getCarnet().equals(carnetPrestamo)) {
                                usuarioEncontrado = biblioteca.getUsuarios().get(i);
                            }
                        }
                        if (usuarioEncontrado != null) {
                            System.out.print("Fecha de prestamo: ");
                            String fecha = sc.nextLine();
                            biblioteca.realizarPrestamo(materialEncontrado, usuarioEncontrado, fecha);
                        } else {
                            System.out.println("Usuario no encontrado.");
                        }
                    } else {
                        System.out.println("Material no encontrado.");
                    }
                    break;

                case 4:
                    System.out.println("--- REGISTRAR DEVOLUCION ---");
                    System.out.print("Titulo del material: ");
                    String tituloDevolucion = sc.nextLine();
                    Material materialDevolucion = null;
                    for (int i = 0; i < biblioteca.getMateriales().size(); i++) {
                        if (biblioteca.getMateriales().get(i).getTitulo().equals(tituloDevolucion)) {
                            materialDevolucion = biblioteca.getMateriales().get(i);
                        }
                    }
                    if (materialDevolucion != null) {
                        System.out.print("Fecha de devolucion: ");
                        String fechaDevolucion = sc.nextLine();
                        biblioteca.registrarDevolucion(materialDevolucion, fechaDevolucion);
                    } else {
                        System.out.println("Material no encontrado.");
                    }
                    break;

                case 5:
                    biblioteca.consultarDisponibles();
                    break;

                case 6:
                    biblioteca.consultarPrestamosActivos();
                    break;

                case 7:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
                    break;
            }

        } while (opcion != 7);

        sc.close();
    }
}