package proyecto.java;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    // Genera un código único para el préstamo (ej. PRES-001)
    private String generarCodigoPrestamo(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) + 1 AS siguiente FROM prestamos";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("PRES-%03d", rs.getInt("siguiente"));
            }
        }
        return "PRES-001";
    }

    // Registrar un préstamo nuevo con transacción y control de stock
    public String registrarPrestamo(String codigoUsuario, String codigoMaterial, LocalDate fechaPrestamo, LocalDate fechaEntregaPrevista) {
        if (fechaEntregaPrevista.isBefore(fechaPrestamo)) {
            return "Error: La fecha prevista de devolucion no puede ser anterior a la fecha de prestamo.";
        }

        String sqlUsuario = "SELECT id_usuario, estado FROM usuarios WHERE cod_usuario = ?";
        String sqlMaterial = "SELECT id_material, titulo, stock, estado FROM materiales WHERE cod_material = ? FOR UPDATE";
        String sqlInsertPrestamo = "INSERT INTO prestamos (cod_prestamo, id_usuario, id_material, fecha_prestamo, fecha_entrega, estado) VALUES (?, ?, ?, ?, ?, 'activo')";
        String sqlUpdateStock = "UPDATE materiales SET stock = stock - 1, estado = IF(stock - 1 <= 0, 'agotado', 'disponible') WHERE id_material = ?";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            int idUsuario;
            try (PreparedStatement psUser = conn.prepareStatement(sqlUsuario)) {
                psUser.setString(1, codigoUsuario);
                try (ResultSet rs = psUser.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return "Error: El usuario '" + codigoUsuario + "' no existe.";
                    }
                    if (!"activo".equalsIgnoreCase(rs.getString("estado"))) {
                        conn.rollback();
                        return "Error: El usuario '" + codigoUsuario + "' se encuentra inactivo.";
                    }
                    idUsuario = rs.getInt("id_usuario");
                }
            }

            int idMaterial;
            int stockActual;
            String tituloMaterial;
            String estadoMaterial;
            try (PreparedStatement psMat = conn.prepareStatement(sqlMaterial)) {
                psMat.setString(1, codigoMaterial);
                try (ResultSet rs = psMat.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return "Error: El material '" + codigoMaterial + "' no existe.";
                    }
                    idMaterial = rs.getInt("id_material");
                    tituloMaterial = rs.getString("titulo");
                    stockActual = rs.getInt("stock");
                    estadoMaterial = rs.getString("estado");
                }
            }

            if (stockActual <= 0 || "inoperativo".equalsIgnoreCase(estadoMaterial)) {
                conn.rollback();
                return "Error: No hay ejemplares disponibles de '" + tituloMaterial + "'.";
            }

            String codigoPrestamo = generarCodigoPrestamo(conn);

            try (PreparedStatement psIns = conn.prepareStatement(sqlInsertPrestamo)) {
                psIns.setString(1, codigoPrestamo);
                psIns.setInt(2, idUsuario);
                psIns.setInt(3, idMaterial);
                psIns.setDate(4, Date.valueOf(fechaPrestamo));
                psIns.setDate(5, Date.valueOf(fechaEntregaPrevista));
                psIns.executeUpdate();
            }

            try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdateStock)) {
                psUpd.setInt(1, idMaterial);
                psUpd.executeUpdate();
            }

            conn.commit();
            return "Prestamo registrado con exito. Codigo: " + codigoPrestamo + " | Material: " + tituloMaterial;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return "Error de base de datos al registrar prestamo: " + e.getMessage();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Registrar devolución, calcular mora y restaurar stock
    public String registrarDevolucion(String codigoPrestamo, LocalDate fechaEntregadoEfectiva) {
        String sqlBuscar = "SELECT id_prestamo, id_material, fecha_prestamo, fecha_entrega, estado FROM prestamos WHERE cod_prestamo = ? FOR UPDATE";
        String sqlCerrar = "UPDATE prestamos SET fecha_entregado = ?, estado = 'devuelto' WHERE id_prestamo = ?";
        String sqlRestaurarStock = "UPDATE materiales SET stock = stock + 1, estado = 'disponible' WHERE id_material = ?";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            int idPrestamo;
            int idMaterial;
            LocalDate fechaPrestamo;
            LocalDate fechaEntregaPrevista;
            String estado;

            try (PreparedStatement psBus = conn.prepareStatement(sqlBuscar)) {
                psBus.setString(1, codigoPrestamo);
                try (ResultSet rs = psBus.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return "Error: No existe ningun prestamo con el codigo '" + codigoPrestamo + "'.";
                    }
                    idPrestamo = rs.getInt("id_prestamo");
                    idMaterial = rs.getInt("id_material");
                    fechaPrestamo = rs.getDate("fecha_prestamo").toLocalDate();
                    fechaEntregaPrevista = rs.getDate("fecha_entrega").toLocalDate();
                    estado = rs.getString("estado");
                }
            }

            if ("devuelto".equalsIgnoreCase(estado)) {
                conn.rollback();
                return "Aviso: El prestamo '" + codigoPrestamo + "' ya fue devuelto anteriormente.";
            }

            if (fechaEntregadoEfectiva.isBefore(fechaPrestamo)) {
                conn.rollback();
                return "Error: La fecha de devolucion no puede ser anterior a la fecha en que se presto (" + fechaPrestamo + ").";
            }

            try (PreparedStatement psCerrar = conn.prepareStatement(sqlCerrar)) {
                psCerrar.setDate(1, Date.valueOf(fechaEntregadoEfectiva));
                psCerrar.setInt(2, idPrestamo);
                psCerrar.executeUpdate();
            }

            try (PreparedStatement psStock = conn.prepareStatement(sqlRestaurarStock)) {
                psStock.setInt(1, idMaterial);
                psStock.executeUpdate();
            }

            conn.commit();

            long diasRetraso = ChronoUnit.DAYS.between(fechaEntregaPrevista, fechaEntregadoEfectiva);
            if (diasRetraso > 0) {
                double multa = diasRetraso * 1.50;
                return String.format("Devolucion registrada con RETRASO de %d dia(s). Multa sugerida: $%.2f", diasRetraso, multa);
            } else {
                return "Devolucion registrada a tiempo. Stock actualizado correctamente.";
            }

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return "Error de base de datos al registrar devolucion: " + e.getMessage();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Listar todos los préstamos para la tabla de la interfaz gráfica
    public List<Object[]> listarHistorialPrestamos() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.cod_prestamo, u.cod_usuario, CONCAT(u.nombres, ' ', u.apellidos) AS nombre_completo, " +
                     "m.cod_material, m.titulo, p.fecha_prestamo, p.fecha_entrega, p.fecha_entregado, p.estado " +
                     "FROM prestamos p " +
                     "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "INNER JOIN materiales m ON p.id_material = m.id_material " +
                     "ORDER BY p.id_prestamo DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Date fEntregado = rs.getDate("fecha_entregado");
                lista.add(new Object[]{
                    rs.getString("cod_prestamo"),
                    rs.getString("cod_usuario") + " - " + rs.getString("nombre_completo"),
                    rs.getString("cod_material") + " - " + rs.getString("titulo"),
                    rs.getDate("fecha_prestamo").toString(),
                    rs.getDate("fecha_entrega").toString(),
                    (fEntregado != null) ? fEntregado.toString() : "Pendiente",
                    rs.getString("estado").toUpperCase()
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar historial: " + e.getMessage());
        }
        return lista;
    }
}