package Library;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 *
 * @author jair_
 */
public class FormatDecimal {

    Number numero;
    DecimalFormat formateador = new DecimalFormat("###,###,##0.00");

    public String decimal(double formato) {
        return formateador.format(formato);
    }

    public double reconstruir(String formato) {
        try {
            numero = formateador.parse(formato.replace(" ", ""));
        } catch (ParseException ex) {
            System.out.println("Error: " + ex);
        }
        return numero.doubleValue();
    }
}
