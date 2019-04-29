package phonebook;

import phonebook.dataaccesscomponents.DataBaseConnector;
import phonebook.dataaccesscomponents.ResourceLoader;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final int WIDTH_FRM = 800;
    private final int HEIGHT_FRM = 900;

    private ResourceLoader resourceLoader;

    private JFrame frm;

    public GUI() {
        //Переключаем стиль интерфейса на системный
        String laf = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(laf);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "Возникла ошибка при попытке переключить стиль интерфейса. Работа программы будет прекращена", "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        //Получаем загрузчик ресурсов
        resourceLoader = MainClass.resourceLoader;

        //Получаем объект доступа к БД
        DataBaseConnector dataBaseConnector = MainClass.dataBaseConnector;

        try {
            dataBaseConnector.loadContacts();
        } catch (Exception e) {
            System.out.println("Не удалось загрузить контакты: "+e.getMessage());
        }

        //Создаем главное окно
        frm = new JFrame("PhoneBook");
        frm.setIconImage(resourceLoader.getImageResource("logo"));
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(WIDTH_FRM, HEIGHT_FRM);
        int xPos = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WIDTH_FRM / 2;
        int yPos = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - HEIGHT_FRM / 2;
        frm.setLocation(xPos, yPos);

        frm.setVisible(true);
    }

}
