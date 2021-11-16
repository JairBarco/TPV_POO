/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewModels;

import Library.Calendario;
import Library.FormatDecimal;
import Library.Objetos;
import Library.Paginador;
import Models.Cajas.TCajas;
import Models.Cajas.TCajas_ingresos;
import Models.Cajas.TCajas_reportes;
import Models.Usuario.TUsuarios;
import Models.Venta.TVentas_temporal;
import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author jair_
 */
public class VentasVM extends Objetos {

    private List<JTextField> _textField;
    private List<JLabel> _label;
    private JSpinner _spinnerPaginas, _spinnerCantidad;
    public String _accion = "insert", _money;
    private static TUsuarios _dataUsuario;
    private static TCajas _cajaData;
    private DefaultTableModel modelo1, modelo2;
    private JTable _tableVentasTemporal, _table_Productos;
    private FormatDecimal _format;
    private JCheckBox _checkBoxEliminar, _checkBoxCredito;
    private Calendario _cal;
    private int _reg_por_pagina = 10, _num_pagina = 1;

    private int _idProducto, _cantidad, filas;
    private double importe = 0;
    private List<TVentas_temporal> ventaTempo;

    private double ingresosTotales = 0;
    private List<TCajas_ingresos> ingresosIniciales;
    private List<TCajas_reportes> ingresosVentas;

    private double _cambio;
    private boolean _verificar = false, _suCambio = false;

    private Double _ingresos, _pagos, _ingresoInicial;
    private Double _deuda = 0.0, _deudaActual = 0.0;
    private int _idCliente = 0;

    private Double _ingresoVenta = 0.0;

    public VentasVM(TUsuarios dataUsuario, TCajas cajaData) {
        _cajaData = cajaData;
        _dataUsuario = dataUsuario;
    }

    public VentasVM(Object[] objetos, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _spinnerCantidad = (JSpinner) objetos[0];
        _tableVentasTemporal = (JTable) objetos[1];
        _checkBoxEliminar = (JCheckBox) objetos[2];
        _spinnerPaginas = (JSpinner) objetos[3];
        _checkBoxCredito = (JCheckBox) objetos[4];
        _format = new FormatDecimal();
        _money = ConfigurationVM.Money;
        _cal = new Calendario();
        Reset();
    }

