package proyecto.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public boolean existeId(String id) throws SQLException {
        String sql = "SELECT 1 FROM materiales WHERE id = ?";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean registrarMaterial(Material m) throws SQLException {
        if (existeId(m.getId())) return false;

        String sqlMat = "INSERT INTO materiales (id, titulo, tipo, anio_publicacion, editorial, cantidad_disponible, disponible) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConexionMySQL.getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlMat)) {
                ps.setString(1, m.getId());
                ps.setString(2, m.getTitulo());
                ps.setString(3, m.getTipo());
                ps.setInt(4, m.getAnioPublicacion());
                ps.setString(5, m.getEditorial());
                ps.setInt(6, m.getCantidadDisponible());
                ps.setBoolean(7, m.isDisponible());
                ps.executeUpdate();
            }

            switch (m) {
                case Libro libro -> {
                    String sqlLib = "INSERT INTO libros (id_material, autor, isbn, num_paginas) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(sqlLib)) {
                        ps.setString(1, libro.getId());
                        ps.setString(2, libro.getAutor());
                        ps.setString(3, libro.getIsbn());
                        ps.setInt(4, libro.getNumPaginas());
                        ps.executeUpdate();
                    }
                }
                case Revista revista -> {
                    String sqlRev = "INSERT INTO revistas (id_material, edicion, volumen, mes_publicacion) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(sqlRev)) {
                        ps.setString(1, revista.getId());
                        ps.setInt(2, revista.getEdicion());
                        ps.setInt(3, revista.getVolumen());
                        ps.setString(4, revista.getMesPublicacion());
                        ps.executeUpdate();
                    }
                }
                default -> {
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public List<Material> listarTodos() throws SQLException {
        return buscarMateriales("Todos", "");
    }

    public List<Material> buscarMateriales(String criterio, String valor) throws SQLException {
        List<Material> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT m.*, l.autor, l.isbn, l.num_paginas, r.edicion, r.volumen, r.mes_publicacion " +
            "FROM materiales m " +
            "LEFT JOIN libros l ON m.id = l.id_material " +
            "LEFT JOIN revistas r ON m.id = r.id_material "
        );

        switch (criterio) {
            case "ID" -> sql.append("WHERE m.id LIKE ?");
            case "Título" -> sql.append("WHERE m.titulo LIKE ?");
            case "Autor" -> sql.append("WHERE l.autor LIKE ?");
            case "Tipo" -> sql.append("WHERE m.tipo = ?");
        }

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            if (!criterio.equals("Todos")) {
                ps.setString(1, criterio.equals("Tipo") ? valor : "%" + valor + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    if ("Libro".equals(tipo)) {
                        lista.add(new Libro(
                            rs.getString("id"), rs.getString("titulo"),
                            rs.getInt("anio_publicacion"), rs.getString("editorial"),
                            rs.getInt("cantidad_disponible"), rs.getString("autor"),
                            rs.getString("isbn"), rs.getInt("num_paginas")
                        ));
                    } else if ("Revista".equals(tipo)) {
                        lista.add(new Revista(
                            rs.getString("id"), rs.getString("titulo"),
                            rs.getInt("anio_publicacion"), rs.getString("editorial"),
                            rs.getInt("cantidad_disponible"), rs.getInt("edicion"),
                            rs.getInt("volumen"), rs.getString("mes_publicacion")
                        ));
                    }
                }
            }
        }
        return lista;
    }

    public boolean modificarMaterial(Material m) throws SQLException {
        String sqlMat = "UPDATE materiales SET titulo=?, anio_publicacion=?, editorial=?, cantidad_disponible=?, disponible=? WHERE id=?";
        Connection con = null;
        try {
            con = ConexionMySQL.getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlMat)) {
                ps.setString(1, m.getTitulo());
                ps.setInt(2, m.getAnioPublicacion());
                ps.setString(3, m.getEditorial());
                ps.setInt(4, m.getCantidadDisponible());
                ps.setBoolean(5, m.getCantidadDisponible() > 0);
                ps.setString(6, m.getId());
                ps.executeUpdate();
            }

            switch (m) {
                case Libro libro -> {
                    String sqlLib = "UPDATE libros SET autor=?, isbn=?, num_paginas=? WHERE id_material=?";
                    try (PreparedStatement ps = con.prepareStatement(sqlLib)) {
                        ps.setString(1, libro.getAutor());
                        ps.setString(2, libro.getIsbn());
                        ps.setInt(3, libro.getNumPaginas());
                        ps.setString(4, libro.getId());
                        ps.executeUpdate();
                    }
                }
                case Revista revista -> {
                    String sqlRev = "UPDATE revistas SET edicion=?, volumen=?, mes_publicacion=? WHERE id_material=?";
                    try (PreparedStatement ps = con.prepareStatement(sqlRev)) {
                        ps.setInt(1, revista.getEdicion());
                        ps.setInt(2, revista.getVolumen());
                        ps.setString(3, revista.getMesPublicacion());
                        ps.setString(4, revista.getId());
                        ps.executeUpdate();
                    }
                }
                default -> {
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public boolean tienePrestamosActivos(String idMaterial) throws SQLException {
        String sql = "SELECT 1 FROM prestamos WHERE id_material = ? AND activo = TRUE";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idMaterial);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public String eliminarMaterial(String id) throws SQLException {
        if (!existeId(id)) return "El material no existe.";
        if (tienePrestamosActivos(id)) return "No se puede eliminar: tiene préstamos activos.";

        String sql = "DELETE FROM materiales WHERE id = ?";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            return "OK";
        }
    }
}
