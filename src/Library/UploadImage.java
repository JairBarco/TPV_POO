package Library;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UploadImage extends javax.swing.JFrame {

    private File archivo;
    private JFileChooser abrirArchivo;
    private static String urlOrigen = null;
    private static byte[] imageByte = null;

    public static byte[] getImageByte() {
        return imageByte;
    }

    public void cargarImagen(JLabel label) {
        abrirArchivo = new JFileChooser();
        abrirArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de Imagen", "jpg", "png", "gif"));
        int respuesta = abrirArchivo.showOpenDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            archivo = abrirArchivo.getSelectedFile();
            urlOrigen = archivo.getAbsolutePath();
            Image foto = getToolkit().getImage(urlOrigen);
            foto = foto.getScaledInstance(160, 136, 1);
            label.setIcon(new ImageIcon(foto));
            imageByte = new byte[(int) archivo.length()];
        }

    }
    
    public byte[] getTransFoto(JLabel label){
        ByteArrayOutputStream baos = null;
        
        try{
            Icon ico = label.getIcon();
            //Create buffered image
            BufferedImage bufferedImage = new BufferedImage(ico.getIconWidth(), ico.getIconHeight(),
            BufferedImage.TYPE_INT_RGB);
            
            baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
        }catch (IOException e){
            
        }
        return baos.toByteArray();
    }
}
