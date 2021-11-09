package ViewModels;

import Library.*;
import Models.Cajas.TCajas_ingresos;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Collectors;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author jair_
 */
public class CajasVM extends Objetos {

    private List<JTextField> _textField;
    private List<JLabel> _label;
    private static TUsuarios _dataUsuario;
    private DefaultTableModel modelo1, modelo2, modelo3;
    private FormatDecimal _format;
    private int _reg_por_pagina = 10, _num_pagina = 1;
    private JTable _tableListaCajas, _tableIngresos;
    private String _accion = "insert", _money;
    public Codigos codigos;
    public int _seccion = 0, _idProducto = 0;
    private JSpinner _spinnerCaja;
    private JCheckBox _checkBoxEstado, _checkBoxIngresos;
    private Calendario _cal;

    public CajasVM(TUsuarios usuario) {
        _dataUsuario = usuario;
    }

    public CajasVM(Object[] objetos, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _spinnerCaja = (JSpinner) objetos[0];
        _checkBoxEstado = (JCheckBox) objetos[1];
        _checkBoxIngresos = (JCheckBox) objetos[2];
        _tableListaCajas = (JTable) objetos[3];
        _format = new FormatDecimal();
        _money = ConfigurationVM.Money;
        _cal = new Calendario();
        Reset();
    }

