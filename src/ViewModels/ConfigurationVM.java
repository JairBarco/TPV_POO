package ViewModels;

import Conexion.Consult;
import Library.FormatDecimal;
import Models.TConfiguration;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JRadioButton;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author jair_
 */
public class ConfigurationVM extends Consult {

    public static String Money;
    public static Double Interests;
    private static List<JRadioButton> _radio;
    private List<JTextField> _textField;
    private List<JLabel> _label;
    private FormatDecimal _format;

    public ConfigurationVM() {
        TypeMoney();
    }

    public ConfigurationVM(List<JRadioButton> radio) {
        _radio = radio;
        RadioEvent();
        TypeMoney();
        Reset();
    }

    public ConfigurationVM(List<JRadioButton> radio, List<JTextField> textField, List<JLabel> label) {
        _radio = radio;
        _textField = textField;
        _label = label;
        _format = new FormatDecimal();
        RadioEvent();
        TypeMoney();
        Reset();
    }

    public void RadioEvent() {
        _radio.get(0).addActionListener((ActionEvent e) -> {
            TypeMoney("MXN. ", _radio.get(0).isSelected());
        });

        _radio.get(1).addActionListener((ActionEvent e) -> {
            TypeMoney("USD. ", _radio.get(1).isSelected());
        });
    }
    private String sqlConfig;

    public void TypeMoney() {
        sqlConfig = "INSERT INTO tconfiguration(TypeMoney) VALUES(?)";

        List<TConfiguration> config = config();
        final QueryRunner qr = new QueryRunner(true);

        if (config.isEmpty()) {
            Money = "MXN. ";
            Object[] dataConfig = {Money};
            try {
                qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);
            } catch (SQLException ex) {

            }
        } else {
            TConfiguration data = config.get(0);
            Money = data.getTypeMoney();
            switch (Money) {
                case "MXN. ":
                    _radio.get(0).setSelected(true);
                    break;
                case "USD. ":
                    _radio.get(1).setSelected(true);
                    break;
            }
        }
    }

    public void TypeMoney(String typeMoney, boolean valor) {
        final QueryRunner qr = new QueryRunner(true);

        if (valor) {
            try {
                List<TConfiguration> config = config();
                if (config.isEmpty()) {

                    sqlConfig = "INSERT INTO tconfiguration(TypeMoney) VALUES(?)";
                    Money = typeMoney;
                    Object[] dataConfig = {Money};
                    qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);

                } else {
                    TConfiguration data = config.get(0);
                    sqlConfig = "UPDATE tconfiguration SET TypeMoney = ? WHERE ID =" + data.getID();
                    if (data.getTypeMoney().equals(typeMoney)) {
                        Money = typeMoney;
                    } else {
                        Money = typeMoney;
                        Object[] dataConfig = {Money};
                        qr.update(getConn(), sqlConfig, dataConfig);
                    }
                }
            } catch (SQLException e) {
            }
        }
    }

    public void RegistrarIntereses() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el interés");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (_radio.get(2).isSelected()) {
                try{
                    final QueryRunner qr = new QueryRunner(true);
                    var _tconfiguration = config();
                    if(_tconfiguration.isEmpty()){
                        var sqlConfig = "INSERT INTO tconfiguration(TypeMoney,Interests) VALUES(?,?)";
                        Object[] dataConfig = {"MXN.", _format.reconstruir(_textField.get(0).getText())};
                        qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);
                    } else {
                        var data = _tconfiguration.get(0);
                        var sqlConfig = "UPDATE tconfiguration SET TypeMoney = ?, "
                                + "Interests = ? WHERE ID= " + data.getID();
                        Object[] dataConfig = {
                            data.getTypeMoney(),
                            _format.reconstruir(_textField.get(0).getText())
                        };
                        qr.update(getConn(), sqlConfig, dataConfig);
                    }
                    Reset();
                } catch(Exception e){
                    
                }
            } else {
                _label.get(0).setText("Seleccione la opción intereses");
                _label.get(0).setForeground(Color.RED);
            }
        }
    }
    private void Reset(){
        var _tconfiguration = config();
        if(!_tconfiguration.isEmpty()){
            var data = _tconfiguration.get(0);
            Interests = data.getInterests();
            if(_label != null){
                var interest = data.getInterests() == 0.0 ? "0.0%" : data.getInterests() + "%";
                _label.get(1).setText(interest);
                _textField.get(0).setText("");
                _label.get(0).setText("");
                _radio.get(0).setSelected(false);
            } else {
                
            }
        }
    }
}
