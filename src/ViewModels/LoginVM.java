package ViewModels;

import Conexion.Consult;
import Library.Objetos;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.apache.commons.dbutils.QueryRunner;

public class LoginVM extends Consult {

    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textField;
    private List<TUsuarios> listUsuarios;

    public LoginVM(ArrayList<JLabel> label, ArrayList<JTextField> textField) {
        _label = label;
        _textField = textField;
    }

    public Object[] Login() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el usuario");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (!Objetos.eventos.isEmail(_textField.get(0).getText())) {
                _label.get(0).setText("Ingrese un email válido");
                _label.get(0).setForeground(Color.RED);
                _label.get(0).requestFocus();
            } else {
                if (_textField.get(1).getText().equals("")) {
                    _label.get(1).setForeground(Color.RED);
                    _label.get(1).setText("Ingrese la contraseña");
                    _textField.get(1).requestFocus();
                } else {
                    listUsuarios = Usuarios().stream().filter(u -> u.getEmail().equals(_textField.get(0).getText())).collect(Collectors.toList());
                    if (!listUsuarios.isEmpty()) {
                        if (listUsuarios.get(0).getPassword().equals(_textField.get(1).getText())) {
                            try{
                                Date date = new Date();
                                final QueryRunner qr = new QueryRunner(true);
                                getConn().setAutoCommit(false);
                            } catch(Exception e) {
                                
                            }
                        } else {
                            _label.get(1).setText("Contraseña incorrecta");
                            _label.get(0).setForeground(Color.RED);
                            _label.get(0).requestFocus();
                        }
                    } else {
                        _label.get(0).setText("El email no está registrado");
                        _label.get(0).setForeground(Color.RED);
                        _label.get(0).requestFocus();
                    }
                }
            }
        }
        return null;
    }
}
