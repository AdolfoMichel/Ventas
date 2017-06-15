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
    String NombreCliente;
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
        this.OrdenCompra = (String) lista.get(25);
        this.Cotización = (String) lista.get(24);
        this.FolioServicio = (String) lista.get(19);
        this.NombreCliente = (String) lista.get(2);
        this.FechaOrdenCompraGINSATEC = (String) lista.get(23);
        this.OrdenCompraGINSATEC = (String) lista.get(22);
        this.FechaEntrega = (Date) lista.get(3);
        this.FechaRecibido = (Date) lista.get(4);
        this.MaterialRecibido = (String) lista.get(5);
        this.RequiereInstalacion = (String) lista.get(6);
        this.RequiereCanalizacion = (String) lista.get(7);
        this.FechaFactura = (String) lista.get(21);
        this.Factura = (String) lista.get(20);
        this.Vendedor = (String) lista.get(8);
        this.Importe = (float) lista.get(9);
        this.ImportePendiente = (float) lista.get(10);
        this.ImporteFacturado = (float) lista.get(11);
        this.TipoMoneda = (String) lista.get(12);
        this.TasaCambio = (float) lista.get(13);
        this.GM = (float) lista.get(14);
        this.DiasCredito = (int) lista.get(15);
        this.FechaVencimientoPago = (Date) lista.get(16);
        this.Status = (String) lista.get(17);
        this.Notas = (String) lista.get(18);
        this.Capturo = (String) lista.get(26);
    }
    
    public void setIndex(int i){
        NoFila = i;
    }
    
}
