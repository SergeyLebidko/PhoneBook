package phonebook.gui_components.contact_components;

import phonebook.MainClass;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ContactsTableHeaderRenderer extends DefaultTableCellRenderer {

    private ContactsTableModel currentTableModel;

    public ContactsTableHeaderRenderer(ContactsTableModel currentTableModel) {
        this.currentTableModel = currentTableModel;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lab = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        lab.setText("");
        lab.setBackground(new Color(230,230,230));
        lab.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        if (currentTableModel.getCurrentSortOrder()==ContactsTableModel.SORT_ORDER_TO_UP){
            lab.setIcon(MainClass.resourceLoader.getImageIconResource("to_up"));
        }
        if (currentTableModel.getCurrentSortOrder()==ContactsTableModel.SORT_ORDER_TO_DOWN){
            lab.setIcon(MainClass.resourceLoader.getImageIconResource("to_down"));
        }
        return lab;
    }

}
