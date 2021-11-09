package proyectotpoo;

import Models.Cajas.TCajas;
import Models.Usuario.TUsuarios;
import ViewModels.LoginVM;
import Views.*;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.util.List;
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
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
        
        var login = new LoginVM();
        Object[] objects = login.Verificar();
        var listUsuario = (List<TUsuarios>) objects[0];
        var caja = (TCajas) objects[1];
        
        if (!listUsuario.isEmpty() && caja !=null) {
            Sistema sys = new Sistema(listUsuario.get(0), caja);
            sys.setVisible(true);
            sys.setExtendedState(MAXIMIZED_BOTH);
        } else {
            Login sistema = new Login();
            //sistema.setExtendedState(MAXIMIZED_BOTH);
            sistema.setVisible(true);
        }
        
    }
}
