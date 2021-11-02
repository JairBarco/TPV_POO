package ViewModels;

import Conexion.Consult;
import Library.*;
import static Library.Objetos.*;
import Models.Proveedor.TPagos_proveedor;
import Models.Proveedor.TProveedor;
import Models.Usuario.TUsuarios;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class ProveedorVM extends Consult {

    private String _accion = "insert", _money;
    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textField;
    private JTable _tableProveedor, _tableReporte, _tablePagosCuotas;
    private DefaultTableModel modelo1, modelo2, modelo3;
    private JSpinner _spinnerPaginas;
    private Paginador<TProveedor> _paginadorProveedor, _paginadorReportes;
    private Paginador<TPagos_proveedor> _paginadorPagos;
    private FormatDecimal _format;
    private Codigos _codigos;
    private int _reg_por_pagina = 10;
    private int _num_pagina = 1;

    private int _idProveedorReport, _idReport;
    private Double _deudaActual = 0.0, _deuda = 0.0, _pago = 0.0, _cambio = 0.0, _mensual = 0.0;
    private Double _deudaActualProveedor = 0.0;
    private String _ticketCuota, nameProveedor;

    private static TUsuarios _dataUsuario;
    private SimpleDateFormat formateador;

    public int _seccion = 1, _seccion1 = 0;
    private DateChooserCombo _dateChooser1, _dateChooser2;
    private Ticket Ticket1 = new Ticket();

    private JRadioButton _radioButton1, _radioButton2;

    public ProveedorVM(TUsuarios dataUsuario) {
        _dataUsuario = dataUsuario;
        formateador = new SimpleDateFormat("dd/MM/yyyy");
    }

    public ProveedorVM(Object[] objects, ArrayList<JLabel> label, ArrayList<JTextField> textField) {
        _label = label;
        _textField = textField;
        _tableProveedor = (JTable) objects[0];
        _spinnerPaginas = (JSpinner) objects[1];
        _tableReporte = (JTable) objects[2];
        _dateChooser1 = (DateChooserCombo) objects[3];
        _dateChooser2 = (DateChooserCombo) objects[4];
        _tablePagosCuotas = (JTable) objects[5];
        _radioButton1 = (JRadioButton) objects[6];
        _radioButton2 = (JRadioButton) objects[7];
        _money = ConfigurationVM.Money;
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        _format = new FormatDecimal();
        _codigos = new Codigos();
        Reset();
        ResetReport();
        ResetFormaPago();
    }

    //<editor-fold defaultstate="collapsed" desc="CÓDIGO DE REGISTRAR PROVEEDOR">
    public void RegistrarProveedor() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el proveedor");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (_textField.get(1).getText().equals("")) {
                _label.get(1).setText("Ingrese el Email");
                _label.get(1).setForeground(Color.RED);
                _textField.get(1).requestFocus();
            } else {
                if (!eventos.isEmail(_textField.get(1).getText())) {
                    _label.get(1).setText("Ingrese un Email válido");
                    _label.get(1).setForeground(Color.RED);
                    _textField.get(1).requestFocus();
                } else {
                    if (_textField.get(2).getText().equals("")) {
                        _label.get(2).setText("Ingrese el Teléfono");
                        _label.get(2).setForeground(Color.RED);
                        _textField.get(2).requestFocus();
                    } else {
                        if (_textField.get(3).getText().equals("")) {
                            _label.get(3).setText("Ingrese la dirección");
                            _label.get(3).setForeground(Color.RED);
                            _textField.get(3).requestFocus();
                        } else {
                            List<TProveedor> listEmail = proveedores().stream().
                                    filter(u -> u.getEmail().equals(_textField.get(1).getText())).
                                    collect(Collectors.toList());
                            try {
                                switch (_accion) {
                                    case "insert":
                                        if (listEmail.size() == 0) {
                                            SaveData();
                                        } else {
                                            if (!listEmail.isEmpty()) {
                                                _label.get(1).setText("El email ya ha sido registrado");
                                                _label.get(1).setForeground(Color.RED);
                                                _textField.get(1).requestFocus();
                                            }
                                        }
                                        break;
                                    case "update":
                                        if (listEmail.size() > 0) {
                                            if (listEmail.get(0).getID() == _idProveedor) {
                                                SaveData();
                                            } else {
                                                if (listEmail.get(0).getID() != _idProveedor) {
                                                    _label.get(1).setText("El email ya está registrado");
                                                    _label.get(1).setForeground(Color.RED);
                                                    _textField.get(1).requestFocus();
                                                }
                                            }
                                        } else {
                                            if (listEmail.size() == 0) {
                                                SaveData();
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

    private void SaveData() throws SQLException {
        try {
            final QueryRunner qr = new QueryRunner(true);
            getConn().setAutoCommit(false);
            byte[] image = UploadImage.getImageByte();
            if (image == null) {
                image = Objetos.uploadImage.getTransFoto(_label.get(4));
            }
            switch (_accion) {
                case "insert":
                    String sqlProveedor1 = "INSERT INTO tproveedor(Proveedor,Email, Telefono,Direccion,Fecha,Imagen) VALUES (?,?,?,?,?,?)";
                    Object[] dataProveedor1 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        new Date(),
                        image,};
                    qr.insert(getConn(), sqlProveedor1, new ColumnListHandler(), dataProveedor1);

                    String sqlReport = "INSERT INTO treportes_proveedor (Deuda,Mensual,Cambio,DeudaActual,FechaDeuda,UltimoPago,FechaPago,Ticket,IdProveedor) VALUES (?,?,?,?,?,?,?,?,?)";
                    List<TProveedor> proveedor = proveedores();
                    Object[] dataReport = {
                        0,
                        0,
                        0,
                        0,
                        null,
                        0,
                        null,
                        "0000000000",
                        proveedor.get(proveedor.size() - 1).getID(),};
                    qr.insert(getConn(), sqlReport, new ColumnListHandler(), dataReport);
                    break;

                case "update":

                    break;
            }
            getConn().commit();
            Reset();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private List<TProveedor> proveedorFilter;

    public void SearchProveedores(String campo) {
        String[] titulos = {"Id", "Proveedor", "Email", "Telefono", "Direccion", "Imagen"};
        modelo1 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.equals("")) {
            proveedorFilter = proveedores();
        } else {
            proveedorFilter = proveedores().stream()
                    .filter(C -> C.getProveedor().startsWith(campo) || C.getEmail().startsWith(campo))
                    .collect(Collectors.toList());
        }
        var data = proveedorFilter;
        var list = data.stream().skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
        if (!list.isEmpty()) {
            proveedorFilter.forEach(item -> {
                Object[] registros = {
                    item.getID(),
                    item.getProveedor(),
                    item.getEmail(),
                    item.getTelefono(),
                    item.getDireccion(),
                    item.getImagen()
                };
                modelo1.addRow(registros);
            });
        }
        _tableProveedor.setModel(modelo1);
        _tableProveedor.setRowHeight(30);
        _tableProveedor.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableProveedor.getColumnModel().getColumn(0).setMinWidth(0);
        _tableProveedor.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableProveedor.getColumnModel().getColumn(5).setMaxWidth(0);
        _tableProveedor.getColumnModel().getColumn(5).setMinWidth(0);
        _tableProveedor.getColumnModel().getColumn(5).setPreferredWidth(0);
    }
    private int _idProveedor = 0;

    public void getProveedor() {
        _accion = "update";
        int filas = _tableProveedor.getSelectedRow();
        _idProveedor = (Integer) modelo1.getValueAt(filas, 0);
        _textField.get(0).setText((String) modelo1.getValueAt(filas, 1));
        _textField.get(1).setText((String) modelo1.getValueAt(filas, 2));
        _textField.get(2).setText((String) modelo1.getValueAt(filas, 3));
        _textField.get(3).setText((String) modelo1.getValueAt(filas, 4));
        uploadImage.byteImage(_label.get(4), (byte[]) modelo1.getValueAt(filas, 5));
    }

    public void Reset() {
        _seccion = 0;
        _accion = "insert";
        String[] reset = {"Proveedor", "Email", "Telefono", "Direccion"};
        _textField.forEach(item -> {
            item.setText("");
        });

        for (int i = 0; i > 4; i++) {
            _label.get(i).setText(reset[i]);
            _label.get(i).setForeground(new Color(0, 0, 0));
        }

        _label.get(4).setIcon(new ImageIcon(getClass().getClassLoader().getResource("Resources/agregar_imagen.png")));
        SearchProveedores("");
        SpinnerNumberModel model = new SpinnerNumberModel(
                10.0,
                1.0,
                100.0,
                1.0
        );
        _spinnerPaginas.setModel(model);
        proveedorFilter = proveedores();
        if (!proveedorFilter.isEmpty()) {
            _paginadorProveedor = new Paginador<>(proveedorFilter, _label.get(5), _reg_por_pagina);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CÓDIGO DE PAGOS Y REPORTES">
    List<TProveedor> reporteFilter;
    private String _formaPago;

    public void SearchReportes(String valor) {
        String[] titulos = {"Id", "Proveedor", "Email", "Dirección", "Telefono"};
        modelo2 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (valor.equals("")) {
            reporteFilter = proveedores();
        } else {
            reporteFilter = proveedores().stream()
                    .filter(C -> C.getProveedor().startsWith(valor) || C.getEmail().startsWith(valor))
                    .collect(Collectors.toList());
        }
        var data = reporteFilter;
        var list = data.stream().skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
        if (!list.isEmpty()) {
            reporteFilter.forEach(item -> {
                Object[] registros = {
                    item.getID(),
                    item.getProveedor(),
                    item.getEmail(),
                    item.getDireccion(),
                    item.getTelefono(),};
                modelo2.addRow(registros);
            });
        }
        _tableReporte.setModel(modelo2);
        _tableReporte.setRowHeight(30);
        _tableReporte.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableReporte.getColumnModel().getColumn(0).setMinWidth(0);
        _tableReporte.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void getReportProveedor() {
        int filas = _tableReporte.getSelectedRow();
        _idProveedorReport = (Integer) modelo2.getValueAt(filas, 0);
        var proveedorFilter1 = Reportes_Proveedores(_idProveedorReport);
        if (!proveedorFilter1.isEmpty()) {
            var proveedor = proveedorFilter1.get(0);
            _idReport = proveedor.getIdReporte();
            nameProveedor = proveedor.getProveedor();
            _label.get(6).setText(nameProveedor);
            _deudaActual = (Double) proveedor.getDeudaActual();
            _deuda = (Double) proveedor.getDeuda();
            _label.get(7).setText(_money + _format.decimal(_deudaActual));
            _label.get(8).setText(_money + _format.decimal((Double) proveedor.getUltimoPago()));
            _ticketCuota = proveedor.getTicket();
            _label.get(9).setText(_ticketCuota);
            if (null != proveedor.getFechaPago()) {
                _label.get(10).setText(proveedor.getFechaPago().toString());

            }
            _label.get(11).setText(_money + _format.decimal((Double) proveedor.getMensual()));
            Pagos();
            historialPagos(false);

            //FORMA DE PAGO
            _formaPago = proveedor.getFormaPago();
            _mensual = proveedor.getMensual();
            
            if(_formaPago == null || _mensual.equals(0.0)){
                _label.get(24).setText("Establezca una forma de pago");
            } else {
                
            }
            _label.get(21).setText(_money + _format.decimal(_deudaActual));
            getCuotas();
        }
    }

    public void Pagos() {
        if (!_textField.get(4).getText().isEmpty()) {
            _label.get(12).setText("Ingrese el pago");
            _label.get(12).setForeground(Color.RED);
            if (_idReport == 0) {
                _label.get(12).setText("Seleccione un proveedor");
                _label.get(12).setForeground(Color.RED);
            } else {
                if (!_textField.get(4).getText().isEmpty()) {
                    _pago = _format.reconstruir(_textField.get(4).getText());
                    var dataReport = ReporteProveedor().stream()
                            .filter(u -> u.getIdReporte() == _idReport).collect(Collectors.toList()).get(0);
                    _mensual = dataReport.getMensual();
                    if (_pago >= _mensual) {
                        if (Objects.equals(_pago, _deudaActual) || _pago > _deudaActual) {
                            _cambio = _pago - _deudaActual;
                            _label.get(12).setText("Cambio: " + _money + _format.decimal(_cambio));
                            _label.get(12).setForeground(Color.RED);
                            _textField.get(7).setText(_money + "0.00");
                            _deudaActual = 0.0;
                            _deudaActualProveedor = 0.0;
                        } else {
                            _cambio = _pago - _mensual;
                            _label.get(12).setText("Cambio: " + _money + _format.decimal(_cambio));
                            _label.get(12).setForeground(Color.RED);
                            _deudaActualProveedor = _deudaActual - _mensual;
                            _label.get(7).setText(_money + _format.decimal(_deudaActualProveedor));
                        }
                    }
                } else if (Objects.equals(_pago, _mensual)) {
                    _deudaActualProveedor = _deudaActual - _mensual;
                    _label.get(7).setText(_money + _format.decimal(_deudaActualProveedor));
                } else {
                    _cambio = 0.0;
                    var deuda = _mensual - _pago;
                    _label.get(12).setText("Importe faltante: " + _money + _format.decimal(deuda));
                    _label.get(12).setForeground(Color.RED);
                }
            }
        } else {
            _label.get(12).setText("Ingrese el pago");
            _label.get(7).setText(_money + _format.decimal(_deudaActual));
        }
    }

    public void EjecutarPago() throws SQLException {
        final QueryRunner qr = new QueryRunner(true);
        if (Objects.equals(_idReport, 0)) {
            _label.get(12).setText("Seleccione un proveedor");
        } else {
            if (_textField.get(4).getText().isEmpty()) {
                _label.get(12).setText("Ingrese el pago");
                _textField.get(4).requestFocus();
            } else {
                String fecha = new Calendario().getFecha();
                var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();
                if (!_deuda.equals(0) || _deuda.equals(0.0)) {
                    if (_pago >= _mensual) {
                        try {
                            getConn().setAutoCommit(false);
                            String ticket = _codigos.codesTickets(_ticketCuota);
                            String query1 = "INSERT INTO tpagos_proveedor(Deuda,Saldo, Pago,Cambio,Fecha,Ticket,IdUsuario,Usuario,IdProveedor,FechaDeuda,Mensual)"
                                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                            var dataReport = ReporteProveedor().stream().filter(u -> u.getIdReporte() == _idProveedorReport)
                                    .collect(Collectors.toList()).get(0);
                            Object[] data1 = {
                                _deuda,
                                _deudaActualProveedor,
                                _pago,
                                _cambio,
                                new Date(),
                                ticket,
                                _dataUsuario.getIdUsuario(),
                                usuario,
                                _idProveedorReport,
                                dataReport.getFechaDeuda(),
                                dataReport.getMensual()
                            };
                            qr.insert(getConn(), query1, new ColumnListHandler(), data1);
                            if (_deudaActualProveedor.equals(0.0)) {
                                String query2 = "UPDATE treportes_proveedor SET Deuda = ?,"
                                        + "Mensual = ?,FechaDeuda = ?,DeudaActual = ?,"
                                        + "UltimoPago = ?,Cambio = ?,FechaPago = ?, Ticket = ?"
                                        + "WHERE IdReporte =" + _idReport;
                                Object[] data2 = {
                                    0.0,
                                    0.0,
                                    null,
                                    0.0,
                                    0.0,
                                    0.0,
                                    null,
                                    "0000000000"
                                };
                                qr.update(getConn(), query2, data2);
                            } else {
                                String query2 = "UPDATE treportes_proveedor SET DeudaActual = ?,UltimoPago = ?,Cambio = ?,"
                                        + "FechaPago = ?, Ticket = ? WHERE IdReporte = " + _idReport;
                                Object[] data2 = {
                                    _deudaActualProveedor,
                                    _pago,
                                    _cambio,
                                    new Date(),
                                    ticket
                                };
                                qr.update(getConn(), query2, data2);
                            }
                            Ticket1.TextoCentro("Sistema de ventas");
                            Ticket1.TextoIzquierda("Dirección");
                            Ticket1.TextoIzquierda("Monterrey, Nuevo León");
                            Ticket1.TextoIzquierda("Tel. 5522001025");
                            Ticket1.LineasGuion();
                            Ticket1.TextoCentro("Factura");
                            Ticket1.LineasGuion();
                            Ticket1.TextoIzquierda("Factura: " + ticket);
                            Ticket1.TextoIzquierda("Cliente: " + nameProveedor);
                            Ticket1.TextoIzquierda("Fecha: " + fecha);
                            Ticket1.TextoIzquierda("Usuario: " + usuario);
                            Ticket1.LineasGuion();
                            Ticket1.TextoCentro("Su credito: " + _money + _format.decimal(_deuda));
                            Ticket1.TextoExtremo("Cuotas por mes: ", _money + _format.decimal(_mensual));
                            Ticket1.TextoExtremo("Deuda anteror", _money + _format.decimal(_deudaActual));
                            Ticket1.TextoExtremo("Pago:", _money + _format.decimal(_pago));
                            Ticket1.TextoExtremo("Cambio:", _money + _format.decimal(_cambio));
                            Ticket1.TextoExtremo("Deuda Actual:", _money + _format.decimal(_deudaActualProveedor));
                            Ticket1.TextoCentro("TPOO");
                            Ticket1.print();
                            getConn().commit();
                            ResetReport();
                        } catch (Exception e) {
                            getConn().rollback();
                            JOptionPane.showMessageDialog(null, e);
                        }
                    }
                } else {
                    _label.get(12).setText("El cliente no tiene deuda");
                }
            }
        }
    }

    private List<TPagos_proveedor> listPagos;

    public void historialPagos(boolean filtrar) {
        listPagos = new ArrayList<>();
        try {
            _dateChooser1.setFormat(3);
            _dateChooser2.setFormat(3);
            var cal = new Calendario();
            String[] titulos = {"ID", "Deuda", "Saldo", "Pago", "Cambio", "Fecha", "Ticket", "Fecha Deuda", "Mensual"};
            modelo3 = new DefaultTableModel(null, titulos);
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            if (filtrar) {
                if (date2.after(date1) || date1.equals(date2)) {
                    var listPagos1 = new ArrayList<TPagos_proveedor>();
                    var pagos = Pagos_proveedor().stream().filter(u -> u.getIdProveedor() == _idProveedorReport).collect(Collectors.toList());
                    formateador = new SimpleDateFormat("yyyy/MM/dd");
                    _dateChooser1.setFormat(3);
                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));

                    for (TPagos_proveedor pago : pagos) {
                        var date5 = pago.getFecha();
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }

                    _dateChooser2.setFormat(3);
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));

                    for (TPagos_proveedor pago : listPagos1) {
                        var date8 = pago.getFecha();

                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getId(),
                                _money + _format.decimal(pago.getDeuda()),
                                _money + _format.decimal(pago.getSaldo()),
                                _money + _format.decimal(pago.getPago()),
                                _money + _format.decimal(pago.getCambio()),
                                pago.getFecha(),
                                pago.getTicket(),
                                pago.getFechaDeuda(),
                                _money + _format.decimal(pago.getMensual()),};
                            modelo3.addRow(registros);
                            listPagos.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha debe ser mayor a la fecha inicial");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = Pagos_proveedor().stream().filter(u -> u.getIdProveedor() == _idProveedorReport).collect(Collectors.toList());
                listPagos = pagos;
                var data = pagos.stream().skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
                Collections.reverse(listPagos);
                Collections.reverse(data);

                for (TPagos_proveedor pago : data) {
                    Object[] registros = {
                        pago.getId(),
                        _money + _format.decimal(pago.getDeuda()),
                        _money + _format.decimal(pago.getSaldo()),
                        _money + _format.decimal(pago.getPago()),
                        _money + _format.decimal(pago.getCambio()),
                        pago.getFecha(),
                        pago.getTicket(),
                        pago.getFechaDeuda(),
                        _money + _format.decimal(pago.getMensual()),};
                    modelo3.addRow(registros);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        _tablePagosCuotas.setModel(modelo3);
        _tablePagosCuotas.setRowHeight(30);
        _tablePagosCuotas.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(0).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setPreferredWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(8).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(8).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(8).setPreferredWidth(0);
    }

    private int _idHistorial = 0;

    public void GetHistorialPago() {
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        int filas = _tablePagosCuotas.getSelectedRow();
        _idHistorial = (Integer) modelo3.getValueAt(filas, 0);

        var deuda = (String) modelo3.getValueAt(filas, 1);
        _label.get(13).setText(deuda);

        var saldo = (String) modelo3.getValueAt(filas, 2);
        _label.get(14).setText(saldo);

        var fechaDeuda = (Date) modelo3.getValueAt(filas, 7);
        if (fechaDeuda != null) {
            _label.get(15).setText(fechaDeuda.toString());
        }

        var ticket = (String) modelo3.getValueAt(filas, 6);
        _label.get(16).setText(ticket);

        var pago = (String) modelo3.getValueAt(filas, 3);
        _label.get(17).setText(pago);

        var mensual = (String) modelo3.getValueAt(filas, 8);
        _label.get(18).setText(mensual);

        var fechaPago = (Date) modelo3.getValueAt(filas, 5);
        if (fechaPago != null) {
            _label.get(19).setText(fechaPago.toString());
        }

        var cambio = (String) modelo3.getValueAt(filas, 4);
        _label.get(20).setText(cambio);
        var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();

        Ticket1.TextoCentro("Sistema de Ventas");
        Ticket1.TextoIzquierda("Dirección");
        Ticket1.TextoIzquierda("Monterrey");
        Ticket1.TextoIzquierda("Tel 5510122060");
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Factura");
        Ticket1.LineasGuion();
        Ticket1.TextoIzquierda("Factura: " + ticket);
        Ticket1.TextoIzquierda("Proveedor: " + nameProveedor);
        Ticket1.TextoIzquierda("Fecha: " + fechaPago);
        Ticket1.TextoIzquierda("Usuario: " + usuario);
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Crédito: " + deuda);
        Ticket1.TextoCentro("Cuotas mensuales: " + mensual);
        Ticket1.TextoExtremo("Pago: ", pago);
        Ticket1.TextoExtremo("Cambio: ", cambio);
        Ticket1.TextoExtremo("Deuda Actual: ", saldo);
        Ticket1.TextoCentro("TPOO");
    }

    public void TicketDeuda() {
        switch (_seccion) {
            case 1:
                if (_idHistorial == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un historial de pago");
                } else {
                    Ticket1.print();
                }
                break;
            case 2:
                if (_idHistorial == 1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un historial de pago");

                } else {
                    Ticket1.print();
                }
                break;
        }
    }

    public final void ResetReport() {
        _idReport = 0;
        _deudaActual = 0.0;
        _cambio = 0.0;
        _pago = 0.0;
        _mensual = 0.0;

        _label.get(6).setText("Nombre del Proveedor");
        _label.get(7).setText(_money + "0.00");
        _label.get(8).setText(_money + "0.00");
        _label.get(9).setText("0000000000");
        _label.get(10).setText("--/--/--");
        _label.get(11).setText(_money + "0.00");
        _label.get(12).setText("Ingrese el pago");
        _textField.get(4).setText("");
        SearchReportes("");
        if (!reporteFilter.isEmpty()) {
            _paginadorReportes = new Paginador<>(reporteFilter, _label.get(5), _reg_por_pagina);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CÓDIGO FORMA DE PAGO">
    private double cuota;
    private boolean ejecutar;
    
    public void getCuotas() {
        if (_idReport == 0) {
            _label.get(22).setText("Seleccione un proveedor");
            _label.get(22).setForeground(Color.RED);
            ejecutar = false;
        } else {
            if (_deudaActual > 0) {
                var cuotas = 0;
                if (!_textField.get(5).getText().equals("")) {
                    cuota = _format.reconstruir(_textField.get(5).getText());
                    var valor1 = Math.ceil(_deudaActual / cuota);
                    cuotas = (int) Math.ceil(valor1);
                    _label.get(23).setText(String.valueOf(cuotas));
                    ejecutar = true;
                } else {
                    _label.get(23).setText("0");
                    ejecutar = false;
                }
                _label.get(22).setText("Cuotas");
            } else {
                _label.get(22).setText("No tiene deuda");
                ejecutar = false;
            }
        }
    }
    
    public void setCuotas(){
        if(ejecutar){
            try{
                var qr = new QueryRunner(true);
                var forma = _radioButton2.isSelected() ? "M" : "Q";
                String query = "UPDATE treportes_proveedor SET Mensual = ?, FormaPago = ? WHERE IdReporte = " + _idReport;
                Object[] data = {
                    cuota,
                    forma
                };
                qr.update(getConn(), query, data);
            } catch(Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            ResetFormaPago();
        } else {
            
        }
    }

    public void ResetFormaPago() {
        _radioButton1.setSelected(true);
        _radioButton2.setSelected(false);
        _textField.get(5).setText("");
        getCuotas();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PAGINADOR">
    public void Paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (_seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.primero();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReportes.primero();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.primero();
                                }
                                break;
                        }
                        break;
                }
                break;
            case "Anterior":
                switch (_seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.anterior();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReportes.anterior();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.anterior();
                                }
                                break;
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (_seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.siguiente();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReportes.siguiente();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.siguiente();
                                }
                                break;
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (_seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.ultimo();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReportes.ultimo();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.ultimo();
                                }
                                break;
                        }
                        break;
                }
                break;
        }
        switch (_seccion) {
            case 0:
                SearchProveedores("");
                break;
            case 1:
                switch (_seccion1) {
                    case 0:
                        SearchReportes("");
                        break;
                    case 1:
                        historialPagos(false);
                        break;
                }
                break;
        }
    }

    public void Registro_Paginas() {
        _num_pagina = 1;
        Number value = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = value.intValue();
        switch (_seccion) {
            case 0:
                if (!proveedorFilter.isEmpty()) {
                    _paginadorProveedor = new Paginador<>(proveedorFilter, _label.get(5), _reg_por_pagina);
                }
                SearchProveedores("");
                break;
            case 1:
                switch (_seccion1) {
                    case 0:
                        if (!reporteFilter.isEmpty()) {
                            _paginadorReportes = new Paginador<>(reporteFilter, _label.get(5), _reg_por_pagina);
                        }
                        SearchReportes("");
                        break;
                    case 1:
                        if (!listPagos.isEmpty()) {
                            _paginadorPagos = new Paginador<>(listPagos, _label.get(5), _reg_por_pagina);
                        }
                        SearchReportes("");
                        break;
                }
                break;
        }
    }
    //</editor-fold>

}
