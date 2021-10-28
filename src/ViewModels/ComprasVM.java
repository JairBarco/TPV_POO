package ViewModels;

import Conexion.Consult;
import Library.FormatDecimal;
import Library.Objetos;
import Library.UploadImage;
import Models.Compras.TCompras_temporal;
import Models.Proveedor.TProveedor;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author jair_
 */
public class ComprasVM extends Consult {

    private List<JTextField> _textField;
    private List<JLabel> _label;
    private JComboBox _comboBoxProveedor;
    private JCheckBox _checkBoxCredito;
    private JCheckBox _checkBoxCreditos;
    private JCheckBox _checkBoxTodos;
    private JCheckBox _checkBoxEliminar;
    private static TUsuarios _dataUsuario;
    private FormatDecimal _format;
    private String _accion = "insert", _money;

    private DefaultTableModel modelo1, modelo2, modelo3;
    private JTable _tableComprasTemporal;
    private int _reg_por_pagina = 10, _num_pagina = 1;

    public ComprasVM(TUsuarios dataUsuario) {
        _dataUsuario = dataUsuario;
    }

    public ComprasVM(Object[] objetos, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _checkBoxCredito = (JCheckBox) objetos[0];
        _comboBoxProveedor = (JComboBox) objetos[1];
        _checkBoxCreditos = (JCheckBox) objetos[2];
        _checkBoxTodos = (JCheckBox) objetos[3];
        _tableComprasTemporal = (JTable) objetos[4];
        _format = new FormatDecimal();
        _money = ConfigurationVM.Money;
        Reset();
    }

    private Double _importe, _precio;
    private int _cantidad, _idProveedor;

    public void importes() {
        _cantidad = _textField.get(1).getText().equals("") ? 0 : Integer.valueOf(_textField.get(1).getText());
        _precio = _textField.get(2).getText().equals("") ? 0 : _format.reconstruir(_textField.get(2).getText());
        _importe = _precio * _cantidad;
        _label.get(3).setText(_money + _format.decimal(_importe));
    }

