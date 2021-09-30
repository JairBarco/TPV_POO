package ViewModels;

import Conexion.Consult;
import Library.*;
import Models.Cliente.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import java.sql.SQLException;
import java.util.Objects;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class ClientesVM extends Consult {

    private String _accion = "insert", _money;
    private final ArrayList<JLabel> _label;
    private final ArrayList<JTextField> _textField;
    private final JCheckBox _checkBoxCredito;
    private final JTable _tableCliente;
    private final JTable _tableReporte;
    private DefaultTableModel modelo1;
    private DefaultTableModel modelo2;
    private JSpinner _spinnerPaginas;
    private JRadioButton _radioCuotas, _radioInteres;
    private int _idCliente = 0;
    private int _reg_por_pagina = 10, _num_pagina = 1;
    public int seccion;
    private final FormatDecimal _format;
    private Paginador<TClientes> _paginadorClientes;
    private Paginador<TClientes> _paginadorReportes;

    private List<TClientes> listClientes;
    private List<TClientes> listClientesReportes;
    private List<TReportes_clientes> listReportes;
    private List<TIntereses_clientes> _listIntereses;
    private int _interesCuotas = 0, _idReport, idClienteReport, _idReportIntereses;
    private Double _intereses = 0.0, _deudaActual = 0.0, _interesPago = 0.0;
    private Double _interesPagos = 0.0, _cambio = 0.0, _interesesCliente = 0.0;
    private Double _pago = 0.0, _mensual = 0.0, _deudaActualCliente = 0.0, _deuda;
    private String _ticketCuota, nameCliente, _ticketIntereses;

    private final Codigos _codigos;

    public ClientesVM(Object[] objects, ArrayList<JLabel> label, ArrayList<JTextField> textField) {
        _label = label;
        _textField = textField;
        _checkBoxCredito = (JCheckBox) objects[0];
        _tableCliente = (JTable) objects[1];
        _spinnerPaginas = (JSpinner) objects[2];
        _tableReporte = (JTable) objects[3];
        _radioCuotas = (JRadioButton) objects[4];
        _radioInteres = (JRadioButton) objects[5];
        _format = new FormatDecimal();
        _money = ConfigurationVM.Money;
        _codigos = new Codigos();

        restablecer();
        restablecerReport();
    }

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE REGISTRAR CLIENTE"> 
    public void RegistrarCliente() {
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
                                    int count;
                                    List<TClientes> listEmail = clientes().stream().
                                            filter(u -> u.getEmail().equals(_textField.get(3).getText())).
                                            collect(Collectors.toList());
                                    count = listEmail.size();
                                    List<TClientes> listNoId = clientes().stream().
                                            filter(u -> u.getNid().equals(_textField.get(3).getText())).
                                            collect(Collectors.toList());
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
                                                        _label.get(0).setText("El NoId ya está registrado");
                                                        _label.get(0).setForeground(Color.RED);
                                                        _label.get(0).requestFocus();
                                                    }
                                                }
                                                break;
                                            case "update":
                                                if (count == 2) {
                                                    if (listEmail.get(0).getID() == _idCliente && listNoId.get(0).getID() == _idCliente) {
                                                        SaveData();
                                                    } else {
                                                        if (listNoId.get(0).getID() != _idCliente) {
                                                            _label.get(0).setText("El NoId ya está registrado");
                                                            _label.get(0).setForeground(Color.RED);
                                                            _label.get(0).requestFocus();
                                                        }
                                                        if (listEmail.get(3).getID() != _idCliente) {
                                                            _label.get(3).setText("El Email ya está registrado");
                                                            _label.get(3).setForeground(Color.RED);
                                                            _label.get(3).requestFocus();
                                                        }
                                                    }
                                                } else {
                                                    if (count == 0) {
                                                        SaveData();
                                                    } else {
                                                        if (!listNoId.isEmpty()) {
                                                            if (listNoId.get(0).getID() == _idCliente) {
                                                                SaveData();
                                                            } else {
                                                                _label.get(0).setText("El NoId ya está registrado");
                                                                _label.get(0).setForeground(Color.RED);
                                                                _label.get(0).requestFocus();
                                                            }
                                                        }
                                                        if (!listEmail.isEmpty()) {
                                                            if (listEmail.get(0).getID() == _idCliente) {
                                                                SaveData();
                                                            } else {
                                                                _label.get(3).setText("El Email ya está registrado");
                                                                _label.get(3).setForeground(Color.RED);
                                                                _label.get(3).requestFocus();
                                                            }
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                    } catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(null, ex);

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
                image = Objetos.uploadImage.getTransFoto(_label.get(6));
            }
            switch (_accion) {
                case "insert":
                    String sqlCliente1 = "INSERT INTO tclientes(Nid,Nombre, Apellido,Email,"
                            + " Telefono,Direccion,Credito,Fecha,Imagen) VALUES (?,?,?,?,?,?,?,?,?)";
                    Object[] dataCliente1 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        _textField.get(4).getText(),
                        _textField.get(5).getText(),
                        _checkBoxCredito.isSelected(), //tinyInt
                        new Calendario().getFecha(),
                        image,};

                    qr.insert(getConn(), sqlCliente1, new ColumnListHandler(), dataCliente1);
                    String sqlReport = "INSERT INTO Treportes_clientes (Deuda,Mensual,Cambio,DeudaActual,FechaDeuda,"
                            + " UltimoPago,FechaPago,Ticket,FechaLimite,IdCliente)"
                            + " VALUES (?,?,?,?,?,?,?,?,?,?)";
                    List<TClientes> cliente = clientes();
                    Object[] dataReport = {
                        0,
                        0,
                        0,
                        0,
                        "--/--/--",
                        0,
                        "--/--/--",
                        "0000000000",
                        "--/--/--",
                        cliente.get(cliente.size() - 1).getID()};
                    qr.insert(getConn(), sqlReport, new ColumnListHandler(), dataReport);

                    String sqlReportInteres = "INSERT INTO treportes_intereses_clientes "
                            + "(Intereses,Pago,Cambio,Cuotas,InteresFecha,TicketIntereses,"
                            + "IdCliente) VALUES (?,?,?,?,?,?,?)";
                    Object[] dataReportInteres = {
                        0,
                        0,
                        0,
                        0,
                        "--/--/--",
                        "0000000000",
                        cliente.get(cliente.size() - 1).getID()
                    };
                    qr.insert(getConn(), sqlReportInteres, new ColumnListHandler(), dataReportInteres);
                    break;
                case "update":
                    Object[] dataCliente2 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        _textField.get(4).getText(),
                        _textField.get(5).getText(),
                        _checkBoxCredito.isSelected(),
                        image,};
                    String sqlCliente2 = "UPDATE tclientes SET Nid = ?,Nombre = ?, Apellido = ?,Email = ?,"
                            + " Telefono = ?,Direccion = ?,Credito = ?,Imagen = ? WHERE ID =" + _idCliente;
                    qr.update(getConn(), sqlCliente2, dataCliente2);
                    break;
            }
            getConn().commit();
            restablecer();
        } catch (SQLException ex) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void SearchClientes(String campo) {
        List<TClientes> clienteFilter;
        String[] titulos = {"Id", "NoId", "Nombre", "Apellido", "Email", "Telefono", "Direccion", "Credito", "Imagen"};
        modelo1 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.equals("")) {
            clienteFilter = clientes().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            clienteFilter = clientes().stream()
                    .filter(C -> C.getNid().startsWith(campo) || C.getNombre().startsWith(campo)
                    || C.getApellido().startsWith(campo))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        }
        if (!clienteFilter.isEmpty()) {
            clienteFilter.forEach(item -> {
                Object[] registros = {
                    item.getID(),
                    item.getNid(),
                    item.getNombre(),
                    item.getApellido(),
                    item.getEmail(),
                    item.getTelefono(),
                    item.getDireccion(),
                    item.isCredito(),
                    item.getImagen()
                };
                modelo1.addRow(registros);
            });
        }
        _tableCliente.setModel(modelo1);
        _tableCliente.setRowHeight(30);
        _tableCliente.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableCliente.getColumnModel().getColumn(0).setMinWidth(0);
        _tableCliente.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableCliente.getColumnModel().getColumn(8).setMaxWidth(0);
        _tableCliente.getColumnModel().getColumn(8).setMinWidth(0);
        _tableCliente.getColumnModel().getColumn(8).setPreferredWidth(0);
        _tableCliente.getColumnModel().getColumn(7).setCellRenderer(new Render_CheckBox());
    }

    public void getCliente() {
        _accion = "update";
        int filas = _tableCliente.getSelectedRow();
        _idCliente = (Integer) modelo1.getValueAt(filas, 0);
        _textField.get(0).setText((String) modelo1.getValueAt(filas, 1));
        _textField.get(1).setText((String) modelo1.getValueAt(filas, 2));
        _textField.get(2).setText((String) modelo1.getValueAt(filas, 3));
        _textField.get(3).setText((String) modelo1.getValueAt(filas, 4));
        _textField.get(4).setText((String) modelo1.getValueAt(filas, 5));
        _textField.get(5).setText((String) modelo1.getValueAt(filas, 6));
        _checkBoxCredito.setSelected((Boolean) modelo1.getValueAt(filas, 7));
        Objetos.uploadImage.byteImage(_label.get(6), (byte[]) modelo1.getValueAt(filas, 8));
    }

    public final void restablecer() {
        seccion = 1;
        _accion = "insert";
        _textField.get(0).setText("");
        _textField.get(1).setText("");
        _textField.get(2).setText("");
        _textField.get(3).setText("");
        _textField.get(4).setText("");
        _textField.get(5).setText("");
        _checkBoxCredito.setSelected(false);
        _checkBoxCredito.setForeground(new Color(102, 102, 102));
        _label.get(0).setText("NoId");
        _label.get(0).setForeground(new Color(102, 102, 102));
        _label.get(1).setText("Nombre");
        _label.get(1).setForeground(new Color(102, 102, 102));
        _label.get(2).setText("Apellido");
        _label.get(2).setForeground(new Color(102, 102, 102));
        _label.get(3).setText("Email");
        _label.get(3).setForeground(new Color(102, 102, 102));
        _label.get(4).setText("Telefono");
        _label.get(4).setForeground(new Color(102, 102, 102));
        _label.get(5).setText("Direccion");
        _label.get(5).setForeground(new Color(102, 102, 102));
        _label.get(6).setIcon(new ImageIcon(getClass().getClassLoader()
                .getResource("Resources/agregar_imagen.png")));
        listClientes = clientes();
        if (!listClientes.isEmpty()) {
            _paginadorClientes = new Paginador<>(listClientes, _label.get(7), _reg_por_pagina);

        }
        SpinnerNumberModel model = new SpinnerNumberModel(
                new Integer(10), // Dato visualizado al inicio en el spinner 
                new Integer(1), // Límite inferior 
                new Integer(100), // Límite superior 
                new Integer(1) // incremento-decremento 
        );
        _spinnerPaginas.setModel(model);
        SearchClientes("");
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE PAGOS Y REPORTES"> 
    public void SearchReportes(String valor) {
        List<TClientes> clienteFilter;
        String[] titulos = {"Id", "NoId", "Nombre", "Apellido", "Email", "Telefono", "Direccion"};
        modelo2 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (valor.equals("")) {
            clienteFilter = clientes().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            clienteFilter = clientes().stream()
                    .filter(C -> C.getNid().startsWith(valor) || C.getNombre().startsWith(valor)
                    || C.getApellido().startsWith(valor))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        }
        if (!clienteFilter.isEmpty()) {
            clienteFilter.forEach(item -> {
                Object[] registros = {
                    item.getID(),
                    item.getNid(),
                    item.getNombre(),
                    item.getApellido(),
                    item.getEmail(),
                    item.getTelefono(),
                    item.getDireccion(),};
                modelo2.addRow(registros);
            });
        }
        _tableReporte.setModel(modelo2);
        _tableReporte.setRowHeight(30);
        _tableReporte.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableReporte.getColumnModel().getColumn(0).setMinWidth(0);
        _tableReporte.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void getReportCliente() {
        int filas = _tableReporte.getSelectedRow();
        idClienteReport = (Integer) modelo2.getValueAt(filas, 0);
        List<TReportes_clientes> clienteFilter = reportesClientes(idClienteReport);
        if (!clienteFilter.isEmpty()) {
            TReportes_clientes cliente = clienteFilter.get(0);
            _idReport = cliente.getIdReporte();
            _idReportIntereses = cliente.getID();
            nameCliente = cliente.getNombre() + " " + cliente.getApellido();

            _label.get(8).setText(nameCliente);
            _deudaActual = (Double) cliente.getDeudaActual();
            _deuda = (Double) cliente.getDeuda();
            _label.get(9).setText(_money + _format.decimal(_deudaActual));
            _label.get(10).setText(_money + _format.decimal((double) cliente.getUltimoPago()));
            _ticketCuota = cliente.getTicket();
            _label.get(11).setText(_ticketCuota);
            _label.get(12).setText(cliente.getFechaPago());
            _label.get(13).setText(_money + _format.decimal((double) cliente.getMensual()));
            _listIntereses = InteresesCliente().stream()
                    .filter(u -> u.getIdCliente() == idClienteReport
                    && u.getCancelado() == false)
                    .collect(Collectors.toList());
            if (_listIntereses.isEmpty()) {
                _label.get(14).setText(_money + "0.00");
                _label.get(12).setText("--/--/--");
                _label.get(15).setText("0");
                _label.get(16).setText("0000000000");
                _label.get(17).setText("--/--/--");
            } else {
                _interesCuotas = cliente.getCuotas();
                _intereses = 0.0;
                _listIntereses.forEach(item -> {
                    _intereses += item.getIntereses();
                });
                _label.get(14).setText(_money + _format.decimal(_intereses));
                _label.get(15).setText(String.valueOf(_interesCuotas));
                _ticketIntereses = cliente.getTicketIntereses();
                _label.get(16).setText(_ticketIntereses);
                _label.get(17).setText(cliente.getInteresFecha());
            }
        }
    }

    public void Pagos() {
        if (!_textField.get(6).getText().isEmpty()) {
            _label.get(19).setText("Ingrese el Pago");
            if (_idReport == 0) {
                _label.get(19).setText("Seleccione un cliente");
            } else {
                if (_radioInteres.isSelected()) {
                    if (!_textField.get(7).getText().isEmpty()) {
                        int cantCuotas = Integer.valueOf(_textField.get(7).getText());
                        if (cantCuotas <= _interesCuotas) {
                            if (!_textField.get(6).getText().isEmpty()) {
                                _interesPago = _format.reconstruir(_textField.get(6).getText());
                                if (_interesPago >= _interesPagos) {
                                    _cambio = _interesPago - _interesPagos;
                                    _label.get(19).setText("Cambio para el cliente " + _money + _format.decimal(_cambio));
                                    _interesesCliente = _intereses - _interesPagos;
                                    _label.get(14).setText(_money + _format.decimal(_interesesCliente));
                                } else {
                                    _label.get(19).setText(_money + _format.decimal(_interesPagos));
                                    _interesesCliente = _intereses - _interesPagos;
                                    _label.get(14).setText(_money + _format.decimal(_interesesCliente));
                                }
                            }
                        } else {
                            _label.get(19).setText("Cuotas invalidas");
                        }
                    } else {
                        _label.get(19).setText("Ingrese el número de cuotas");
//                        _textField.get(7).requestFocus();
                    }
                } else if (_radioCuotas.isSelected()) {
                    if (!_textField.get(6).getText().isEmpty()) {
                        _pago = _format.reconstruir(_textField.get(6).getText());
                        TReportes_clientes dataReport = ReporteCliente().stream().filter(u -> u.getIdReporte() == _idReport).collect(Collectors.toList()).get(0);
                        _mensual = dataReport.getMensual();
                        if (_pago > _mensual) {
                            if (Objects.equals(_pago, _deudaActual) || _pago > _deudaActual) {
                                _cambio = _pago - _deudaActual;
                                _label.get(19).setText("Cambio para el cliente " + _money + _format.decimal(_cambio));
                                _label.get(9).setText(_money + "0.00");
                                _deudaActual = 0.0;
                            } else {
                                _cambio = _pago - _mensual;
                                _label.get(19).setText("Cambio para el cliente " + _money + _format.decimal(_cambio));
                                _deudaActualCliente = _deudaActual - _mensual;
                                _label.get(9).setText(_money + _format.decimal(_deudaActualCliente));
                            }
                        } else if (Objects.equals(_pago, _mensual)) {
                            _deudaActualCliente = _deudaActual - _mensual;
                            _label.get(9).setText(_money + _format.decimal(_deudaActualCliente));
                        }
                    }
                }
            }

        } else {
            _label.get(19).setText("Ingresar el pago");
            _label.get(9).setText(_money + _format.decimal(_deudaActual));
            _label.get(14).setText(_money + _format.decimal(_intereses));
        }
    }

    public void CuotasIntereses() {
        if (_idReport == 0) {
            _label.get(19).setText("Seleccione un cliente");
        } else {
            if (_deudaActual > 0 || _intereses > 0) {
                _label.get(19).setText("Ingrese el pago");
                if (null != _textField.get(7)) {
                    if (_textField.get(7).getText().isEmpty()) {
                        _label.get(14).setText(_money + _format.decimal(_intereses));
                        _label.get(15).setText(String.valueOf(_interesCuotas));
                        _label.get(18).setText(_money + "0.00");
                        _label.get(19).setText("Ingrese el Pago");
                    } else {
                        _label.get(18).setText(_money + "0.00");
                        int cantCuotas = Integer.valueOf(_textField.get(7).getText());
                        if (cantCuotas <= _interesCuotas) {
                            _label.get(19).setText("Ingrese el Pago");
                            if (!_listIntereses.isEmpty()) {
                                _interesPagos = 0.0;
                                for (int i = 0; i < cantCuotas; i++) {
                                    _interesPagos += _listIntereses.get(i).getIntereses();
                                }
                                int cuotas = _interesCuotas - cantCuotas;
                                double intereses = _intereses - _interesPagos;
                                _label.get(14).setText(_money + _format.decimal(intereses));
                                _label.get(15).setText(String.valueOf(cuotas));
                                _label.get(14).setText(_money + _format.decimal(_interesPagos));
                            }
                        } else {
                            _label.get(19).setText("Cuotas invalidas");
//                            _textField.get(7).requestFocus();
                        }
                    }
                }
                Pagos();
            } else {
                _label.get(19).setText("El cliente no tiene deuda");
            }
        }
    }

    public void EjecutarPago() throws SQLException {
        final QueryRunner qr = new QueryRunner(true);
        if (Objects.equals(_idReport, 0)) {
            _label.get(19).setText("Seleccione un cliente");
        } else {
            if (_textField.get(6).getText().isEmpty()) {
                _label.get(19).setText("Seleccione un cliente");
                _textField.get(6).requestFocus();
            } else {
                String fecha = new Calendario().getFecha();
                //Consulta usuario que inicia sesión
                if (_radioCuotas.isSelected()) {
                    if (!_deuda.equals(0) || !_deuda.equals(0.0)) {
                        if (_pago >= _mensual) {
                            try {
                                getConn().setAutoCommit(false);
                                String dateNow = new Calendario().addMes(1);
                                String _fechalimit = Objects.equals(_deudaActual, 0) ? "--/--/--" : dateNow;
                                String ticket = _codigos.codesTickets(_ticketCuota);

                                String query1 = "INSERT INTO tpagos_clientes(Deuda,Saldo, Pago,Cambio,"
                                        + "Fecha,FechaLimite,Ticket,IdUsuario,Usuario,IdCliente) VALUES (?,?,?,?,?,?,?,?,?,?)";

                                Object[] data1 = {
                                    _deuda, _deudaActual, _pago, _cambio, fecha,
                                    _fechalimit, ticket, 1, "Jair", idClienteReport
                                };

                                qr.insert(getConn(), query1, new ColumnListHandler(), data1);

                                String query2 = "UPDATE treportes_clientes SET DeudaActual = ?,FechaDeuda = ?,"
                                        + "UltimoPago = ?,Cambio = ?,FechaPago = ?,Ticket = ?,"
                                        + "FechaLimite = ? WHERE IdReporte =" + _idReport;

                                Object[] data2 = {
                                    _deudaActualCliente, fecha, _pago,
                                    _cambio, fecha, ticket, _fechalimit,};

                                qr.update(getConn(), query2, data2);

                                Ticket Ticket1 = new Ticket();

                                Ticket1.TextoCentro("Sistema de ventas");
                                Ticket1.TextoIzquierda("Dirección");
                                Ticket1.TextoIzquierda("Monterrey, Nuevo León");
                                Ticket1.TextoIzquierda("Tel. 5522001025");
                                Ticket1.LineasGuion();
                                Ticket1.TextoCentro("Factura");
                                Ticket1.LineasGuion();
                                Ticket1.TextoIzquierda("Factura: " + ticket);
                                Ticket1.TextoIzquierda("Cliente: " + nameCliente);
                                Ticket1.TextoIzquierda("Fecha: " + fecha);
                                //Ticket1.TextoIzquierda("Usuario:usuario");
                                Ticket1.LineasGuion();
                                Ticket1.TextoCentro("Su credito: " + _money + _format.decimal(_deuda));
                                Ticket1.TextoExtremo("Cuotas por 12 meses: ", _money + _format.decimal(_mensual));
                                Ticket1.TextoExtremo("Deuda anteror", _money + _format.decimal(_deudaActual));
                                Ticket1.TextoExtremo("Pago:", _money + _format.decimal(_pago));
                                Ticket1.TextoExtremo("Cambio:", _money + _format.decimal(_cambio));
                                Ticket1.TextoExtremo("Deuda Actual:", _money + _format.decimal(_deudaActualCliente));
                                Ticket1.TextoExtremo("Proximo Pago:", _fechalimit);
                                Ticket1.TextoCentro("TPOO");
                                Ticket1.print();
                                getConn().commit();
                                restablecerReport();

                            } catch (Exception e) {
                                getConn().rollback();
                                JOptionPane.showMessageDialog(null, e);
                            }
                        }
                    } else {
                        _label.get(19).setText("El cliente no tiene deuda");
                    }
                } else if (_radioInteres.isSelected()) {
                    if (!_intereses.equals(0)) {
                        if (!_textField.get(7).getText().equals("")) {
                            try {
                                Integer cantCuotas = Integer.valueOf(_textField.get(7).getText());
                                if (cantCuotas <= _interesCuotas) {
                                    if (_interesPago >= _interesPagos) {
                                        getConn().setAutoCommit(false);
                                        if (!_listIntereses.isEmpty()) {
                                            Object[] data1 = {true};
                                            
                                            for (int i = 0; i < cantCuotas; i++) {    
                                                String query1 = "UPDATE tintereses_clientes SET Cancelado = ?"
                                                        + " WHERE id=" + _listIntereses.get(i).getId() + " AND "
                                                        + " IdCliente=" + idClienteReport;
                                                qr.update(getConn(), query1, data1);
                                            }

                                            String ticket = _codigos.codesTickets(_ticketIntereses);
                                            String query2 = "INSERT INTO tpagos_reportes_intereses_cliente(Intereses,Pago, Cambio,Cuotas,"
                                                    + "Fecha,Ticket,IdUsuario,Usuario,IdCliente) VALUES(?,?,?,?,?,?,?,?,?)";
                                            Object[] data2 = {
                                                _intereses, _interesPago, _cambio, cantCuotas, fecha, ticket, 7, "Jair", idClienteReport
                                            };
                                            qr.insert(getConn(), query2, new ColumnListHandler(), data2);

                                            String query3 = "UPDATE treportes_intereses_clientes SET Intereses = ?,"
                                                    + "Pago = ?,Cambio = ?,Cuotas = ?,InteresFecha = ? ,TicketIntereses = ?"
                                                    + " WHERE Id = " + _idReportIntereses + " AND "
                                                    + " IdCliente=" + idClienteReport;
                                            Object[] data3 = {_interesesCliente, _interesPago, _cambio, cantCuotas, fecha, ticket};
                                            qr.update(getConn(), query3, data3);

                                            Ticket Ticket1 = new Ticket();
                                            Ticket1.TextoCentro("Sistema de Ventas");
                                            Ticket1.TextoIzquierda("Dirección");
                                            Ticket1.TextoIzquierda("Monterrey");
                                            Ticket1.TextoIzquierda("Tel 5510122060");
                                            Ticket1.LineasGuion();
                                            Ticket1.TextoCentro("Factura de Pagos Intereses");
                                            Ticket1.LineasGuion();
                                            Ticket1.TextoIzquierda("Factura: " + ticket);
                                            Ticket1.TextoIzquierda("Cliente: " + nameCliente);
                                            Ticket1.TextoIzquierda("Fecha: " + fecha);
                                            //usuario
                                            Ticket1.LineasGuion();
                                            Ticket1.TextoCentro("Intereses " + _money + _format.decimal(_intereses));
                                            Ticket1.TextoExtremo("Cuotas: ", cantCuotas.toString());
                                            Ticket1.TextoExtremo("Pago: ", _money + _format.decimal(_interesPago));
                                            Ticket1.TextoExtremo("Cambio: ", _money + _format.decimal(_cambio));
                                            Ticket1.TextoCentro("TPOO");
                                            Ticket1.print();
                                            getConn().commit();
                                            restablecerReport();
                                        }
                                    } else {
                                        _label.get(19).setText("El pago debe de ser: " + _money + _format.decimal(_interesPagos));
                                    }

                                } else {
                                    _label.get(19).setText("Cuotas no válidas");
                                }
                            } catch (Exception e) {
                                getConn().rollback();
                                JOptionPane.showMessageDialog(null, e);
                            }
                        } else {
                            _label.get(19).setText("Ingrese el numero de cuotas");
                        }

                    } else {
                        _label.get(19).setText("El cliente no tiene deuda");
                    }
                }
            }
        }
    }

    public final void restablecerReport() {
        _idReport = 0;
        _interesCuotas = 0;
        _intereses = 0.0;
        _interesPago = 0.0;
        _deudaActual = 0.0;
        _interesPagos = 0.0;
        _cambio = 0.0;
        _interesesCliente = 0.0;
        _pago = 0.0;
        _mensual = 0.0;
        idClienteReport = 0;
        _idReportIntereses = 0;
        _ticketIntereses = "0000000000";
        _deudaActualCliente = 0.0;
        _ticketCuota = "0000000000";

        _label.get(8).setText("Nombre del cliente");
        _label.get(9).setText(_money + "0.00");
        _label.get(10).setText(_money + "0.00");
        _label.get(11).setText("0000000000");
        _label.get(12).setText("--/--/--");
        _label.get(13).setText(_money + "0.00");
        _label.get(14).setText(_money + "0.00");
        _label.get(15).setText("0");
        _label.get(16).setText("0000000000");
        _label.get(17).setText("--/--/--");
        _label.get(19).setText("Ingrese el pago");
        _textField.get(6).setText("");
        _textField.get(7).setText("");

        listClientesReportes = clientes();
        if (!listClientesReportes.isEmpty()) {
            _paginadorReportes = new Paginador<>(listClientesReportes,
                    _label.get(7), _reg_por_pagina);
        }
        SearchReportes("");
    }
// </editor-fold>

    public void Paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.primero();
                        }
                        break;
                    case 2:
                        if (!listClientesReportes.isEmpty()) {
                            _num_pagina = _paginadorReportes.primero();
                        }
                        break;

                }
                break;
            case "Anterior":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.anterior();
                        }
                        break;
                    case 2:
                        if (!listClientesReportes.isEmpty()) {
                            _num_pagina = _paginadorReportes.anterior();
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.siguiente();
                        }
                        break;
                    case 2:
                        if (!listClientesReportes.isEmpty()) {
                            _num_pagina = _paginadorReportes.siguiente();
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.ultimo();
                        }
                        break;
                    case 2:
                        if (!listClientesReportes.isEmpty()) {
                            _num_pagina = _paginadorReportes.ultimo();
                        }
                        break;
                }
                break;
        }
        switch (seccion) {
            case 1:
                SearchClientes("");
                break;
            case 2:
                SearchReportes("");
                break;
        }
    }

    public void Registro_Paginas() {
        _num_pagina = 1;
        Number caja = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = caja.intValue();
        switch (seccion) {
            case 1:
                if (!listClientes.isEmpty()) {
                    _paginadorClientes = new Paginador<>(listClientes, _label.get(7), _reg_por_pagina);
                }
                SearchClientes("");
                break;
            case 2:
                if (!listClientesReportes.isEmpty()) {
                    _paginadorReportes = new Paginador<>(listClientesReportes, _label.get(7), _reg_por_pagina);
                }
                SearchReportes("");
                break;
        }
    }
}
