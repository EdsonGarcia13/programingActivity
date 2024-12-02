import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IMCApp extends JFrame {

    private JTextField weightField;
    private JTextField heightField;
    private JTextArea resultArea;

    public IMCApp() {
        setTitle("IMC HOSPITAL");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel weightLabel = new JLabel("Peso en kilogramos:");
        weightField = new JTextField();

        JLabel heightLabel = new JLabel("Estatura en metros:");
        heightField = new JTextField();

        JButton acceptButton = new JButton("Aceptar");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateIMC();
            }
        });

        add(weightLabel);
        add(weightField);
        add(heightLabel);
        add(heightField);
        add(acceptButton);
        add(new JScrollPane(resultArea));
    }

    private void calculateIMC() {
        try {
            double weight = Double.parseDouble(weightField.getText());
            double height = Double.parseDouble(heightField.getText());

            double imc = weight / (height * height);

            String result = "Su IMC es: " + String.format("%.1f", imc) + "\n";

            if (imc < 18.5) {
                result += "La persona tiene bajo peso";
            } else if (imc < 24.9) {
                result += "La persona tiene peso normal";
            } else if (imc < 29.9) {
                result += "La persona tiene sobrepeso";
            } else if (imc < 34.9) {
                result += "La persona tiene obesidad grado I";
            } else if (imc < 39.9) {
                result += "La persona tiene obesidad grado II";
            } else {
                result += "La persona tiene obesidad grado III";
            }

            resultArea.setText(result);
        } catch (NumberFormatException e) {
            resultArea.setText("Por favor, ingrese valores numéricos válidos.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new IMCApp().setVisible(true);
            }
        });
    }
}