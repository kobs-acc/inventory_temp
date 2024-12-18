package employee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class Table extends JTable {
    public Table() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);

        // Custom header renderer
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set header properties
                component.setBackground(new Color(136, 215, 255)); // #88d7ff
                component.setForeground(Color.BLACK);             
                component.setFont(new Font("Google Sans Display", Font.BOLD, 14)); // Bold font
                setHorizontalAlignment(CENTER);

                return component;
            }
        });

        // Add custom renderer for alternating row colors
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Alternating row colors
                if (row % 2 == 0) {
                    component.setBackground(new Color(237, 249, 255)); // #edf9ff
                } else {
                    component.setBackground(new Color(185, 229, 255)); // #b9e5ff
                }

                // Preserve selection color
                if (isSelected) {
                    component.setBackground(new Color(15, 89, 140)); // Selection background
                    component.setForeground(Color.BLACK);           // Selection text color
                } else {
                    component.setForeground(Color.BLACK);           // Default text color
                }

                return component;
            }

            @Override
            public void setValue(Object value) {
                super.setValue(value != null ? value : ""); // Ensures even empty cells have consistent rendering
            }
        });
    }
}
