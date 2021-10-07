package proyectotpoo;

import Views.*;
import static java.awt.Frame.MAXIMIZED_BOTH;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Jair Barco
 */
public class ProyectoTPOO {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {

        }
        Login sistema = new Login();
        //sistema.setExtendedState(MAXIMIZED_BOTH);
        sistema.setVisible(true);
    }
    
}
