/*
 * Autor: Adolfo Michel
 * Clase para administracion de filas.
 */
package ventas;

import java.util.Date;
import java.util.ArrayList;

public class Filas {
    int NoFila;
    int FolioVenta;
    java.util.Date FechaAutorizacion;
    String OrdenCompra;
    String Cotización;
    String FolioServicio;
    String NombreComercial;
    String RazonSocial;
    String ConceptoVenta;
    String FechaOrdenCompraGINSATEC;
    String OrdenCompraGINSATEC;
    Date FechaEntrega;
    Date FechaRecibido;
    String MaterialRecibido;
    String RequiereInstalacion;
    String RequiereCanalizacion;
    String FechaFactura;
    String Factura;
    String Vendedor;
    float Importe;
    float ImportePendiente;
    float ImporteFacturado;
    String TipoMoneda;
    float TasaCambio;
    float GM;
    int DiasCredito;
    Date FechaVencimientoPago;
    String Status;
    String Notas;
    String Capturo;
    
    Filas(){
    
    }
    
    Filas(ArrayList<Object> lista){
        this.FolioVenta = (int) lista.get(0);
        this.FechaAutorizacion = (Date) lista.get(1);
        this.NombreComercial = (String) lista.get(2);
        this.RazonSocial = (String) lista.get(3);
        this.ConceptoVenta = (String) lista.get(4);
        this.FechaEntrega = (Date) lista.get(5);
        this.FechaRecibido = (Date) lista.get(6);
        this.MaterialRecibido = (String) lista.get(7);
        this.RequiereInstalacion = (String) lista.get(8);
        this.RequiereCanalizacion = (String) lista.get(9);
        this.Vendedor = (String) lista.get(10);
        this.Importe = (float) lista.get(11);
        this.ImportePendiente = (float) lista.get(12);
        this.ImporteFacturado = (float) lista.get(13);
        this.TipoMoneda = (String) lista.get(14);
        this.TasaCambio = (float) lista.get(15);
        this.GM = (float) lista.get(16);
        this.DiasCredito = (int) lista.get(17);
        this.FechaVencimientoPago = (Date) lista.get(18);
        this.Status = (String) lista.get(19);
        this.Notas = (String) lista.get(20);
        this.FolioServicio = (String) lista.get(21);
        this.Factura = (String) lista.get(22);
        this.FechaFactura = (String) lista.get(23);
        this.OrdenCompraGINSATEC = (String) lista.get(24);
        this.FechaOrdenCompraGINSATEC = (String) lista.get(25);
        this.Cotización = (String) lista.get(26);
        this.OrdenCompra = (String) lista.get(27);
        this.Capturo = (String) lista.get(28);
    }
    
    public void setIndex(int i){
        NoFila = i;
    }
    
}
