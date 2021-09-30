package Models.Cliente;

/**
 *
 * @author jair_
 */
public class TPagos_reportes_intereses_clientes {
    
    private int Id;
    private Double Intereses;
    private Double Pago;
    private Double Cambio;
    private int Cuotas;
    private String Fecha;
    private String Ticket;
    private int IdUsuario;
    private String Usuario;
    private int IdCliente;

    public TPagos_reportes_intereses_clientes() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public Double getIntereses() {
        return Intereses;
    }

    public void setIntereses(Double Intereses) {
        this.Intereses = Intereses;
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

    public int getCuotas() {
        return Cuotas;
    }

    public void setCuotas(int Cuotas) {
        this.Cuotas = Cuotas;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
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

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int IdCliente) {
        this.IdCliente = IdCliente;
    }
}