package phonebook;

import javax.swing.*;

public class MainClass {

    public static DataBaseConnector dataBaseConnector;

    public static void main(String[] args) {
        try {
            dataBaseConnector = new DataBaseConnector();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка при подключении к базе данных", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }

}
