package phonebook;

import phonebook.data_access_components.DataBaseConnector;
import phonebook.data_access_components.ResourceLoader;

import javax.swing.*;
import java.io.IOException;

public class MainClass {

    public static DataBaseConnector dataBaseConnector;

    public static ResourceLoader resourceLoader;

    public static GUI gui;

    public static void main(String[] args) {
        //Создаем подключение к базе данных
        try {
            dataBaseConnector = new DataBaseConnector();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка при подключении к базе данных", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Создаем поставщик ресурсов и загружаем ресурсы (используемые приложением иконки)
        resourceLoader = new ResourceLoader();
        try {
            resourceLoader.loadResource();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка при загрузке ресурсов", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Запускаем графический интерфейс
        gui = new GUI();
    }

}
