/*
 * Autor: Adolfo Michel
 * Tabla que hereda AbstactTableModel para ordenarla por columna
 */
package ventas;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;

public class SortTable extends AbstractTableModel{
    
    private static final int COLUMN_ID      = 0;
    private static final int COLUMN_FECHAAUTORIZACION    = 1;
    private static final int COLUMN_ORDENCOMPRA     = 2;
    private static final int COLUMN_COTIZACION     = 3;
    private static final int COLUMN_FOLIOSERVICIO = 4;
    private static final int COLUMN_NOMBRECOMERCIAL = 5;
    private static final int COLUMN_RAZONSOCIAL = 6;
    private static final int COLUMN_CONCEPTOVENTA = 7;
    private static final int COLUMN_FECHAORDENGINSATEC = 8;
    private static final int COLUMN_ORDENCOMPRAGINSATEC = 9;
    private static final int COLUMN_FECHAENTREGA = 10;
    private static final int COLUMN_FECHARECIBIDO = 11;
    private static final int COLUMN_RECIBIDO = 12;
    private static final int COLUMN_INSTALACION = 13;
    private static final int COLUMN_CANALIZACION = 14;
    private static final int COLUMN_FECHAFACTURA = 15;
    private static final int COLUMN_FACTURA = 16;
    private static final int COLUMN_VENDEDOR = 17;
    private static final int COLUMN_IMPORTE = 18;
    private static final int COLUMN_IMPORTEPENDIENTE = 19;
    private static final int COLUMN_IMPORTEFACTURADO = 20;
    private static final int COLUMN_MONEDA = 21;
    private static final int COLUMN_TASACAMBIO = 22;
    private static final int COLUMN_GM = 23;
    private static final int COLUMN_DIASCREDITO = 24;
    private static final int COLUMN_FECHAVENCIMIENTO = 25;
    private static final int COLUMN_STATUS = 26;
    private static final int COLUMN_NOTAS = 27;
    private static final int COLUMN_CAPTURO = 28;
    private static final int COLUMN_NO = 29;
    
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
        DecimalFormat df = new DecimalFormat("####.0");
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        
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
        case COLUMN_NOMBRECOMERCIAL:
            returnValue = fila.NombreComercial;
            break;
        case COLUMN_RAZONSOCIAL:
            returnValue = fila.RazonSocial;
            break;
        case COLUMN_CONCEPTOVENTA:
            returnValue = fila.ConceptoVenta;
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
            returnValue = Float.valueOf(df.format(fila.Importe));
            //returnValue = fila.Importe;
            break;
        case COLUMN_IMPORTEPENDIENTE:
            returnValue = Float.valueOf(df.format(fila.ImportePendiente));
            //returnValue = fila.ImportePendiente;
            break;
        case COLUMN_IMPORTEFACTURADO:
            returnValue = Float.valueOf(df.format(fila.ImporteFacturado));
            //returnValue = fila.ImporteFacturado;
            break;
        case COLUMN_MONEDA:
            returnValue = fila.TipoMoneda;
            break;
        case COLUMN_TASACAMBIO:
            returnValue = Float.valueOf(df.format(fila.TasaCambio));
            //returnValue = fila.TasaCambio;
            break;
        case COLUMN_GM:
            returnValue = Float.valueOf(df.format(fila.GM));
            //returnValue = fila.GM;
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
    
