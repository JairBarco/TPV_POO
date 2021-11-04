package ViewModels;

import Conexion.Consult;
import Library.*;
import Models.Usuario.*;
import Models.Compras.*;
import Models.Producto.ProductosModel;
import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author jair_
 */
public class ProductosVM extends Consult {

    private List<JTextField> _textField;
    private List<JLabel> _label;
    private static TUsuarios _dataUsuario;
    private DefaultTableModel modelo1, modelo2, modelo3;
    private FormatDecimal _format;
    private int _reg_por_pagina = 10, _num_pagina = 1;
    private JTable _tableComprasTemporal;
    private String _accion = "insert", _money;
    public Codigos codigos;
    public int _seccion = 0;
    private JCheckBox _checkBoxCodigo;

    public ProductosVM(TUsuarios dataUsuario) {
        _dataUsuario = dataUsuario;
    }

    public ProductosVM(Object[] objetos, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _checkBoxCodigo = (JCheckBox) objetos[1];
        _tableComprasTemporal = (JTable) objetos[2];
        _format = new FormatDecimal();
        _money = ConfigurationVM.Money;
        codigos = new Codigos();
        Reset();
    }

    private List<ProductosModel> listTemporalCompras;

    public void SearchCompras(String campo) {
        String[] titulos = {"IdCompra", "Descripción", "Cantidad", "Precio", "Importe", "Credito", "Ticket", "Fecha", "ID"};
        modelo1 = new DefaultTableModel(null, titulos) {
            public Class<?> getColumnClass(int column) {
                if (column == 5) {
                    return Boolean.class;
                } else {
                    return String.class;
                }
            }
        };
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.equals("")) {
            listTemporalCompras = Temporal_Productos(_dataUsuario.getIdUsuario()).stream().collect(Collectors.toList());
        } else {
            listTemporalCompras = Temporal_Productos(_dataUsuario.getIdUsuario()).stream().filter(p -> p.getDescripcion().startsWith(campo)).collect(Collectors.toList());
        }
        var data = listTemporalCompras;
        var list = data.stream().skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
        if (!list.isEmpty()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getIdCompra(),
                    item.getDescripcion(),
                    item.getCantidad(),
                    _money + _format.decimal(item.getPrecio()),
                    _money + _format.decimal(item.getImporte()),
                    item.getCredito(),
                    item.getTicket(),
                    item.getFecha(),
                    item.getId()};
                modelo1.addRow(registros);
            });
        }
        _tableComprasTemporal.setModel(modelo1);
        _tableComprasTemporal.setRowHeight(30);
        _tableComprasTemporal.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(0).setMinWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(8).setMaxWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(8).setMinWidth(0);
        _tableComprasTemporal.getColumnModel().getColumn(8).setPreferredWidth(0);
    }

    private int _idCompra = 0, cantidad, _idTemporal;
    private String precioCompra;
    private String producto;

    public void dataCompra() {
        var filas = _tableComprasTemporal.getSelectedRow();
        switch (_seccion) {
            case 0:
                _accion = "insert";
                _idCompra = (Integer) modelo1.getValueAt(filas, 0);
                producto = (String) modelo1.getValueAt(filas, 1);
                cantidad = (Integer) modelo1.getValueAt(filas, 2);
                precioCompra = (String) modelo1.getValueAt(filas, 3);
                _idTemporal = (Integer) modelo1.getValueAt(filas, 8);
                _label.get(0).setText(producto);
                _label.get(1).setText(precioCompra);
                _textField.get(1).setText(producto);
                _textField.get(2).setText("");
                _label.get(4).setForeground(new Color(0, 153, 51));
                break;
        }
        codigos.codigoBarra(_label.get(2), _textField.get(0).getText(), producto, _textField.get(2).getText());
    }

    private Double Precio;
    private boolean verificar = false;

    public void verificarPrecioVenta() {
        double compra;
        if (!_textField.get(2).getText().equals("") && !precioCompra.equals("")) {
            compra = _format.reconstruir(precioCompra.replace(_money, ""));
            Precio = _format.reconstruir(_textField.get(2).getText());
            if (compra > Precio || compra == Precio) {
                switch (_seccion) {
                    case 0:
                        _label.get(5).setText("El precio debe ser mayor");
                        _label.get(5).setForeground(Color.RED);
                        verificar = false;
                        break;
                }
            } else {
                _label.get(5).setText("Precio venta");
                _label.get(5).setForeground(new Color(0, 153, 51));
                verificar = true;
            }
            _label.get(1).setText(_money + _format.decimal(Precio));
        }
    }

    public int idProducto = 0;

    public boolean RegistrarProducto() throws SQLException {
        if (_idCompra == 0) {
            JOptionPane.showConfirmDialog(null, "Seleccione un producto", "Producto", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        } else {
            if (_checkBoxCodigo.isSelected()) {
                if (_textField.get(0).getText().equals("")) {
                    _label.get(3).setText("Ingrese el código");
                    _label.get(3).setForeground(Color.RED);
                    _textField.get(0).requestFocus();
                    return false;
                }
            }
            if (_textField.get(1).getText().equals("")) {
                _label.get(4).setText("Ingrese la descripción");
                _label.get(4).setForeground(Color.RED);
                _textField.get(1).requestFocus();
            } else if(_textField.get(2).getText().equals("")){
                _label.get(5).setText("Ingrese el precio de venta");
                _label.get(5).setForeground(Color.RED);
                _textField.get(2).requestFocus();
            } else if(_textField.get(3).getText().equals("")){
                _label.get(6).setText("Ingrese el departamento");
                _label.get(6).setForeground(Color.RED);
                _textField.get(3).requestFocus();
            } else if(_textField.get(4).getText().equals("")){
                _label.get(7).setText("Ingrese la categoría");
                _label.get(7).setForeground(Color.RED);
                _textField.get(4).requestFocus();
            } else if(codigos.Codigo == null){
                _label.get(3).setText("El código ya está registrado");
                _label.get(3).setForeground(Color.RED);
                _textField.get(0).requestFocus();
            } else if(verificar){
                try{
                    var qr = new QueryRunner(true);
                    getConn().setAutoCommit(false);
                    var _producto = _textField.get(1).getText().replace(" ", "").toLowerCase();
                    var codigo = _textField.get(0).getText().equals("") ? codigos.Codigo : _textField.get(0).getText();
                    var product1 = Productos().stream().filter(p -> p.getProducto().replace(" ", "").toLowerCase().equals(producto) 
                            && p.getPrecio().equals(Precio)).collect(Collectors.toList());
                } catch (Exception e){
                    
                }
            }
        }
        return false;
    }

    public void Reset() {
        _seccion = 0;
        _accion = "insert";

        _textField.forEach(item -> {
            item.setText("");
            _label.forEach(item1 -> {
                item.setForeground(Color.BLACK);
            });
        });

        _textField.get(0).setEnabled(false);
        _checkBoxCodigo.setSelected(false);
        _checkBoxCodigo.setForeground(Color.BLACK);
        _label.get(0).setText("Descripción");
        _label.get(1).setText(_money + "0.00");

        SearchCompras("");
        codigos.codigoBarra(_label.get(2), "0000000000", _textField.get(1).getText(), _textField.get(2).getText());
    }
}
