package ViewModels;

import Conexion.Consult;
import Library.*;
import Models.Cliente.*;
import Models.Usuario.TUsuarios;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class ClientesVM extends Consult {

    // <editor-fold defaultstate="collapsed" desc="VARIABLES GLOBALES">
    private String _accion = "insert";
    private String _money;
    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textField;
    private JCheckBox _checkBoxCredito, _checkBox_Dia;
    private JTable _tableCliente, _tableReporte, _tableReporteDeuda, _tablePagosCuotas, _tablePagosIntereses;
    private DefaultTableModel modelo1, modelo2, modelo3, modelo4, modelo5;
    private JSpinner _spinnerPaginas;
    private JRadioButton _radioCuotas;
    private JRadioButton _radioInteres;
    private int _idCliente = 0;
    private int _reg_por_pagina = 10, _num_pagina = 1;
    public int seccion;
    private FormatDecimal _format;
    private Paginador<TClientes> _paginadorClientes;
    private Paginador<TClientes> _paginadorReportes;
    private Paginador<TReportes_clientes> _paginadorReportesDeuda;
    private Paginador<TPagos_clientes> _paginadorPagos;
    private Paginador<TPagos_reportes_intereses_clientes> _paginadorPagosIntereses;

    private List<TClientes> listClientes;
    private List<TClientes> listReportesDeuda;
    private List<TPagos_clientes> listPagos = new ArrayList<>();
    private List<TIntereses_clientes> _listIntereses;
    private int _interesCuotas = 0, _idReport, idClienteReport, _idReportIntereses;
    private Double _intereses = 0.0, _deudaActual = 0.0, _interesPago = 0.0;
    private Double _interesPagos = 0.0, _cambio = 0.0, _interesesCliente = 0.0;
    private Double _pago = 0.0, _mensual = 0.0, _deudaActualCliente = 0.0, _deuda;
    private String _ticketCuota, nameCliente, _ticketIntereses;

    private Codigos _codigos;
    private SimpleDateFormat formateador;

    private long diasMoras;
    private DefaultTableModel _selectedCliente;
    private Date _fechaLimite;
    private DateChooserCombo _dateChooser, _dateChooser1, _dateChooser2;
    private Integer _idReporte;

    private static TUsuarios _dataUsuario;
    private int cuotas;
    private int _idHistorial;
    Ticket Ticket1 = new Ticket();
    public int _seccion1;

    private List<TReportes_clientes> _list = new ArrayList<>();
    private List<TPagos_reportes_intereses_clientes> listPagosIntereses = new ArrayList<>();
    // </editor-fold>

    public ClientesVM() {

    }

    public ClientesVM(TUsuarios dataUsuario) {
        _dataUsuario = dataUsuario;
        formateador = new SimpleDateFormat("dd/MM/yyyy");
    }

    public ClientesVM(Object[] objects, ArrayList<JLabel> label, ArrayList<JTextField> textField) {
        _label = label;
        _textField = textField;
        _checkBoxCredito = (JCheckBox) objects[0];
        _tableCliente = (JTable) objects[1];
        _spinnerPaginas = (JSpinner) objects[2];
        _tableReporte = (JTable) objects[3];
        _radioCuotas = (JRadioButton) objects[4];
        _radioInteres = (JRadioButton) objects[5];
        _tableReporteDeuda = (JTable) objects[6];
        _dateChooser = (DateChooserCombo) objects[7];
        _checkBox_Dia = (JCheckBox) objects[8];
        _dateChooser1 = (DateChooserCombo) objects[9];
        _dateChooser2 = (DateChooserCombo) objects[10];
        _tablePagosCuotas = (JTable) objects[11];
        _tablePagosIntereses = (JTable) objects[12];
        _format = new FormatDecimal();
        _money = ConfigurationVM.Money;
        _codigos = new Codigos();
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        restablecer();
        restablecerReport();
        ResetReportDeudas();
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
                10.0, //Dato visualizado al inicio del Spinner
                1.0, //Límite inferior
                100.0, //Límite superior
                1.0); //Incremento - Decremento
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
            historialPagos(false);
            historialIntereses(false);
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
                        if (_pago >= _mensual) {
                            if (Objects.equals(_pago, _deudaActual) || _pago > _deudaActual) {
                                _cambio = _pago - _deudaActual;
                                _label.get(19).setText("Cambio para el cliente " + _money + _format.decimal(_cambio));
                                _label.get(9).setText(_money + _cambio);
                                _deudaActual = 0.0;
                                _deudaActualCliente = 0.0;
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
                                cuotas = _interesCuotas - cantCuotas;
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
                _label.get(19).setText("Ingrese el pago");
                _textField.get(6).requestFocus();
            } else {
                String fecha = new Calendario().getFecha();
                //Consulta usuario que inicia sesión
                var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();
                if (_radioCuotas.isSelected()) {
                    if (!_deuda.equals(0) || !_deuda.equals(0.0)) {
                        if (_pago >= _mensual) {
                            try {
                                getConn().setAutoCommit(false);
                                String dateNow = new Calendario().addMes(1);
                                String _fechalimit = Objects.equals(_deudaActual, 0) ? new Calendario().getFecha() : dateNow;
                                String ticket = _codigos.codesTickets(_ticketCuota);

                                String query1 = "INSERT INTO tpagos_clientes(Deuda,Saldo, Pago,Cambio,"
                                        + "Fecha,FechaLimite,Ticket,IdUsuario,Usuario,IdCliente,FechaDeuda,Mensual) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

                                var dataReport = ReporteCliente().stream()
                                        .filter(u -> u.getIdCliente() == idClienteReport)
                                        .collect(Collectors.toList()).get(0);

                                Object[] data1 = {
                                    _deuda,
                                    _deudaActualCliente,
                                    _pago,
                                    _cambio,
                                    formateador.parse(fecha),
                                    formateador.parse(_fechalimit),
                                    ticket,
                                    _dataUsuario.getIdUsuario(),
                                    usuario,
                                    idClienteReport,
                                    formateador.parse(dataReport.getFechaDeuda()),
                                    dataReport.getMensual()
                                };

                                qr.insert(getConn(), query1, new ColumnListHandler(), data1);

                                if (_deudaActualCliente.equals(0.0)) {
                                    String query2 = "UPDATE treportes_clientes SET Deuda = ?,"
                                            + "Mensual = ?,FechaDeuda = ?,DeudaActual = ?,UltimoPago = ?,"
                                            + "Cambio = ?,FechaPago = ?,Ticket = ?,FechaLimite = ? WHERE IdReporte =" + _idReport;

                                    Object[] data2 = {
                                        0.0,
                                        0.0,
                                        "--/--/--",
                                        0.0,
                                        0.0,
                                        0.0,
                                        "--/--/--",
                                        "0000000000",
                                        "--/--/--",};
                                    qr.update(getConn(), query2, data2);
                                } else {
                                    String query2 = "UPDATE treportes_clientes SET DeudaActual = ?,"
                                            + "UltimoPago = ?,Cambio = ?,FechaPago = ?,Ticket = ?,"
                                            + "FechaLimite = ? WHERE IdReporte =" + _idReport;

                                    Object[] data2 = {
                                        _deudaActualCliente, _pago,
                                        _cambio, fecha, ticket, _fechalimit,};

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
                                Ticket1.TextoIzquierda("Cliente: " + nameCliente);
                                Ticket1.TextoIzquierda("Fecha: " + fecha);
                                Ticket1.TextoIzquierda("Usuario: " + usuario);
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
                                                _intereses, _interesPago, _cambio, cantCuotas, fecha, ticket, _dataUsuario.getIdUsuario(), usuario, idClienteReport
                                            };
                                            qr.insert(getConn(), query2, new ColumnListHandler(), data2);

                                            String query3 = "UPDATE treportes_intereses_clientes SET Intereses = ?,"
                                                    + "Pago = ?,Cambio = ?,Cuotas = ?,InteresFecha = ? ,TicketIntereses = ?"
                                                    + " WHERE Id = " + _idReportIntereses + " AND "
                                                    + " IdCliente=" + idClienteReport;

                                            if (cuotas == 0) {
                                                Object[] data3 = {
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0,
                                                    fecha,
                                                    "0000000000"
                                                };
                                                qr.update(getConn(), query3, data3);
                                            } else {
                                                Object[] data3 = {_interesesCliente, _interesPago, _cambio, cantCuotas, fecha, ticket};
                                                qr.update(getConn(), query3, data3);
                                            }

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
                                            Ticket1.TextoIzquierda("Usuario: " + usuario);
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

    public void historialPagos(boolean filtrar) {
        try {
            listPagos = new ArrayList();
            String[] titulos = {"Id", "Deuda", "Saldo", "Pago", "Cambio", "Fecha", "Ticket", "FechaDeuda", "Mensual", "Fecha Límite"};
            modelo4 = new DefaultTableModel(null, titulos);
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            var cal = new Calendario();
            if (filtrar) {
                if (date2.after(date1) || date1.equals(date2)) {
                    var listPagos1 = new ArrayList<TPagos_clientes>();
                    var pagos = Pagos_clientes().stream().filter(u -> u.getIdCliente() == idClienteReport).collect(Collectors.toList());
                    formateador = new SimpleDateFormat("yyyy/MM/dd");
                    _dateChooser1.setFormat(3);
                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));

                    for (TPagos_clientes pago : pagos) {
                        var data = pago.getFecha().toString().replace('-', '/');
                        var date5 = formateador.parse(data);
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }
                    _dateChooser2.setFormat(3);
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));

                    for (TPagos_clientes pago : listPagos1) {
                        var data = pago.getFecha().toString().replace('-', '/');
                        var date8 = formateador.parse(data);

                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getId(),
                                _format.decimal(pago.getDeuda()),
                                _format.decimal(pago.getSaldo()),
                                _format.decimal(pago.getPago()),
                                _format.decimal(pago.getCambio()),
                                pago.getFecha(),
                                pago.getTicket(),
                                pago.getFechaDeuda(),
                                _format.decimal(pago.getMensual()),
                                pago.getFechaLimite()
                            };
                            modelo4.addRow(registros);
                            listPagos.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha debe ser mayor a la fecha inicial");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = Pagos_clientes().stream().filter(u -> u.getIdCliente() == idClienteReport).collect(Collectors.toList());
                listPagos = pagos;
                var data = pagos.stream().skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
                Collections.reverse(listPagos);
                Collections.reverse(data);

                for (TPagos_clientes pago : data) {
                    Object[] registros = {
                        pago.getId(),
                        _format.decimal(pago.getDeuda()),
                        _format.decimal(pago.getSaldo()),
                        _format.decimal(pago.getPago()),
                        _format.decimal(pago.getCambio()),
                        pago.getFecha(),
                        pago.getTicket(),
                        pago.getFechaDeuda(),
                        _format.decimal(pago.getMensual()),
                        pago.getFechaLimite()
                    };
                    modelo4.addRow(registros);
                }
            }
        } catch (ParseException ex) {
            var data = ex.getMessage();
        }
        _tablePagosCuotas.setModel(modelo4);
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
        _tablePagosCuotas.getColumnModel().getColumn(9).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(9).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(9).setPreferredWidth(0);
    }

    public void getHistorialPagos() {
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        int filas = _tablePagosCuotas.getSelectedRow();
        _idHistorial = (Integer) modelo4.getValueAt(filas, 0);
        var deuda = _money + (String) modelo4.getValueAt(filas, 1);
        _label.get(22).setText(deuda);
        var saldo = _money + (String) modelo4.getValueAt(filas, 2);
        _label.get(23).setText(saldo);
        var fechaDeuda = (Date) modelo4.getValueAt(filas, 7);
        _label.get(24).setText(fechaDeuda.toString());

        var ticket = (String) modelo4.getValueAt(filas, 6);
        _label.get(25).setText(ticket);
        var pago = _money + (String) modelo4.getValueAt(filas, 3);
        _label.get(26).setText(pago);
        var mensual = _money + (String) modelo4.getValueAt(filas, 8);
        _label.get(27).setText(mensual);
        var fechaPago = (Date) modelo4.getValueAt(filas, 5);
        _label.get(28).setText(fechaPago.toString());
        var fechaLimite = (Date) modelo4.getValueAt(filas, 9);
        _label.get(29).setText(fechaLimite.toString());
        var cambio = _money + (String) modelo4.getValueAt(filas, 4);
        var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();

        Ticket1.TextoCentro("Sistema de Ventas");
        Ticket1.TextoIzquierda("Dirección");
        Ticket1.TextoIzquierda("Monterrey");
        Ticket1.TextoIzquierda("Tel 5510122060");
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Factura");
        Ticket1.LineasGuion();
        Ticket1.TextoIzquierda("Factura: " + ticket);
        Ticket1.TextoIzquierda("Cliente: " + nameCliente);
        Ticket1.TextoIzquierda("Fecha: " + fechaPago);
        Ticket1.TextoIzquierda("Usuario: " + usuario);
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Crédito: " + deuda);
        Ticket1.TextoCentro("Cuotas mensuales: " + mensual);
        Ticket1.TextoExtremo("Pago: ", _money + _format.decimal(_interesPago));
        Ticket1.TextoExtremo("Cambio: ", cambio);
        Ticket1.TextoExtremo("Deuda Actual: ", saldo);
        Ticket1.TextoExtremo("Proximo Pago: ", fechaLimite.toString());
        Ticket1.TextoCentro("TPOO");
    }

    public void TicketDeuda() {
        switch (_seccion1) {
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

    public void historialIntereses(boolean filtrar) {
        listPagosIntereses = new ArrayList<>();
        try {
            listPagos = new ArrayList();
            String[] titulos = {"Id", "Intereses", "Pago", "Cambio", "Cuotas", "Fecha", "Ticket"};
            modelo5 = new DefaultTableModel(null, titulos);
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            var cal = new Calendario();
            if (filtrar) {
                if (date2.after(date1) || date1.equals(date2)) {
                    var listPagos1 = new ArrayList<TPagos_reportes_intereses_clientes>();
                    var pagos = Pagos_reportes_intereses_clientes().stream().filter(u -> u.getIdCliente() == idClienteReport).collect(Collectors.toList());
                    formateador = new SimpleDateFormat("yyyy/MM/dd");
                    _dateChooser1.setFormat(3);
                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));

                    for (TPagos_reportes_intereses_clientes pago : pagos) {
                        var data = pago.getFecha().toString().replace('-', '/');
                        var array = data.split("/");
                        var date5 = formateador.parse(array[2] + "/" + array[1] + "/" + array[0]);
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }
                    _dateChooser2.setFormat(3);
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));

                    for (TPagos_reportes_intereses_clientes pago : listPagos1) {
                        var data = pago.getFecha().toString().replace('-', '/');
                        var array = data.split("/");
                        var date8 = formateador.parse(array[2] + "/" + array[1] + "/" + array[0]);

                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getId(),
                                _format.decimal(pago.getIntereses()),
                                _format.decimal(pago.getPago()),
                                _format.decimal(pago.getCambio()),
                                pago.getCuotas(),
                                pago.getFecha(),
                                pago.getTicket(),};
                            modelo5.addRow(registros);
                            listPagosIntereses.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha debe ser mayor a la fecha inicial");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = Pagos_reportes_intereses_clientes().stream().filter(u -> u.getIdCliente() == idClienteReport).collect(Collectors.toList());
                listPagosIntereses = pagos;
                var data = pagos.stream().skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
                Collections.reverse(listPagosIntereses);
                Collections.reverse(data);

                for (TPagos_reportes_intereses_clientes pago : data) {
                    Object[] registros = {
                        pago.getId(),
                        _format.decimal(pago.getIntereses()),
                        _format.decimal(pago.getPago()),
                        _format.decimal(pago.getCambio()),
                        pago.getCuotas(),
                        pago.getFecha(),
                        pago.getTicket(),};
                    modelo5.addRow(registros);
                }
            }
        } catch (ParseException ex) {
            var data = ex.getMessage();
        }
        _tablePagosIntereses.setModel(modelo5);
        _tablePagosIntereses.setRowHeight(30);
        _tablePagosIntereses.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablePagosIntereses.getColumnModel().getColumn(0).setMinWidth(0);
        _tablePagosIntereses.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    private int _idHistorial1;

    public void getHistorialIntereses() {
        int filas = _tablePagosIntereses.getSelectedRow();
        _idHistorial1 = (Integer) modelo5.getValueAt(filas, 0);
        var intereses = _money + (String) modelo5.getValueAt(filas, 1);
        _label.get(30).setText(intereses);
        var ticket = (String) modelo5.getValueAt(filas, 6);
        _label.get(31).setText(ticket);
        var fecha = (String) modelo5.getValueAt(filas, 5);
        _label.get(32).setText(fecha);
        var cambio = _money + (String) modelo5.getValueAt(filas, 3);
        _label.get(33).setText(cambio);
        var pago = _money + (String) modelo5.getValueAt(filas, 2);
        _label.get(34).setText(pago);

        var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();
        Ticket1.TextoCentro("Sistema de Ventas");
        Ticket1.TextoIzquierda("Dirección");
        Ticket1.TextoIzquierda("Monterrey");
        Ticket1.TextoIzquierda("Tel 5510122060");
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Factura");
        Ticket1.LineasGuion();
        Ticket1.TextoIzquierda("Factura: " + ticket);
        Ticket1.TextoIzquierda("Cliente: " + nameCliente);
        Ticket1.TextoIzquierda("Fecha: " + fecha);
        Ticket1.TextoIzquierda("Usuario: " + usuario);
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Intereses: " + intereses);
        Ticket1.TextoCentro("Cuotas: " + cuotas);
        Ticket1.TextoExtremo("Pago: ", pago);
        Ticket1.TextoExtremo("Cambio: ", cambio);
        Ticket1.TextoCentro("TPOO");
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

        listReportesDeuda = clientes();
        if (!listReportesDeuda.isEmpty()) {
            _paginadorReportes = new Paginador<>(listReportesDeuda,
                    _label.get(7), _reg_por_pagina);
        }
        SearchReportes("");
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE DEUDAS Y REPORTES"> 
    public void GetReportesDeudas(String valor) {
        _list = new ArrayList<>();
        String[] titulos = {"Id", "NoID", "Nombre", "Apellido",
            "Email", "Telefono", "IdReporte", "Fecha Limite"};
        modelo3 = new DefaultTableModel(null, titulos);
        var inicio = (_num_pagina - 1) * _reg_por_pagina;
        List<TReportes_clientes> list = new ArrayList<>();

        if (valor.equals("")) {
            list = Reportes_Clientes().stream().skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
            if (!list.isEmpty()) {

                list.forEach(item -> {
                    if (!item.getFechaLimite().equals("--/--/--")) {
                        try {
                            Date date1 = formateador.parse(item.getFechaLimite());
                            Date date2 = formateador.parse(new Calendario().getFecha());
                            long time = date1.getTime() - date2.getTime();
                            long days = time / (1000 * 60 * 60 * 24);
                            if (3 >= days) {
                                Object[] registros = {
                                    item.getID(),
                                    item.getNid(),
                                    item.getNombre(),
                                    item.getApellido(),
                                    item.getEmail(),
                                    item.getTelefono(),
                                    item.getIdReporte(),
                                    item.getFechaLimite(),
                                    item.getFechaPago(),
                                    item.getMensual(),
                                    item.getDeuda()
                                };
                                modelo3.addRow(registros);
                                if (0 > days) {
                                    InteresMora(registros, days);
                                }
                                _list.add(item);
                            }
                        } catch (ParseException ex) {
                            System.out.println("Error Date (Parse): " + ex);
                        }
                    }
                });

            }
        } else {
            list = Reportes_Clientes().stream().filter(C -> C.getNid().startsWith(valor)
                    || C.getNombre().startsWith(valor) || C.getApellido().startsWith(valor))
                    .skip(inicio).limit(_reg_por_pagina).collect(Collectors.toList());
            if (!list.isEmpty()) {

                list.forEach(item -> {
                    if (!item.getFechaLimite().equals("--/--/--")) {
                        try {
                            Date date1 = formateador.parse(item.getFechaLimite());
                            Date date2 = formateador.parse(new Calendario().getFecha());
                            long time = date1.getTime() - date2.getTime();
                            long days = time / (1000 * 60 * 60 * 24);
                            if (3 >= days) {
                                Object[] registros = {
                                    item.getID(),
                                    item.getNid(),
                                    item.getNombre(),
                                    item.getApellido(),
                                    item.getEmail(),
                                    item.getTelefono(),
                                    item.getIdReporte(),
                                    item.getFechaLimite(),
                                    item.getFechaPago(),
                                    item.getMensual(),
                                    item.getDeuda()
                                };
                                modelo3.addRow(registros);
                                _list.add(item);
                            }
                        } catch (ParseException ex) {
                            System.out.println("Error Date (Parse): " + ex);
                        }
                    }
                });

            }
        }
        if (_tableReporteDeuda != null) {
            _tableReporteDeuda.setModel(modelo3);
            _tableReporteDeuda.setRowHeight(30);
            _tableReporteDeuda.getColumnModel().getColumn(0).setMaxWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(0).setMinWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(0).setPreferredWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(6).setMaxWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(6).setMinWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(6).setPreferredWidth(0);
        }
    }

    public void GetReporteDeuda(DefaultTableModel selected, int fila) {
        if (selected != null) {
            Calendar calendar = Calendar.getInstance();
            try {
                var nombre = (String) selected.getValueAt(fila, 2);
                var apellido = (String) selected.getValueAt(fila, 3);
                _label.get(20).setText(nombre + " " + apellido);
                _idReporte = (Integer) selected.getValueAt(fila, 6);
                _fechaLimite = formateador.parse((String) selected.getValueAt(fila, 7));
                calendar.setTime(_fechaLimite);
                _dateChooser.setSelectedDate(calendar);
                Date date = formateador.parse(new Calendario().getFecha());
                long time = _fechaLimite.getTime() - date.getTime();
                diasMoras = time / (1000 * 60 * 60 * 24);

                if (0 < diasMoras) {
                    _label.get(21).setText(diasMoras + " días restantes");
                } else {
                    _label.get(21).setText("Se ha sobrepasado " + Math.abs(diasMoras) + " días");
                }
                _selectedCliente = selected;
            } catch (Exception e) {

            }
        }
    }

    public void ExtenderDias() {
        if (_selectedCliente != null) {
            if (0 <= diasMoras) {
                if (_checkBox_Dia.isSelected()) {
                    try {
                        _dateChooser.setFormat(3);
                        var date1 = formateador.parse(new Calendario().getFecha());
                        var date2 = _dateChooser.getSelectedDate().getTime();
                        if (date1.before(date2) && _fechaLimite.before(date2)) {
                            final QueryRunner qr = new QueryRunner(true);
                            String query = "UPDATE treportes_clientes SET FechaLimite = ?"
                                    + " WHERE IdReporte = " + _idReporte;
                            Object[] data = {formateador.format(date2)};

                            qr.update(getConn(), query, data);
                            ResetReportDeudas();
                        } else {
                            var fechaLimite = !date1.before(date2) ? new Calendario().getFecha() : formateador.format(_fechaLimite);
                            JOptionPane.showConfirmDialog(null, "Seleccione una fecha mayor a la fecha: " + fechaLimite + "\npara extender los días de pago al cliente", "Fecha para extender días",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        }
                        _dateChooser.setFormat(0);
                    } catch (Exception e) {
                    }
                } else {
                    JOptionPane.showConfirmDialog(null, "Seleccione la casilla para verificar \n" + "que extenderá los días de pago al cliente", "Extender días",
                            JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
                }
            } else {
                JOptionPane.showConfirmDialog(null, "No se le pueden extender los días al cliente seleccionado", "Extender días",
                        JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        } else {
            JOptionPane.showConfirmDialog(null, "Seleccione un cliente de la lista", "Extender días",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
    }

    private void InteresMora(Object[] cliente, long days) {
        var interests = ConfigurationVM.Interests;
        var id = (Integer) cliente[0];
        var fechaPago = (String) cliente[8];
        var mensual = (Double) cliente[9];
        if (!interests.equals(0.0)) {
            var clientesInteres1 = InteresesCliente().stream()
                    .filter(i -> i.getIdCliente() == id && i.getFechaInicial().equals(fechaPago))
                    .collect(Collectors.toList());

            int count1 = clientesInteres1.size();

            var clientesInteres2 = InteresesCliente().stream()
                    .filter(i -> i.getIdCliente() == id && i.getFechaInicial().equals(fechaPago) && i.getCancelado() == false)
                    .collect(Collectors.toList());

            long dias = Math.abs(days);
            Double porcentaje = interests / 100;
            Double moratorio = mensual * porcentaje;
            //Double moratorioDia = moratorioMensual / 30;
            //Double interes = moratorioDia * dias;
            int count2 = clientesInteres2.size();
            int pos = count2;
            pos--;
            if (count2 == 0) {
                for (int i = 1; i <= dias; i++) {
                    insert(cliente, new TIntereses_clientes(), i, false, moratorio);
                }
            } else {
                if (count1 < dias) {
                    if (count2 <= dias) {
                        long interesDias = dias - count1;
                        for (int i = 1; i <= interesDias; i++) {
                            insert(cliente, clientesInteres2.get(pos), i, true, moratorio);
                        }
                    }

                }
            }
        }
    }

    private void insert(Object[] cliente, TIntereses_clientes clientesInteres, int day, boolean value, Double interes) {
        Date fecha = null;
        final QueryRunner qr = new QueryRunner(true);
        var id = (Integer) cliente[0];
        var fechaLimite = (String) cliente[7];
        var fechaPago = (String) cliente[8];
        var mensual = (Double) cliente[9];
        var deuda = (Double) cliente[10];

        try {
            getConn().setAutoCommit(false);
            if (value) {
                fecha = formateador.parse(clientesInteres.getFecha());
            } else {
                fecha = formateador.parse(fechaLimite);
            }
            var dateNow = new Calendario().addDay(fecha, day);
            String query = "INSERT INTO tintereses_clientes(IdCliente,FechaInicial, Deuda,Mensual,Intereses,Cancelado,Fecha) VALUES(?,?,?,?,?,?,?)";
            Object[] data = {
                id,
                fechaPago,
                deuda,
                mensual,
                interes,
                false,
                dateNow,};
            qr.insert(getConn(), query, new ColumnListHandler(), data);
            getConn().commit();
        } catch (Exception e) {
            try {
                getConn().rollback();
            } catch (SQLException ex) {

            }
        }
    }

    private void ResetReportDeudas() {
        diasMoras = 0;
        _label.get(20).setText("Cliente");
        _label.get(21).setText("Días");
        Calendar c = new GregorianCalendar();
        _dateChooser.setSelectedDate(c);
        GetReportesDeudas("");
        GetReportesDeudas("");
        if (_list.isEmpty()) {
            _paginadorReportesDeuda = new Paginador<>(_list, _label.get(7), _reg_por_pagina);
        }
    }

// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="PAGINADOR"> 
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
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.primero();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.primero();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.primero();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.primero();
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
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.anterior();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.anterior();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.anterior();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.anterior();
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
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.siguiente();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.siguiente();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.siguiente();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.siguiente();
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
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.ultimo();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.ultimo();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.ultimo();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.ultimo();
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
                switch (_seccion1) {
                    case 0:
                        SearchReportes("");
                        break;
                    case 1:
                        historialPagos(false);
                        break;
                    case 2:
                        historialIntereses(false);
                        break;
                }
                break;
            case 3:
                GetReportesDeudas("");
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
                switch (_seccion1) {
                    case 0:
                        if (!listReportesDeuda.isEmpty()) {
                            _paginadorReportes = new Paginador<>(listReportesDeuda, _label.get(7), _reg_por_pagina);
                        }
                        break;
                    case 1:
                        if (!listPagos.isEmpty()) {
                            _paginadorPagos = new Paginador<>(listPagos, _label.get(7), _reg_por_pagina);
                        }
                        historialPagos(false);
                        break;
                    case 2:
                        if (!listPagosIntereses.isEmpty()) {
                            _paginadorPagosIntereses = new Paginador<>(listPagosIntereses, _label.get(7), _reg_por_pagina);
                        }
                        historialIntereses(false);
                        break;
                }
                break;
            case 3:
                if (!_list.isEmpty()) {
                    _paginadorReportesDeuda = new Paginador<>(_list, _label.get(7), _reg_por_pagina);
                }
                GetReportesDeudas("");
                break;
        }
    }
    // </editor-fold>
}
