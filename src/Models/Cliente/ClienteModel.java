package Models.Cliente;

public class ClienteModel {
    private int ID;
    private String Nid;
    private String Nombre;
    private String Apellido;
    private String Email;
    private String Telefono;
    private int IdReporte;
    private String FechaLimite;

    public ClienteModel(int ID, String Nid, String Nombre, String Apellido, String Email, String Telefono, int IdReporte, String FechaLimite) {
        this.ID = ID;
        this.Nid = Nid;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Email = Email;
        this.Telefono = Telefono;
        this.IdReporte = IdReporte;
        this.FechaLimite = FechaLimite;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNid() {
        return Nid;
    }

    public void setNid(String Nid) {
        this.Nid = Nid;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public int getIdReporte() {
        return IdReporte;
    }

    public void setIdReporte(int IdReporte) {
        this.IdReporte = IdReporte;
    }

    public String getFechaLimite() {
        return FechaLimite;
    }

    public void setFechaLimite(String FechaLimite) {
        this.FechaLimite = FechaLimite;
    }
}