    //<editor-fold defaultstate="collapsed" desc="CÓDIGO DE VENTAS">
    public boolean saveVentasTempo() {
        if (_textField != null) {
            if (_textField.get(0).getText().equals("")) {
                JOptionPane.showConfirmDialog(null,
                        "Ingrese el código del producto", "Ventas",
                        JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                _textField.get(0).requestFocus();
            } else {
                var codigo = _textField.get(0).getText();
                var producto = Productos_bodega().stream().filter(p -> p.getCodigo().equals(codigo)).collect(Collectors.toList());
                if (!producto.isEmpty()) {
                    var data1 = 0;
                    var data2 = 0;
                    var value = (Number) _spinnerCantidad.getValue();
                    var valor = value.intValue();
                    var cant = _accion.equals("insert") ? valor : (Integer) modelo1.getValueAt(filas, 5);
                    var ventatemp = Ventas_temporal(_dataUsuario.getIdUsuario(),
                            _cajaData.getCaja());
                    if (!ventatemp.isEmpty()) {
                        if (_cantidad > cant) {
                            if (producto.get(0).getExistencia() > 0) {
                                if (_idProducto == 0) {
                                    data2 = producto.get(0).getExistencia() - cant;
                                } else {
                                    data1 = _cantidad - cant;
                                    data2 = producto.get(0).getExistencia() + data1;
                                }
                            } else {
                                JOptionPane.showConfirmDialog(null,
                                        "Productos sin existencia", "Ventas", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                                Reset();
                                return false;
                            }
                        } else if (cant > _cantidad) {
                            if (producto.get(0).getExistencia() > 0) {
                                data1 = cant - _cantidad;
                                if (data1 <= producto.get(0).getExistencia()) {
                                    data2 = producto.get(0).getExistencia() - data1;
                                } else {
                                    JOptionPane.showConfirmDialog(null,
                                            "Se sobrepasó de la cantidad de existencia del producto", "Ventas", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                                    Reset();
                                    return false;
                                }
                            } else {
                                JOptionPane.showConfirmDialog(null,
                                        "Productos sin existencia", "Ventas", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                                Reset();
                                return false;
                            }
                        } else if (cant == _cantidad) {
                            if (_idProducto == 0) {
                                data2 = producto.get(0).getExistencia() - cant;
                            } else {
                                _textField.get(0).requestFocus();
                                Reset();
                                return false;
                            }
                        }
                    } else {
                        if (producto.get(0).getExistencia() > 0) {
                            if (cant <= producto.get(0).getExistencia()) {
                                data2 = producto.get(0).getExistencia() - cant;
                            } else {
                                JOptionPane.showConfirmDialog(null,
                                        "Se sobrepasó de la cantidad de existencia del producto", "Ventas", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                                Reset();
                                return false;
                            }
                        } else {
                            JOptionPane.showConfirmDialog(null,
                                    "Producto sin existencia", "Ventas", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                            Reset();
                            return false;
                        }
                    }
                    try {
                        var descuento = producto.get(0).getDescuento();
                        var precio = producto.get(0).getPrecio();
                        descuento /= 100;
                        descuento *= precio;
                        precio -= descuento;

                        var qr = new QueryRunner(true);
                        getConn().setAutoCommit(false);
                        switch (_accion) {
                            case "insert":
                                if (!ventatemp.isEmpty()) {
                                    var importe1 = precio * cant;
                                    String sqlProducto = "UPDATE tventas_temporal SET Cantidad = ?" + ",Importe = ? WHERE IdTempo =" + ventatemp.get(0).getIdTempo()
                                            + " AND Caja =" + _cajaData.getCaja();
                                    Object[] dataProducto = {
                                        cant,
                                        importe1
                                    };
                                    qr.update(getConn(), sqlProducto, dataProducto);
                                } else {
                                    var importe2 = precio * cant;
                                    String sqlProducto = "INSERT INTO tventas_temporal(Codigo,Descripcion, Precio,Descuento," + " Cantidad,Importe,Caja,IdUsuario) VALUES(?,?,?,?,?,?,?,?)";
                                    Object[] dataProducto = {
                                        codigo,
                                        producto.get(0).getProducto(),
                                        precio,
                                        descuento,
                                        cant,
                                        importe2,
                                        _cajaData.getCaja(),
                                        _dataUsuario.getIdUsuario()
                                    };
                                    qr.insert(getConn(), sqlProducto, new ColumnListHandler(), dataProducto);
                                }
                                break;
                            case "update":
                                var importe3 = precio * cant;
                                String sqlProducto = "UPDATE tventas_temporal SET Cantidad = ?" + ",Importe = ? WHERE IdTempo =" + _idProducto;
                                Object[] dataProducto = {
                                    cant,
                                    importe3
                                };
                                qr.update(getConn(), sqlProducto, dataProducto);
                                break;
                        }
                        String sqlBodega = "UPDATE tbodega SET Existencia = ?" + " WHERE IdProducto =" + producto.get(0).getIdProducto();
                        Object[] dataBodega = {
                            data2,};
                        qr.update(getConn(), sqlBodega, dataBodega);
                        getConn().commit();
                        Reset();
                    } catch (Exception e) {
                    }
                } else {
                    JOptionPane.showConfirmDialog(null, "Productos sin existencia", "Ventas", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        return true;
    }
    private List<TVentas_temporal> listTemporary;

    public void searchVentasTempo(String search, boolean eliminar) {
        if (_dataUsuario != null) {
            String[] titulos = {"IdProducto", "Codigo", "Producto",
                "Precio", "Descuento", "Cantidad", "Importe", "Eliminar"};
            modelo1 = new DefaultTableModel(null, titulos) {
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex == 5 || columnIndex == 7 ? true : false;
                }

                public Class<?> getColumnClass(int column) {
                    return column == 7 ? Boolean.class : column == 5 ? Integer.class : String.class;
                }
            };
            listTemporary = search.equals("") ? Ventas_temporal(_dataUsuario.getIdUsuario(), _cajaData.getCaja()) : Ventas_temporal(_dataUsuario.getIdUsuario(), _cajaData.getCaja()).stream()
                    .filter(p -> p.getDescripcion().startsWith(search) || p.getCodigo().startsWith(search)).collect(Collectors.toList());
            int inicio = (_num_pagina - 1) * _reg_por_pagina;
            var data = listTemporary;
            var list = data.stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
            if (!list.isEmpty()) {
                list.forEach(item -> {
                    Object[] registros = {
                        item.getIdTempo(),
                        item.getCodigo(),
                        item.getDescripcion(),
                        _money + _format.decimal(item.getPrecio()),
                        "%" + _format.decimal(item.getDescuento()),
                        item.getCantidad(),
                        _money + _format.decimal(item.getImporte()),
                        eliminar
                    };
                    modelo1.addRow(registros);
                });

            }
            _tableVentasTemporal.setModel(modelo1);
            _tableVentasTemporal.setRowHeight(30);
            _tableVentasTemporal.getColumnModel().getColumn(0).setMaxWidth(0);
            _tableVentasTemporal.getColumnModel().getColumn(0).setMinWidth(0);
            _tableVentasTemporal.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    }

    public void getProductVentas() {
        _accion = "update";
        filas = _tableVentasTemporal.getSelectedRow();
        _idProducto = (Integer) modelo1.getValueAt(filas, 0);
        var codigo = (String) modelo1.getValueAt(filas, 1);
        _textField.get(0).setText(codigo);
        var cant = (Object) modelo1.getValueAt(filas, 5);
        if (cant.toString().matches("[0-9]*")) {
            _cantidad = (Integer) modelo1.getValueAt(filas, 5);
        }
        //_cantidad = _cantidad == 0 ? 1 : _cantidad;
        var model = new SpinnerNumberModel(
                _cantidad, // Dato visualizado al inicio en el spinner 
                0.0, // Límite inferior 
                1000.0, // Límite superior 
                1.0 // incremento-decremento 
        );
        _spinnerCantidad.setModel(model);
    }

    private void importes() {
        if (_dataUsuario != null) {
            importe = 0;
            ventaTempo = Ventas_temporal(_dataUsuario.getIdUsuario(),
                    _cajaData.getCaja()).stream()
                    .collect(Collectors.toList());
            if (!ventaTempo.isEmpty()) {
                ventaTempo.forEach(item -> {
                    importe += item.getImporte();
                });
                _label.get(1).setText(_money + _format.decimal(importe));
                _label.get(2).setText(_money + _format.decimal(importe));
            } else {
                _label.get(1).setText(_money + "0.00");
                _label.get(2).setText(_money + "0.00");
            }
        }
    }

    public void eliminar() throws SQLException {
        var qr = new QueryRunner(true);
        try {
            getConn().setAutoCommit(false);
            var listEliminar = new ArrayList<TVentas_temporal>();
            var data = _tableVentasTemporal.getModel();
            int filas = data.getRowCount();
            if (0 < filas) {
                for (int i = 0; i < filas; i++) {
                    var tempo = new TVentas_temporal();
                    if ((boolean) data.getValueAt(i, 7)) {
                        tempo.setIdTempo((Integer) data.getValueAt(i, 0));
                        tempo.setCodigo((String) data.getValueAt(i, 1));
                        tempo.setCantidad((Integer) data.getValueAt(i, 5));
                        listEliminar.add(tempo);
                    }
                }
                if (!listEliminar.isEmpty()) {
                    if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null,
                            "¿Estás seguro de eliminar los " + listEliminar.size()
                            + " registro/s ? ", "Eliminar compras",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                        var sql = "DELETE FROM tventas_temporal WHERE IdTempo LIKE ?";
                        listEliminar.forEach(item -> {
                            try {

                                var producto = Productos_bodega().stream()
                                        .filter(p -> p.getCodigo().equals(item.getCodigo()))
                                        .collect(Collectors.toList());
                                var existencia = producto.get(0).getExistencia() + item.getCantidad();
                                var sqlBodega = "UPDATE tbodega SET Existencia = ? "
                                        + "WHERE IdProducto =" + producto.get(0).getIdProducto();
                                Object[] dataBodega = {
                                    existencia
                                };
                                qr.update(getConn(), sqlBodega, dataBodega);

                                qr.update(getConn(), sql, "%" + item.getIdTempo() + "%");
                                getConn().commit();
                                searchVentasTempo(_textField.get(0).getText(), _checkBoxEliminar.isSelected());
                                importes();
                            } catch (SQLException ex) {
                                try {
                                    getConn().rollback();
                                } catch (SQLException ex1) {
                                }
                                JOptionPane.showMessageDialog(null, ex);
                            }

                        });
                    }
                }
            }
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void ingresosCajas() {
        ingresosTotales = 0;
        ingresosIniciales = CajasIngreso().stream()
                .filter(p -> p.getIdCaja() == _cajaData.getIdCaja()
                && p.getIdUsuario() == _dataUsuario.getIdUsuario())
                .collect(Collectors.toList());
        if (!ingresosIniciales.isEmpty()) {
            ingresosTotales = ingresosIniciales.get(0).getIngreso();
            _label.get(3).setText(_money + _format.decimal(ingresosTotales));
            _label.get(3).setForeground(Color.BLACK);
        } else {
            _label.get(3).setText(_money + "0.00");
            _label.get(3).setForeground(Color.RED);
        }
        ingresosVentas = Cajas_reportes().stream()
                .filter(p -> p.getIdCaja() == _cajaData.getIdCaja()
                && p.getIdUsuario() == _dataUsuario.getIdUsuario()
                && p.getTipoIngreso().equals("ventas")
                && _cal.getFecha(p.getFecha()).equals(_cal.getFecha(new Date())))
                .collect(Collectors.toList());
        if (!ingresosVentas.isEmpty()) {
            var data = ingresosVentas.get(0).getIngreso();
            _label.get(4).setText(_money + _format.decimal(data));
            _label.get(4).setForeground(Color.BLACK);
            ingresosTotales += data;
        } else {
            _label.get(4).setText(_money + "0.00");
            _label.get(4).setForeground(Color.RED);
        }
        _label.get(5).setText(_money + _format.decimal(ingresosTotales));
        _label.get(5).setForeground(Color.BLACK);
    }

    public void pagosCliente() {
        double pago, pagar;
        if (!_textField.get(1).getText().equals("")) {
            pagar = importe;
            pago = _format.reconstruir(_textField.get(1).getText());
            if (pago > pagar) {
                _cambio = pago - pagar;
                if (_cambio > ingresosTotales) {
                    _label.get(6).setText("No hay suficientes ingresos en caja");
                    _label.get(6).setForeground(Color.RED);
                    _label.get(7).setText("Cambio para el cliente");
                    _label.get(7).setForeground(new Color(0, 153, 51));
                    _verificar = false;
                    _suCambio = false;
                } else if (_checkBoxCredito.isSelected()) {
                    _label.get(6).setText("Desmarque la opción crédito");
                    _label.get(6).setForeground(Color.RED);
                    _verificar = false;
                    _suCambio = false;
                } else {
                    _label.get(7).setText("Cambio para el cliente");
                    _label.get(7).setForeground(new Color(0, 153, 51));
                    _verificar = true;
                    _suCambio = true;
                }
            } else if (pago < pagar) {
                _label.get(6).setText("Pago insuficiente");
                _label.get(6).setForeground(Color.RED);
                _cambio = pagar - pago;
                _suCambio = false;
                _verificar = _checkBoxCredito.isSelected();
                _label.get(7).setText("Importe faltante");
                _label.get(7).setForeground(Color.RED);
            } else if (pago == pagar) {
                _label.get(7).setText("Importe");
                _label.get(7).setForeground(Color.BLACK);
                _suCambio = false;
                _verificar = true;
                _cambio = 0.0;
            }
            _label.get(8).setText(_money + _format.decimal(_cambio));
        } else {
            _label.get(6).setText("Pago");
            _label.get(6).setForeground(Color.BLACK);
            _label.get(7).setText("Importe");
            _label.get(7).setForeground(Color.BLACK);
            _label.get(8).setText(_money + "0.00");
        }
    }

    public void cobrar() {
        if (_textField.get(1).getText().equals("")) {
            _label.get(6).setText("Ingrese el pago");
            _label.get(6).setForeground(Color.RED);
            _textField.get(1).requestFocus();
        } else {
            _pagos = _format.reconstruir(_textField.get(1).getText());
            if (_checkBoxCredito.isSelected()) {

            } else {
                if (_verificar) {
                    if (_pagos >= importe) {
                        try {
                            insertVentas();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    private void insertVentas() throws SQLException {
        try {
            if (!ventaTempo.isEmpty()) {
                var qr = new QueryRunner(true);
                getConn().setAutoCommit(false);
                if (_suCambio) {
                    _ingresoInicial = 0.0;
                    if (!ingresosIniciales.isEmpty()) {
                        _ingresoInicial += ingresosIniciales.get(0).getIngreso();
                        if (0 < _ingresoInicial) {
                            if (_ingresoInicial >= _cambio) {
                                _ingresoInicial -= _cambio;
                                String sqlIngresos = "UPDATE tcajas_ingresos SET Ingreso = ?"
                                        + " WHERE IdCajaIngreso =" + ingresosIniciales.get(0).getIdCajaIngreso();
                                Object[] dataIngresos = {
                                    _ingresoInicial
                                };
                                qr.update(getConn(), sqlIngresos, dataIngresos);

                                setVentas();
                            } else {
                                getIngresosVentas();
                            }
                        } else {
                            getIngresosVentas();
                        }
                    } else {
                        getIngresosVentas();
                    }
                } else {
                    setVentas();
                }
                getConn().commit();
                Reset();
            }
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void getIngresosVentas() {
        _ingresoVenta = 0.0;
        _ingresoInicial = 0.0;
        if (!ingresosVentas.isEmpty()) {
            var size = ingresosVentas.size();
            _ingresoVenta = ingresosVentas.get(size - 1).getIngreso();
            if (_cambio <= _ingresoVenta || _cambio <= ingresosTotales) {
                var qr = new QueryRunner(true);
                try {
                    if (!ingresosIniciales.isEmpty()) {
                        _cambio -= ingresosIniciales.get(0).getIngreso();
                        var sqlIngresos = "UPDATE tcajas_ingresos SET Ingreso = ?"
                                + " WHERE IdCajaIngreso =" + ingresosIniciales.get(0).getIdCajaIngreso();
                        Object[] dataIngresos = {0.0};

                        qr.update(getConn(), sqlIngresos, dataIngresos);
                    }
                    _ingresoVenta -= _cambio;
                    var sqlIngresos = "UPDATE tcajas_reportes SET Ingreso = ?"
                            + " WHERE IdCajaReporte ="
                            + ingresosVentas.get(size - 1).getIdCajaReporte();
                    Object[] dataIngresos = {_ingresoVenta};

                    qr.update(getConn(), sqlIngresos, dataIngresos);
                    setVentas();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } else {
            JOptionPane.showConfirmDialog(null, "No hay ingresos", "Ventas",
                    JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setVentas() {
        var qr = new QueryRunner(true);
        try {

        } catch (Exception e) {
        }
    }

    public void Reset() {
        _pagos = 0.0;
        _ingresoInicial = 0.0;
        _accion = "insert";
        var model = new SpinnerNumberModel(
                1.0, // Dato visualizado al inicio en el spinner 
                1.0, // Límite inferior 
                1000.0, // Límite superior 
                1.0 // incremento-decremento 
        );
        _spinnerCantidad.setModel(model);
        searchVentasTempo(_textField.get(0).getText(), _checkBoxEliminar.isSelected());
        importes();
        if (modelo1 != null) {
            modelo1.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    var ejecutar = true;
                    try {
                        var data = _tableVentasTemporal.getModel();
                        int filas = data.getRowCount();
                        for (int i = 0; i < filas; i++) {
                            var numeric = String.valueOf(data.getValueAt(i, 5));
                            if (!numeric.matches("[0-9]*")) {
                                JOptionPane.showConfirmDialog(null, "Ingrese un valor valido", "Ventas", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
                                ejecutar = false;
                                break;
                            }
                        }
                        if (ejecutar && e.getColumn() == 5) {
                            saveVentasTempo();
                        }
                    } catch (Exception ex) {
                    }
                }

            });
            var model1 = new SpinnerNumberModel(
                    10.0, // Dato visualizado al inicio en el spinner 
                    1.0, // Límite inferior 
                    100.0, // Límite superior 
                    1.0 // incremento-decremento 
            );
            _spinnerPaginas.setModel(model1);
            if (listTemporary != null && !listTemporary.isEmpty()) {
                _paginadorCompras = new Paginador<TVentas_temporal>(listTemporary, _label.get(0), _reg_por_pagina);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PAGINADOR">
    public int _seccion = 0;
    private Paginador<TVentas_temporal> _paginadorCompras;

    public void Paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (_seccion) {
                    case 0:
                        if (!listTemporary.isEmpty()) {
                            _num_pagina = _paginadorCompras.primero();
                        }
                        break;
                }
                break;
            case "Anterior":
                switch (_seccion) {
                    case 0:
                        if (!listTemporary.isEmpty()) {
                            _num_pagina = _paginadorCompras.anterior();
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (_seccion) {
                    case 0:
                        if (!listTemporary.isEmpty()) {
                            _num_pagina = _paginadorCompras.siguiente();
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (_seccion) {
                    case 0:
                        if (!listTemporary.isEmpty()) {
                            _num_pagina = _paginadorCompras.ultimo();
                        }
                        break;
                }
                break;
        }
        switch (_seccion) {
            case 0:
                searchVentasTempo(_textField.get(0).getText(), _checkBoxEliminar.isSelected());
                break;
        }
    }

    public void Registro_Paginas() {
        _num_pagina = 1;
        var value = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = value.intValue();
        switch (_seccion) {
            case 0:
                if (!listTemporary.isEmpty()) {
                    _paginadorCompras = new Paginador<TVentas_temporal>(listTemporary,
                            _label.get(0), _reg_por_pagina);
                    searchVentasTempo(_textField.get(0).getText(), _checkBoxEliminar.isSelected());
                }
                break;
        }
    }
    //</editor-fold>
}
