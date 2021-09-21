package Models.Cliente;

/**
 *
 * @author jair_
 */
public class TReportes_intereses_clientes {
    private int Id;
    private double Intereses;
    private double Pago;
    private double Cambio;
    private int Cuotas;
    private String InteresFecha;
    private String TicketIntereses;
    private int IdCliente;

    public TReportes_intereses_clientes() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public double getIntereses() {
        return Intereses;
    }

    public void setIntereses(double Intereses) {
        this.Intereses = Intereses;
    }

    public double getPago() {
        return Pago;
    }

    public void setPago(double Pago) {
        this.Pago = Pago;
    }

    public double getCambio() {
        return Cambio;
    }

    public void setCambio(double Cambio) {
        this.Cambio = Cambio;
    }

    public int getCuotas() {
        return Cuotas;
    }

    public void setCuotas(int Cuotas) {
        this.Cuotas = Cuotas;
    }

    public String getInteresFecha() {
        return InteresFecha;
    }

    public void setInteresFecha(String InteresFecha) {
        this.InteresFecha = InteresFecha;
    }

    public String getTicketIntereses() {
        return TicketIntereses;
    }

    public void setTicketIntereses(String TicketIntereses) {
        this.TicketIntereses = TicketIntereses;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int IdCliente) {
        this.IdCliente = IdCliente;
    }
}
