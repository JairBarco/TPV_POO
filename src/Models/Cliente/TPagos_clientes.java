package Models.Cliente;

import java.util.Date;

/**
 *
 * @author jair_
 */
public class TPagos_clientes {
    public int Id;
    public int IdCliente;
    public Double Deuda;
    public Double Saldo;
    public Double Pago;
    public Double Cambio;
    public Date Fecha;
    public Date FechaLimite;
    public String Ticket;
    public int IdUsuario;
    public String Usuario;
    public Date FechaDeuda;
    public Double Mensual;

    public TPagos_clientes() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int IdCliente) {
        this.IdCliente = IdCliente;
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

    public Date getFechaLimite() {
        return FechaLimite;
    }

    public void setFechaLimite(Date FechaLimite) {
        this.FechaLimite = FechaLimite;
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