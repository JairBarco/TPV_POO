package Models.Compras;

import java.util.Date;

/**
 *
 * @author jair_
 */
public class TReportes_compras {
    public int IdReportes;
    public String Ticket;
    public int Productos;
    public Double Efectivo;
    public Double Credito;
    public Double Pago;
    public Double Deuda;
    public Double Cambio;
    public Date Fecha;
    public int IdProveedor;

    public TReportes_compras() {
    }

    public int getIdReportes() {
        return IdReportes;
    }

    public void setIdReportes(int IdReportes) {
        this.IdReportes = IdReportes;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String Ticket) {
        this.Ticket = Ticket;
    }

    public int getProductos() {
        return Productos;
    }

    public void setProductos(int Productos) {
        this.Productos = Productos;
    }

    public Double getEfectivo() {
        return Efectivo;
    }

    public void setEfectivo(Double Efectivo) {
        this.Efectivo = Efectivo;
    }

    public Double getCredito() {
        return Credito;
    }

    public void setCredito(Double Credito) {
        this.Credito = Credito;
    }

    public Double getPago() {
        return Pago;
    }

    public void setPago(Double Pago) {
        this.Pago = Pago;
    }

    public Double getDeuda() {
        return Deuda;
    }

    public void setDeuda(Double Deuda) {
        this.Deuda = Deuda;
    }

    public Double getCambio() {
        return Cambio;
    }

    public void setCambio(Double Cambio) {
        this.Cambio = Cambio;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }

    public int getIdProveedor() {
        return IdProveedor;
    }

    public void setIdProveedor(int IdProveedor) {
        this.IdProveedor = IdProveedor;
    }
}