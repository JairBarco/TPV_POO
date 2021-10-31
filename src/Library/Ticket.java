package Library;

import javax.print.*;
import javax.print.attribute.*;
import javax.swing.JOptionPane;

/**
 *
 * @author jair_
 */
public class Ticket {

    private StringBuilder lineas = new StringBuilder();
    private final int maxCaracter = 40;
    private int stop;
    private FormatDecimal _format = new FormatDecimal();

    public String LineasGuion() {
        String linea = "";
        for (int i = 0; i < maxCaracter; i++) {
            linea += "-";
        }
        return lineas.append(linea).append("\n").toString();
    }

    public String LineAsteriscos() {
        String asterisco = "";
        for (int i = 0; i < maxCaracter; i++) {
            asterisco += "*";
        }
        return lineas.append(asterisco).append("\n").toString();
    }

    public String LineaIgual() {
        String igual = "";
        for (int i = 0; i < maxCaracter; i++) {
            igual += "=";
        }
        return lineas.append(igual).append("\n").toString();
    }

    public void EncabezadoVenta(String columnas) {
        lineas.append(columnas).append("\n");
    }

    public void TextoIzquierda(String texto) {
        if (texto.length() > maxCaracter) {
            int caracterActual = 0;
            for (int i = texto.length(); i > maxCaracter; i -= maxCaracter) {
                lineas.append(texto.substring(caracterActual, maxCaracter)).append("\n");
                caracterActual += maxCaracter;
            }
            lineas.append(texto.substring(caracterActual, texto.length() - caracterActual)).append("\n");
        } else {
            lineas.append(texto).append("\n");
        }
    }

    public void TextoDerecho(String texto) {
        if (texto.length() > maxCaracter) {
            int caracterActual = 0;
            for (int i = texto.length(); i > maxCaracter; i -= maxCaracter) {
                lineas.append(texto.substring(caracterActual, maxCaracter)).append("\n");
                caracterActual += maxCaracter;
            }
            String espacios = "";
            for (int i = 0; i < (maxCaracter - texto.substring(caracterActual,
                    texto.length() - caracterActual).length()); i++) {
                espacios += " ";
            }
            lineas.append(espacios).append(texto.substring(caracterActual, texto.length() - caracterActual)).append("\n");
        } else {
            String espacios = "";
            for (int i = 0; i < (maxCaracter - texto.length()); i++) {
                espacios += " ";
            }
            lineas.append(espacios).append(texto).append("\n");
        }
    }

    public void TextoCentro(String texto) {
        if (texto.length() > maxCaracter) {
            int caracterActual = 0;
            for (int i = texto.length(); i > maxCaracter; i -= maxCaracter) {
                lineas.append(texto.substring(caracterActual, maxCaracter)).append("\n");
                caracterActual += maxCaracter;
            }
            String espacios = "";
            int centrar = (maxCaracter - texto.substring(caracterActual,
                    texto.length() - caracterActual).length()) / 2;
            for (int i = 0; i < centrar; i++) {
                espacios += " ";
            }
            lineas.append(espacios).append(texto.substring(caracterActual,
                    texto.length() - caracterActual)).append("\n");
        } else {
            String espacios = "";
            int centrar = (maxCaracter - texto.length()) / 2;
            for (int i = 0; i < centrar; i++) {
                espacios += " ";
            }
            lineas.append(espacios).append(texto).append("\n");
        }
    }

    public void TextoExtremo(String izquierdo, String derecho) {
        String der, izq, completo = "", espacio = "";
        if (izquierdo.length() > 18) {
            stop = izquierdo.length() - 18;
            izq = izquierdo.substring(stop, 19);
        } else {
            izq = izquierdo;
        }
        completo = izq;
        if (derecho.length() > 20) {
            stop = derecho.length() - 20;
            der = izquierdo.substring(stop, 20);
        } else {
            der = derecho;
        }
        int numEspacios = maxCaracter - (izq.length() + der.length());
        for (int i = 0; i < numEspacios; i++) {
            espacio += "";
        }
        completo += espacio + derecho;
        lineas.append(completo).append("\n");
    }

    public void AgregarTotales(String texto, double total, String money) {
        String resumen, valor, completo = "", espacio = "";
        if (texto.length() > 25) {
            stop = texto.length() - 25;
            resumen = texto.substring(stop, 25);
        } else {
            resumen = texto;
        }
        completo = resumen;
        valor = money + _format.decimal(total);
        int numEspacios = maxCaracter - (resumen.length() + valor.length());
        for (int i = 0; i < numEspacios; i++) {
            espacio += " ";
        }
        completo += espacio + valor;
        lineas.append(completo).append("\n");
    }

    public void AgregarArticulo(String articulo, String cant, String precio) {
        String elemento = "", espacios = "";
        boolean bandera = false;
        int numEspacios = 10;
        if (articulo.length() > 20) {
            //Colocar la cantidad a la derecha
            espacios = "";
            for (int i = 0; i < (numEspacios - cant.length()); i++) {
                espacios += " ";
            }
            elemento += cant + espacios;

            //Colocar el precio a la derecha
            espacios = "";
            for (int i = 0; i < numEspacios; i++) {
                espacios += " ";
            }
            elemento += precio + espacios;
           
            int caracterActual = 0;
            for (int i = articulo.length(); i > 20; i -= 20) {
                if (bandera) {
                    lineas.append(articulo.substring(0, caracterActual)).append("\n");
                } else {
                    lineas.append(articulo.substring(caracterActual, 20)).append(elemento).append("\n");
                    bandera = true;
                }
                caracterActual +=20;
            }
            lineas.append(articulo.substring(0, caracterActual)).append("\n");
        } else {
            for (int i = 0; i < (20 - articulo.length()); i++) {
                espacios += " "; //Agregar espacios para poner el valor del articulo
            }
            elemento = articulo + espacios;
            //Colocar cantidad a la derecha
            espacios = "";
            for (int i = 0; i < (numEspacios - cant.length()); i++) {
                espacios += " ";
            }
            elemento += cant + espacios;
            //Colocar precio a la derecha
            espacios = "";
            for (int i = 0; i < (numEspacios - cant.length()); i++) {
                espacios += " ";
            }
            elemento += precio + espacios;
            lineas.append(elemento).append("\n");
        }
    }

    public void print() {
        //Tipo de dato a imprimir
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        //Obtener el servicio de impresion por defecto con el siguiente codigo
        //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        //Procedimiento para escoger por nuestra cuenta la impresora
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        PrintService service = ServiceUI.printDialog(null, 700, 200, printService, defaultService, flavor, pras);

        //Arreglo byte
        byte[] bytes;
        //Convertir String a Bytes para que lo acepte la impresora
        bytes = lineas.toString().getBytes();
        //Crear documento para imprimir
        Doc doc = new SimpleDoc(bytes, flavor, null);
        //Crear job de impresión
        DocPrintJob job = service.createPrintJob();

        //Imprimir (en un try catch)
        try {
            //Método para imprimir
            job.print(doc, null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + e.getMessage());
        }
    }
}
