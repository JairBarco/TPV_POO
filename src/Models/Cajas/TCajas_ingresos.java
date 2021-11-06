package Models.Cajas;

import java.util.Date;

/**
 *
 * @author jair_
 */
public class TCajas_ingresos extends TCajas{
    private int IdCajaIngreso;
    private int IdCaja;
    private int IdUsuario;
    private Double Billete;
    private Double Moneda;
    private Double Ingreso;
    private Date Fecha;

    public TCajas_ingresos() {
    }

    public int getIdCajaIngreso() {
        return IdCajaIngreso;
    }

    public void setIdCajaIngreso(int IdCajaIngreso) {
        this.IdCajaIngreso = IdCajaIngreso;
    }

    public int getIdCaja() {
        return IdCaja;
    }

    public void setIdCaja(int IdCaja) {
        this.IdCaja = IdCaja;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public Double getBillete() {
        return Billete;
    }

    public void setBillete(Double Billete) {
        this.Billete = Billete;
    }

    public Double getMoneda() {
        return Moneda;
    }

    public void setMoneda(Double Moneda) {
        this.Moneda = Moneda;
    }

    public Double getIngreso() {
        return Ingreso;
    }

    public void setIngreso(Double Ingreso) {
        this.Ingreso = Ingreso;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }
}