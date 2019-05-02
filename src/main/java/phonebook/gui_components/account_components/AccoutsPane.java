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
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
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

    private JPopupMenu addAccountsMenu;
    private JMenuItem addPhoneAccount;
    private JMenuItem addMailAccount;
    private JMenuItem addFacebookAccount;
    private JMenuItem addInstagramAccount;
    private JMenuItem addOkAccount;
    private JMenuItem addTelegramAccount;
    private JMenuItem addTwitterAccount;
    private JMenuItem addVkAccount;

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

        addAccountsMenu = new JPopupMenu();

        addPhoneAccount = new JMenuItem("Номер телефона", resourceLoader.getImageIconResource(PHONE.getType()));
        addPhoneAccount.setActionCommand(PHONE.getType());

        addMailAccount = new JMenuItem("Элетронная почта", resourceLoader.getImageIconResource(MAIL.getType()));
        addMailAccount.setActionCommand(MAIL.getType());

        addFacebookAccount = new JMenuItem("Facebook", resourceLoader.getImageIconResource(FACEBOOK.getType()));
        addFacebookAccount.setActionCommand(FACEBOOK.getType());

        addInstagramAccount = new JMenuItem("Instagram", resourceLoader.getImageIconResource(INSTAGRAM.getType()));
        addInstagramAccount.setActionCommand(INSTAGRAM.getType());

        addOkAccount = new JMenuItem("Одноклассники", resourceLoader.getImageIconResource(OK.getType()));
        addOkAccount.setActionCommand(OK.getType());

        addTelegramAccount = new JMenuItem("Telegram", resourceLoader.getImageIconResource(TELEGRAM.getType()));
        addTelegramAccount.setActionCommand(TELEGRAM.getType());

        addTwitterAccount = new JMenuItem("Twitter", resourceLoader.getImageIconResource(TWITTER.getType()));
        addTwitterAccount.setActionCommand(TWITTER.getType());

        addVkAccount = new JMenuItem("ВКонтакте", resourceLoader.getImageIconResource(VK.getType()));
        addVkAccount.setActionCommand(VK.getType());

        addAccountsMenu.add(addPhoneAccount);
        addAccountsMenu.add(addMailAccount);
        addAccountsMenu.add(addFacebookAccount);
        addAccountsMenu.add(addInstagramAccount);
        addAccountsMenu.add(addOkAccount);
        addAccountsMenu.add(addTelegramAccount);
        addAccountsMenu.add(addTwitterAccount);
        addAccountsMenu.add(addVkAccount);

        addBtn.addMouseListener(addBtnListner);
        editBtn.addActionListener(editBtnListener);
        addPhoneAccount.addActionListener(addAccountListener);
        addMailAccount.addActionListener(addAccountListener);
        addFacebookAccount.addActionListener(addAccountListener);
        addInstagramAccount.addActionListener(addAccountListener);
        addOkAccount.addActionListener(addAccountListener);
        addTelegramAccount.addActionListener(addAccountListener);
        addTwitterAccount.addActionListener(addAccountListener);
        addVkAccount.addActionListener(addAccountListener);

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
                addAccountsMenu.show(addBtn, e.getX(), e.getY());
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
            Account account = new Account( contact_id, type, protocol, address, accountName);

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

    private ActionListener editBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = accountsTable.getSelectedRow();
            if (selectedRow==(-1))return;

            //Получаем новое имя аккаунта
            Account account = (Account) tableModel.getValueAt(selectedRow, 0);
            String oldName = account.getAccount();
            String newName = getAccountName(account.getType(), account.getAccount());
            if (newName==null)return;

            //Пытаемся записать его в базу данных
            account.setAccount(newName);
            try {
                dataBaseConnector.changeAccount(currentContact.getId(), account);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Не удалось изменить аккаунт", "", JOptionPane.INFORMATION_MESSAGE);
                account.setAccount(oldName);
                return;
            }

            //Отображаем изменения
            tableModel.refresh(currentContact);
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
        }else{
            title = "Введите аккаунт " + accountType;
        }

        //Создаем дополнительную текстовую надпись для окна ввода
        JLabel accountLabel = new JLabel();
        if (!accountType.equals(PHONE.getType()) & !accountType.equals(MAIL.getType())){
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
            if (!accountType.equals(PHONE.getType())){
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

        //Формируем ответ
        return answerValue;
    }

}

