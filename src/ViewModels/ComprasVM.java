package ViewModels;

import Conexion.Consult;
import Library.*;
import Models.Compras.TCompras;
import Models.Compras.TCompras_temporal;
import Models.Proveedor.TProveedor;
import Models.Proveedor.TReportes_proveedor;
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
    private JCheckBox _checkBoxCredito, _checkBoxCredito1;
    private JCheckBox _checkBoxCreditos;
    private JCheckBox _checkBoxTodos;
    private JCheckBox _checkBoxEliminar;
    private static TUsuarios _dataUsuario;
    private FormatDecimal _format;
    private String _accion = "insert", _money;

    private DefaultTableModel modelo1, modelo2, modelo3;
    private JTable _tableComprasTemporal;
    public int _reg_por_pagina = 10;
    public int _num_pagina = 1;
    public int _seccion1, _seccion = 0;
    private Calendario cal;
    private Codigos _codigos;
    private JSpinner _spinnerPaginas;

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
        _checkBoxEliminar = (JCheckBox) objetos[5];
        _checkBoxCredito1 = (JCheckBox) objetos[6];
        _spinnerPaginas = (JSpinner) objetos[7];
        _format = new FormatDecimal();
        _money = ConfigurationVM.Money;
        _codigos = new Codigos();
        Reset();
        ResetPagos();
    }

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE COMPRAS">
    private Double _importe, _precio, _pago, _deuda, _cambio;
    private int _cantidad, _idProveedor;

    public void importes() {
        switch (_seccion1) {
            case 0:
                _cantidad = _textField.get(1).getText().equals("") ? 0 : Integer.valueOf(_textField.get(1).getText());
                _precio = _textField.get(2).getText().equals("") ? 0 : _format.reconstruir(_textField.get(2).getText());
                _importe = _precio * _cantidad;
                _label.get(3).setText(_money + _format.decimal(_importe));
                break;
            case 1:
                _label.get(6).setText("Ingrese el pago");
                _label.get(6).setForeground(new Color(0, 153, 51));
                _pago = _textField.get(5).getText().equals("") ? 0.0 : _format.reconstruir(_textField.get(5).getText());
                _deuda = _importeDeuda - _pago;
                if (_importeDeuda < _pago) {
                    _label.get(7).setText("Cambio del proveedor al sistema");
                    _label.get(7).setForeground(Color.RED);
                    _cambio = Math.abs(_deuda);
                    _label.get(8).setForeground(Color.RED);
                    _label.get(8).setText(_money + _format.decimal(_cambio));
                    _deuda = 0.0;
                } else {
                    _cambio = 0.0;
                    _label.get(7).setText("Deuda");
                    _label.get(7).setForeground(new Color(102, 102, 102));
                    _label.get(8).setForeground(new Color(102, 102, 102));
                    _label.get(8).setText(_money + _format.decimal(_deuda));
                }
                break;
        }
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

    private Integer cantEfectivo = 0;
    public List<TCompras_temporal> listTemporalCompras;

    public void SearchCompras(String campo, boolean eliminar) {
        cantEfectivo = 0;
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
                if (!item.getCredito()) {
                    cantEfectivo += 1;
                }
            });
            if (cantEfectivo.equals(0)) {
                _textField.get(5).setEnabled(false);
                _textField.get(5).setText("0.0");
                _checkBoxCredito1.setEnabled(true);
                _checkBoxCredito1.setEnabled(false);
            } else {
                _textField.get(5).setEnabled(true);
                _checkBoxCredito1.setEnabled(false);
                _checkBoxCredito1.setEnabled(true);
            }
        } else {
            _textField.get(5).setEnabled(false);
            _checkBoxCredito1.setEnabled(false);
            _checkBoxCredito1.setEnabled(false);
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
        var precio = modelo1.getValueAt(filas, 3);
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

    public void eliminar() {
        final QueryRunner qr = new QueryRunner(true);
        var listEliminar = new ArrayList<Integer>();
        var data = _tableComprasTemporal.getModel();
        int cols = data.getColumnCount();
        int fils = data.getRowCount();
        if (0 < fils) {
            for (int i = 0; i < fils; i++) {
                if ((boolean) data.getValueAt(i, 6)) {
                    listEliminar.add((Integer) data.getValueAt(i, 0));
                }
            }
            if (0 < listEliminar.size()) {
                if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar " + listEliminar.size() + " registro/s?", "Eliminar compras",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                    var sql = "DELETE FROM tcompras_temporal WHERE IdCompra LIKE ?";
                    listEliminar.forEach(item -> {
                        try {
                            qr.update(getConn(), sql, "%" + item + "%");
                        } catch (Exception e) {

                        }
                    });
                    Reset();
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
        SearchCompras(_textField.get(4).getText(), _checkBoxEliminar.isSelected());
        if (0 < listTemporalCompras.size()) {
            _paginadorCompras = new Paginador<TCompras_temporal>(listTemporalCompras, _label.get(11), _reg_por_pagina);
        }
        var model = new SpinnerNumberModel(
                10.0,
                1.0,
                100.0,
                1.0
        );
        _spinnerPaginas.setModel(model);

        getImporteTotal();
        _checkBoxCreditos.setSelected(false);
        _checkBoxCreditos.setForeground(new Color(102, 102, 102));
        _checkBoxTodos.setSelected(false);
        _checkBoxTodos.setForeground(new Color(102, 102, 102));
        _checkBoxEliminar.setSelected(false);
        _checkBoxEliminar.setForeground(new Color(102, 102, 102));
    }

    private Double _importeDeuda;
    private TProveedor dataProveedor;

    public void getImporteTotal() {
        _importeDeuda = 0.0;
        List<TCompras_temporal> list;
        if (_checkBoxTodos.isSelected()) {
            list = Compras_temporal().stream().filter(p -> p.getIdUsuario() == _dataUsuario.getIdUsuario()).collect(Collectors.toList());
        } else {
            list = Compras_temporal().stream().filter(p -> p.getCredito().equals(_checkBoxCreditos.isSelected()) && p.getIdUsuario() == _dataUsuario.getIdUsuario()).collect(Collectors.toList());
        }
        if (0 < list.size()) {
            list.forEach(item -> {
                _importeDeuda += item.getImporte();
            });
            switch (_seccion1) {
                case 0:
                    _label.get(10).setText(_money + _format.decimal(_importeDeuda));
                    break;
                case 1:
                    _label.get(5).setText(_money + _format.decimal(_importeDeuda));
                    _label.get(8).setText(_money + _format.decimal(_importeDeuda));
                    dataProveedor = proveedores().stream().filter(p -> p.getID() == list.get(0).getIdProveedor()).collect(Collectors.toList()).get(0);
                    _label.get(9).setText(dataProveedor.getProveedor());
                    break;
            }
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE PAGOS">
    public void efectuarCompras() {
        var list = Compras_temporal().stream().filter(p -> p.getIdUsuario() == _dataUsuario.getIdUsuario()).collect(Collectors.toList());
        if (list.size() > 0) {
            if (_textField.get(5).getText().equals("")) {
                _label.get(6).setText("Ingrese el pago");
                _label.get(6).setForeground(Color.RED);
                _textField.get(5).requestFocus();
            } else {
                try {
                    if (_importeDeuda > _pago) {
                        _label.get(6).setText("Complete el pago o solicite crédito");
                        _label.get(6).setForeground(Color.RED);
                        if (_checkBoxCredito1.isSelected()) {
                            guardar(list);
                        }
                    } else {
                        guardar(list);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        } else {
            JOptionPane.showMessageDialog(null, "Escoja los productos", "Producto", JOptionPane.QUESTION_MESSAGE);
        }
    }

    private Double credito = 0.0, efectivo = 0.0;
    private int cantidad = 0;

    public void guardar(List<TCompras_temporal> list) throws SQLException {
        try {
            cal = new Calendario();
            final QueryRunner qr = new QueryRunner(true);
            var dateNow = new Date();

            var dataReport = ReporteProveedor().stream().filter(p -> p.getIdProveedor() == dataProveedor.getID()).reduce((first, second) -> second).orElse(new TReportes_proveedor());
            var nameUser = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();
            var codes = Compras().stream().filter(p -> p.getIdProveedor() == dataProveedor.getID() && cal.getYear(p.getFecha()) == cal.getYear(dateNow)).reduce((first, second) -> second).orElse(new TCompras());
            var code = codes != null ? codes.getTicket() : null;
            var ticket = _codigos.codesTickets(code);
            list.forEach(item -> {
                try {
                    var sqlCompra = "INSERT INTO tcompras(Descripcion,Cantidad, Precio,Importe,IdProveedor,IdUsuario,Credito,Fecha,Ticket) VALUES (?,?,?,?,?,?,?,?,?)";
                    Object[] dataCompra = {
                        item.getDescripcion(),
                        item.getCantidad(),
                        item.getPrecio(),
                        item.getImporte(),
                        item.getIdProveedor(),
                        item.getIdUsuario(),
                        item.getCredito(),
                        dateNow,
                        ticket
                    };
                    qr.insert(getConn(), sqlCompra, new ColumnListHandler(), dataCompra);
                    var data = Compras().stream().filter(p -> p.getIdProveedor() == dataProveedor.getID() && cal.getYear(p.getFecha()) == cal.getYear(dateNow)).reduce((first, second) -> second).orElse(new TCompras());
                    String sqlProducto = "INSERT INTO ttemporal_productos (IdProducto,IdUsuario) VALUES(?,?)";
                    Object[] dataProducto = {
                        data.getIdCompra(),
                        item.getIdUsuario()
                    };

                    qr.insert(getConn(), sqlProducto, new ColumnListHandler(), dataProducto);
                    cantidad += item.getCantidad();
                    if (item.getCredito()) {
                        credito += item.getImporte();
                    } else {
                        efectivo += item.getImporte();
                    }
                } catch (Exception e) {

                }
            });
            var deuda = dataReport.getDeudaActual() + _deuda + credito;
            var reportesCompra = "INSERT INTO treportes_compras (Ticket,Productos,Efectivo,Credito,Pago,Deuda,Cambio,Fecha,IdProveedor) VALUES (?,?,?,?,?,?,?,?,?)";
            Object[] dataCompra = {
                ticket,
                cantidad,
                efectivo,
                credito,
                _pago,
                deuda,
                _cambio,
                dateNow,
                dataProveedor.getID()
            };
            qr.insert(getConn(), reportesCompra, new ColumnListHandler(), dataCompra);

            var reporte = "INSERT INTO treportes_proveedor SET DeudaActual = ?, Deuda = ?,FechaDeuda = ?,Ticket = ? WHERE IdReporte =" + dataReport.getIdReporte();
            Object[] data = {
                deuda,
                deuda,
                dateNow,
                ticket
            };
            qr.update(getConn(), reporte, data);

            Ticket Ticket1 = new Ticket();
            Ticket1.TextoCentro("Sistema de ventas");
            Ticket1.TextoIzquierda("Dirección");
            Ticket1.TextoIzquierda("Monterrey, Nuevo León");
            Ticket1.TextoIzquierda("Tel. 5522001025");
            Ticket1.LineasGuion();
            Ticket1.TextoCentro("Factura");
            Ticket1.LineasGuion();
            Ticket1.TextoIzquierda("Factura: " + ticket);
            Ticket1.TextoIzquierda("Cliente: " + dataProveedor.getProveedor());
            Ticket1.TextoIzquierda("Fecha: " + dateNow);
            Ticket1.TextoIzquierda("Usuario: " + nameUser);
            if (!efectivo.equals(0.0)) {
                Ticket1.LineasGuion();
                Ticket1.TextoCentro("Productos de contado");
                Ticket1.AgregarArticulo("Producto", "Cantidad", "Importe");
                list.stream().filter(p -> p.getCredito().equals(false)).collect(Collectors.toList()).forEach(item -> {
                    var Amount = _format.decimal(item.getImporte());
                    Ticket1.AgregarArticulo(item.getDescripcion(), String.valueOf(item.getCantidad()), _money + Amount);
                });
                var formatPago = _format.decimal(_pago);
                Ticket1.LineasGuion();
                Ticket1.TextoCentro("Deuda y pago generado");
                Ticket1.AgregarTotales("Total a pagar", efectivo, _money);
                Ticket1.TextoExtremo("Pago: ", _money + formatPago);
                if (_checkBoxCredito1.isSelected()) {
                    Ticket1.TextoExtremo("Importe faltante", _money + _format.decimal(_deuda));
                } else {
                    if (_pago >= efectivo) {
                        Ticket1.TextoExtremo("Cambio: ", _money + _format.decimal(_cambio));
                    }
                }
            }
            if (!credito.equals(0.0)) {
                Ticket1.LineasGuion();
                Ticket1.TextoCentro("Productos a crédito");
                Ticket1.AgregarArticulo("Producto", "Cantidad", "Importe");
                list.stream().filter(p -> p.getCredito().equals(true)).collect(Collectors.toList()).forEach(item -> {
                    var Amount = _format.decimal(item.getImporte());
                    Ticket1.AgregarArticulo(item.getDescripcion(), String.valueOf(item.getCantidad()), _money + Amount);
                });
                Ticket1.LineasGuion();
                Ticket1.TextoCentro("Deuda generada");
                Ticket1.AgregarTotales("Total a pagar", credito, _money);
            }
            if (!_deuda.equals(0.0) || !credito.equals(0.0)) {
                Ticket1.LineasGuion();
                Ticket1.TextoCentro("Deuda total generada");
                if (!efectivo.equals(0.0)) {
                    Ticket1.AgregarTotales("Total a pagar", credito + _deuda, _money);
                } else {
                    Ticket1.AgregarTotales("Total a pagar", credito, _money);
                }
            }
            Ticket1.TextoCentro("TALLER POO");
            Ticket1.print();
            var sql = "DELETE FROM tcompras_temporal WHERE IdCompra LIKE ?";
            list.forEach(item -> {
                try {
                    qr.update(getConn(), sql, "%" + item.getIdCompra() + "%");
                } catch (Exception e) {
                }
            });
            getConn().commit();
            ResetPagos();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void ResetPagos() {
        credito = 0.0;
        efectivo = 0.0;
        cantidad = 0;
        _textField.get(5).setText("");
        _checkBoxCredito1.setSelected(false);
        SearchCompras(_textField.get(4).getText(), _checkBoxEliminar.isSelected());
        getImporteTotal();
        _checkBoxCreditos.setSelected(false);
        _checkBoxCreditos.setForeground(new Color(102, 102, 102));
        _checkBoxTodos.setSelected(false);
        _checkBoxTodos.setForeground(new Color(102, 102, 102));
        _checkBoxEliminar.setSelected(false);
        _checkBoxEliminar.setForeground(new Color(102, 102, 102));
    }

    // </editor-fold>
    private Paginador<TCompras_temporal> _paginadorCompras;

    //<editor-fold defaultstate="collapsed" desc="PAGINADOR">
    public void Paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (_seccion) {
                    case 0:
                        if (0 < listTemporalCompras.size()) {
                            _num_pagina = _paginadorCompras.primero();
                        }
                        break;
                }
                break;
            case "Anterior":
                switch (_seccion) {
                    case 0:
                        if (0 < listTemporalCompras.size()) {
                            _num_pagina = _paginadorCompras.anterior();
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (_seccion) {
                    case 0:
                        if (0 < listTemporalCompras.size()) {
                            _num_pagina = _paginadorCompras.siguiente();
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (_seccion) {
                    case 0:
                        if (0 < listTemporalCompras.size()) {
                            _num_pagina = _paginadorCompras.ultimo();
                        }
                        break;
                }
                break;
        }
        switch (_seccion) {
            case 0:
                SearchCompras(_textField.get(4).getText(), _checkBoxEliminar.isSelected());
                break;
        }
    }

    public void Registro_Paginas() {
        _num_pagina = 1;
        Number value = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = value.intValue();
        switch (_seccion) {
            case 0:
                if (0 < listTemporalCompras.size()) {
                    _paginadorCompras = new Paginador<>(listTemporalCompras, _label.get(11), _reg_por_pagina);
                }
                SearchCompras(_textField.get(4).getText(), _checkBoxEliminar.isSelected());
                break;
        }
    }
    //</editor-fold>
}
