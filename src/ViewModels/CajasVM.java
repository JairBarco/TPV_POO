package ViewModels;

import Library.*;
import Models.Usuario.TUsuarios;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
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
    private JTable _tableComprasTemporal, _tableProductos;
    private String _accion = "insert", _money;
    public Codigos codigos;
    public int _seccion = 0, _idProducto = 0;
    private JSpinner _spinnerCaja;

    public CajasVM(TUsuarios usuario) {
        _dataUsuario = usuario;
    }

    public CajasVM(Object[] objetos, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _spinnerCaja = (JSpinner) objetos[0];
        Reset();
    }

    public void RegistrarCajas() {
        try {
            var qr = new QueryRunner(true);
            getConn().setAutoCommit(false);
            var value = (Number) _spinnerCaja.getValue();
            var caja = value.intValue();
        } catch (Exception e) {
            
        }
    }

    public void Reset() {
        var model = new SpinnerNumberModel(
                10.0, 1.0, 30.0, 1.0
        );
        _spinnerCaja.setModel(model);
    }
}
