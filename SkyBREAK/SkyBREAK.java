package com.mycompany.katesjavaproj;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

public class SkyBREAK extends JPanel {

    private static final long serialVersionUID = 7148504528835036003L;

    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    static int screenWidth = (int)toolkit.getScreenSize().getWidth();
    static int screenHeight = (int)toolkit.getScreenSize().getHeight();

    public static ImageIcon caretImage = new ImageIcon(SkyBREAK.class.getResource("cursor.png"));
    static Image scaledCaretImage;

    public static ImageIcon icon = new ImageIcon(SkyBREAK.class.getResource("desktop.png"));
    static final Color darkGreen = new Color(4, 161, 34);

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SetupDesktop();

            JFrame terminal = SetupTerminal();
            terminal.setVisible(true);

            JPanel introPanel = CreatePanel(1, terminal);
            terminal.add(introPanel);
            introPanel.add(Box.createRigidArea(new Dimension(10, screenHeight/75)));
            introPanel.setMinimumSize(new Dimension(terminal.getWidth()*15, 10));

            introPanel.add(CreateLabel("Initializing..."));
            introPanel.add(CreateLabel("skyBREAK ver. 1.6.13"));
            introPanel.add(CreateLabel(" "));
            introPanel.add(CreateLabel("    Welcome to skyBREAK. You have the following crackers installed:"));
            introPanel.add(CreateLabel("        > WEP"));


            JPanel inputPanel = CreatePanel(0, terminal);
            inputPanel.setMaximumSize(new Dimension(screenWidth, screenWidth/85));
            terminal.add(inputPanel);

            inputPanel.add(CreateLabel("skyBREAK > "));

            final JTextField field = new JTextField();
            field.setFont(new Font("Consolas", Font.PLAIN, screenWidth/90));
            field.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            field.setForeground(darkGreen);
            field.setBackground(Color.black);
            scaledCaretImage = caretImage.getImage().getScaledInstance(screenWidth/150, screenHeight/50, Image.SCALE_DEFAULT);
            field.setCaret(new MyCaret());

            inputPanel.add(field);
            inputPanel.requestFocus();

            field.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    field.setText("works");
                }
            });

        });

    }


    public static void SetupDesktop() {

        JTextArea text = new JTextArea() {
            Image img = icon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
            // instance initializer
            {setOpaque(false);}
            public void paintComponent(Graphics graphics)
            {
                graphics.drawImage(img, 0, 0, this);
                super.paintComponent(graphics);
            }
        };

        JFrame desktop = new JFrame();

        JScrollPane backgroundIMG = new JScrollPane(text);
        Container content = desktop.getContentPane();
        content.add(backgroundIMG, BorderLayout.CENTER);

        desktop.setSize(screenWidth, screenHeight);
        desktop.setUndecorated(true);
        desktop.setExtendedState(JFrame.MAXIMIZED_BOTH);
        desktop.setVisible(true);


    }

    private static JFrame SetupTerminal() {

        JFrame terminal = new JFrame("skyBREAK");
        terminal.getContentPane().setBackground(Color.black);
        terminal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        terminal.setSize(screenWidth/2, screenHeight/2);
        terminal.setLocation(screenWidth/9, screenHeight/7);
        terminal.setLayout(new BoxLayout(terminal.getContentPane(), BoxLayout.Y_AXIS));
        terminal.setResizable(false);
        return terminal;

    }

    public static JLabel CreateLabel(String content) {

        JLabel label = new JLabel(content);
        label.setFont(new Font("Consolas", Font.PLAIN, screenWidth/90));
        label.setForeground(darkGreen);
        label.setBorder(new EmptyBorder(5, 20, 5, 0));
        return label;

    }

    public static JPanel CreatePanel(int axis, JFrame terminal) {

        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        BoxLayout boxLayout = new BoxLayout(panel, axis);
        panel.setLayout(boxLayout);
        return panel;

    }

    public static class MyCaret extends DefaultCaret {

        public MyCaret() {
            setBlinkRate(500);
        }

        @Override
        protected synchronized void damage(Rectangle r) {
            if (r == null) {
                return;
            }

            x = r.x;
            y = r.y;
            repaint(); // calls getComponent().repaint(x, y, width, height)
        }

        @Override
        public void paint(Graphics g) {

            setSize(scaledCaretImage.getWidth(null), scaledCaretImage.getHeight(null));

            JTextComponent comp = getComponent();
            if (comp == null) {
                return;
            }

            int dot = getDot();
            Rectangle r = null;
            try {
                r = comp.modelToView(dot);
            } catch (BadLocationException e) {
                return;
            }
            if (r == null) {
                return;
            }

            if ((x != r.x) || (y != r.y)) {
                repaint(); // erase previous location of caret
                damage(r);
            }

            if (isVisible()) {
                g.drawImage(scaledCaretImage, x, y, null);
            }
        }

    }
    
}
