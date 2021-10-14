package ViewModels;

import Conexion.Consult;
import Library.*;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class LoginVM extends Consult {

    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textField;
    private List<TUsuarios> listUsuarios;

    public LoginVM(ArrayList<JLabel> label, ArrayList<JTextField> textField) {
        _label = label;
        _textField = textField;
    }

    public LoginVM() {

    }

    public Object[] Login() throws SQLException {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el email");
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
                        try {
                            if (listUsuarios.get(0).isState()) {
                                var password = Encriptar.decrypt(listUsuarios.get(0).getPassword());
                                if (password.equals(_textField.get(1).getText())) {
                                    Date date = new Date();
                                    String hdd = Ordenador.getSerialNumber('c');
                                    final QueryRunner qr = new QueryRunner(true);
                                    getConn().setAutoCommit(false);
                                    Object[] usuario = {true};
                                    String sql1 = "UPDATE tusuarios SET Is_active = ? "
                                            + "WHERE IdUsuario = " + listUsuarios.get(0).getIdUsuario();
                                    qr.update(getConn(), sql1, usuario);
                                    var dataOrdenador = Ordenadores().stream().filter(o -> o.getOrdenador().equals(hdd)).collect(Collectors.toList());
                                    if (dataOrdenador.isEmpty()) {
                                        String sql2 = "INSERT INTO tordenadores " + "(Ordenador,Is_active,Usuario,InFecha,IdUsuario)" + " VALUES (?,?,?,?,?)";
                                        Object[] ordenador = {
                                            hdd,
                                            true,
                                            listUsuarios.get(0).getEmail(),
                                            date,
                                            listUsuarios.get(0).getIdUsuario()
                                        };
                                        qr.insert(getConn(), sql2, new ColumnListHandler(), ordenador);
                                    } else {
                                        Object[] ordenador = {true, listUsuarios.get(0).getEmail(), date};
                                        String sql3 = "UPDATE tordenadores SET Is_active = ?,Usuario = ?,InFecha = ? WHERE IdOrdenador = " + dataOrdenador.get(0).getIdOrdenador();
                                        qr.update(getConn(), sql3, ordenador);
                                    }
                                    getConn().commit();

                                } else {
                                    _label.get(1).setText("Contraseña incorrecta");
                                    _label.get(1).setForeground(Color.RED);
                                    _label.get(1).requestFocus();
                                }
                            } else {
                                listUsuarios.clear();
                                JOptionPane.showConfirmDialog(null, "El usuario no está disponible", "Estado", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception e) {
                            getConn().rollback();
                            System.out.println("Error en LoginVM: " + e);
                        }
                    } else {
                        _label.get(0).setText("El email no está registrado");
                        _label.get(0).setForeground(Color.RED);
                        _label.get(0).requestFocus();
                    }
                }
            }
        }
        Object[] objects = {listUsuarios};
        return objects;
    }

    public Object[] Verificar() {
        listUsuarios = new ArrayList<>();
        try {
            var hdd = Ordenador.getSerialNumber('c');
            var dataOrdenador = Ordenadores().stream().filter(o -> o.getOrdenador().equals(hdd) && o.isIs_active() == true).collect(Collectors.toList());
            if (!dataOrdenador.isEmpty()) {
                listUsuarios = Usuarios().stream().filter(u -> u.getEmail().equals(dataOrdenador.get(0).getUsuario())).collect(Collectors.toList());
            }
        } catch (Exception e) {
        }
        Object[] objects = {listUsuarios};
        return objects;
    }

    public void Close() throws SQLException {
        listUsuarios = new ArrayList<>();
        final QueryRunner qr = new QueryRunner(true);
        getConn().setAutoCommit(false);

        try {
            Date date = new Date();
            var hdd = Ordenador.getSerialNumber('c');
            var dataOrdenador = Ordenadores().stream().filter(o -> o.getOrdenador().equals(hdd)).collect(Collectors.toList());
            listUsuarios = Usuarios().stream().filter(u -> u.getEmail().equals(dataOrdenador.get(0).getUsuario())).collect(Collectors.toList());
            Object[] usuarios = {false};
            String sql1 = "UPDATE tusuarios SET Is_active = ? " + "WHERE IdUsuario = " + listUsuarios.get(0).getIdUsuario();
            qr.update(getConn(), sql1, usuarios);

            Object[] ordenadores = {false, date};
            String sql2 = "UPDATE tordenadores SET Is_active = ?," + "OutFecha = ? WHERE IdOrdenador = " + dataOrdenador.get(0).getIdOrdenador();
            qr.update(getConn(), sql2, ordenadores);
            getConn().commit();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
