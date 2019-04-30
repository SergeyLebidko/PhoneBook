package phonebook;

import phonebook.data_access_components.ResourceLoader;
import phonebook.gui_components.contact_components.ContactsPane;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final int WIDTH_FRM = 1200;
    private final int HEIGHT_FRM = 800;

    private ResourceLoader resourceLoader;

    private JFrame frm;

    public GUI() {
        //Получаем загрузчик ресурсов
        resourceLoader = MainClass.resourceLoader;

        //Русифицируем некоторые надписи в диалоговых окнах
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.inputDialogTitle", "");

        //Создаем главное окно
        frm = new JFrame("PhoneBook");
        frm.setIconImage(resourceLoader.getImageResource("logo"));
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(WIDTH_FRM, HEIGHT_FRM);
        frm.setMinimumSize(new Dimension((int)(WIDTH_FRM*0.8), (int)(HEIGHT_FRM*0.8)));
        int xPos = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WIDTH_FRM / 2;
        int yPos = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - HEIGHT_FRM / 2;
        frm.setLocation(xPos, yPos);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        ContactsPane contactsPane = new ContactsPane();
        splitPane.add(contactsPane.getVisualComponent());
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setResizeWeight(0.5);

        splitPane.add(new ContactsPane().getVisualComponent());

        contentPane.add(splitPane, BorderLayout.CENTER);
        frm.setContentPane(contentPane);
        frm.setVisible(true);
    }

}
