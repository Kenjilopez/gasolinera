package formulario;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formulario extends JFrame {

    // Componentes de la interfaz
    private JTextField txtCapacidadTotal;
    private JTextField txtGalonesActuales;
    private JTextField txtPlaca;
    private JTextField txtDinero;
    private JTextField txtGalonesIngresar;
    private JRadioButton rbtnDinero;
    private JRadioButton rbtnGalones;
    private JButton btnLlenarTanque;
    private JButton btnIniciarLlenado;
    private JButton btnVerTotales;
    private JButton btnReset;
    private JComboBox<String> comboFormaPago;
    private JLabel lblProgresoGasolina;
    private JLabel lblValorAPagar;
    private JTextArea txtFactura;

    // Variables del surtidor
    private final double PRECIO_POR_GALON = 15540.0;
    private double capacidadTotal = 10; // Capacidad máxima del tanque
    private double gasolinaActual = 0; // Gasolina actual en el tanque
    private double montoSeleccionado = 0; // Monto seleccionado

    // Variables de control acumulativo
    private double galonesVendidosTotal = 0; // Total de galones vendidos
    private int vehiculosAtendidos = 0; // Total de vehículos atendidos
    private double ingresosTotalesEfectivo = 0; // Total de ingresos en efectivo
    private double ingresosTotalesTarjeta = 0; // Total de ingresos con tarjeta

    public Formulario() {
        setTitle("Surtidor de Gasolina");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Componentes
        JLabel lblCapacidadTotal = new JLabel("Capacidad total (galones):");
        lblCapacidadTotal.setBounds(30, 30, 200, 25);
        add(lblCapacidadTotal);

        txtCapacidadTotal = new JTextField();
        txtCapacidadTotal.setBounds(230, 30, 150, 25);
        add(txtCapacidadTotal);

        JLabel lblGalonesActuales = new JLabel("Galones actuales:");
        lblGalonesActuales.setBounds(30, 70, 150, 25);
        add(lblGalonesActuales);

        txtGalonesActuales = new JTextField();
        txtGalonesActuales.setBounds(230, 70, 150, 25);
        add(txtGalonesActuales);

        JLabel lblPlaca = new JLabel("Placa del vehículo:");
        lblPlaca.setBounds(30, 110, 150, 25);
        add(lblPlaca);

        txtPlaca = new JTextField();
        txtPlaca.setBounds(230, 110, 150, 25);
        add(txtPlaca);

        rbtnDinero = new JRadioButton("Ingresar por dinero");
        rbtnDinero.setBounds(30, 150, 150, 25);
        add(rbtnDinero);

        txtDinero = new JTextField();
        txtDinero.setBounds(230, 150, 150, 25);
        txtDinero.setEnabled(false);
        add(txtDinero);

        rbtnGalones = new JRadioButton("Ingresar por galones");
        rbtnGalones.setBounds(30, 190, 150, 25);
        add(rbtnGalones);

        txtGalonesIngresar = new JTextField();
        txtGalonesIngresar.setBounds(230, 190, 150, 25);
        txtGalonesIngresar.setEnabled(false);
        add(txtGalonesIngresar);

        ButtonGroup bgOpciones = new ButtonGroup();
        bgOpciones.add(rbtnDinero);
        bgOpciones.add(rbtnGalones);

        btnLlenarTanque = new JButton("Llenar tanque");
        btnLlenarTanque.setBounds(30, 230, 150, 25);
        add(btnLlenarTanque);

        JLabel lblFormaPago = new JLabel("Forma de pago:");
        lblFormaPago.setBounds(30, 270, 150, 25);
        add(lblFormaPago);

        comboFormaPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta"});
        comboFormaPago.setBounds(230, 270, 150, 25);
        add(comboFormaPago);

        btnIniciarLlenado = new JButton("Iniciar llenado");
        btnIniciarLlenado.setBounds(30, 310, 150, 25);
        btnIniciarLlenado.setEnabled(false);
        add(btnIniciarLlenado);

        btnVerTotales = new JButton("Ver Totales");
        btnVerTotales.setBounds(230, 310, 150, 25);
        add(btnVerTotales);

        btnReset = new JButton("Resetear");
        btnReset.setBounds(30, 350, 150, 25);
        add(btnReset);

        lblProgresoGasolina = new JLabel("Progreso de gasolina: 0 galones");
        lblProgresoGasolina.setBounds(30, 390, 300, 25);
        add(lblProgresoGasolina);

        lblValorAPagar = new JLabel("Valor a pagar: $0.00");
        lblValorAPagar.setBounds(30, 430, 300, 25);
        add(lblValorAPagar);

        txtFactura = new JTextArea("Factura:\nN/A");
        txtFactura.setBounds(30, 470, 500, 250);
        txtFactura.setEditable(false);
        txtFactura.setLineWrap(true);
        txtFactura.setWrapStyleWord(true);
        add(txtFactura);

        // Eventos
        rbtnDinero.addActionListener(e -> {
            txtDinero.setEnabled(true);
            txtGalonesIngresar.setEnabled(false);
            txtGalonesIngresar.setText("");
            validarCampos();
        });

        rbtnGalones.addActionListener(e -> {
            txtDinero.setEnabled(false);
            txtGalonesIngresar.setEnabled(true);
            txtDinero.setText("");
            validarCampos();
        });

        txtGalonesActuales.addCaretListener(e -> validarCampos());
        txtCapacidadTotal.addCaretListener(e -> validarCampos());
        txtDinero.addCaretListener(e -> validarCampos());
        txtGalonesIngresar.addCaretListener(e -> validarCampos());
        txtPlaca.addCaretListener(e -> validarCampos());

        btnLlenarTanque.addActionListener(e -> llenarTanque());
        btnIniciarLlenado.addActionListener(e -> iniciarLlenado());
        btnVerTotales.addActionListener(e -> mostrarTotales());
        btnReset.addActionListener(e -> resetearCampos());

        setVisible(true);
    }

    private void validarCampos() {
        try {
            gasolinaActual = Double.parseDouble(txtGalonesActuales.getText());
            capacidadTotal = Double.parseDouble(txtCapacidadTotal.getText());
            String placa = txtPlaca.getText();

            if (placa.isEmpty() || gasolinaActual < 0 || capacidadTotal <= 0 || gasolinaActual > capacidadTotal) {
                btnIniciarLlenado.setEnabled(false);
                return;
            }

            if (rbtnDinero.isSelected()) {
                double dinero = txtDinero.getText().isEmpty() ? 0 : Double.parseDouble(txtDinero.getText());
                btnIniciarLlenado.setEnabled(dinero > 0);
            } else if (rbtnGalones.isSelected()) {
                double galones = txtGalonesIngresar.getText().isEmpty() ? 0 : Double.parseDouble(txtGalonesIngresar.getText());
                btnIniciarLlenado.setEnabled(galones > 0);
            }
        } catch (NumberFormatException ex) {
            btnIniciarLlenado.setEnabled(false);
        }
    }

    private void llenarTanque() {
        try {
            capacidadTotal = Double.parseDouble(txtCapacidadTotal.getText());
            gasolinaActual = Double.parseDouble(txtGalonesActuales.getText());

            if (gasolinaActual >= capacidadTotal) {
                JOptionPane.showMessageDialog(this, "El tanque ya está lleno.", "Tanque Lleno", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            double galonesFaltantes = capacidadTotal - gasolinaActual;
            double costoTotal = galonesFaltantes * PRECIO_POR_GALON;

            incrementarProgreso(galonesFaltantes, costoTotal);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void incrementarProgreso(double galonesFaltantes, double costoTotal) {
        new Thread(() -> {
            double progreso = 0;
            double incremento = galonesFaltantes / 100;

            while (progreso < galonesFaltantes) {
                progreso += incremento;
                gasolinaActual += incremento;

                double costoActual = gasolinaActual * PRECIO_POR_GALON;

                SwingUtilities.invokeLater(() -> {
                    lblProgresoGasolina.setText(String.format("Progreso de gasolina: %.0f galones", gasolinaActual));
                    lblValorAPagar.setText(String.format("Valor a pagar: $%.0f", costoActual));
                });

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            gasolinaActual = capacidadTotal;

            String metodoPago = (String) comboFormaPago.getSelectedItem();
            if (metodoPago.equals("Efectivo")) {
                ingresosTotalesEfectivo += costoTotal;
            } else {
                ingresosTotalesTarjeta += costoTotal;
            }

            galonesVendidosTotal += galonesFaltantes;
            vehiculosAtendidos++;

            String factura = generarFactura(galonesFaltantes, costoTotal, metodoPago);
            txtFactura.setText(factura);

            JOptionPane.showMessageDialog(this, "Llenado completado.", "Proceso Completado", JOptionPane.INFORMATION_MESSAGE);
        }).start();
    }

    private String generarFactura(double galones, double costo, String metodoPago) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = sdf.format(new Date());
        String placa = txtPlaca.getText();

        return String.format(
            "Gasolinera: Gasolinería BossB\nNIT: 8.000.154\nCiudad: Bogotá\nFecha: %s\nPlaca: %s\nGalones ingresados: %.2f\nCosto: $%.2f\nForma de pago: %s",
            fecha, placa, galones, costo, metodoPago
        );
    }

    private void iniciarLlenado() {
        double galonesParaIngresar = 0;

        if (rbtnDinero.isSelected()) {
            double dinero = Double.parseDouble(txtDinero.getText());
            galonesParaIngresar = dinero / PRECIO_POR_GALON;
        } else if (rbtnGalones.isSelected()) {
            galonesParaIngresar = Double.parseDouble(txtGalonesIngresar.getText());
        }

        double espacioDisponible = capacidadTotal - gasolinaActual;
        if (galonesParaIngresar > espacioDisponible) {
            galonesParaIngresar = espacioDisponible;
        }

        incrementarProgreso(galonesParaIngresar, galonesParaIngresar * PRECIO_POR_GALON);
    }

    private void mostrarTotales() {
        String mensaje = String.format(
            "=== Totales Acumulados ===\n" +
            "Galones vendidos: %.2f\n" +
            "Vehículos atendidos: %d\n" +
            "Ingresos totales en efectivo: $%.2f\n" +
            "Ingresos totales con tarjeta: $%.2f",
            galonesVendidosTotal, vehiculosAtendidos, ingresosTotalesEfectivo, ingresosTotalesTarjeta
        );

        JOptionPane.showMessageDialog(this, mensaje, "Totales Acumulados", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetearCampos() {
        txtCapacidadTotal.setText("");
        txtGalonesActuales.setText("");
        txtPlaca.setText("");
        txtDinero.setText("");
        txtGalonesIngresar.setText("");
        lblProgresoGasolina.setText("Progreso de gasolina: 0 galones");
        lblValorAPagar.setText("Valor a pagar: $0.00");
        comboFormaPago.setSelectedIndex(0);
        btnIniciarLlenado.setEnabled(false);

        JOptionPane.showMessageDialog(this, "Los campos de entrada han sido reiniciados.", "Campos Reiniciados", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
    }
}
