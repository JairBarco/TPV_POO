package Models.Cajas;

import java.util.Date;

/**
 *
 * @author jair_
 */
public class TCajas_reportes {
    private int IdCajaReporte;
    private int IdCaja;
    private int IdUsuario;
    private double Billete;
    private double Moneda;
    private String TipoIngreso;
    private double Ingreso;
    private Date Fecha;

    public TCajas_reportes() {
    }

    public int getIdCajaReporte() {
        return IdCajaReporte;
    }

    public void setIdCajaReporte(int IdCajaReporte) {
        this.IdCajaReporte = IdCajaReporte;
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

    public double getBillete() {
        return Billete;
    }

    public void setBillete(double Billete) {
        this.Billete = Billete;
    }

    public double getMoneda() {
        return Moneda;
    }

    public void setMoneda(double Moneda) {
        this.Moneda = Moneda;
    }

    public String getTipoIngreso() {
        return TipoIngreso;
    }

    public void setTipoIngreso(String TipoIngreso) {
        this.TipoIngreso = TipoIngreso;
    }

    public double getIngreso() {
        return Ingreso;
    }

    public void setIngreso(double Ingreso) {
        this.Ingreso = Ingreso;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }
}