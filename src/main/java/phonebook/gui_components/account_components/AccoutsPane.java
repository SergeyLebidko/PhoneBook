package phonebook.gui_components.account_components;

import phonebook.Account;
import phonebook.AccountTypes;
import phonebook.Contact;
import phonebook.MainClass;
import phonebook.data_access_components.DataBaseConnector;
import phonebook.data_access_components.ResourceLoader;

import static phonebook.gui_components.GUIProperties.*;
import static phonebook.AccountTypes.*;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

public class AccoutsPane {

    private JPanel contentPane;
    private DataBaseConnector dataBaseConnector;
    private ResourceLoader resourceLoader;

    private JTextField contactField;
    private JButton addBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JTextField statusField;

    private JPopupMenu createAccountsMenu;
    private JMenuItem createPhoneAccount;
    private JMenuItem createMailAccount;
    private JMenuItem createFacebookAccount;
    private JMenuItem createInstagramAccount;
    private JMenuItem createOkAccount;
    private JMenuItem createTelegramAccount;
    private JMenuItem createTwitterAccount;
    private JMenuItem createVkAccount;

    private Contact currentContact;

    private JTable accountsTable;
    private AccountsTableModel tableModel;

    public AccoutsPane() {
        dataBaseConnector = MainClass.dataBaseConnector;
        resourceLoader = MainClass.resourceLoader;

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Box topBox = Box.createHorizontalBox();

        contactField = new JTextField();
        contactField.setFont(mainFont);
        contactField.setEditable(false);
        contactField.setMinimumSize(new Dimension(100, 20));

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

        tableModel = new AccountsTableModel();
        accountsTable = new JTable(tableModel);
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        createAccountsMenu = new JPopupMenu();

        createPhoneAccount = new JMenuItem("Номер телефона", resourceLoader.getImageIconResource(PHONE.getType()));
        createPhoneAccount.setActionCommand(PHONE.getType());

        createMailAccount = new JMenuItem("Элетронная почта", resourceLoader.getImageIconResource(MAIL.getType()));
        createMailAccount.setActionCommand(MAIL.getType());

        createFacebookAccount = new JMenuItem("Facebook", resourceLoader.getImageIconResource(FACEBOOK.getType()));
        createFacebookAccount.setActionCommand(FACEBOOK.getType());

        createInstagramAccount = new JMenuItem("Instagram", resourceLoader.getImageIconResource(INSTAGRAM.getType()));
        createInstagramAccount.setActionCommand(INSTAGRAM.getType());

        createOkAccount = new JMenuItem("Одноклассники", resourceLoader.getImageIconResource(OK.getType()));
        createOkAccount.setActionCommand(OK.getType());

        createTelegramAccount = new JMenuItem("Telegram", resourceLoader.getImageIconResource(TELEGRAM.getType()));
        createTelegramAccount.setActionCommand(TELEGRAM.getType());

        createTwitterAccount = new JMenuItem("Twitter", resourceLoader.getImageIconResource(TWITTER.getType()));
        createTwitterAccount.setActionCommand(TWITTER.getType());

        createVkAccount = new JMenuItem("ВКонтакте", resourceLoader.getImageIconResource(VK.getType()));
        createVkAccount.setActionCommand(VK.getType());

        createAccountsMenu.add(createPhoneAccount);
        createAccountsMenu.add(createMailAccount);
        createAccountsMenu.add(createFacebookAccount);
        createAccountsMenu.add(createInstagramAccount);
        createAccountsMenu.add(createOkAccount);
        createAccountsMenu.add(createTelegramAccount);
        createAccountsMenu.add(createTwitterAccount);
        createAccountsMenu.add(createVkAccount);

        addBtn.addMouseListener(addBtnListner);
        deleteBtn.addActionListener(deleteBtnListener);
        editBtn.addActionListener(editBtnListener);
        accountsTable.addMouseListener(tableMouseListener);
        createPhoneAccount.addActionListener(addAccountListener);
        createMailAccount.addActionListener(addAccountListener);
        createFacebookAccount.addActionListener(addAccountListener);
        createInstagramAccount.addActionListener(addAccountListener);
        createOkAccount.addActionListener(addAccountListener);
        createTelegramAccount.addActionListener(addAccountListener);
        createTwitterAccount.addActionListener(addAccountListener);
        createVkAccount.addActionListener(addAccountListener);

        contentPane.add(topBox, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(statusField, BorderLayout.SOUTH);
    }

    public JPanel getVisualComponent() {
        return contentPane;
    }

    public void setContact(Contact contact) {
        currentContact = contact;
        contactField.setText(currentContact.getName());
        tableModel.refresh(currentContact);
        statusField.setText("");
    }

    public void clear() {
        tableModel.clearTable();
        currentContact = null;
        contactField.setText("");
        statusField.setText("");
    }

    private MouseListener addBtnListner = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (currentContact == null) return;
                createAccountsMenu.show(addBtn, e.getX(), e.getY());
            }
        }
    };

    private ActionListener addAccountListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String accountType = e.getActionCommand();
            String accountName = getAccountName(accountType, "");
            if (accountName == null) return;

            int contact_id = currentContact.getId();
            String type = AccountTypes.valueOf(accountType.toUpperCase()).getType();
            String protocol = AccountTypes.valueOf(accountType.toUpperCase()).getProtocol();
            String address = AccountTypes.valueOf(accountType.toUpperCase()).getAddress();
            Account account = new Account(contact_id, type, protocol, address, accountName);

            //Добавляем созданный аккаунт в базу данных
            try {
                dataBaseConnector.addAccount(account);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Не удалось добавить аккаунт", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //Если аккаунт успешно добавлен, то обновляем список аккаунтов данного пользователя
            currentContact.addAccount(account);
            tableModel.refresh(currentContact);
        }
    };

    private ActionListener deleteBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = accountsTable.getSelectedRow();
            if (selectedRow == (-1)) return;

            //Пытаемся удалить выбранный аккаунт
            Account account = (Account) tableModel.getValueAt(selectedRow, 0);
            try {
                dataBaseConnector.deleteAccount(account);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Не удалось удалить аккаунт", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //Если удаление прошло успешно, то отображаем изменения на экране
            currentContact.removeAccount(account);
            tableModel.refresh(currentContact);
        }
    };

    private ActionListener editBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = accountsTable.getSelectedRow();
            if (selectedRow == (-1)) return;

            //Получаем новое имя аккаунта
            Account account = (Account) tableModel.getValueAt(selectedRow, 0);
            String oldName = account.getAccountName();
            String newName = getAccountName(account.getType(), account.getAccountName());
            if (newName == null) return;

            //Пытаемся записать его в базу данных
            account.setAccountName(newName);
            try {
                dataBaseConnector.updateAccount(account);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Не удалось изменить аккаунт", "", JOptionPane.INFORMATION_MESSAGE);
                account.setAccountName(oldName);
                return;
            }

            //Отображаем изменения
            tableModel.refresh(currentContact);
        }
    };

    private MouseListener tableMouseListener = new MouseAdapter() {

        @Override
        public void mouseReleased(MouseEvent e) {
            int selectedRow;
            selectedRow = accountsTable.getSelectedRow();
            Account account = (Account) tableModel.getValueAt(selectedRow, 0);
            String accountPath = account.getProtocol() + account.getAddress() + account.getAccountName();

            if (account.getType().equals(PHONE.getType()) | account.getType().equals(MAIL.getType())) {
                statusField.setText(account.getAccountName());
                return;
            }
            statusField.setText(accountPath);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int selectedRow;
            selectedRow = accountsTable.getSelectedRow();
            Account account = (Account) tableModel.getValueAt(selectedRow, 0);
            String accountPath = account.getProtocol() + account.getAddress() + account.getAccountName();

            //Если двойной щелчёк - открываем аккаунт (за исключением телефонных номеров и адресов электронной почты)
            if (e.getClickCount() == 2 & e.getButton() == MouseEvent.BUTTON1) {
                if (account.getType().equals(PHONE.getType())) return;
                try {
                    URI accountURI = new URI(accountPath);
                    if (account.getType().equals(MAIL.getType())){
                        Desktop.getDesktop().mail(accountURI);
                        return;
                    }
                    Desktop.getDesktop().browse(accountURI);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Не удалось открыть "+accountPath, "", JOptionPane.INFORMATION_MESSAGE);
                } catch (URISyntaxException e1) {
                    System.out.println("Ошибка синтаксиса URI");
                }
                return;
            }
        }

    };

    private String getAccountName(String accountType, String startValue) {
        //Создаем поле ввода данных. Если вводится номер телефона - применяем маску
        JFormattedTextField inputField;
        if (accountType.equals(PHONE.getType())) {
            MaskFormatter mask = null;
            try {
                mask = new MaskFormatter("+#(###)##-##-###");
            } catch (ParseException ex) {
            }
            inputField = new JFormattedTextField(mask);
            inputField.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            inputField = new JFormattedTextField();
            inputField.setHorizontalAlignment(SwingConstants.LEFT);
        }
        Dimension preferredDim = inputField.getPreferredSize();
        inputField.setPreferredSize(new Dimension(200, preferredDim.height));

        //Создаем заголовок окна ввода данных
        String title = null;
        if (accountType.equals(PHONE.getType()) | accountType.equals(MAIL.getType())) {
            if (accountType.equals(PHONE.getType())) {
                title = "Введите номер телефона";
            }
            if (accountType.equals(MAIL.getType())) {
                title = "Введите адрес электронной почты";
            }
        } else {
            title = "Введите аккаунт " + accountType;
        }

        //Создаем дополнительную текстовую надпись для окна ввода
        JLabel accountLabel = new JLabel();
        if (!accountType.equals(PHONE.getType()) & !accountType.equals(MAIL.getType())) {
            accountLabel.setText(AccountTypes.valueOf(accountType.toUpperCase()).getAddress());
        }

        //Получаем иконку для панели ввода
        ImageIcon icon = resourceLoader.getImageIconResource(accountType);

        //Создаем диалоговую панель
        Box dialogPane = Box.createHorizontalBox();

        //Добавляем компоненты на диалоговую панель
        dialogPane.add(accountLabel);
        dialogPane.add(Box.createHorizontalStrut(10));
        dialogPane.add(inputField);

        //Получаем ответ от пользователя
        int answerCode;
        String answerValue = "";
        while (true) {

            //Если мы запрашиваем не номер телефона, то по-умолчанию в поле ввода выводим старое значение
            if (!accountType.equals(PHONE.getType())) {
                inputField.setText(startValue);
            }

            //Запрашиваем данные от пользователя
            answerCode = JOptionPane.showConfirmDialog(null, dialogPane, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);

            //Если пользователь отказался от ввода - возвращаем null
            if (answerCode != 0) return null;

            //Если вводили номер телефона
            if (accountType.equals(PHONE.getType())) {
                answerValue = (String) inputField.getValue();
                if (answerValue == null) {
                    JOptionPane.showMessageDialog(null, "Введите корректный номер телефона", "", JOptionPane.INFORMATION_MESSAGE);
                    continue;
                }
            }

            //Проверяем корректность введенных данных
            answerValue = inputField.getText();
            answerValue = answerValue.trim();
            if (answerValue.equals("")) {
                JOptionPane.showMessageDialog(null, "Имя аккаунта не может быть пустым", "", JOptionPane.INFORMATION_MESSAGE);
                continue;
            }

            break;
        }

        return answerValue;
    }

}

