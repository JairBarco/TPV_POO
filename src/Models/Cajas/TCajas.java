package Models.Cajas;

import java.util.Date;

/**
 *
 * @author jair_
 */
public class TCajas {
    private int IdCaja;
    private int Caja;
    private boolean Estado;
    private Date Fecha;

    public TCajas() {
    }

    public int getIdCaja() {
        return IdCaja;
    }

    public void setIdCaja(int IdCaja) {
        this.IdCaja = IdCaja;
    }

    public int getCaja() {
        return Caja;
    }

    public void setCaja(int Caja) {
        this.Caja = Caja;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean Estado) {
        this.Estado = Estado;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }
}