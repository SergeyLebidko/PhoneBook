package phonebook.gui_components.account_components;

import phonebook.Account;
import phonebook.AccountTypes;
import phonebook.Contact;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class AccauntsTableModel extends AbstractTableModel {

    private Account[] currentAccounts;
    private AccountComparator accountComparator;

    private class AccountComparator implements Comparator<Account>{

        @Override
        public int compare(Account o1, Account o2) {
            AccountTypes t1 = AccountTypes.valueOf(o1.getType().toUpperCase());
            AccountTypes t2 = AccountTypes.valueOf(o2.getType().toUpperCase());
            Integer p1 = t1.ordinal();
            Integer p2 = t2.ordinal();
            return p1.compareTo(p2);
        }

    }

    public AccauntsTableModel() {
        currentAccounts = new Account[0];
        accountComparator = new AccountComparator();
    }

    public void refresh(Contact contact){
        LinkedList<Account> accountsList = contact.getAccounts();
        currentAccounts = new Account[accountsList.size()];
        currentAccounts = accountsList.toArray(currentAccounts);

        Arrays.sort(currentAccounts, accountComparator);

        fireTableDataChanged();
    }

    public void clearTable(){
        currentAccounts=new Account[0];
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return currentAccounts.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         return currentAccounts[rowIndex];
    }

}
