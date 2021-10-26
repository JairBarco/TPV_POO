package ViewModels;

import Conexion.Consult;
import Library.FormatDecimal;
import Models.Proveedor.TProveedor;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

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

    public ComprasVM(TUsuarios dataUsuario) {
        _dataUsuario = dataUsuario;
    }

    public ComprasVM(Object[] objetos, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _checkBoxCredito = (JCheckBox) objetos[0];
        _comboBoxProveedor = (JComboBox) objetos[1];
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
                        
                    }
                }
            }
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
    }
}
