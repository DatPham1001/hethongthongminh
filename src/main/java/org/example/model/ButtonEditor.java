package org.example.model;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private int row;
    private JFrame frame;
    private ClusterO clusterO;

    public ButtonEditor(JFrame frame, ClusterO clusterO) {
        button = new JButton();
        this.frame = frame;
        this.clusterO = clusterO;
        button.addActionListener(this);
    }

    public Object getCellEditorValue() {
        return "";
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        button.setText((value == null) ? "" : value.toString());
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        // Handle the button click
        System.out.println(button + "Button clicked for row " + row);
        // Get data from selected row
        String id = clusterO.getId();
        String address = clusterO.getAddress();
        double lng = clusterO.getLongitude();
        double lat = clusterO.getLatitude();
        List<GeoAddressData> geoAddresses = clusterO.getGeoAddresses();
        for (GeoAddressData geoAddress : geoAddresses) {
            if (geoAddress.getFormatted_address().equalsIgnoreCase(clusterO.getAddress())) {
                geoAddresses.remove(geoAddress);
                geoAddresses.add(0, geoAddress);
                break;
            }

        }
        ComboBoxModel<GeoAddressData> comboBoxModel
                = new DefaultComboBoxModel<>(geoAddresses.toArray(new GeoAddressData[geoAddresses.size()]));
        // create a JComboBox with the ComboBoxModel
        JComboBox<GeoAddressData> comboBox = new JComboBox<>(comboBoxModel);
        // Create dialog and fill with data
        JDialog dialog = new JDialog(frame, "Điều chỉnh tâm", true);
        dialog.setLayout(new GridLayout(7, 1));
        dialog.add(new JLabel("Id: "));
        dialog.add(new JLabel(id + "\n"));
        dialog.add(new JLabel("<html><br> Địa chỉ : </html>"));
//        dialog.add(new JLabel(address + "\n"));
        dialog.add(comboBox);
        dialog.add(new JLabel("<html><br> Kinh độ : </html>"));
        dialog.add(new JLabel(String.valueOf(lng) + "\n"));
        dialog.add(new JLabel("<html><br> Vĩ độ : </html>"));
        dialog.add(new JLabel(String.valueOf(lat) + "\n"));
        dialog.setSize(400, 200);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        fireEditingStopped();
    }
}
