package Conexion;

import Models.*;
import Models.Cliente.*;
import Models.Ordenador.TOrdenadores;
import Models.Usuario.TRoles;
import Models.Usuario.TUsuarios;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author jair_
 */
public class Consult extends Conexion {

    private QueryRunner QR = new QueryRunner();

    public List<TClientes> clientes() {
        List<TClientes> cliente = new ArrayList();
        try {
            cliente = (List<TClientes>) QR.query(getConn(), "SELECT * FROM tclientes",
                    new BeanListHandler(TClientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return cliente;
    }

    public List<TReportes_clientes> reportesClientes(int idCliente) {
        String where = " WHERE tclientes.ID =" + idCliente;
        List<TReportes_clientes> reportes = new ArrayList();
        String condicion1 = " tclientes.ID = treportes_clientes.IdCliente ";
        String condicion2 = " tclientes.ID = treportes_intereses_clientes.IdCliente ";

        String campos = " tclientes.Nid,tclientes.Nombre,tclientes.Apellido,"
                + "treportes_clientes.IdReporte,treportes_clientes.DeudaActual,"
                + "treportes_clientes.FechaDeuda,treportes_clientes.UltimoPago,"
                + "treportes_clientes.FechaPago, treportes_clientes.Ticket,"
                + "treportes_clientes.FechaLimite,treportes_clientes.Deuda,treportes_clientes.Mensual,"
                + "treportes_clientes.Cambio,treportes_intereses_clientes.Intereses,"
                + "treportes_intereses_clientes.Pago,treportes_intereses_clientes.Cambio,"
                + "treportes_intereses_clientes.Cuotas,treportes_intereses_clientes.InteresFecha,"
                + "treportes_intereses_clientes.TicketIntereses,treportes_intereses_clientes.Id";
        try {
            reportes = (List<TReportes_clientes>) QR.query(getConn(),
                    "SELECT" + campos + " FROM tclientes Inner Join treportes_clientes ON"
                    + condicion1 + "Inner Join treportes_intereses_clientes ON" + condicion2
                    + where, new BeanListHandler(TReportes_clientes.class));
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return reportes;
    }

    public List<TConfiguration> config() {
        List<TConfiguration> config = new ArrayList();
        try {
            config = (List<TConfiguration>) QR.query(getConn(), "SELECT * FROM tconfiguration",
                    new BeanListHandler(TConfiguration.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return config;
    }

    public List<TIntereses_clientes> InteresesCliente() {
        List<TIntereses_clientes> intereses = new ArrayList();
        try {
            intereses = (List<TIntereses_clientes>) QR.query(getConn(), "SELECT * FROM tintereses_clientes",
                    new BeanListHandler(TIntereses_clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return intereses;
    }

    public List<TReportes_clientes> ReporteCliente() {
        List<TReportes_clientes> reporte = new ArrayList();
        try {
            reporte = (List<TReportes_clientes>) QR.query(getConn(), "SELECT * FROM treportes_clientes",
                    new BeanListHandler(TReportes_clientes.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }

        return reporte;
    }

    public List<TReportes_clientes> Reportes_Clientes() {
        String where = "";
        List<TReportes_clientes> reportes = new ArrayList();
        String condicion1 = " tclientes.ID = treportes_clientes.IdCliente ";
        String campos = " tclientes.ID,tclientes.Nid,tclientes.Nombre,tclientes.Apellido,"
                + "tclientes.Telefono,tclientes.Email,tclientes.Direccion,"
                + "treportes_clientes.IdReporte,treportes_clientes.DeudaActual,"
                + "treportes_clientes.FechaDeuda,treportes_clientes.UltimoPago,"
                + "treportes_clientes.FechaPago, treportes_clientes.Ticket,"
                + "treportes_clientes.Deuda,treportes_clientes.Mensual,treportes_clientes.Cambio,"
                + "treportes_clientes.FechaLimite";
        try {
            reportes = (List<TReportes_clientes>) QR.query(getConn(),
                    "SELECT" + campos + " FROM tclientes Inner Join treportes_clientes ON"
                    + condicion1 + where, new BeanListHandler(TReportes_clientes.class));

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return reportes;
    }

    public List<TUsuarios> Usuarios() {
        List<TUsuarios> usuarios = new ArrayList();
        try {
            usuarios = (List<TUsuarios>) QR.query(getConn(), "SELECT * FROM tusuarios",
                    new BeanListHandler(TUsuarios.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return usuarios;
    }

    public List<TOrdenadores> Ordenadores() {
        List<TOrdenadores> ordenadores = new ArrayList();
        try {
            ordenadores = (List<TOrdenadores>) QR.query(getConn(), "SELECT * FROM tordenadores",
                    new BeanListHandler(TOrdenadores.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return ordenadores;
    }

    public List<TRoles> Roles() {
        List<TRoles> rol = new ArrayList();
        try {
            rol = (List<TRoles>) QR.query(getConn(), "SELECT * FROM troles",
                    new BeanListHandler(TRoles.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return rol;
    }
    
    public List<TPagos_clientes> Pagos_clientes(){
        List<TPagos_clientes> pagos = new ArrayList();
        try {
            pagos = (List<TPagos_clientes>) QR.query(getConn(), "SELECT * FROM tpagos_clientes",
                    new BeanListHandler(TPagos_clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return pagos;
    }
}