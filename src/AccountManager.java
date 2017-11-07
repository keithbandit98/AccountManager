package accountmanager;

import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Todesca_Samuele
 */
public class AccountManager
{
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
    {
        new Thread(new screen()).start();
        new JForm().setVisible(true);
    }
    
}
