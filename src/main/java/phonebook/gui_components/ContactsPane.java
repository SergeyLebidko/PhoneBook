package phonebook.gui_components;

import phonebook.Contact;
import phonebook.MainClass;
import phonebook.data_access_components.DataBaseConnector;
import phonebook.data_access_components.ResourceLoader;

import static phonebook.gui_components.GUIProperties.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class ContactsPane {

    private JPanel contentPane;
    private DataBaseConnector dataBaseConnector;

    private JButton addBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JTextField findField;

    private JTable contactsTable;
    private ContactsTableModel tableModel;

    public ContactsPane() {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ResourceLoader resourceLoader = MainClass.resourceLoader;

        Box topBox = Box.createHorizontalBox();

        addBtn = new JButton(resourceLoader.getImageIconResource("add"));
        deleteBtn = new JButton(resourceLoader.getImageIconResource("delete"));
        editBtn = new JButton(resourceLoader.getImageIconResource("edit"));
        findField = new JTextField();
        findField.setMinimumSize(new Dimension(100, 20));
        findField.setFont(mainFont);

        topBox.add(addBtn);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(deleteBtn);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(editBtn);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(findField);

        dataBaseConnector = MainClass.dataBaseConnector;

        tableModel = new ContactsTableModel();
        contactsTable = new JTable(tableModel);
        contactsTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        contactsTable.setFont(mainFont);
        contactsTable.setRowHeight(rowHeight);
        contactsTable.setShowVerticalLines(false);
        contactsTable.setGridColor(gridColor);
        contactsTable.getTableHeader().setReorderingAllowed(false);
        try {
            tableModel.setContent(dataBaseConnector.loadContacts());
            tableModel.setSortOrder(ContactsTableModel.SORT_ORDER_TO_UP);
            tableModel.setFilter("");
            tableModel.refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при получении списка контактов: "+e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        ContactsTableHeaderRenderer headerRenderer = new ContactsTableHeaderRenderer(tableModel);
        contactsTable.getTableHeader().setDefaultRenderer(headerRenderer);

        JScrollPane scrollPane = new JScrollPane(contactsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        contentPane.add(topBox, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        addBtn.addActionListener(addBtnListener);
        contactsTable.getTableHeader().addMouseListener(headerClickListener);
        findField.addKeyListener(findFieldListener);
    }

    public JPanel getVisualComponent() {
        return contentPane;
    }

    private ActionListener addBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String deniedChars = "_%*?\"'";
            boolean findDeniedChar;
            String name;

            //Получаем от пользователя имя создаваемого контакта
            do {
                name = JOptionPane.showInputDialog(null, "Введите имя", "");
                if (name == null) return;
                name = name.trim();
                if (name.equals("")){
                    JOptionPane.showMessageDialog(null, "Имя не может быть пустым", "", JOptionPane.INFORMATION_MESSAGE);
                    continue;
                }
                findDeniedChar=false;
                for (char c : name.toCharArray()) {
                    if (deniedChars.indexOf(c)!=(-1)){
                        findDeniedChar=true;
                        break;
                    }
                }
                if (findDeniedChar){
                    JOptionPane.showMessageDialog(null, "Имя не может содержать символы "+deniedChars, "", JOptionPane.INFORMATION_MESSAGE);
                    continue;
                }
                break;
            } while (true);

            //Пробуем добавить контакт в БД
            try {
                dataBaseConnector.addContact(name);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Не удалось добавить контакт в базу данных", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //Если контакт успешно добавлен - обновляем панель контактов
            try {
                tableModel.setContent(dataBaseConnector.loadContacts());
                findField.setText(name);
                tableModel.setFilter(name);
                tableModel.refresh();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ошибка при получении списка контактов: "+ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    };

    private MouseListener headerClickListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1 & e.getButton() == MouseEvent.BUTTON1) {
                tableModel.revertSortOrder();
                tableModel.refresh();
                contactsTable.getTableHeader().repaint();
            }
        }

    };

    private KeyListener findFieldListener = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String filter = findField.getText();
            tableModel.setFilter(filter);
            tableModel.refresh();
        }
    };

}
