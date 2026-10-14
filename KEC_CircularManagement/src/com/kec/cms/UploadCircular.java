package com.kec.cms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class UploadCircular extends JFrame implements ActionListener {
    private JTextField descField;
    private JButton chooseBtn, uploadBtn;
    private File selectedFile;
    private Home home;

    public UploadCircular(Home home) {
        this.home = home;
        setTitle("Upload Circular");
        setSize(400, 200);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Description:"));
        descField = new JTextField();
        add(descField);

        chooseBtn = new JButton("Choose Image");
        chooseBtn.addActionListener(this);
        add(chooseBtn);

        uploadBtn = new JButton("Upload");
        uploadBtn.addActionListener(this);
        add(uploadBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseBtn) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
            }
        } else if (e.getSource() == uploadBtn) {
            if (selectedFile == null || descField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select image and enter description");
                return;
            }

            try {
                Connection con = DBConnection.getConnection();
                String query = "INSERT INTO circulars(description, image) VALUES(?, ?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, descField.getText());
                FileInputStream fis = new FileInputStream(selectedFile);
                pst.setBinaryStream(2, fis, (int) selectedFile.length());
                pst.executeUpdate();
                con.close();

                JOptionPane.showMessageDialog(this, "Uploaded Successfully!");
                home.loadCirculars();
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex);
            }
        }
    }
}
