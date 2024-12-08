import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.NumberFormat;

public class BancoMexicanoGUI {
    private Connection connection;
    private int cuentaId;
    private JFrame frame;
    private JPanel panel;
    private JLabel saldoLabel;

    public BancoMexicanoGUI(JFrame mainFrame) {
        connectToDatabase();
        frame = new JFrame("Banco Mexicano");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mainFrame.setVisible(true);
            }
        });
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        saldoLabel = new JLabel("Saldo: $" + getSaldo());
        panel.add(saldoLabel);

        JButton depositoButton = new JButton("Depósito");
        depositoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarDeposito();
            }
        });
        panel.add(depositoButton);

        JButton retiroButton = new JButton("Retiro");
        retiroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarRetiro();
            }
        });
        panel.add(retiroButton);

        JButton saldoButton = new JButton("Saldo");
        saldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSaldo();
            }
        });
        panel.add(saldoButton);

        JButton salirButton = new JButton("Salir");
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(salirButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void connectToDatabase() {
        String url = "jdbc:sqlite:/home/edson/apps/activityEncript.db";

        try {
            // Cargar el driver JDBC de SQLite
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS cuentas (id INTEGER PRIMARY KEY AUTOINCREMENT, saldo REAL NOT NULL DEFAULT 0.00)");
            stmt.execute("CREATE TABLE IF NOT EXISTS transacciones (id INTEGER PRIMARY KEY AUTOINCREMENT, cuenta_id INTEGER NOT NULL, tipo TEXT NOT NULL CHECK(tipo IN ('DEPOSITO', 'RETIRO')), cantidad REAL NOT NULL, fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (cuenta_id) REFERENCES cuentas(id))");

            ResultSet rs = stmt.executeQuery("SELECT id FROM cuentas LIMIT 1");
            if (rs.next()) {
                cuentaId = rs.getInt("id");
            } else {
                stmt.executeUpdate("INSERT INTO cuentas (saldo) VALUES (0.00)");
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cuentaId = generatedKeys.getInt(1);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private double getSaldo() {
        if (connection == null) {
            System.err.println("No database connection.");
            return 0.0;
        }
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT saldo FROM cuentas WHERE id = " + cuentaId);
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private void realizarDeposito() {
        JFormattedTextField cantidadField = createCurrencyField();
        int result = JOptionPane.showConfirmDialog(frame, cantidadField, "Ingrese la cantidad a depositar:", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double cantidad = ((Number) cantidadField.getValue()).doubleValue();
                updateSaldo(cantidad, "DEPOSITO");
                saldoLabel.setText("Saldo: $" + getSaldo());
                int respuesta = JOptionPane.showConfirmDialog(frame, "Depósito realizado con éxito. ¿Desea realizar otro depósito?", "Depósito", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    realizarDeposito();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Cantidad no válida. Intente de nuevo.");
            }
        }
    }

    private void realizarRetiro() {
        JFormattedTextField cantidadField = createCurrencyField();
        int result = JOptionPane.showConfirmDialog(frame, cantidadField, "Ingrese la cantidad a retirar:", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double cantidad = ((Number) cantidadField.getValue()).doubleValue();
                if (cantidad > getSaldo()) {
                    JOptionPane.showMessageDialog(frame, "Fondos insuficientes. Saldo actual: $" + getSaldo());
                } else {
                    updateSaldo(-cantidad, "RETIRO");
                    saldoLabel.setText("Saldo: $" + getSaldo());
                    JOptionPane.showMessageDialog(frame, "Retiro realizado con éxito. Nuevo saldo: $" + getSaldo());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Cantidad no válida. Intente de nuevo.");
            }
        }
    }

    private void updateSaldo(double cantidad, String tipo) {
        if (connection == null) {
            System.err.println("No database connection.");
            return;
        }
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("UPDATE cuentas SET saldo = saldo + " + cantidad + " WHERE id = " + cuentaId);
            stmt.executeUpdate("INSERT INTO transacciones (cuenta_id, tipo, cantidad) VALUES (" + cuentaId + ", '" + tipo + "', " + cantidad + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarSaldo() {
        JOptionPane.showMessageDialog(frame, "Su saldo actual es: $" + getSaldo());
    }

    private JFormattedTextField createCurrencyField() {
        NumberFormat format = NumberFormat.getNumberInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(Double.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return new JFormattedTextField(formatter);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Main Frame");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300, 200);
        mainFrame.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BancoMexicanoGUI(mainFrame);
            }
        });
    }
}