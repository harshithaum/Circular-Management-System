package com.kec.cms;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Home extends JFrame {
    private JPanel panel;

    public Home() {
        setTitle("Circular Management");
        setSize(1000, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton addBtn = new JButton("Add Circular");
        addBtn.setFont(new Font("Arial", Font.BOLD, 16));
        addBtn.addActionListener(e -> new UploadCircular(this));
        add(addBtn, BorderLayout.NORTH);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);

        loadCirculars();

        setVisible(true);
    }

    public void loadCirculars() {
        panel.removeAll();

        List<Circular> list = Circular.getAll();

        for (Circular c : list) {

            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));

            // --- BUTTON PANEL (TOP) ---
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton edit = new JButton("Edit");
            edit.addActionListener(e -> new EditCircular(c, this));

            JButton delete = new JButton("Delete");
            delete.addActionListener(e -> {
                c.delete();
                loadCirculars();
            });

            buttonPanel.add(edit);
            buttonPanel.add(delete);

            // --- IMAGE (CENTER, LARGE) ---
            byte[] imgBytes = c.getImage();
            JLabel imageLabel;

            if (imgBytes != null && imgBytes.length > 0) {
                ImageIcon icon = new ImageIcon(imgBytes);
                Image img = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(img));
            } else {
                imageLabel = new JLabel("No Image");
            }

            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // --- DESCRIPTION (BOTTOM) ---
            JLabel descLabel = new JLabel(c.getDescription());
            descLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // --- OPTIONAL: CLICK TO VIEW FULL IMAGE ---
            if (imgBytes != null) {
                imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        JLabel full = new JLabel(new ImageIcon(imgBytes));
                        JOptionPane.showMessageDialog(null, full, "Full Image", JOptionPane.PLAIN_MESSAGE);
                    }
                });
            }

            // --- ADD COMPONENTS ---
            card.add(buttonPanel);
            card.add(Box.createVerticalStrut(10));
            card.add(imageLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(descLabel);

            panel.add(card);
            panel.add(Box.createVerticalStrut(15)); // spacing between cards
        }

        panel.revalidate();
        panel.repaint();
    }

    public static void main(String[] args) {
        new Home();
    }
}