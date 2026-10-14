package com.kec.cms;

import java.sql.*;
import java.util.*;
import javax.swing.*;

public class Circular {
    private int id;
    private String description;
    private byte[] image;

    public Circular(int id, String description, byte[] image) {
        this.id = id;
        this.description = description;
        this.image = image;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public byte[] getImage() { return image; }

    public static List<Circular> getAll() {
        List<Circular> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM circulars");
            while (rs.next()) {
                int id = rs.getInt("id");
                String desc = rs.getString("description");
                Blob blob = rs.getBlob("image");
                byte[] img = blob.getBytes(1, (int) blob.length());
                list.add(new Circular(id, desc, img));
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return list;
    }

    public void delete() {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement("DELETE FROM circulars WHERE id=?");
            pst.setInt(1, id);
            pst.executeUpdate();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
