package Models.Producto;

/**
 *
 * @author jair_
 */
public class TTemporal_productos {

    public int Id;
    public int IdProducto;
    public int IdUsuario;

    public TTemporal_productos() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(int IdProducto) {
        this.IdProducto = IdProducto;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }
}