package Models.Cajas;

import java.util.Date;

/**
 *
 * @author jair_
 */
public class TCajas_registros {
    private int IdCajaRegistro;
    private int IdCaja;
    private int IdUsuario;
    private boolean Estado;
    private Date Fecha;

    public TCajas_registros() {
    }

    public int getIdCajaRegistro() {
        return IdCajaRegistro;
    }

    public void setIdCajaRegistro(int IdCajaRegistro) {
        this.IdCajaRegistro = IdCajaRegistro;
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