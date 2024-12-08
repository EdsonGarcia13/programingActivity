// src/MainScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {

    public MainScreen() {
        setTitle("Main Screen");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JButton imcButton = new JButton("IMC App");
        imcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IMCApp(MainScreen.this).setVisible(true);
                setVisible(false);
            }
        });

        JButton bancoButton = new JButton("Banco Mexicano");
        bancoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BancoMexicanoGUI(MainScreen.this);
                setVisible(false);
            }
        });

        add(new JLabel("Seleccione una aplicación:"));
        add(imcButton);
        add(bancoButton);
    }

}