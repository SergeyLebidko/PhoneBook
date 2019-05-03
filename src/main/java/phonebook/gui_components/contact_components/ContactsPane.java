package phonebook.gui_components.contact_components;

import phonebook.Contact;
import phonebook.MainClass;
import phonebook.data_access_components.DataBaseConnector;
import phonebook.data_access_components.ResourceLoader;
import phonebook.gui_components.account_components.AccoutsPane;

import static phonebook.gui_components.GUIProperties.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class ContactsPane {

    private JPanel contentPane;
    private DataBaseConnector dataBaseConnector;
    private ResourceLoader resourceLoader;

    private JButton addBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JTextField findField;
    private JButton cleanFindFieldBtn;

    private JTable contactsTable;
    private ContactsTableModel tableModel;
    private AccoutsPane accoutsPane;

    public ContactsPane(AccoutsPane accoutsPane) {
        dataBaseConnector = MainClass.dataBaseConnector;
        resourceLoader = MainClass.resourceLoader;
        this.accoutsPane = accoutsPane;

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Box topBox = Box.createHorizontalBox();

        addBtn = new JButton(resourceLoader.getImageIconResource("add"));
        addBtn.setToolTipText("Добавить контакт");
        deleteBtn = new JButton(resourceLoader.getImageIconResource("delete"));
        deleteBtn.setToolTipText("Удалить контакт");
        editBtn = new JButton(resourceLoader.getImageIconResource("edit"));
        editBtn.setToolTipText("Редактировать контакт");
        findField = new JTextField();
        findField.setMinimumSize(new Dimension(100, 20));
        findField.setFont(mainFont);
        cleanFindFieldBtn = new JButton(resourceLoader.getImageIconResource("clean"));
        cleanFindFieldBtn.setToolTipText("Очистить поле поиска");

        topBox.add(addBtn);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(deleteBtn);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(editBtn);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(findField);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(cleanFindFieldBtn);

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
            JOptionPane.showMessageDialog(null, "Ошибка при получении списка контактов: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        ContactsTableHeaderRenderer headerRenderer = new ContactsTableHeaderRenderer(tableModel);
        contactsTable.getTableHeader().setDefaultRenderer(headerRenderer);

        JScrollPane scrollPane = new JScrollPane(contactsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        contentPane.add(topBox, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        addBtn.addActionListener(addBtnListener);
        deleteBtn.addActionListener(deleteBtdListener);
        editBtn.addActionListener(editBtnListener);
        cleanFindFieldBtn.addActionListener(cleanBtnListener);
        contactsTable.addMouseListener(tableMouseListener);
        contactsTable.getTableHeader().addMouseListener(headerClickListener);
        findField.addKeyListener(findFieldListener);
    }

    public JPanel getVisualComponent() {
        return contentPane;
    }

    private ActionListener addBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = getContactName("");
            if (name == null) return;

            //Пробуем добавить контакт в БД
            try {
                dataBaseConnector.addContact(name);
            } catch (Exception ex) {
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
                JOptionPane.showMessageDialog(null, "Ошибка при получении списка контактов: " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    };

    private ActionListener deleteBtdListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[] selectedRows = contactsTable.getSelectedRows();
            int rowsCount = selectedRows.length;
            if (rowsCount == 0) return;

            //Получаем объекты Contact для выделенных строк
            Contact[] contacts = new Contact[rowsCount];
            for (int i = 0; i < rowsCount; i++) {
                contacts[i] = (Contact) tableModel.getValueAt(selectedRows[i], 0);
            }

            //Последовательно удаляем контакты
            for (Contact contact : contacts) {
                try {
                    dataBaseConnector.deleteContact(contact);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка при удалении контакта " + contact.getName() + ": " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                }
            }

            //Обновляем панель контактов после удаления
            try {
                tableModel.setContent(dataBaseConnector.loadContacts());
                tableModel.refresh();
                accoutsPane.clear();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ошибка при получении списка контактов: " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    };

    private ActionListener editBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Получаем список выделенных строк
            int[] selectedRows = contactsTable.getSelectedRows();
            int rowsCount = selectedRows.length;
            if (rowsCount != 1) return;

            //Получаем новое имя для контакта
            Contact selectedContact = (Contact) tableModel.getValueAt(selectedRows[0], 0);
            String name;
            name = getContactName(selectedContact.getName());
            if (name==null)return;

            //Пытаемся записать новое имя в базу данных
            selectedContact.setName(name);
            try {
                dataBaseConnector.updateContact(selectedContact);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Не удалось изменить контакт " + ex.getMessage(), "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //Если контакт успешно обновлен - обновляем и панель контактов
            try {
                tableModel.setContent(dataBaseConnector.loadContacts());
                findField.setText(name);
                tableModel.setFilter(name);
                tableModel.refresh();
                selectedContact.setName(name);
                accoutsPane.setContact(selectedContact);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ошибка при получении списка контактов: " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    };

    private ActionListener cleanBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filter = findField.getText();
            if (filter.equals("")) return;
            findField.setText("");
            tableModel.setFilter("");
            tableModel.refresh();
        }
    };

    private MouseListener tableMouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) return;

            //Получаем список выделенных строк
            int[] selectedRows = contactsTable.getSelectedRows();
            int rowsCount = selectedRows.length;
            if (rowsCount == 0) return;

            //Если выделен один контакт - показать сведения о нем
            if (rowsCount == 1) {
                accoutsPane.setContact((Contact) tableModel.getValueAt(selectedRows[0], 0));
                return;
            }

            //Если выделено несколько - очистить панель аккаунтов
            if (rowsCount > 1) {
                accoutsPane.clear();
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

    private String getContactName(String startValue) {
        String deniedChars = "_%*\"?'";
        boolean findDeniedChar;
        String name;

        do {
            name = JOptionPane.showInputDialog(null, "Введите имя", startValue);
            if (name == null) return null;
            name = name.trim();
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Имя не может быть пустым", "", JOptionPane.INFORMATION_MESSAGE);
                continue;
            }
            findDeniedChar = false;
            for (char c : name.toCharArray()) {
                if (deniedChars.indexOf(c) != (-1)) {
                    findDeniedChar = true;
                    break;
                }
            }
            if (findDeniedChar) {
                JOptionPane.showMessageDialog(null, "Имя не может содержать символы " + deniedChars, "", JOptionPane.INFORMATION_MESSAGE);
                continue;
            }
            break;
        } while (true);

        return name;
    }

}