    public void GuardarCompras() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Este campo es requerido");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (_textField.get(1).getText().equals("")) {
                _label.get(1).setText("Este campo es requerido");
                _label.get(1).setForeground(Color.RED);
                _textField.get(1).requestFocus();
            } else {
                if (_textField.get(2).getText().equals("")) {
                    _label.get(2).setText("Este campo es requerido");
                    _label.get(2).setForeground(Color.RED);
                    _textField.get(2).requestFocus();
                } else {
                    var data = (TProveedor) _comboBoxProveedor.getSelectedItem();
                    _idProveedor = data.getID();
                    if (_idProveedor == 0) {
                        _label.get(4).setText("Seleccione un proveedor");
                        _label.get(4).setForeground(Color.RED);
                        _textField.get(3).requestFocus();
                    } else {
                        try {
                            var compras = Compras_temporal().stream().filter(p -> p.getIdUsuario() == _dataUsuario.getIdUsuario()).collect(Collectors.toList());
                            if (compras.size() == 0) {
                                SaveData();
                            } else {
                                var dataCompra = compras.get(0);
                                if (dataCompra.getIdProveedor() == _idProveedor) {
                                    SaveData();
                                } else {
                                    JOptionPane.showConfirmDialog(null, "Finalice la compra antes de seleccionar otro proveedor", "Compras", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e);
                        }
                    }
                }
            }
        }
    }

    public void SaveData() throws SQLException {
        try {
            final QueryRunner qr = new QueryRunner(true);
            getConn().setAutoCommit(false);

            switch (_accion) {
                case "insert":
                    String sqlCompra = "INSERT INTO tcompras_temporal(Descripcion,Cantidad, Precio,Importe,IdProveedor,IdUsuario,Credito,Fecha) VALUES (?,?,?,?,?,?,?,?)";
                    Object[] dataCompra = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _importe,
                        _idProveedor,
                        _dataUsuario.getIdUsuario(),
                        _checkBoxCredito.isSelected(),
                        new Date(),};

                    qr.insert(getConn(), sqlCompra, new ColumnListHandler(), dataCompra);
                    break;
                case "update":
                    String sqlCompra1 = "UPDATE tcompras_temporal SET Descripcion = ?,Cantidad = ?,Precio = ?,Importe = ?,Credito = ?"
                            + " WHERE IdCompra =" + _idCompra;
                    Object[] dataCompra1 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _importe,
                        _checkBoxCredito.isSelected(),};
                    qr.update(getConn(), sqlCompra1, dataCompra1);
                    break;
            }
            getConn().commit();
            Reset();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void SearchProveedor(String campo) {
        List<TProveedor> listProveedores = null;
        var model = new DefaultComboBoxModel();
        if (campo.equals("")) {
            var proveedor = new TProveedor();
            proveedor.setID(0);
            proveedor.setProveedor("Proveedores");
            model.addElement(proveedor);
            proveedores().forEach(item -> {
                model.addElement(item);
            });
        } else {
            listProveedores = proveedores().stream().filter(p -> p.getProveedor().startsWith(campo) || p.getEmail().startsWith(campo)).collect(Collectors.toList());
            listProveedores.forEach(item -> {
                model.addElement(item);
            });
        }
        _comboBoxProveedor.setModel(model);
    }

    public List<TCompras_temporal> listTemporalCompras;

    public void SearchCompras(String campo, boolean eliminar) {
        String[] titulos = {"IdCompra", "Descripción", "Cantidad", "Precio", "Importe", "Crédito", "Eliminar", "IdProveedor"};
        modelo1 = new DefaultTableModel(null, titulos) {
            public Class<?> getColumnClass(int column) {
                if (column == 5 || column == 6) {
                    return Boolean.class;
                } else {
                    return String.class;
                }
            }
        };
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.equals("")) {
            if (_checkBoxTodos.isSelected()) {
                listTemporalCompras = Compras_temporal().stream().filter(p -> p.getIdUsuario() == _dataUsuario.getIdUsuario()).collect(Collectors.toList());
            } else {
                listTemporalCompras = Compras_temporal().stream().filter(p -> p.getCredito().equals(_checkBoxCreditos.isSelected()) && p.getIdUsuario() == _dataUsuario.getIdUsuario()).collect(Collectors.toList());
            }
        } else {
            if (_checkBoxTodos.isSelected()) {
                listTemporalCompras = Compras_temporal().stream().filter(p -> p.getIdUsuario() == _dataUsuario.getIdUsuario() && p.getDescripcion().startsWith(campo))
                        .collect(Collectors.toList());
            } else {
                listTemporalCompras = Compras_temporal().stream().filter(p -> p.getIdUsuario() == _dataUsuario.getIdUsuario() && p.getDescripcion().startsWith(campo) && p.getCredito().equals(_checkBoxCreditos.isSelected()))
                        .collect(Collectors.toList());
            }
        }
        var data = listTemporalCompras;
        var list = data.stream().skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
        if (!list.isEmpty()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getIdCompra(),
                    item.getDescripcion(),
                    item.getCantidad(),
                    _format.decimal(item.getPrecio()),
                    _format.decimal(item.getImporte()),
                    item.getCredito(),
                    eliminar,
                    item.getIdProveedor()
                };
                modelo1.addRow(registros);
            });
        }
        _tableComprasTemporal.setModel(modelo1);
        _tableComprasTemporal.setRowHeight(30);
        _tableComprasTemporal.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(0).setMinWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(7).setMaxWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(7).setMinWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(7).setPreferredWidth(0);
    }

    private int _idCompra = 0;

    public void getCompra() {
        _accion = "update";
        int filas = _tableComprasTemporal.getSelectedRow();
        _idCompra = (Integer) modelo1.getValueAt(filas, 0);
        _textField.get(0).setText((String) modelo1.getValueAt(filas, 1));
        var cantidad = (Integer) modelo1.getValueAt(filas, 2);
        _textField.get(1).setText(cantidad.toString());
        var precio =  modelo1.getValueAt(filas, 3);
        _textField.get(2).setText(precio.toString());
        _checkBoxCredito.setSelected((Boolean) modelo1.getValueAt(filas, 5));
        var idProveedor = (Integer) modelo1.getValueAt(filas, 7);
        var data = proveedores().stream().filter(p -> p.getID() == idProveedor).collect(Collectors.toList()).get(0);
        _textField.get(3).setText(data.getProveedor());
        SearchProveedor(data.getProveedor());
        for (int i = 0; i < _textField.size(); i++) {
            _label.get(i).setForeground(new Color(0, 153, 51));
        }
        importes();
    }
    
    public void eliminar(){
        final QueryRunner qr = new QueryRunner(true);
        var listEliminar = new ArrayList<Integer>();
        var data = _tableComprasTemporal.getModel();
        int cols = data.getColumnCount();
        int fils = data.getRowCount();
        if(0 < fils){
            for(int i = 0; i < fils; i++){
                if((boolean) data.getValueAt(i,6)){
                    listEliminar.add((Integer) data.getValueAt(i,0));
                }
            }
            if(0 < listEliminar.size()){
                if(JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar " + listEliminar.size() + " registro/s?", "Eliminar compras",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)){
                    
                }
            }
        }
    }

    public void Reset() {
        _idProveedor = 0;
        _accion = "insert";
        String[] reset = {"Descripción", "Cantidad", "Precio", _money + "0.00", "Proveedor"};

        _textField.forEach(item -> {
            item.setText("");
        });

        _label.get(0).setText(reset[0]);
        _label.get(0).setForeground(Color.BLACK);

        _label.get(1).setText(reset[1]);
        _label.get(1).setForeground(Color.BLACK);

        _label.get(2).setText(reset[2]);
        _label.get(2).setForeground(Color.BLACK);

        _label.get(3).setText(reset[3]);
        _label.get(3).setForeground(Color.BLACK);

        _label.get(4).setText(reset[4]);
        _label.get(4).setForeground(Color.BLACK);

        _checkBoxCredito.setSelected(false);
        _checkBoxCredito.setForeground(new Color(0, 0, 0));
        SearchProveedor("");
        SearchCompras("", false);
    }
}
