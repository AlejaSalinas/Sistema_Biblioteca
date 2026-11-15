package proyecto.java;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrmPrestamosDevoluciones extends JFrame {

    private final PrestamoDAO prestamoDAO;
    private JTextField txtCodUsuario, txtCodMaterial, txtFechaPrestamo, txtFechaPrevista;
    private JTextField txtCodPrestamoDev, txtFechaEfectiva;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    public FrmPrestamosDevoluciones() {
        this.prestamoDAO = new PrestamoDAO();
        initComponents();
        cargarHistorial();
    }

    private void initComponents() {
        setTitle("Gestion de Prestamos y Devoluciones - Sistema Biblioteca");
        setSize(920, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior con los dos formularios lado a lado
        JPanel panelFormularios = new JPanel(new GridLayout(1, 2, 15, 0));
        panelFormularios.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // --- Formulario 1: Nuevo Préstamo ---
        JPanel panelPrestamo = new JPanel(new GridLayout(5, 2, 8, 8));
        panelPrestamo.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Prestamo"));

        txtCodUsuario = new JTextField();
        txtCodMaterial = new JTextField();
        txtFechaPrestamo = new JTextField(LocalDate.now().toString());
        txtFechaPrevista = new JTextField(LocalDate.now().plusDays(7).toString());
        JButton btnPrestar = new JButton("Registrar Prestamo");

        panelPrestamo.add(new JLabel("Codigo Usuario:"));
        panelPrestamo.add(txtCodUsuario);
        panelPrestamo.add(new JLabel("Codigo Material:"));
        panelPrestamo.add(txtCodMaterial);
        panelPrestamo.add(new JLabel("Fecha Prestamo (AAAA-MM-DD):"));
        panelPrestamo.add(txtFechaPrestamo);
        panelPrestamo.add(new JLabel("Fecha Devolucion (AAAA-MM-DD):"));
        panelPrestamo.add(txtFechaPrevista);
        panelPrestamo.add(new JLabel(""));
        panelPrestamo.add(btnPrestar);

        // --- Formulario 2: Registrar Devolución ---
        JPanel panelDevolucion = new JPanel(new GridLayout(5, 2, 8, 8));
        panelDevolucion.setBorder(BorderFactory.createTitledBorder("Registrar Devolucion"));

        txtCodPrestamoDev = new JTextField();
        txtFechaEfectiva = new JTextField(LocalDate.now().toString());
        JButton btnDevolver = new JButton("Confirmar Devolucion");
        JButton btnRefrescar = new JButton("Actualizar Tabla");

        panelDevolucion.add(new JLabel("Codigo Prestamo (ej. PRES-001):"));
        panelDevolucion.add(txtCodPrestamoDev);
        panelDevolucion.add(new JLabel("Fecha Efectiva (AAAA-MM-DD):"));
        panelDevolucion.add(txtFechaEfectiva);
        panelDevolucion.add(new JLabel(""));
        panelDevolucion.add(btnDevolver);
        panelDevolucion.add(new JLabel(""));
        panelDevolucion.add(btnRefrescar);
        panelDevolucion.add(new JLabel(""));
        panelDevolucion.add(new JLabel(""));

        panelFormularios.add(panelPrestamo);
        panelFormularios.add(panelDevolucion);
        add(panelFormularios, BorderLayout.NORTH);

        // --- Tabla Central: Historial de Préstamos ---
        String[] columnas = {"Cod. Prestamo", "Usuario", "Material", "F. Prestamo", "F. Prevista", "F. Efectiva", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaHistorial = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Historial de Prestamos y Devoluciones"));
        add(scrollTabla, BorderLayout.CENTER);

        // Eventos de botones
        btnPrestar.addActionListener(e -> ejecutarPrestamo());
        btnDevolver.addActionListener(e -> ejecutarDevolucion());
        btnRefrescar.addActionListener(e -> cargarHistorial());
    }

    private void ejecutarPrestamo() {
        String codUser = txtCodUsuario.getText().trim();
        String codMat = txtCodMaterial.getText().trim();

        if (codUser.isEmpty() || codMat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete el codigo de usuario y de material.", "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fPrest = LocalDate.parse(txtFechaPrestamo.getText().trim());
            LocalDate fPrev = LocalDate.parse(txtFechaPrevista.getText().trim());

            String resultado = prestamoDAO.registrarPrestamo(codUser, codMat, fPrest, fPrev);
            JOptionPane.showMessageDialog(this, resultado);
            if (resultado.startsWith("Prestamo registrado")) {
                txtCodUsuario.setText("");
                txtCodMaterial.setText("");
                cargarHistorial();
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha invalido. Use AAAA-MM-DD.", "Error de fecha", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarDevolucion() {
        String codPres = txtCodPrestamoDev.getText().trim();
        if (codPres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el codigo del prestamo a devolver.", "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fEfec = LocalDate.parse(txtFechaEfectiva.getText().trim());
            String resultado = prestamoDAO.registrarDevolucion(codPres, fEfec);
            JOptionPane.showMessageDialog(this, resultado);
            if (resultado.startsWith("Devolucion registrada")) {
                txtCodPrestamoDev.setText("");
                cargarHistorial();
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha invalido. Use AAAA-MM-DD.", "Error de fecha", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial() {
        modeloTabla.setRowCount(0);
        List<Object[]> registros = prestamoDAO.listarHistorialPrestamos();
        for (Object[] fila : registros) {
            modeloTabla.addRow(fila);
        }
    }
}