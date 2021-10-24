package Models.Proveedor;

import java.util.Date;

/**
 *
 * @author jair_
 */
public class TPagos_proveedor {
    private int Id;
    private int IdProveedor;
    private Double Deuda;
    private Double Saldo;
    private Double Pago;
    private Double Cambio;
    private Date Fecha;
    private String Ticket;
    private int IdUsuario;
    private String Usuario;
    private Date FechaDeuda;
    private Double Mensual;

    public TPagos_proveedor() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdProveedor() {
        return IdProveedor;
    }

    public void setIdProveedor(int IdProveedor) {
        this.IdProveedor = IdProveedor;
    }

    public Double getDeuda() {
        return Deuda;
    }

    public void setDeuda(Double Deuda) {
        this.Deuda = Deuda;
    }

    public Double getSaldo() {
        return Saldo;
    }

    public void setSaldo(Double Saldo) {
        this.Saldo = Saldo;
    }

    public Double getPago() {
        return Pago;
    }

    public void setPago(Double Pago) {
        this.Pago = Pago;
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

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String Ticket) {
        this.Ticket = Ticket;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public Date getFechaDeuda() {
        return FechaDeuda;
    }

    public void setFechaDeuda(Date FechaDeuda) {
        this.FechaDeuda = FechaDeuda;
    }

    public Double getMensual() {
        return Mensual;
    }

    public void setMensual(Double Mensual) {
        this.Mensual = Mensual;
    }
}