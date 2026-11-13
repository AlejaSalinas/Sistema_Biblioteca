package proyecto.java;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class FrmGestionMateriales extends JFrame {

    private final MaterialDAO dao = new MaterialDAO();

    private JComboBox<String> cmbTipo, cmbFiltro;
    private JTextField txtId, txtTitulo, txtAnio, txtEditorial, txtCantidad;
    private JTextField txtAutor, txtIsbn, txtPaginas;
    private JTextField txtEdicion, txtVolumen, txtMes, txtBuscar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public FrmGestionMateriales() {
        setTitle("Gestión de Catálogo de Materiales - Biblioteca");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        actualizarCamposPorTipo();
        cargarTabla("Todos", "");
    }

    private void inicializarComponentes() {
        JPanel panelForm = new JPanel(new GridLayout(6, 4, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Material Bibliográfico"));

        cmbTipo = new JComboBox<>(new String[]{"Libro", "Revista"});
        txtId = new JTextField();
        txtTitulo = new JTextField();
        txtAnio = new JTextField();
        txtEditorial = new JTextField();
        txtCantidad = new JTextField("1");

        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtPaginas = new JTextField();

        txtEdicion = new JTextField();
        txtVolumen = new JTextField();
        txtMes = new JTextField();

        panelForm.add(new JLabel("Tipo de Material:")); panelForm.add(cmbTipo);
        panelForm.add(new JLabel("ID Único:")); panelForm.add(txtId);

        panelForm.add(new JLabel("Título:")); panelForm.add(txtTitulo);
        panelForm.add(new JLabel("Año Publicación:")); panelForm.add(txtAnio);

        panelForm.add(new JLabel("Editorial:")); panelForm.add(txtEditorial);
        panelForm.add(new JLabel("Cantidad Disponible:")); panelForm.add(txtCantidad);

        panelForm.add(new JLabel("Autor (Libro):")); panelForm.add(txtAutor);
        panelForm.add(new JLabel("ISBN (Libro):")); panelForm.add(txtIsbn);

        panelForm.add(new JLabel("N° Páginas (Libro):")); panelForm.add(txtPaginas);
        panelForm.add(new JLabel("N° Edición (Revista):")); panelForm.add(txtEdicion);

        panelForm.add(new JLabel("Volumen (Revista):")); panelForm.add(txtVolumen);
        panelForm.add(new JLabel("Mes Publicación (Revista):")); panelForm.add(txtMes);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnGuardar = new JButton("Registrar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelForm, BorderLayout.CENTER);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbFiltro = new JComboBox<>(new String[]{"Todos", "ID", "Título", "Autor", "Tipo"});
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");

        panelBusqueda.add(new JLabel("Buscar por:"));
        panelBusqueda.add(cmbFiltro);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Tipo", "Título", "Año", "Editorial", "Disponibles", "Detalles Específicos"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);

        panelCentro.add(panelBusqueda, BorderLayout.NORTH);
        panelCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        cmbTipo.addActionListener(e -> actualizarCamposPorTipo());
        btnGuardar.addActionListener(e -> guardarMaterial());
        btnModificar.addActionListener(e -> modificarMaterial());
        btnEliminar.addActionListener(e -> eliminarMaterial());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> cargarTabla(cmbFiltro.getSelectedItem().toString(), txtBuscar.getText().trim()));

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                seleccionarFilaTabla();
            }
        });
    }

    private void actualizarCamposPorTipo() {
        boolean esLibro = "Libro".equals(cmbTipo.getSelectedItem());
        txtAutor.setEnabled(esLibro);
        txtIsbn.setEnabled(esLibro);
        txtPaginas.setEnabled(esLibro);

        txtEdicion.setEnabled(!esLibro);
        txtVolumen.setEnabled(!esLibro);
        txtMes.setEnabled(!esLibro);
    }

    private Material construirMaterialDesdeFormulario() {
        String id = txtId.getText().trim();
        String titulo = txtTitulo.getText().trim();
        String editorial = txtEditorial.getText().trim();

        if (id.isEmpty() || titulo.isEmpty() || editorial.isEmpty() || txtAnio.getText().trim().isEmpty() || txtCantidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos generales son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int anio, cantidad;
        try {
            anio = Integer.parseInt(txtAnio.getText().trim());
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (anio < 1450 || anio > 2026) {
                JOptionPane.showMessageDialog(this, "Ingrese un año de publicación válido (1450 - 2026).", "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(this, "La cantidad disponible debe ser mayor o igual a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Año y Cantidad deben ser números enteros.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if ("Libro".equals(cmbTipo.getSelectedItem())) {
            String autor = txtAutor.getText().trim();
            String isbn = txtIsbn.getText().trim();
            if (autor.isEmpty() || isbn.isEmpty() || txtPaginas.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete Autor, ISBN y Número de Páginas.", "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            try {
                int paginas = Integer.parseInt(txtPaginas.getText().trim());
                if (paginas <= 0) {
                    JOptionPane.showMessageDialog(this, "El número de páginas debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return null;
                }
               return new Libro(id, titulo, anio, editorial, cantidad, autor, isbn, paginas);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El número de páginas debe ser un entero.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            String mes = txtMes.getText().trim();
            if (mes.isEmpty() || txtEdicion.getText().trim().isEmpty() || txtVolumen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete Edición, Volumen y Mes de publicación.", "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            try {
                int edicion = Integer.parseInt(txtEdicion.getText().trim());
                int volumen = Integer.parseInt(txtVolumen.getText().trim());
                if (edicion <= 0 || volumen <= 0) {
                    JOptionPane.showMessageDialog(this, "Edición y Volumen deben ser mayores a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return null;
                }
                return new Revista(id, titulo, anio, editorial, cantidad, edicion, volumen, mes);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Edición y Volumen deben ser números enteros.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }

    private void guardarMaterial() {
        Material m = construirMaterialDesdeFormulario();
        if (m == null) return;

        try {
            if (dao.registrarMaterial(m)) {
                JOptionPane.showMessageDialog(this, "Material registrado exitosamente.");
                limpiarFormulario();
                cargarTabla("Todos", "");
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un material con el ID: " + m.getId(), "ID Duplicado", JOptionPane.ERROR_MESSAGE);
}
} catch (SQLException ex) {
JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
}
}
private void modificarMaterial() {
Material m = construirMaterialDesdeFormulario();
if (m == null) return;
try {
if (!dao.existeId(m.getId())) {
JOptionPane.showMessageDialog(this, "No existe un material con ese ID para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
return;
}
if (dao.modificarMaterial(m)) {
JOptionPane.showMessageDialog(this, "Material actualizado correctamente.");
limpiarFormulario();
cargarTabla("Todos", "");
}
} catch (SQLException ex) {
JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
}
}
private void eliminarMaterial() {
String id = txtId.getText().trim();
if (id.isEmpty()) {
JOptionPane.showMessageDialog(this, "Seleccione o escriba el ID del material a eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
return;
}
int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar el material " + id + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
if (confirm != JOptionPane.YES_OPTION) return;
try {
String resultado = dao.eliminarMaterial(id);
if ("OK".equals(resultado)) {
JOptionPane.showMessageDialog(this, "Material eliminado del catálogo.");
limpiarFormulario();
cargarTabla("Todos", "");
} else {
JOptionPane.showMessageDialog(this, resultado, "No permitido", JOptionPane.WARNING_MESSAGE);
}
} catch (SQLException ex) {
JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
}
}
private void cargarTabla(String criterio, String valor) {
modeloTabla.setRowCount(0);
try {
List<Material> lista = dao.buscarMateriales(criterio, valor);
for (Material m : lista) {
modeloTabla.addRow(new Object[]{
m.getId(), m.getTipo(), m.getTitulo(), m.getAnioPublicacion(),
m.getEditorial(), m.getCantidadDisponible(), m.obtenerDetalleEspecifico()
});
}
} catch (SQLException ex) {
JOptionPane.showMessageDialog(this, "Error al cargar catálogo: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
}
}
private void seleccionarFilaTabla() {
int fila = tabla.getSelectedRow();
String id = modeloTabla.getValueAt(fila, 0).toString();
try {
List<Material> res = dao.buscarMateriales("ID", id);
if (!res.isEmpty()) {
Material m = res.get(0);
txtId.setText(m.getId());
txtId.setEditable(false);
cmbTipo.setSelectedItem(m.getTipo());
cmbTipo.setEnabled(false);
txtTitulo.setText(m.getTitulo());
txtAnio.setText(String.valueOf(m.getAnioPublicacion()));
txtEditorial.setText(m.getEditorial());
txtCantidad.setText(String.valueOf(m.getCantidadDisponible()));
    switch (m) {
        case Libro l -> {
            txtAutor.setText(l.getAutor());
            txtIsbn.setText(l.getIsbn());
            txtPaginas.setText(String.valueOf(l.getNumPaginas()));
        }       case Revista r -> {
            txtEdicion.setText(String.valueOf(r.getEdicion()));
            txtVolumen.setText(String.valueOf(r.getVolumen()));
            txtMes.setText(r.getMesPublicacion());
        }       default -> {
        }
    }
}
} catch (SQLException ex) {
JOptionPane.showMessageDialog(this, "Error al recuperar detalles: " + ex.getMessage());
}
}
private void limpiarFormulario() {
txtId.setText("");
txtId.setEditable(true);
cmbTipo.setEnabled(true);
txtTitulo.setText("");
txtAnio.setText("");
txtEditorial.setText("");
txtCantidad.setText("1");
txtAutor.setText("");
txtIsbn.setText("");
txtPaginas.setText("");
txtEdicion.setText("");
txtVolumen.setText("");
txtMes.setText("");
tabla.clearSelection();
}
public static void main(String[] args) {
SwingUtilities.invokeLater(() -> new FrmGestionMateriales().setVisible(true));
}
}
