package phonebook.gui_components;

import phonebook.Contact;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ContactsTableModel extends AbstractTableModel {

    public static final int SORT_ORDER_TO_UP = 1;
    public static final int SORT_ORDER_TO_DOWN = -1;

    private int currentSortOrder;
    private String currentFilter;
    private Contact[] currentContent;
    private ContactComparator contactComparator;

    private ArrayList<Contact> rowValues;

    private class ContactComparator implements Comparator<Contact> {

        @Override
        public int compare(Contact o1, Contact o2) {
            String name1 = o1.getName();
            String name2 = o2.getName();
            return currentSortOrder * name1.compareTo(name2);
        }

    }

    public ContactsTableModel() {
        currentSortOrder = SORT_ORDER_TO_UP;
        currentFilter = "";
        rowValues = new ArrayList<>();
        currentContent = new Contact[0];
        contactComparator = new ContactComparator();
    }

    public void refresh() {
        //Сортируем контент
        Arrays.sort(currentContent, contactComparator);

        //Отбираем контакты для отображения
        rowValues.clear();
        String name;
        for (Contact contact: currentContent){
            if (!currentFilter.equals("")){
                name = contact.getName();
                if (name.toLowerCase().indexOf(currentFilter)==(-1))continue;
            }
            rowValues.add(contact);
        }

        //Оповещаем слушателей об изменении в модели
        fireTableDataChanged();
    }

    public void setContent(Contact[] content) {
        currentContent = content;
    }

    public void setFilter(String filter) {
        currentFilter = filter.toLowerCase().trim();
    }

    public void setSortOrder(int sortOrder) {
        currentSortOrder = sortOrder;
    }

    public void revertSortOrder(){
        if (currentSortOrder==SORT_ORDER_TO_UP){
            currentSortOrder=SORT_ORDER_TO_DOWN;
            return;
        }
        if (currentSortOrder==SORT_ORDER_TO_DOWN){
            currentSortOrder=SORT_ORDER_TO_UP;
            return;
        }
    }

    public int getCurrentSortOrder(){
        return currentSortOrder;
    }

    @Override
    public int getRowCount() {
        return rowValues.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowValues.get(rowIndex);
    }
}
