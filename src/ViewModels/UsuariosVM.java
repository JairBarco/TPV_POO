package ViewModels;

import Conexion.Consult;
import Library.*;
import Models.Usuario.*;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import java.sql.SQLException;
import java.util.Date;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author jair_
 */
public class UsuariosVM extends Consult {

    private static TUsuarios _dataUsuario;
    private static JLabel _nombrePerfil;
    private static JLabel _picturePerfil;
    private JLabel _imagePicture;
    private List<JTextField> _textField;
    private List<JLabel> _label;
    private JCheckBox _checkBoxState;
    private JComboBox _comboBoxRoles;
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
        _comboBoxRoles = (JComboBox) objects[4];
        Reset();
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
                                            int count;
                                            var listEmail = Usuarios().stream().filter(u -> u.getEmail().equals(_textField.get(3).getText())).collect(Collectors.toList());
                                            count = listEmail.size();
                                            var listNoId = Usuarios().stream().filter(u -> u.getNoId().equals(_textField.get(0).getText())).collect(Collectors.toList());
                                            count += listNoId.size();
                                            try {
                                                switch (_accion) {
                                                    case "insert":
                                                        if (count == 0) {
                                                            SaveData();
                                                        } else {
                                                            if (!listEmail.isEmpty()) {
                                                                _label.get(3).setText("El Email ya está registrado");
                                                                _label.get(3).setForeground(Color.RED);
                                                                _label.get(3).requestFocus();
                                                            }

                                                            if (!listNoId.isEmpty()) {
                                                                _label.get(0).setText("El No. Id ya está registrado");
                                                                _label.get(0).setForeground(Color.RED);
                                                                _label.get(0).requestFocus();
                                                            }
                                                        }
                                                        break;
                                                }
                                            } catch (Exception e) {
                                                JOptionPane.showMessageDialog(null, e);
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
    }

    private void SaveData() throws SQLException {
        try {
            final QueryRunner qr = new QueryRunner(true);
            getConn().setAutoCommit(false);
            byte[] image = UploadImage.getImageByte();
            if (image == null) {
                image = Objetos.uploadImage.getTransFoto(_imagePicture);
            }

            switch (_accion) {
                case "insert":
                    String sqlUsuario1 = "INSERT INTO tusuarios(NoId,Nombre,Apellido,Direccion,Email" + ",Telefono,Usuario,Password,Role,Imagen,Is_active" +",State,Fecha) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    var rol = (TRoles) _comboBoxRoles.getSelectedItem();
                    Object[] dataUsuario1 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        _textField.get(4).getText(),
                        _textField.get(5).getText(),
                        _textField.get(6).getText(),
                        Encriptar.encrypt(_textField.get(7).getText()),
                        rol.getRol(),
                        image,
                        true,
                        _checkBoxState.isSelected(),
                        new Date()
                    };
                    qr.insert(getConn(), sqlUsuario1, new ColumnListHandler(), dataUsuario1);
                    break;
            }
            getConn().commit();
            Reset();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Reset() {
        String reset[] = {"No. Id", "Nombre", "Apellido", "Email", "Teléfono", "Dirección", "Usuario", "Contraseña"};
        _accion = "insert";
        _textField.forEach(item -> {
            item.setText("");
        });

        for (int i = 0; i < _label.size() - 1; i++) {
            _label.get(i).setText(reset[i]);
            _label.get(i).setForeground(new Color(0, 0, 0));
        }

        _checkBoxState.setSelected(false);
        _checkBoxState.setForeground(new Color(0, 0, 0));
        _imagePicture.setIcon(new ImageIcon(getClass().getClassLoader().getResource("Resources/agregar_imagen.png")));
        getRoles();
    }

    public void getRoles() {
        var model = new DefaultComboBoxModel();
        Roles().forEach(item -> {
            model.addElement(item);
        });
        _comboBoxRoles.setModel(model);
    }
    // </editor-fold>
}
