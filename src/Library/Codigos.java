package Library;

import Conexion.Consult;
import Models.Producto.TProductos;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;

/**
 *
 * @author jair_
 */
public class Codigos extends Objetos{

    private String tickets = null;
    public String Codigo = null;

    public String codesTickets(String ticket) {
        if (ticket == null || ticket.equals("9999999999")) {
            tickets = "0000000001";
        } else {
            int num = Integer.valueOf(ticket);
            num++;
            tickets = String.format("%010d", num);
        }
        return tickets;
    }
    
    private Double precio1 = 0.0;
    public String codigoBarra(JLabel labelCodigo, String codigosBarra, String producto, String precio){
        int codigo = 0, count = 0;
        Codigo = codigosBarra;
        precio1 = precio.equals("") ? 0.0 : Double.valueOf(precio);
        List<TProductos> product1;
        if(0 < Productos().size()){
            product1 = Productos().stream().filter(p -> p.getProducto().replace(" ", "").toLowerCase().equals(producto.replace(" ", "").toLowerCase()) && p.getPrecio().equals(precio1)).collect(Collectors.toList());
        } else {
            product1 = new ArrayList<TProductos>();
        }
        if(codigosBarra.equals("")){
            if(0 < product1.size()){
                Codigo = product1.get(0).getCodigo();
            } else {
                do{
                    codigo = ThreadLocalRandom.current().nextInt(100000, 1000000000 + 1);
                    Codigo = String.valueOf(codigo);
                    var product2 = Productos().stream().filter(p -> p.getCodigo().equals(Codigo)).collect(Collectors.toList());
                    count = product2.size();
                } while (count > 0);
            }
        } else {
            var product2 = Productos().stream().filter(p -> p.getCodigo().equals(codigosBarra)).collect(Collectors.toList());
            if(0 < product2.size()){
                if(0 < product1.size()){
                    var data1 = product1.get(0).getProducto().replace(" ", "").toLowerCase();
                    var data2 = product2.get(0).getProducto().replace(" ", "").toLowerCase();
                    if(data1.equals(data2) && product1.get(0).getPrecio().equals(product2.get(0).getPrecio())){
                        Codigo = codigosBarra;
                    } else {
                        Codigo = null;
                    }
                } else {
                    Codigo = codigosBarra;
                }
            } else {
                Codigo = codigosBarra;
            }
        }
        if(Codigo != null){
            try{
                Barcode barcode = BarcodeFactory.createCode39(Codigo, true);
                barcode.setDrawingText(true);
                barcode.setBarWidth(2);
                BufferedImage image = new BufferedImage(344, 80, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) image.getGraphics(); 
                barcode.draw(g,5,20);
                ImageIcon icon = new ImageIcon(image);
                labelCodigo.setIcon(icon);
            } catch(Exception e){
                
            }
        } else {
            
        }
        return Codigo;
    }
}
