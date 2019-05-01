package phonebook.gui_components.account_components;

import phonebook.Account;
import phonebook.MainClass;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AccountTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lab = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Account account = (Account)value;
        lab.setHorizontalAlignment(SwingConstants.LEFT);
        if (column==0){
            lab.setText("");
            lab.setIcon(MainClass.resourceLoader.getImageIconResource(account.getType()));
        }
        if (column==1){
            lab.setText(account.getAccount());
            lab.setIcon(null);
        }
        return lab;
    }

}
