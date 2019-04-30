package phonebook.gui_components.account_components;

import phonebook.Contact;
import phonebook.MainClass;
import phonebook.data_access_components.DataBaseConnector;
import phonebook.data_access_components.ResourceLoader;
import static phonebook.gui_components.GUIProperties.*;

import javax.swing.*;
import java.awt.*;

public class AccoutsPane {

    private JPanel contentPane;
    private DataBaseConnector dataBaseConnector;
    private ResourceLoader resourceLoader;

    private JTextField contactField;
    private JButton addBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JTextField statusField;

    private Contact currentContact;

    private JTable accountsTable;
    private AccauntsTableModel tableModel;

    public AccoutsPane() {
        dataBaseConnector = MainClass.dataBaseConnector;
        resourceLoader = MainClass.resourceLoader;

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Box topBox = Box.createHorizontalBox();

        contactField = new JTextField();
        contactField.setHorizontalAlignment(SwingConstants.LEFT);
        contactField.setFont(mainFont);
        contactField.setEditable(false);
        contactField.setMinimumSize(new Dimension(100,20));

        addBtn = new JButton(resourceLoader.getImageIconResource("add"));
        deleteBtn = new JButton(resourceLoader.getImageIconResource("delete"));
        editBtn = new JButton(resourceLoader.getImageIconResource("edit"));

        topBox.add(contactField);
        topBox.add(Box.createHorizontalGlue());
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(addBtn);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(deleteBtn);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(editBtn);

        tableModel = new AccauntsTableModel();
        accountsTable = new JTable(tableModel);
        accountsTable.setFont(mainFont);
        accountsTable.setRowHeight(rowHeight);
        accountsTable.setShowVerticalLines(false);
        accountsTable.setGridColor(gridColor);
        accountsTable.getTableHeader().setReorderingAllowed(false);
        accountsTable.getColumnModel().getColumn(0).setMaxWidth(70);
        accountsTable.getColumnModel().getColumn(0).setMinWidth(70);
        accountsTable.setDefaultRenderer(Object.class, new AccountTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(accountsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        statusField = new JTextField();
        statusField.setEditable(false);
        statusField.setFont(mainFont);
        statusField.setHorizontalAlignment(SwingConstants.LEFT);

        contentPane.add(topBox, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(statusField, BorderLayout.SOUTH);
    }

    public JPanel getVisualComponent(){
        return contentPane;
    }

    public void setContact(Contact contact){
        currentContact = contact;
        contactField.setText(currentContact.getName());
        tableModel.refresh(currentContact);
    }

    public void clear(){
        tableModel.clearTable();
        currentContact=null;
        contactField.setText("");
        statusField.setText("");
    }

}

