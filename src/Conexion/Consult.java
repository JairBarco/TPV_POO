package Conexion;

import Models.*;
import Models.Cajas.*;
import Models.Cliente.*;
import Models.Compras.*;
import Models.Ordenador.TOrdenadores;
import Models.Producto.*;
import Models.Proveedor.*;
import Models.Usuario.*;
import Models.Venta.TVentas_temporal;
import java.sql.SQLException;
import java.util.*;
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

    public List<TPagos_clientes> Pagos_clientes() {
        List<TPagos_clientes> pagos = new ArrayList();
        try {
            pagos = (List<TPagos_clientes>) QR.query(getConn(), "SELECT * FROM tpagos_clientes",
                    new BeanListHandler(TPagos_clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return pagos;
    }

    public List<TPagos_reportes_intereses_clientes> Pagos_reportes_intereses_clientes() {
        List<TPagos_reportes_intereses_clientes> pagos = new ArrayList();
        try {
            pagos = (List<TPagos_reportes_intereses_clientes>) QR.query(getConn(), "SELECT * FROM tpagos_reportes_intereses_cliente",
                    new BeanListHandler(TPagos_reportes_intereses_clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        return pagos;
    }

    public List<TProveedor> proveedores() {
        List<TProveedor> proveedor = new ArrayList();
        try {
            proveedor = (List<TProveedor>) QR.query(getConn(), "SELECT * FROM tproveedor",
                    new BeanListHandler(TProveedor.class));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return proveedor;
    }

    public List<TReportes_proveedor> ReporteProveedor() {
        List<TReportes_proveedor> reporte = new ArrayList();
        try {
            reporte = (List<TReportes_proveedor>) QR.query(getConn(), "SELECT * FROM treportes_proveedor",
                    new BeanListHandler(TReportes_proveedor.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }

        return reporte;
    }

    public List<TReportes_proveedor> Reportes_Proveedores(int idProveedor) {
        String where = " WHERE tproveedor.ID =" + idProveedor;
        List<TReportes_proveedor> reportes = new ArrayList();
        String condicion1 = " tproveedor.ID = treportes_proveedor.IdProveedor ";

        String campos = " *";
        try {
            reportes = (List<TReportes_proveedor>) QR.query(getConn(),
                    "SELECT" + campos + " FROM tproveedor Inner Join treportes_proveedor ON"
                    + condicion1 + where, new BeanListHandler(TReportes_proveedor.class));

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return reportes;
    }

    public List<TPagos_proveedor> Pagos_proveedor() {
        List<TPagos_proveedor> pagos = new ArrayList();
        try {
            pagos = (List<TPagos_proveedor>) QR.query(getConn(), "SELECT * FROM tpagos_proveedor",
                    new BeanListHandler(TPagos_proveedor.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }

        return pagos;
    }

    public List<TCompras_temporal> Compras_temporal() {
        List<TCompras_temporal> productos = new ArrayList();
        try {
            productos = (List<TCompras_temporal>) QR.query(getConn(), "SELECT * FROM tcompras_temporal",
                    new BeanListHandler(TCompras_temporal.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return productos;
    }

    public List<TCompras> Compras() {
        List<TCompras> compras = new ArrayList();
        try {
            compras = (List<TCompras>) QR.query(getConn(), "SELECT * FROM tcompras",
                    new BeanListHandler(TCompras.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return compras;
    }

    public List<ProductosModel> Temporal_Productos(int IdUsuario) {
        var where = " WHERE ttemporal_productos.IdUsuario =" + IdUsuario;
        List<ProductosModel> compras = new ArrayList();
        String condicion1 = " tcompras.IdCompra = ttemporal_productos.IdProducto ";
        var campos = " * ";
        try {
            compras = (List<ProductosModel>) QR.query(getConn(), "SELECT" + campos + " FROM tcompras Inner Join ttemporal_productos ON" + condicion1 + where,
                    new BeanListHandler(ProductosModel.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return compras;
    }

    public List<TProductos> Productos() {
        List<TProductos> producto = new ArrayList();
        try {
            producto = (List<TProductos>) QR.query(getConn(), "SELECT * FROM tproductos",
                    new BeanListHandler(TProductos.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return producto;
    }

    public List<TBodega> Bodega() {
        List<TBodega> bodega = new ArrayList();
        try {
            bodega = (List<TBodega>) QR.query(getConn(), "SELECT * FROM tbodega",
                    new BeanListHandler(TBodega.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return bodega;
    }

    public List<TCajas> Cajas() {
        List<TCajas> cajas = new ArrayList();
        try {
            cajas = (List<TCajas>) QR.query(getConn(), "SELECT * FROM tcajas",
                    new BeanListHandler(TCajas.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return cajas;
    }

    public List<TCajas_ingresos> CajasIngreso() {
        List<TCajas_ingresos> cajas = new ArrayList();
        var condicion1 = " tcajas.IdCaja = tcajas_ingresos.IdCaja ";
        var campos = " * ";
        try {
            cajas = (List<TCajas_ingresos>) QR.query(getConn(), "SELECT" + campos + "FROM tcajas INNER JOIN tcajas_ingresos ON" + condicion1,
                    new BeanListHandler(TCajas_ingresos.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return cajas;
    }

    public List<TCajas_registros> Cajas_registros() {
        List<TCajas_registros> registros = new ArrayList();
        try {
            registros = (List<TCajas_registros>) QR.query(getConn(), "SELECT * FROM tcajas_registros",
                    new BeanListHandler(TCajas_registros.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return registros;
    }

    public List<TCajas_reportes> Cajas_reportes() {
        List<TCajas_reportes> reportes = new ArrayList();
        try {
            reportes = (List<TCajas_reportes>) QR.query(getConn(), "SELECT * FROM tcajas_reportes",
                    new BeanListHandler(TCajas_reportes.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return reportes;
    }

    public List<TProductos> Productos_bodega() {
        List<TProductos> producto = new ArrayList();
        var condicion1 = " tproductos.IdProducto = tbodega.IdProducto ";
        var campos = " * ";
        try {
            producto = (List<TProductos>) QR.query(getConn(), "SELECT" + campos + "FROM tproductos INNER JOIN tbodega ON" + condicion1,
                    new BeanListHandler(TProductos.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return producto;
    }

    public List<TVentas_temporal> Ventas_temporal(int idUsuario, int caja) {
        List<TVentas_temporal> venta = new ArrayList();
        var where = " WHERE IdUsuario =" + idUsuario + "AND Caja =" + caja;
        var campos = " * ";
        try {
            venta = (List<TVentas_temporal>) QR.query(getConn(), "SELECT" + campos + "FROM tventas_temporal" + where,
                    new BeanListHandler(TProductos.class));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return venta;
    }
}
