package ViewModels;

import Conexion.Consult;
import Library.Calendario;
import Library.Encriptar;
import Library.Objetos;
import Library.Ordenador;
import Models.Cajas.TCajas;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author jair_
 */
public class LoginVM extends Consult {

    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textField;
    private List<TUsuarios> listUsuarios;
    private static TCajas caja = null;
    private Calendario cal;
    private SimpleDateFormat formateador;

    public LoginVM(ArrayList<JLabel> label, ArrayList<JTextField> textField) {
        _label = label;
        _textField = textField;
        cal = new Calendario();
        formateador = new SimpleDateFormat("yyy-MM-dd");
    }

    public LoginVM() {
    }

    public Object[] Login() throws SQLException {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el e-mail");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (!Objetos.eventos.isEmail(_textField.get(0).getText())) {
                _label.get(0).setText("Ingrese un e-mail valido");
                _label.get(0).setForeground(Color.RED);
                _textField.get(0).requestFocus();
            } else {
                if (_textField.get(1).getText().equals("")) {
                    _label.get(1).setText("Ingrese la contraseña");
                    _label.get(1).setForeground(Color.RED);
                    _textField.get(1).requestFocus();
                } else {
                    listUsuarios = Usuarios().stream()
                            .filter(u -> u.getEmail().equals(_textField.get(0).getText()))
                            .collect(Collectors.toList());
                    var dataUsuario = listUsuarios.size() == 0 ? null : listUsuarios.get(0);
                    var idUsuario = dataUsuario != null ? dataUsuario.getIdUsuario() : 0;
                    var cajas_ingresos = CajasIngreso().stream()
                            .filter(c -> c.getIdUsuario() == idUsuario)
                            .collect(Collectors.toList());
                    var cajas = Cajas().stream()
                            .filter(c -> c.isEstado() == true)
                            .collect(Collectors.toList());
                    if (0 < cajas_ingresos.size() || 0 < cajas.size()) {
                        if (dataUsuario != null) {
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
                                                + "WHERE IdUsuario =" + listUsuarios.get(0).getIdUsuario();
                                        qr.update(getConn(), sql1, usuario);
                                        var dataOrdenador = Ordenadores().stream()
                                                .filter(o -> o.getOrdenador().equals(hdd))
                                                .collect(Collectors.toList());
                                        if (dataOrdenador.isEmpty()) {
                                            String sql2 = "INSERT INTO tordenadores "
                                                    + "(Ordenador,Is_active,Usuario,InFecha,IdUsuario)"
                                                    + " VALUES (?,?,?,?,?)";
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
                                            String sql3 = "UPDATE tordenadores SET Is_active = ?,Usuario = ?,"
                                                    + "InFecha = ? WHERE IdOrdenador ="
                                                    + dataOrdenador.get(0).getIdOrdenador();
                                            qr.update(getConn(), sql3, ordenador);
                                        }
                                        caja = new TCajas();
                                        var data = cajas_ingresos.size() > 0
                                                ? cajas_ingresos.get(0)
                                                : cajas.size() > 0 ? cajas.get(0) : new TCajas();
                                        caja.setIdCaja(data.getIdCaja());
                                        caja.setCaja(data.getCaja());
                                        caja.setEstado(data.isEstado());
                                        var sqlCaja = "UPDATE tcajas SET Estado = ?"
                                                + " WHERE IdCaja =" + caja.getIdCaja();
                                        Object[] dataCaja = {false};
                                        qr.update(getConn(), sqlCaja, dataCaja);

                                        String sqlIngresos = "UPDATE tcajas_ingresos SET IdUsuario = ?,Fecha = ?"
                                                + " WHERE IdCaja =" + caja.getIdCaja();
                                        Object[] dataIngresos = {
                                            dataUsuario.getIdUsuario(),
                                            new Date()
                                        };
                                        qr.update(getConn(), sqlIngresos, dataIngresos);

                                        Calendar calendar = new GregorianCalendar();
                                        var fecha = formateador.parse(cal.getFecha(calendar).replace("/", "-"));
                                        var reportes = Cajas_reportes().stream()
                                                .filter(c -> c.getIdCaja() == caja.getIdCaja()
                                                && c.getFecha().equals(fecha)
                                                && c.getTipoIngreso().equals("inicial"))
                                                .collect(Collectors.toList());
                                        if (reportes.size() > 0) {
                                            reportes.forEach(item -> {
                                                try {
                                                    String sqlReportes = "UPDATE tcajas_reportes SET IdUsuario = ?"
                                                            + " WHERE IdCaja =" + caja.getIdCaja();
                                                    Object[] dataReportes = {
                                                        dataUsuario.getIdUsuario()
                                                    };
                                                    qr.update(getConn(), sqlReportes, dataReportes);
                                                } catch (Exception e) {
                                                }

                                            });
                                        }
                                        String sqlRegistros = "INSERT INTO tcajas_registros"
                                                + "(IdCaja,IdUsuario, Estado,Fecha) VALUES(?,?,?,?)";
                                        Object[] dataRegistros = {
                                            caja.getIdCaja(),
                                            dataUsuario.getIdUsuario(),
                                            true,
                                            new Date()
                                        };
                                        qr.insert(getConn(), sqlRegistros, new ColumnListHandler(), dataRegistros);
                                        getConn().commit();

                                    } else {
                                        _label.get(1).setText("Contraseña incorrecta");
                                        _label.get(1).setForeground(Color.RED);
                                        _textField.get(1).requestFocus();
                                        listUsuarios.clear();
                                    }
                                } else {
                                    listUsuarios.clear();
                                    JOptionPane.showConfirmDialog(null,
                                            "El usuario no está disponible", "Estado",
                                            JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
                                }
                            } catch (Exception e) {
                                caja = null;
                                listUsuarios.clear();
                                getConn().rollback();
                                JOptionPane.showMessageDialog(null, e);
                            }
                        } else {
                            _label.get(0).setText("El e-mail no está registrado");
                            _label.get(0).setForeground(Color.RED);
                            _textField.get(0).requestFocus();
                            listUsuarios.clear();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "No hay cajas disponibles", "Caja",
                                JOptionPane.OK_OPTION);
                        listUsuarios = new ArrayList<TUsuarios>();
                    }
                }
            }
        }
        Object[] objects = {listUsuarios, caja};
        return objects;
    }

    public Object[] Verificar() {
        listUsuarios = new ArrayList<>();
        try {
            var hdd = Ordenador.getSerialNumber('c');
            var dataOrdenador = Ordenadores().stream()
                    .filter(o -> o.getOrdenador().equals(hdd)
                    && o.isIs_active() == true)
                    .collect(Collectors.toList());
            if (!dataOrdenador.isEmpty()) {
                listUsuarios = Usuarios().stream()
                        .filter(u -> u.getEmail().equals(dataOrdenador.get(0).getUsuario()))
                        .collect(Collectors.toList());
            }
            var dataUsuario = listUsuarios.size() == 0 ? null : listUsuarios.get(0);
            var idUsuario = dataUsuario != null ? dataUsuario.getIdUsuario() : 0;
            var cajas_ingresos = CajasIngreso().stream()
                    .filter(c -> c.getIdUsuario() == idUsuario)
                    .collect(Collectors.toList());
            var cajaData = 0 < cajas_ingresos.size() ? cajas_ingresos.get(0) : null;
            caja = new TCajas();
            if (cajaData != null) {
                caja.setIdCaja(cajaData.getIdCaja());
                caja.setCaja(cajaData.getCaja());
                caja.setEstado(cajaData.isEstado());
            }
        } catch (Exception e) {
        }
        Object[] objects = {listUsuarios, caja};
        return objects;
    }

    public void Close() throws SQLException {
        listUsuarios = new ArrayList<>();
        final QueryRunner qr = new QueryRunner(true);
        getConn().setAutoCommit(false);
        try {
            Date date = new Date();
            var hdd = Ordenador.getSerialNumber('c');
            var dataOrdenador = Ordenadores().stream()
                    .filter(o -> o.getOrdenador().equals(hdd))
                    .collect(Collectors.toList());
            listUsuarios = Usuarios().stream()
                    .filter(u -> u.getEmail().equals(dataOrdenador.get(0).getUsuario()))
                    .collect(Collectors.toList());
            Object[] usuario = {false};
            String sql1 = "UPDATE tusuarios SET Is_active = ? "
                    + "WHERE IdUsuario =" + listUsuarios.get(0).getIdUsuario();
            qr.update(getConn(), sql1, usuario);

            Object[] ordenador = {false, date};
            String sql3 = "UPDATE tordenadores SET Is_active = ?,"
                    + "OutFecha = ? WHERE IdOrdenador ="
                    + dataOrdenador.get(0).getIdOrdenador();
            qr.update(getConn(), sql3, ordenador);

            String sqlRegistros = "INSERT INTO tcajas_registros"
                    + "(IdCaja,IdUsuario, Estado,Fecha) VALUES(?,?,?,?)";
            Object[] dataRegistros = {
                caja.getIdCaja(),
                listUsuarios.get(0).getIdUsuario(),
                false,
                new Date()
            };
            qr.insert(getConn(), sqlRegistros, new ColumnListHandler(), dataRegistros);
            var sqlCaja = "UPDATE tcajas SET Estado = ?"
                    + " WHERE IdCaja =" + caja.getIdCaja();
            Object[] dataCaja = {true};
            qr.update(getConn(), sqlCaja, dataCaja);

            String sqlIngresos = "UPDATE tcajas_ingresos SET IdUsuario = ?"
                    + " WHERE IdCaja =" + caja.getIdCaja();
            Object[] dataIngresos = {
                0
            };
            qr.update(getConn(), sqlIngresos, dataIngresos);
            getConn().commit();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
