/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models.Venta;

/**
 *
 * @author jair_
 */
public class TVentas_temporal {
    private int IdTempo;
    private String Codigo;
    private String Descripcion;
    private Double Precio;
    private Double Descuento;
    private int Cantidad;
    private Double Importe;
    private int Caja;
    private int IdUsuario;

    public TVentas_temporal() {
    }

    public int getIdTempo() {
        return IdTempo;
    }

    public void setIdTempo(int IdTempo) {
        this.IdTempo = IdTempo;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double Precio) {
        this.Precio = Precio;
    }

    public Double getDescuento() {
        return Descuento;
    }

    public void setDescuento(Double Descuento) {
        this.Descuento = Descuento;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public Double getImporte() {
        return Importe;
    }

    public void setImporte(Double Importe) {
        this.Importe = Importe;
    }

    public int getCaja() {
        return Caja;
    }

    public void setCaja(int Caja) {
        this.Caja = Caja;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }
}
