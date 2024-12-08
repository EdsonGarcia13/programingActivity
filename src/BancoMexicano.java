// src/BancoMexicano.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BancoMexicano {
    private double saldo = 0.0;
    private JFrame frame;
    private JPanel panel;
    private JLabel saldoLabel;

    public BancoMexicano(JFrame mainFrame) {
        frame = new JFrame("Banco Mexicano");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 200);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mainFrame.setVisible(true);
            }
        });

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        saldoLabel = new JLabel("Saldo: $" + saldo);
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

    private void realizarDeposito() {
        String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad a depositar:");
        if (cantidadStr != null && !cantidadStr.isEmpty()) {
            try {
                double cantidad = Double.parseDouble(cantidadStr);
                saldo += cantidad;
                saldoLabel.setText("Saldo: $" + saldo);
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
        String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad a retirar:");
        if (cantidadStr != null && !cantidadStr.isEmpty()) {
            try {
                double cantidad = Double.parseDouble(cantidadStr);
                if (cantidad > saldo) {
                    JOptionPane.showMessageDialog(frame, "Fondos insuficientes. Saldo actual: $" + saldo);
                } else {
                    saldo -= cantidad;
                    saldoLabel.setText("Saldo: $" + saldo);
                    JOptionPane.showMessageDialog(frame, "Retiro realizado con éxito. Nuevo saldo: $" + saldo);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Cantidad no válida. Intente de nuevo.");
            }
        }
    }

    private void mostrarSaldo() {
        JOptionPane.showMessageDialog(frame, "Su saldo actual es: $" + saldo);
    }

}