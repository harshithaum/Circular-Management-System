package com.kec.cms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class EditCircular extends JFrame implements ActionListener {
    private JTextField descField;
    private JButton chooseBtn, updateBtn;
    private File selectedFile;
    private Circular circular;
    private Home home;

    public EditCircular(Circular circular, Home home) {
        this.circular = circular;
        this.home = home;
        setTitle("Edit Circular");
        setSize(400, 200);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Description:"));
        descField = new JTextField(circular.getDescription());
        add(descField);

        chooseBtn = new JButton("Choose Image");
        chooseBtn.addActionListener(this);
        add(chooseBtn);

        updateBtn = new JButton("Update");
        updateBtn.addActionListener(this);
        add(updateBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseBtn) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
            }
        } else if (e.getSource() == updateBtn) {
            if (descField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter description");
                return;
            }

            try {
                Connection con = DBConnection.getConnection();
                String query;
                PreparedStatement pst;

                if (selectedFile != null) {
                    query = "UPDATE circulars SET description=?, image=? WHERE id=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, descField.getText());
                    FileInputStream fis = new FileInputStream(selectedFile);
                    pst.setBinaryStream(2, fis, (int) selectedFile.length());
                    pst.setInt(3, circular.getId());
                } else {
                    query = "UPDATE circulars SET description=? WHERE id=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, descField.getText());
                    pst.setInt(2, circular.getId());
                }

                pst.executeUpdate();
                con.close();

                JOptionPane.showMessageDialog(this, "Updated Successfully!");
                home.loadCirculars();
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex);
            }
        }
    }
}
