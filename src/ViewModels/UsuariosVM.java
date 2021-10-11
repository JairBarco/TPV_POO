package ViewModels;

import Library.Objetos;
import Library.UploadImage;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author jair_
 */
public class UsuariosVM {

    private static TUsuarios _dataUsuario;
    private static JLabel _nombrePerfil;
    private static JLabel _picturePerfil;
    private JLabel _imagePicture;
    private List<JTextField> _textField;
    private List<JLabel> _label;
    private JCheckBox _checkBoxState;
    private JComboBox _comboBoxRole;
    private JTable _tableUser;
    private JSpinner _spinnerPaginas;
    private String _accion = "insert";
    private UploadImage _uploadImage = new UploadImage();

    public UsuariosVM(TUsuarios dataUsuario, Object[] perfil) {
        _dataUsuario = dataUsuario;
        _nombrePerfil = (JLabel) perfil[0];
        _picturePerfil = (JLabel) perfil[1];
        Perfil();
    }

    public UsuariosVM(Object[] objects, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _imagePicture = (JLabel) objects[0];
        _checkBoxState = (JCheckBox) objects[1];
        _tableUser = (JTable) objects[2];
        _spinnerPaginas = (JSpinner) objects[3];
        _comboBoxRole = (JComboBox) objects[4];
    }

    private void Perfil() {
        _nombrePerfil.setText(_dataUsuario.getNombre());
        if (null != _dataUsuario.getImagen()) {
            _uploadImage.byteImage(_picturePerfil, _dataUsuario.getImagen());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE REGISTRO DE USUARIOS">
    public void RegistrarUsuario() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el No. Id");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (_textField.get(1).getText().equals("")) {
                _label.get(1).setText("Ingrese el nombre");
                _label.get(1).setForeground(Color.RED);
                _textField.get(1).requestFocus();
            } else {
                if (_textField.get(2).getText().equals("")) {
                    _label.get(2).setText("Ingrese el apellido");
                    _label.get(2).setForeground(Color.RED);
                    _textField.get(2).requestFocus();
                } else {
                    if (_textField.get(3).getText().equals("")) {
                        _label.get(3).setText("Ingrese el Email");
                        _label.get(3).setForeground(Color.RED);
                        _textField.get(3).requestFocus();
                    } else {
                        if (!Objetos.eventos.isEmail(_textField.get(3).getText())) {
                            _label.get(3).setText("Ingrese un email valido");
                            _label.get(3).setForeground(Color.RED);
                            _textField.get(3).requestFocus();
                        } else {
                            if (_textField.get(4).getText().equals("")) {
                                _label.get(4).setText("Ingrese el telefono");
                                _label.get(4).setForeground(Color.RED);
                                _textField.get(4).requestFocus();
                            } else {
                                if (_textField.get(5).getText().equals("")) {
                                    _label.get(5).setText("Ingrese la dirección");
                                    _label.get(5).setForeground(Color.RED);
                                    _textField.get(5).requestFocus();
                                } else {
                                    if (_textField.get(6).getText().equals("")) {
                                        _label.get(6).setText("Ingrese el usuario");
                                        _label.get(6).setForeground(Color.RED);
                                        _textField.get(6).requestFocus();
                                    } else {
                                        if (_textField.get(7).getText().equals("")) {
                                            _label.get(7).setText("Ingrese la contraseña");
                                            _label.get(7).setForeground(Color.RED);
                                            _textField.get(7).requestFocus();
                                        } else {

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void Reset() {
        String reset[] = {"No. Id", "Nombre", "Apellido", "Email", "Teléfono", "Dirección", "Usuario", "Contraseña"};

        _textField.forEach(item -> {
            item.setText("");
        });

        for (int i = 0; i < _label.size(); i++) {
            _label.get(i).setText(reset[i]);
            _label.get(i).setForeground(new Color(0,0,0));
        }
        
        _checkBoxState.setSelected(false);
        _checkBoxState.setForeground(new Color(0,0,0));
        _imagePicture.setIcon(new ImageIcon(getClass().getClassLoader().getResource("Resources/agregar-imagen.png")));
    }
    // </editor-fold>
}