    public ArrayList<Filas> Filtrar(int month, int year, ArrayList<Filas> filtro){
        ArrayList<Filas> fechas = new ArrayList<>();
        Date inicio = null;
        Date fin;
        try {
            inicio = new SimpleDateFormat( "yyyy-MM-dd" ).parse(Integer.toString(year) + "-" + Integer.toString(month) + "-01" );
        } catch (ParseException ex) {
            Logger.getLogger(SortTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(inicio);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        fin = c.getTime();
        for(int i = 0; i < filtro.size(); i++){
            if(filtro.get(i).FechaAutorizacion.compareTo(inicio) >= 0 && filtro.get(i).FechaAutorizacion.compareTo(fin) <= 0){
                fechas.add(filtro.get(i));
            }
        }
        return fechas;
    }
    
    public ArrayList<Filas> Filtrar(int month, ArrayList<Filas> filtro){
        ArrayList<Filas> fechas = new ArrayList<>();
        Date inicio = null;
        Date fin;
        for(int i = 0; i < filtro.size(); i++){
            try {
                inicio = new SimpleDateFormat( "yyyy-MM-dd" ).parse(Integer.toString(getYear(filtro.get(i).FechaAutorizacion)) + "-" + Integer.toString(month) + "-01" );
            } catch (ParseException ex) {
                Logger.getLogger(SortTable.class.getName()).log(Level.SEVERE, null, ex);
            }
            Calendar c = Calendar.getInstance();
            c.setTime(inicio);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            fin = c.getTime();
            if(filtro.get(i).FechaAutorizacion.compareTo(inicio) >= 0 && filtro.get(i).FechaAutorizacion.compareTo(fin) <= 0){
                fechas.add(filtro.get(i));
            }
        }
        return fechas;
    }
    
    public int getYear(Date fecha){
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        return c.get(Calendar.YEAR);
    }
    
    TableModel Filtrar(ArrayList<String> mes, ArrayList<Integer> concepto, 
            Hashtable<Integer, ArrayList> detalle, String anio, String status, 
            String cliente, ArrayList<String> vendedor) {
        
        ArrayList<Filas> filtro = new ArrayList<>();
        ArrayList<Filas> fechas = new ArrayList<>();
        boolean existeConcepto;
        if(mes.size() == 12 && concepto.size() == 15 && vendedor.size() == 6){
            return new SortTable(columnas, datos);
        }
        if(mes.isEmpty() || concepto.isEmpty() || vendedor.isEmpty()){
            return new SortTable(columnas, filtro);
        }
        
        for(int i = 0; i < datos.size(); i++){
            existeConcepto = false;
            for(int indice : concepto){
                if(Float.parseFloat(String.valueOf(detalle.get(datos.get(i).FolioVenta).get(indice + 1))) > 0){
                    existeConcepto = true;
                    break;
                }
            }
            if(     (status.equals(datos.get(i).Status) || status.equals("Todos")) &&
                    (cliente.equals(datos.get(i).NombreComercial) || cliente.equals("Todos")) &&
                    vendedor.contains(datos.get(i).Vendedor) &&
                    existeConcepto){
                filtro.add(datos.get(i));
            }
        }
        
        if(filtro.isEmpty()){
            return new SortTable(columnas, filtro);
        }
        
        for(String m : mes){
            int numero;
            switch(m){
                case "Enero":
                    numero = 1;
                    break;
                case "Febrero":
                    numero = 2;
                    break;
                case "Marzo":
                    numero = 3;
                    break;
                case "Abril":
                    numero = 4;
                    break;
                case "Mayo":
                    numero = 5;
                    break;
                case "Junio":
                    numero = 6;
                    break;
                case "Julio":
                    numero = 7;
                    break;
                case "Agosto":
                    numero = 8;
                    break;
                case "Septiembre":
                    numero = 9;
                    break;
                case "Octubre":
                    numero = 10;
                    break;
                case "Noviembre":
                    numero = 11;
                    break;
                case "Diciembre":
                    numero = 12;
                    break;
                default:
                    numero = 0;
                    break;
            }
            if(anio.equals("Todos")){
                fechas.addAll(Filtrar(numero, filtro));
            }
            else{
                fechas.addAll(Filtrar(numero, Integer.parseInt(anio),filtro));
            }
        }
        return new SortTable(columnas, fechas);
    }
    
    public void modificar(int id, Filas f){
        int i;
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
        if(columnIndex == COLUMN_NO){
            fila.setIndex((int) value);
        }      
    }
}
