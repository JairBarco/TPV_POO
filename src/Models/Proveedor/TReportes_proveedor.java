package Models.Proveedor;

import java.util.Date;

public class TReportes_proveedor extends TProveedor{
    private int IdReporte;
    private Double DeudaActual;
    private Double Deuda;
    private Double Mensual;
    private Double Cambio;
    private Date FechaDeuda;
    private double UltimoPago;
    private Date FechaPago;
    private String Ticket;
    private int IdProveedor;
    private String FormaPago;

    public TReportes_proveedor() {
    }

    public int getIdReporte() {
        return IdReporte;
    }

    public void setIdReporte(int IdReporte) {
        this.IdReporte = IdReporte;
    }

    public Double getDeudaActual() {
        return DeudaActual;
    }

    public void setDeudaActual(Double DeudaActual) {
        this.DeudaActual = DeudaActual;
    }

    public Double getDeuda() {
        return Deuda;
    }

    public void setDeuda(Double Deuda) {
        this.Deuda = Deuda;
    }

    public Double getMensual() {
        return Mensual;
    }

    public void setMensual(Double Mensual) {
        this.Mensual = Mensual;
    }

    public Double getCambio() {
        return Cambio;
    }

    public void setCambio(Double Cambio) {
        this.Cambio = Cambio;
    }

    public Date getFechaDeuda() {
        return FechaDeuda;
    }

    public void setFechaDeuda(Date FechaDeuda) {
        this.FechaDeuda = FechaDeuda;
    }

    public double getUltimoPago() {
        return UltimoPago;
    }

    public void setUltimoPago(double UltimoPago) {
        this.UltimoPago = UltimoPago;
    }

    public Date getFechaPago() {
        return FechaPago;
    }

    public void setFechaPago(Date FechaPago) {
        this.FechaPago = FechaPago;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String Ticket) {
        this.Ticket = Ticket;
    }

    public int getIdProveedor() {
        return IdProveedor;
    }

    public void setIdProveedor(int IdProveedor) {
        this.IdProveedor = IdProveedor;
    }

    public String getFormaPago() {
        return FormaPago;
    }

    public void setFormaPago(String FormaPago) {
        this.FormaPago = FormaPago;
    }
}