    public void SearchCajas(int caja) {
        String[] titulos = {"IdCaja", "Caja", "Estado", "Billete", "Moneda", "Ingreso"};
        modelo1 = new DefaultTableModel(null, titulos) {
            public Class<?> getColumnClass(int column) {
                return column == 2 ? Boolean.class : String.class;
            }
        };
        var list = caja == 0 ? CajasIngreso() : CajasIngreso().stream().filter(p -> p.getCaja() == caja).collect(Collectors.toList());
        if (!list.isEmpty()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getIdCaja(),
                    item.getCaja(),
                    item.isEstado(),
                    _money + _format.decimal(item.getBillete()),
                    _money + _format.decimal(item.getMoneda()),
                    _money + _format.decimal(item.getIngreso()),};
                modelo1.addRow(registros);
            });
            _tableListaCajas.setModel(modelo1);
            _tableListaCajas.setRowHeight(30);
            _tableListaCajas.getColumnModel().getColumn(0).setMaxWidth(0);
            _tableListaCajas.getColumnModel().getColumn(0).setMinWidth(0);
            _tableListaCajas.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    }

    public void RegistrarCajas() throws SQLException {
        try {
            var qr = new QueryRunner(true);
            getConn().setAutoCommit(false);
            var value = (Number) _spinnerCaja.getValue();
            var caja = value.intValue();
            var cajas = Cajas().stream().filter(p -> p.getCaja() == caja).collect(Collectors.toList());
            switch (_accion) {
                case "insert":
                    if (!_checkBoxIngresos.isSelected()) {
                        if (caja > 0) {
                            if (cajas.isEmpty()) {
                                String sqlCaja = "INSERT INTO tcajas(Caja,Estado,Fecha) VALUES (?,?,?)";
                                Object[] dataCaja = {
                                    caja,
                                    _checkBoxEstado.isSelected(),
                                    new Date(),};
                                var data = (List<? extends Number>) qr.insert(getConn(), sqlCaja, new ColumnListHandler(), dataCaja);
                                var idCaja = data.get(0).intValue();
                                String sqlIngresos = "INSERT INTO tcajas_ingresos (IdCaja,IdUsuario, Billete,Moneda, Ingreso,Fecha) VALUES (?,?,?,?,?,?)";
                                Object[] dataIngresos = {
                                    idCaja,
                                    0,
                                    0.0,
                                    0.0,
                                    0.0,
                                    new Date()
                                };
                                qr.insert(getConn(), sqlIngresos, new ColumnListHandler(), dataIngresos);
                                Reset();
                            } else {
                                _label.get(0).setText("El número de caja ya está registrado");
                                _label.get(0).setForeground(Color.RED);
                            }
                        } else {
                            _label.get(0).setText("Ingrse un número de caja");
                            _label.get(0).setForeground(Color.RED);
                        }
                    }
                    break;
                case "update":
                    var sqlCaja = "UPDATE tcajas SET Estado = ?"
                            + " WHERE IdCaja =" + idCaja;
                    Object[] dataCaja = {
                        _checkBoxEstado.isSelected()};
                    qr.update(getConn(), sqlCaja, dataCaja);
                    Reset();
                    break;
            }
            getConn().commit();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private int idCaja, numCaja;
    private double _ingresos;

    public void dataCaja() {
        switch (_seccion) {
            case 0:
                
                break;
            case 1:
                _accion = "update";
                var filas1 = _tableListaCajas.getSelectedRow();
                idCaja = (Integer) modelo1.getValueAt(filas1, 0);
                numCaja = (Integer) modelo1.getValueAt(filas1, 1);
                _label.get(1).setText("#" + numCaja);
                var estado = (boolean) modelo1.getValueAt(filas1, 2);
                var color = estado ? new Color(0, 153, 51) : Color.BLACK;
                _checkBoxEstado.setSelected(estado);
                _checkBoxEstado.setForeground(color);
                _spinnerCaja.setValue(numCaja);
                break;
        }
    }
    
    public void asignarIngresos() throws SQLException {
        if (idCaja == 0){
            JOptionPane.showConfirmDialog(null, "Seleccione un numero de caja", "Caja",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        }else{
            if (_textField.get(0).getText().equals("")){
                _label.get(2).setText("Ingrese los billetes");
                _label.get(2).setForeground(Color.RED);
                _textField.get(0).requestFocus();
            }else if (_textField.get(1).getText().equals("")){
                _label.get(3).setText("Ingrese las monedas equivalente a " + _money);
                _label.get(3).setForeground(Color.RED);
                _textField.get(1).requestFocus();
            }else{
                try {
                     var qr = new QueryRunner(true);
                    getConn().setAutoCommit(false);
                    var billetes1 = _format.reconstruir(_textField.get(0).getText());
                    var monedas1 = _format.reconstruir(_textField.get(1).getText());
                    var ingresos1 = billetes1 + monedas1;
                    
                    var ingresosData = CajasIngreso().stream()
                            .filter(c -> c.getIdCaja() == idCaja && 
                                    _cal.getFecha(c.getFecha())
                                            .equals(_cal.getFecha(new Date())))
                            .collect(Collectors.toList());
                    var ingresoData = 0 < ingresosData.size() ? ingresosData.get(0) 
                            : new TCajas_ingresos();
                    var billetes2 = billetes1 + ingresoData.getBillete();
                    var monedas2 = monedas1 + ingresoData.getMoneda();
                    var ingresos2 = ingresoData.getMoneda() + ingresoData.getBillete()+ ingresos1;
                    String sqlIngresos = "UPDATE tcajas_ingresos SET Billete = ?,"
                            + "Moneda = ?,Ingreso = ?,Fecha = ? WHERE IdCaja =" + idCaja;
                    Object[] dataIngresos = {
                        billetes2,
                        monedas2,
                        ingresos2,
                        new Date()
                    };
                    qr.update(getConn(), sqlIngresos, dataIngresos);
                    var cajasData = Cajas_registros().stream()
                            .filter(c -> c.getIdCaja() == idCaja
                            && c.isEstado() == true).collect(Collectors.toList());
                    var idUsuario = 0 < cajasData.size() ? cajasData.get(0).getIdUsuario() : 0;
                    String sqlReportes = "INSERT INTO tcajas_reportes(IdCaja,IdUsuario,"
                            + "Billete,Moneda,Ingreso,TipoIngreso,Fecha)"
                            + " VALUES(?,?,?,?,?,?,?)";
                    Object[] dataReportes = {
                        idCaja,
                        idUsuario,
                        billetes1,
                        monedas1,
                        ingresos1,
                        "inicial",
                        new Date()
                    };
                    qr.insert(getConn(), sqlReportes, new ColumnListHandler(), dataReportes);
                    getConn().commit();
                    Reset();
                } catch (Exception e) {
                    getConn().rollback();
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }

    public void Reset() {
        var model = new SpinnerNumberModel(
                1.0, 1.0, 30.0, 1.0
        );
        _spinnerCaja.setModel(model);
        _label.get(0).setText("Númer de caja");
        _label.get(0).setForeground(Color.BLACK);
        _label.get(1).setText("#" + 0);
        _checkBoxEstado.setSelected(false);
        _checkBoxEstado.setForeground(Color.BLACK);
        _checkBoxIngresos.setSelected(false);
        _checkBoxIngresos.setForeground(Color.BLACK);
        _textField.get(0).setEnabled(false);
        _textField.get(1).setEnabled(false);
        _spinnerCaja.setValue(1);
        _label.get(2).setText("Billetes");
        _label.get(2).setForeground(Color.BLACK);
        _label.get(3).setText("Monedas");
        _label.get(3).setForeground(Color.BLACK);
        _textField.get(0).setText("");
        _textField.get(1).setText("");
        var numCaja = _textField.get(2).getText().equals("") ? 0 : Integer.valueOf(_textField.get(2).getText());
        SearchCajas(numCaja);
    }
}
