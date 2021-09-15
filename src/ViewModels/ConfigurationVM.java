package ViewModels;

import Conexion.Consult;
import Models.TConfiguration;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import java.sql.SQLException;

/**
 *
 * @author jair_
 */
public class ConfigurationVM extends Consult {

    private static String Money;
    private static List<JRadioButton> _radio;

    public ConfigurationVM() {
        TypeMoney();
    }

    public ConfigurationVM(List<JRadioButton> radio) {
        _radio = radio;
        RadioEvent();
        TypeMoney();
    }

    public void RadioEvent() {
        _radio.get(0).addActionListener((ActionEvent e) -> {
            TypeMoney("MXN.", _radio.get(0).isSelected());
        });

        _radio.get(1).addActionListener((ActionEvent e) -> {
            TypeMoney("USD.", _radio.get(1).isSelected());
        });
    }
    private String sqlConfig;

    public void TypeMoney() {
        sqlConfig = "INSERT INTO tconfiguration(TypeMoney) VALUES(?)";

        List<TConfiguration> config = config();
        final QueryRunner qr = new QueryRunner(true);

        if (config.isEmpty()) {
            Money = "MXN.";
            Object[] dataConfig = {Money};
            try {
                qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);
            } catch (SQLException ex) {

            }
        } else {
            TConfiguration data = config.get(0);
            Money = data.getTypeMoney();
            switch (Money) {
                case "MXN.":
                    _radio.get(0).setSelected(true);
                    break;
                case "USD.":
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
                    sqlConfig = "UPDATE tconfiguration SET TypeMoney = ? WHERE ID ="+data.getID();
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
}