package proyecto.java;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ProyectoJava {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        int opcion = 0;

        do {
            System.out.println("========================================");
            System.out.println("   SISTEMA DE GESTION DE BIBLIOTECA");
            System.out.println("========================================");
            System.out.println("1. Registrar material (Pendiente modulo 1)");
            System.out.println("2. Registrar usuario (Pendiente modulo 2)");
            System.out.println("3. Realizar prestamo (MySQL)");
            System.out.println("4. Registrar devolucion (MySQL)");
            System.out.println("5. Consultar materiales disponibles (Pendiente modulo 1)");
            System.out.println("6. Abrir interfaz de Prestamos, Devoluciones e Historial");
            System.out.println("7. Salir");
            System.out.println("========================================");
            System.out.print("Seleccione una opcion: ");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                case 2:
                case 5:
                    System.out.println("Modulo en desarrollo por otro integrante.");
                    break;

                case 3:
                    System.out.println("--- REALIZAR PRESTAMO ---");
                    try {
                        System.out.print("Codigo de usuario (ej. JUAPEREZ): ");
                        String codUser = sc.nextLine().trim();
                        System.out.print("Codigo de material (ej. LIB-001): ");
                        String codMat = sc.nextLine().trim();
                        System.out.print("Fecha de prestamo (AAAA-MM-DD): ");
                        LocalDate fPrest = LocalDate.parse(sc.nextLine().trim());
                        System.out.print("Fecha prevista de devolucion (AAAA-MM-DD): ");
                        LocalDate fEnt = LocalDate.parse(sc.nextLine().trim());

                        System.out.println(prestamoDAO.registrarPrestamo(codUser, codMat, fPrest, fEnt));
                    } catch (DateTimeParseException e) {
                        System.out.println("Error: Formato de fecha invalido. Use AAAA-MM-DD.");
                    }
                    break;

                case 4:
                    System.out.println("--- REGISTRAR DEVOLUCION ---");
                    try {
                        System.out.print("Codigo de prestamo (ej. PRES-001): ");
                        String codPres = sc.nextLine().trim();
                        System.out.print("Fecha efectiva de devolucion (AAAA-MM-DD): ");
                        LocalDate fEfectiva = LocalDate.parse(sc.nextLine().trim());

                        System.out.println(prestamoDAO.registrarDevolucion(codPres, fEfectiva));
                    } catch (DateTimeParseException e) {
                        System.out.println("Error: Formato de fecha invalido. Use AAAA-MM-DD.");
                    }
                    break;

                case 6:
                    System.out.println("Abriendo interfaz grafica...");
                    java.awt.EventQueue.invokeLater(() -> {
                        new FrmPrestamosDevoluciones().setVisible(true);
                    });
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