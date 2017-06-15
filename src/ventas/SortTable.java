/*
 * Autor: Adolfo Michel
 * Tabla que hereda AbstactTableModel para ordenarla por columna
 */
package ventas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SortTable extends AbstractTableModel{
    
    private static final int COLUMN_ID      = 0;
    private static final int COLUMN_FECHAAUTORIZACION    = 1;
    private static final int COLUMN_ORDENCOMPRA     = 2;
    private static final int COLUMN_COTIZACION     = 3;
    private static final int COLUMN_FOLIOSERVICIO = 4;
    private static final int COLUMN_CLIENTE = 5;
    private static final int COLUMN_FECHAORDENGINSATEC = 6;
    private static final int COLUMN_ORDENCOMPRAGINSATEC = 7;
    private static final int COLUMN_FECHAENTREGA = 8;
    private static final int COLUMN_FECHARECIBIDO = 9;
    private static final int COLUMN_RECIBIDO = 10;
    private static final int COLUMN_INSTALACION = 11;
    private static final int COLUMN_CANALIZACION = 12;
    private static final int COLUMN_FECHAFACTURA = 13;
    private static final int COLUMN_FACTURA = 14;
    private static final int COLUMN_VENDEDOR = 15;
    private static final int COLUMN_IMPORTE = 16;
    private static final int COLUMN_IMPORTEPENDIENTE = 17;
    private static final int COLUMN_IMPORTEFACTURADO = 18;
    private static final int COLUMN_MONEDA = 19;
    private static final int COLUMN_TASACAMBIO = 20;
    private static final int COLUMN_GM = 21;
    private static final int COLUMN_DIASCREDITO = 22;
    private static final int COLUMN_FECHAVENCIMIENTO = 23;
    private static final int COLUMN_STATUS = 24;
    private static final int COLUMN_NOTAS = 25;
    private static final int COLUMN_CAPTURO = 26;
    private static final int COLUMN_NO = 27;
    
    String[] columnas;
    ArrayList<Filas> datos = null; 
    
    SortTable(String[] c, ArrayList<Filas> d){
        columnas = c;
        datos = d;
    }
    
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
 
    @Override
    public int getRowCount() {
        return datos.size();
    }
     
    @Override
    public String getColumnName(int columnIndex) {
        return columnas[columnIndex];
    }
     
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (datos == null) {
            return Object.class;
        }
        Object n = getValueAt(0, columnIndex);
        if(n == null) return null;
        return getValueAt(0, columnIndex).getClass();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(datos.isEmpty()) return null;
        Filas fila = datos.get(rowIndex);
        Object returnValue = null;
        switch (columnIndex) {
        case COLUMN_ID:
            returnValue = fila.FolioVenta;
            break;
        case COLUMN_FECHAAUTORIZACION:
            returnValue = fila.FechaAutorizacion;
            break;
        case COLUMN_ORDENCOMPRA:
            returnValue = fila.OrdenCompra;
            break;
        case COLUMN_COTIZACION:
            returnValue = fila.Cotización;
            break;
        case COLUMN_FOLIOSERVICIO:
            returnValue = fila.FolioServicio;
            break;
        case COLUMN_CLIENTE:
            returnValue = fila.NombreCliente;
            break;
        case COLUMN_FECHAORDENGINSATEC:
            returnValue = fila.FechaOrdenCompraGINSATEC;
            break;
        case COLUMN_ORDENCOMPRAGINSATEC:
            returnValue = fila.OrdenCompraGINSATEC;
            break;
        case COLUMN_FECHAENTREGA:
            returnValue = fila.FechaEntrega;
            break;
        case COLUMN_FECHARECIBIDO:
            returnValue = fila.FechaRecibido;
            break;
        case COLUMN_RECIBIDO:
            returnValue = fila.MaterialRecibido;
            break;
        case COLUMN_INSTALACION:
            returnValue = fila.RequiereInstalacion;
            break;
        case COLUMN_CANALIZACION:
            returnValue = fila.RequiereCanalizacion;
            break;
        case COLUMN_FECHAFACTURA:
            returnValue = fila.FechaFactura;
            break;
        case COLUMN_FACTURA:
            returnValue = fila.Factura;
            break;
        case COLUMN_VENDEDOR:
            returnValue = fila.Vendedor;
            break;
        case COLUMN_IMPORTE:
            returnValue = fila.Importe;
            break;
        case COLUMN_IMPORTEPENDIENTE:
            returnValue = fila.ImportePendiente;
            break;
        case COLUMN_IMPORTEFACTURADO:
            returnValue = fila.ImporteFacturado;
            break;
        case COLUMN_MONEDA:
            returnValue = fila.TipoMoneda;
            break;
        case COLUMN_TASACAMBIO:
            returnValue = fila.TasaCambio;
            break;
        case COLUMN_GM:
            returnValue = fila.GM;
            break;
        case COLUMN_DIASCREDITO:
            returnValue = fila.DiasCredito;
            break;
        case COLUMN_FECHAVENCIMIENTO:
            returnValue = fila.FechaVencimientoPago;
            break;
        case COLUMN_STATUS:
            returnValue = fila.Status;
            break;
        case COLUMN_NOTAS:
            returnValue = fila.Notas;
            break;
        case COLUMN_CAPTURO:
            returnValue = fila.Capturo;
            break;
        default:
            throw new IllegalArgumentException("Invalid column index");
        }
         
        return returnValue;
    }
    
    public SortTable Filtrar(int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        Date inicio = null;
        Date fin = null;
        try {
            inicio = new SimpleDateFormat( "yyyy-MM-dd" ).parse(Integer.toString(year) + "-01-01" );
            fin = new SimpleDateFormat( "yyyy-MM-dd" ).parse(Integer.toString(year) + "-12-31" );
        } catch (ParseException ex) {
            Logger.getLogger(SortTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).FechaAutorizacion.compareTo(inicio) >= 0 && datos.get(i).FechaAutorizacion.compareTo(fin) <= 0){
                filtro.add(datos.get(i));
            }
        }
        return new SortTable(columnas, filtro);
    }
    
    public SortTable Filtrar(int month, int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        Date inicio = null;
        Date fin = null;
        try {
            inicio = new SimpleDateFormat( "yyyy-MM-dd" ).parse(Integer.toString(year) + "-" + Integer.toString(month) + "-01" );
        } catch (ParseException ex) {
            Logger.getLogger(SortTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(inicio);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        fin = c.getTime();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).FechaAutorizacion.compareTo(inicio) >= 0 && datos.get(i).FechaAutorizacion.compareTo(fin) <= 0){
                filtro.add(datos.get(i));
            }
        }
        return new SortTable(columnas, filtro);
    }
    
    public SortTable Filtrar(int firstMonth, int lastMonth, int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        Date inicio = null;
        Date fin = null;
        try {
            inicio = new SimpleDateFormat( "yyyy-MM-dd" ).parse(Integer.toString(year) + "-" + Integer.toString(firstMonth) + "-01" );
            fin = new SimpleDateFormat( "yyyy-MM-dd" ).parse(Integer.toString(year) + "-" + Integer.toString(lastMonth) + "-01" );
        } catch (ParseException ex) {
            Logger.getLogger(SortTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(fin);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        fin = c.getTime();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).FechaAutorizacion.compareTo(inicio) >= 0 && datos.get(i).FechaAutorizacion.compareTo(fin) <= 0){
                filtro.add(datos.get(i));
            }
        }
        return new SortTable(columnas, filtro);
    }
     
    public SortTable Filtrar(String vendedor){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).Vendedor.equals(vendedor)){
                filtro.add(datos.get(i));
            }
        }
        return new SortTable(columnas, filtro);
    }
    
    public SortTable Filtrar(String vendedor, int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).Vendedor.equals(vendedor)){
                filtro.add(datos.get(i));
            }
        }
        SortTable fechas = new SortTable(columnas, filtro);
        return fechas.Filtrar(year);
    }
    
    public SortTable Filtrar(String vendedor, int month, int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).Vendedor.equals(vendedor)){
                filtro.add(datos.get(i));
            }
        }
        SortTable fechas = new SortTable(columnas, filtro);
        return fechas.Filtrar(month, year);
    }
    
    public SortTable Filtrar(String vendedor, int firstMonth, int lastMonth, int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).Vendedor.equals(vendedor)){
                filtro.add(datos.get(i));
            }
        }
        SortTable fechas = new SortTable(columnas, filtro);
        return fechas.Filtrar(firstMonth, lastMonth, year);
    }
    
    public SortTable FiltrarCliente(String cliente){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).NombreCliente.equals(cliente)){
                filtro.add(datos.get(i));
            }
        }
        return new SortTable(columnas, filtro);
    }
    
    public SortTable FiltrarCliente(String cliente, int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).NombreCliente.equals(cliente)){
                filtro.add(datos.get(i));
            }
        }
        SortTable fechas = new SortTable(columnas, filtro);
        return fechas.Filtrar(year);
    }
    
    public SortTable FiltrarCliente(String cliente, int month, int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).NombreCliente.equals(cliente)){
                filtro.add(datos.get(i));
            }
        }
        SortTable fechas = new SortTable(columnas, filtro);
        return fechas.Filtrar(month, year);
    }
    
    public SortTable FiltrarCliente(String cliente, int firstMonth, int lastMonth, int year){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).NombreCliente.equals(cliente)){
                filtro.add(datos.get(i));
            }
        }
        SortTable fechas = new SortTable(columnas, filtro);
        return fechas.Filtrar(firstMonth, lastMonth, year);
    }
    
    public SortTable FiltrarStatus(String status){
        ArrayList<Filas> filtro = new ArrayList<Filas>();
        for(int i = 0; i < datos.size(); i++){
            if(datos.get(i).Status.equals(status)){
                filtro.add(datos.get(i));
            }
        }
        return new SortTable(columnas, filtro);
    }
    
    public void modificar(int id, Filas f){
        int i = 0;
        for(i = 0; i < datos.size(); i++){
            if(datos.get(i).FolioVenta == id){
                datos.remove(i);
                datos.add(i, f);
                break;
            }
        }   
        if(i == datos.size()){
            datos.add(f);
        }
        fireTableChanged(null);
    }
    
    public void refresh(){
        fireTableChanged(null);
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Filas fila = datos.get(rowIndex);
        if (columnIndex == COLUMN_NO) {
            fila.setIndex((int) value);
        }      
    }
    
}
