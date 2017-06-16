/*
 * Autor: Adolfo Michel
 * Formulario de captura
 */
package ventas;

import datechooser.beans.DateChooserCombo;
import datechooser.model.multiple.Period;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.*;

public class Capturar extends JFrame implements ActionListener, Runnable{

    Boolean nuevo;
    Ventas ventas;
    int folio;
    Filas f = new Filas();
    
    static final String noHayRuta = "No hay archivo seleccionado";

    Hashtable<String,String> hstArchivoCotizacion = new Hashtable<>();
    Hashtable<String,String> hstArchivoOrdenCompra = new Hashtable<>();
    Hashtable<String,String> hstFechaOrdenGINSATEC = new Hashtable<>();
    Hashtable<String,String> hstFechaFactura = new Hashtable<>();
    
    JPanel pnlPrimero = new JPanel();
    JLabel lblFechaAutorizacion = new JLabel("Fecha Autorizacion:");
    DateChooserCombo dccFechaAutorizacion = new DateChooserCombo();
    
    JLabel lblOrdenCompra = new JLabel("Orden de Compra:");
    JTextField txtOrdenCompra = new JTextField();
    JTextField txtArchivoOrdenCompra = new JTextField();
    JButton btnArchivoOrdenCompra = new JButton("Agregar Archivo");
    JButton btnAgregarOrdenCompra = new JButton("Agregar");
    JButton btnEliminarOrdenCompra = new JButton("Eliminar");
    DefaultListModel dlmOrdenCompra = new DefaultListModel();
    JList lstOrdenCompra = new JList(dlmOrdenCompra);
    JScrollPane pnlListaOrdenCompra = new JScrollPane(lstOrdenCompra);
    
    JLabel lblCotizacion = new JLabel("Cotización:");
    JTextField txtCotizacion = new JTextField();
    JTextField txtArchivoCotizacion = new JTextField();
    JButton btnArchivoCotizacion = new JButton("Agregar Archivo");
    JButton btnAgregarCotizacion = new JButton("Agregar");
    JButton btnEliminarCotizacion = new JButton("Eliminar");
    DefaultListModel dlmCotizacion = new DefaultListModel();
    JList lstCotizacion = new JList(dlmCotizacion);
    JScrollPane pnlListaCotizacion = new JScrollPane(lstCotizacion);
    
    JLabel lblFolio = new JLabel("Folio de servicio");
    JTextField txtFolio = new JTextField();
    JButton btnAgregarFolio = new JButton("Agregar");
    JButton btnEliminarFolio = new JButton("Eliminar");
    DefaultListModel dlmFolio = new DefaultListModel();
    JList lstFolio = new JList(dlmFolio);
    JScrollPane pnlListaFolio = new JScrollPane(lstFolio);
    
    JLabel lblFactura = new JLabel("Factura:");
    JTextField txtFactura = new JTextField();
    JLabel lblFechaFactura = new JLabel("Fecha Factura:");
    DateChooserCombo dccFechaFactura = new DateChooserCombo();
    JButton btnAgregarFactura = new JButton("Agregar");
    JButton btnEliminarFactura = new JButton("Eliminar");
    DefaultListModel dlmFactura = new DefaultListModel();
    JList lstFactura = new JList(dlmFactura);
    JScrollPane pnlListaFactura = new JScrollPane(lstFactura);
    
    JLabel lblOrdenGINSATEC = new JLabel("Orden de Compra GINSATEC:");
    JTextField txtOrdenGINSATEC = new JTextField();
    JLabel lblFechaOrdenGINSATEC = new JLabel("Fecha Orden GINSATEC:");
    DateChooserCombo dccFechaOrdenGINSATEC = new DateChooserCombo();
    JButton btnAgregarOrdenGINSATEC = new JButton("Agregar");
    JButton btnEliminarOrdenGINSATEC = new JButton("Eliminar");
    DefaultListModel dlmOrdenGINSATEC = new DefaultListModel();
    JList lstOrdenGINSATEC = new JList(dlmOrdenGINSATEC);
    JScrollPane pnlListaOrdenGINSATEC = new JScrollPane(lstOrdenGINSATEC);
    
    Vector<String> opcionesCliente;
    JLabel lblCliente = new JLabel("Cliente:");
    DefaultComboBoxModel model;
    JComboBox cmbCliente;
    JButton btnNuevoCliente = new JButton("Agregar Nuevo");
    JButton btnModificarCliente = new JButton("Modificar");
    
    JLabel lblRecibido = new JLabel("Material Recibido");
    JCheckBox chkRecibido = new JCheckBox();
    
    JLabel lblInstalacion = new JLabel("Requiere Instalación");
    JCheckBox chkInstalacion = new JCheckBox();
    
    JLabel lblCanalizacion = new JLabel("Requiere Canalización");
    JCheckBox chkCanalizacion = new JCheckBox();
    
    JLabel lblGM = new JLabel("GM:");
    JTextField txtGM = new JTextField();
    JLabel lblGMporciento = new JLabel("%");
    
    JLabel lblDiasCredito = new JLabel("Días de Credito:");
    JTextField txtDiasCredito = new JTextField();
    
    JLabel lblFechaEntrega = new JLabel("Fecha Entrega:");
    DateChooserCombo dccFechaEntrega = new DateChooserCombo();
    
    JLabel lblFechaRecibido = new JLabel("Fecha Recibido:");
    DateChooserCombo dccFechaRecibido = new DateChooserCombo();
    
    Vector<String> opcionesVendedor; // = {"Mario Gonzalez", "Marco Padilla", "Daniel Martinez", "Rosario Arellano", "Alberto Lomeli", "Mariano Ruiz"};
    JLabel lblVendedor = new JLabel("Vendedor:");
    JComboBox cmbVendedor;
    
    String[] opcionesMoneda = {"Dólares", "Pesos"};
    JLabel lblImporte = new JLabel("Importe:");
    JTextField txtImporte = new JTextField();
    JLabel lblImportePendiente = new JLabel("Importe Pendiente:");
    JTextField txtImportePendiente = new JTextField();
    JLabel lblImporteFacturado = new JLabel("Importe Facturado:");
    JTextField txtImporteFacturado = new JTextField();
    JComboBox cmbMoneda = new JComboBox(opcionesMoneda);
    
    JLabel lblTasaCambio = new JLabel("Tasa de cambio:");
    JTextField txtTasaCambio = new JTextField();
    
    JLabel lblFechaVencimiento = new JLabel("Fecha Vencimiento Pago:");
    DateChooserCombo dccFechaVencimiento = new DateChooserCombo();
    
    String[] opcionesStatus = {"Pendiente", "Activa", "Pagada", "No pagada", "Cancelada"};
    JLabel lblStatus = new JLabel("Status:");
    JComboBox cmbStatus = new JComboBox(opcionesStatus);
    
    JLabel lblNotas = new JLabel("Notas:");
    JTextArea txtaNotas = new JTextArea();
    
    JButton btnGuardar = new JButton("Guardar");
    JButton btnCancelar = new JButton("Cancelar");
    
    Capturar(Ventas ref){
        super("Registro de Ventas - Nuevo Registro");
        inicializarVentana();
        nuevo = true;
        txtArchivoOrdenCompra.setText(noHayRuta);
        txtArchivoCotizacion.setText(noHayRuta);
        ventas = ref;
    }
    
    Capturar(Ventas ref, int folioVenta){
        super("Registro de Ventas - Modificar Registro");
        inicializarVentana();
        CargarDatos(folioVenta);
        nuevo = false;
        txtArchivoOrdenCompra.setText(noHayRuta);
        txtArchivoCotizacion.setText(noHayRuta);
        ventas = ref;
        folio = folioVenta;
    }
    
    public void inicializarVentana(){
    	this.setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        
        opcionesVendedor = cargarVendedores();
        cmbVendedor = new JComboBox(opcionesVendedor);
        opcionesCliente = cargarClientes();
        model = new DefaultComboBoxModel(opcionesCliente);
        cmbCliente = new JComboBox(model);
        
        pnlPrimero.add(lblFechaAutorizacion);
        pnlPrimero.add(dccFechaAutorizacion);
        pnlPrimero.add(lblCliente);
        pnlPrimero.add(cmbCliente);
        pnlPrimero.add(btnNuevoCliente);
        pnlPrimero.add(btnModificarCliente);
        cmbCliente.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, 30));
        
        JPanel pnlOrdenes = new JPanel(new GridLayout(3,1));
        JPanel pnlComplementarioOrdenes = new JPanel(new GridLayout(1,2));
        pnlComplementarioOrdenes.add(lblOrdenCompra);
        pnlComplementarioOrdenes.add(txtOrdenCompra);
        JPanel pnlArchivoOrdenes = new JPanel(new GridLayout(2,1));
        pnlArchivoOrdenes.add(txtArchivoOrdenCompra);
        txtArchivoOrdenCompra.setEditable(false);
        pnlArchivoOrdenes.add(btnArchivoOrdenCompra);
        JPanel pnlBotonesOrdenes = new JPanel(new GridLayout(1,2));
        pnlBotonesOrdenes.add(btnAgregarOrdenCompra);
        pnlBotonesOrdenes.add(btnEliminarOrdenCompra);
        pnlOrdenes.add(pnlComplementarioOrdenes);
        pnlOrdenes.add(pnlArchivoOrdenes);
        pnlOrdenes.add(pnlBotonesOrdenes);
        JPanel pnlListaOrdenes = new JPanel(new GridLayout(1,2));
        pnlListaOrdenes.add(pnlOrdenes);
        pnlListaOrdenes.add(pnlListaOrdenCompra);
        
        JPanel pnlCotizaciones = new JPanel(new GridLayout(3,1));
        JPanel pnlComplementarioCotizaciones = new JPanel(new GridLayout(1,2));
        pnlComplementarioCotizaciones.add(lblCotizacion);
        pnlComplementarioCotizaciones.add(txtCotizacion);
        JPanel pnlArchivoCotizaciones = new JPanel(new GridLayout(2,1));
        pnlArchivoCotizaciones.add(txtArchivoCotizacion);
        txtArchivoCotizacion.setEditable(false);
        pnlArchivoCotizaciones.add(btnArchivoCotizacion);
        JPanel pnlBotonesCotizaciones = new JPanel(new GridLayout(1,2));
        pnlBotonesCotizaciones.add(btnAgregarCotizacion);
        pnlBotonesCotizaciones.add(btnEliminarCotizacion);
        pnlCotizaciones.add(pnlComplementarioCotizaciones);
        pnlCotizaciones.add(pnlArchivoCotizaciones);
        pnlCotizaciones.add(pnlBotonesCotizaciones);
        JPanel pnlListaCotizaciones = new JPanel(new GridLayout(1,2));
        pnlListaCotizaciones.add(pnlCotizaciones);
        pnlListaCotizaciones.add(pnlListaCotizacion);
        
        JPanel pnlFolios = new JPanel(new GridLayout(2,2,10,10));
        pnlFolios.add(lblFolio);
        pnlFolios.add(txtFolio);
        pnlFolios.add(btnAgregarFolio);
        pnlFolios.add(btnEliminarFolio);
        JPanel pnlListaFolios = new JPanel(new GridLayout(1,2,10,10));
        pnlListaFolios.add(pnlFolios);
        pnlListaFolios.add(pnlListaFolio);
        
        JPanel pnlFacturas = new JPanel(new GridLayout(3,2,10,10));
        pnlFacturas.add(lblFactura);
        pnlFacturas.add(txtFactura);
        pnlFacturas.add(lblFechaFactura);
        pnlFacturas.add(dccFechaFactura);
        pnlFacturas.add(btnAgregarFactura);
        pnlFacturas.add(btnEliminarFactura);
        JPanel pnlListaFacturas = new JPanel(new GridLayout(1,2,10,10));
        pnlListaFacturas.add(pnlFacturas);
        pnlListaFacturas.add(pnlListaFactura);
        
        JPanel pnlOrdenesGINSATEC = new JPanel(new GridLayout(3,2,10,10));
        pnlOrdenesGINSATEC.add(lblOrdenGINSATEC);
        pnlOrdenesGINSATEC.add(txtOrdenGINSATEC);
        pnlOrdenesGINSATEC.add(lblFechaOrdenGINSATEC);
        pnlOrdenesGINSATEC.add(dccFechaOrdenGINSATEC);
        pnlOrdenesGINSATEC.add(btnAgregarOrdenGINSATEC);
        pnlOrdenesGINSATEC.add(btnEliminarOrdenGINSATEC);
        JPanel pnlListaOrdenesGINSATEC = new JPanel(new GridLayout(1,2,10,10));
        pnlListaOrdenesGINSATEC.add(pnlOrdenesGINSATEC);
        pnlListaOrdenesGINSATEC.add(pnlListaOrdenGINSATEC);
        
        JPanel pnlFinanciero = new JPanel(new GridLayout(6,2));
        pnlFinanciero.add(lblVendedor);
        pnlFinanciero.add(cmbVendedor);
        pnlFinanciero.add(lblImporte);
        JPanel pnlImporte = new JPanel(new GridLayout(1,2));
        pnlImporte.add(txtImporte);
        pnlImporte.add(cmbMoneda);
        pnlFinanciero.add(pnlImporte);
        pnlFinanciero.add(lblImportePendiente);
        pnlFinanciero.add(txtImportePendiente);
        pnlFinanciero.add(lblImporteFacturado);
        pnlFinanciero.add(txtImporteFacturado);
        pnlFinanciero.add(lblTasaCambio);
        pnlFinanciero.add(txtTasaCambio);
        pnlFinanciero.add(lblGM);
        JPanel pnlGM = new JPanel(new GridLayout(1,2));
        pnlGM.add(txtGM);
        pnlGM.add(lblGMporciento);
        pnlFinanciero.add(pnlGM);
        
        
        JPanel pnlCheckBox = new JPanel(new GridLayout(3,2));
        pnlCheckBox.add(lblRecibido);
        pnlCheckBox.add(chkRecibido);
        pnlCheckBox.add(lblInstalacion);
        pnlCheckBox.add(chkInstalacion);
        pnlCheckBox.add(lblCanalizacion);
        pnlCheckBox.add(chkCanalizacion);
        JPanel pnlDivision = new JPanel(new GridLayout(1,2));
        JPanel pnlDiasCredito = new JPanel(new GridLayout(2,2));
        pnlDiasCredito.add(lblDiasCredito);
        pnlDiasCredito.add(txtDiasCredito);
        pnlDiasCredito.add(lblStatus);
        pnlDiasCredito.add(cmbStatus);
        pnlDivision.add(pnlCheckBox);
        pnlDivision.add(pnlDiasCredito);
        
        JPanel pnlFechas = new JPanel(new GridLayout(3,2));
        pnlFechas.add(lblFechaEntrega);
        pnlFechas.add(dccFechaEntrega);
        pnlFechas.add(lblFechaRecibido);
        pnlFechas.add(dccFechaRecibido);
        pnlFechas.add(lblFechaVencimiento);
        pnlFechas.add(dccFechaVencimiento);
        
        JPanel pnlFormulariosListas = new JPanel(new GridLayout(4,2,10,10));
        pnlFormulariosListas.add(pnlListaOrdenes);
        pnlFormulariosListas.add(pnlListaFacturas);
        pnlFormulariosListas.add(pnlListaCotizaciones);
        pnlFormulariosListas.add(pnlListaOrdenesGINSATEC);
        pnlFormulariosListas.add(pnlListaFolios);
        pnlFormulariosListas.add(pnlFinanciero);
        pnlFormulariosListas.add(pnlDivision);
        pnlFormulariosListas.add(pnlFechas);
        
        JPanel pnlPrincipal = new JPanel(new BorderLayout());
        pnlPrincipal.add(pnlFormulariosListas, BorderLayout.CENTER);
        
        JPanel pnlNotas = new JPanel(new BorderLayout());
        txtaNotas.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 60));
        JScrollPane scrollNotas = new JScrollPane(txtaNotas);
        pnlNotas.add(lblNotas, BorderLayout.PAGE_START);
        pnlNotas.add(scrollNotas, BorderLayout.CENTER);
        pnlPrincipal.add(pnlNotas, BorderLayout.SOUTH);
        
        JPanel pnlFormulario = new JPanel();
        pnlFormulario.add(btnGuardar);
        pnlFormulario.add(btnCancelar);
        
        setLayout(new BorderLayout());
        add(pnlPrimero, BorderLayout.NORTH);
        add(pnlPrincipal, BorderLayout.CENTER);
        add(pnlFormulario, BorderLayout.SOUTH);
        
        btnGuardar.addActionListener(this);
        btnCancelar.addActionListener(this);
        btnNuevoCliente.addActionListener(this);
        btnArchivoOrdenCompra.addActionListener(this);
        btnAgregarOrdenCompra.addActionListener(this);
        btnEliminarOrdenCompra.addActionListener(this);
        btnArchivoCotizacion.addActionListener(this);
        btnAgregarCotizacion.addActionListener(this);
        btnEliminarCotizacion.addActionListener(this);
        btnAgregarFactura.addActionListener(this);
        btnEliminarFactura.addActionListener(this);
        btnAgregarOrdenGINSATEC.addActionListener(this);
        btnEliminarOrdenGINSATEC.addActionListener(this);
        btnAgregarFolio.addActionListener(this);
        btnEliminarFolio.addActionListener(this);
        btnModificarCliente.addActionListener(this);
        
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                ventas.actualizarFiltro();
            }
        });
        
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public Connection ConectarDB(){
        new Servidor().arrancarServidor();
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver"); //jdbc:postgresql://localhost:5432/sistemabasedatos
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5344/SistemaBaseDatos", "Sersitec-Laboratorio", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return c;
    }
    
    public void CargarDatos(int folioVenta){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                int x;
                comando = c.createStatement();
                String sql = "select * from ventas where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                if(consulta.next()){
                    //java.sql.Date FechaAutorizacion = consulta.getDate("fechaautorizacion");
                    Calendar calendario = Calendar.getInstance();
                    calendario.setTime(consulta.getDate("fechaautorizacion"));
                    dccFechaAutorizacion.setSelectedDate(calendario);
                    //String OrdenCompra = cargarOrdenesCompra(folioVenta);
                    cargarOrdenesCompra(folioVenta);
                    //String Cotización = cargarCotizaciones(folioVenta);
                    cargarCotizaciones(folioVenta);
                    //String FolioServicio = cargarFoliosServicio(folioVenta);
                    cargarFoliosServicio(folioVenta);
                    //String NombreCliente = consulta.getString("nombrecliente");
                    cmbCliente.setSelectedItem(consulta.getString("nombrecliente"));
                    //String FechaOrdenCompraGINSATEC = cargarFechasOrdenesCompraGINSATEC(folioVenta);
                    //String OrdenCompraGINSATEC = cargarOrdenesCompraGINSATEC(folioVenta);
                    cargarOrdenesCompraGINSATEC(folioVenta);
                    //java.sql.Date FechaEntrega = consulta.getDate("fechaentrega");
                    calendario.setTime(consulta.getDate("fechaentrega"));
                    dccFechaEntrega.setSelectedDate(calendario);
                    //java.sql.Date FechaRecibido = consulta.getDate("fecharecibido");
                    calendario.setTime(consulta.getDate("fecharecibido"));
                    dccFechaRecibido.setSelectedDate(calendario);
                    if(consulta.getBoolean("materialrecibido")){
                        chkRecibido.setSelected(true);
                    }
                    if(consulta.getBoolean("requiereinstalacion")){
                        chkInstalacion.setSelected(true);
                    }
                    if(consulta.getBoolean("requierecanalizacion")){
                        chkCanalizacion.setSelected(true);
                    }
                    //String FechaFactura = cargarFechasFacturas(folioVenta);
                    //String Factura = cargarFacturas(folioVenta);
                    cargarFacturas(folioVenta);
                    //String Vendedor = consulta.getString("vendedor");
                    cmbVendedor.setSelectedItem(consulta.getString("vendedor"));
                    //float Importe = consulta.getFloat("importe");
                    txtImporte.setText(Float.toString(consulta.getFloat("importe")));
                    //float ImportePendiente = consulta.getFloat("importePendiente");
                    txtImportePendiente.setText(Float.toString(consulta.getFloat("importependiente")));
                    //float ImporteFacturado = consulta.getFloat("importeFacturado");
                    txtImporteFacturado.setText(Float.toString(consulta.getFloat("importefacturado")));
                    //String TipoMoneda = consulta.getString("tipomoneda");
                    cmbMoneda.setSelectedItem(consulta.getString("tipomoneda"));
                    //float TasaCambio = consulta.getFloat("tasacambio");
                    txtTasaCambio.setText(Float.toString(consulta.getFloat("tasacambio")));
                    //float GM = consulta.getFloat("gm");
                    txtGM.setText(Float.toString(consulta.getFloat("gm")));
                    //int DiasCredito = consulta.getInt("diascredito");
                    txtDiasCredito.setText(Integer.toString(consulta.getInt("diascredito")));
                    //java.sql.Date FechaVencimientoPago = consulta.getDate("fechavencimientopago");
                    calendario.setTime(consulta.getDate("fechavencimientopago"));
                    dccFechaVencimiento.setSelectedDate(calendario);
                    //String Status = consulta.getString("status");
                    cmbStatus.setSelectedItem(consulta.getString("status"));
                    //String Notas = consulta.getString("notas");
                    txtaNotas.setText(consulta.getString("notas"));
                    comando.close();
                    c.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Vector<String> cargarVendedores(){
        Vector<String> vendedores = new Vector<String>();
        Connection c = ConectarDB();
        Statement comando = null;

        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try{
                comando = c.createStatement();
                String sql = "select vendedor from vendedores";
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    vendedores.add(consulta.getString("vendedor"));
                }
                comando.close();
                c.close();
            }
            catch(Exception ex){
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return vendedores;
    }
    
    public Vector<String> cargarClientes(){
        Vector<String> clientes = new Vector<String>();
        clientes.add("");
        Connection c = ConectarDB();
        Statement comando = null;

        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try{
                comando = c.createStatement();
                String sql = "select cliente from clientes";
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    clientes.add(consulta.getString("cliente"));
                }
                comando.close();
                c.close();
            }
            catch(Exception ex){
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return clientes;
    }

    public void cargarFoliosServicio(int folioVenta){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select folioservicio from foliosservicio where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    dlmFolio.addElement(consulta.getString("folioservicio"));
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarFacturas(int folioVenta){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select factura, fecha from facturas where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String factura = "", fecha = "";
                while(consulta.next()){
                    factura = consulta.getString("factura");
                    fecha = String.valueOf(consulta.getDate("fecha"));
                    System.out.println("Factura " + factura + " | " + fecha);
                    hstFechaFactura.put(factura, fecha);
                    dlmFactura.addElement(factura);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarOrdenesCompra(int folioVenta){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select ordencompra, ruta from ordenescompra where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String orden = "", ruta = "";
                while(consulta.next()){
                    orden = consulta.getString("ordencompra");
                    ruta = consulta.getString("ruta");
                    if(ruta == null){
                        ruta = noHayRuta;
                    }
                    System.out.println("Orden Compra: " + orden + " | " + ruta);
                    hstArchivoOrdenCompra.put(orden, ruta);
                    dlmOrdenCompra.addElement(orden);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarOrdenesCompraGINSATEC(int folioVenta){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select ordencompra, fecha from ordenescompraginsatec where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String orden = "", fecha = "";
                while(consulta.next()){
                    orden = consulta.getString("ordencompra");
                    fecha = String.valueOf(consulta.getDate("fecha"));
                    System.out.println("Orden GINSATEC: " + orden + " | " + fecha);
                    hstFechaOrdenGINSATEC.put(orden, fecha);
                    dlmOrdenGINSATEC.addElement(orden);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarCotizaciones(int folioVenta){
        Connection c = ConectarDB();
        Statement comando = null;

        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select cotizacion, ruta from cotizaciones where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String cotizacion = "", ruta = "";
                while(consulta.next()){
                    cotizacion = consulta.getString("cotizacion");
                    ruta = consulta.getString("ruta");
                    if(ruta == null){
                        ruta = noHayRuta;
                    }
                    hstArchivoCotizacion.put(cotizacion, ruta);
                    System.out.println("Cotizacion: " + cotizacion + " | " + ruta);
                    dlmCotizacion.addElement(cotizacion);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void nuevoCliente(String cliente){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try{
                comando = c.createStatement();
                String sql = "select max(idcliente) from clientes";
                ResultSet consulta = comando.executeQuery(sql);
                int id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                sql = "insert into clientes values(" + id + ",'" + cliente + "')";
                comando.executeUpdate(sql);
                comando.close();
                c.close();
            }
            catch(Exception ex){
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarCliente(String clienteViejo, String clienteNuevo){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try{
                comando = c.createStatement();
                String sql = "update clientes set cliente='" + clienteNuevo + "' where cliente='" + clienteViejo + "'";
                comando.executeUpdate(sql);
                comando.close();
                c.close();
            }
            catch(Exception ex){
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void nuevoRegistro(){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                int id;
                int folioVenta;
                comando = c.createStatement();
                String sql = "select max(folioventa) from ventas";
                ResultSet consulta = comando.executeQuery(sql);
                folioVenta = 0;
                if(consulta.next()){
                    folioVenta = consulta.getInt("max") + 1;
                }
                folio = folioVenta;
                
                sql = "insert into ventas values(" + folioVenta + ",'" + dccFechaAutorizacion.getText() + "','" 
                        + cmbCliente.getSelectedItem() + "','" + dccFechaEntrega.getText() + "','" + dccFechaRecibido.getText() 
                        + "','" + chkRecibido.isSelected() + "','" + chkInstalacion.isSelected() + "','" + chkCanalizacion.isSelected() 
                        + "','" + cmbVendedor.getSelectedItem() + "'," + txtImporte.getText() + "," + txtImportePendiente.getText() + ","
                        + txtImporteFacturado.getText() + ",'" + cmbMoneda.getSelectedItem() + "'," + txtTasaCambio.getText() + "," 
                        + txtGM.getText() + "," + txtDiasCredito.getText() + ",'" + dccFechaVencimiento.getText() + "','" 
                        + cmbStatus.getSelectedItem() + "','" + txtaNotas.getText() + "'," + ventas.idUsuario + ")";
                comando.executeUpdate(sql);
                
                sql = "select max(idfolio) from foliosservicio";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                for(int i = 0; i < dlmFolio.getSize(); i++, id++){
                    sql = "insert into foliosservicio values(" + id + ",'" + dlmFolio.get(i) + "'," + folioVenta + ")";
                    comando.executeUpdate(sql);
                }
                
                sql = "select max(idcotizacion) from cotizaciones";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                String ruta = "";
                for(int i = 0; i < dlmCotizacion.getSize(); i++, id++){
                    ruta = hstArchivoCotizacion.get(dlmCotizacion.get(i));
                    if(ruta.equals(noHayRuta)){
                        ruta = "null";
                    }
                    else{
                        ruta = "'" + ruta + "'";
                    }
                    sql = "insert into cotizaciones values(" + id + ",'" + dlmCotizacion.get(i) + "'," + folioVenta + "," + ruta + ")";
                    comando.executeUpdate(sql);
                }
                
                sql = "select max(idorden) from ordenescompra";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                for(int i = 0; i < dlmOrdenCompra.getSize(); i++, id++){
                    ruta = hstArchivoOrdenCompra.get(dlmOrdenCompra.get(i));
                    if(ruta.equals(noHayRuta)){
                        ruta = "null";
                    }
                    else{
                        ruta = "'" + ruta + "'";
                    }
                    sql = "insert into ordenescompra values(" + id + ",'" + dlmOrdenCompra.get(i) + "'," + folioVenta + "," + ruta + ")";
                    comando.executeUpdate(sql);
                }
                
                sql = "select max(idorden) from ordenescompraginsatec";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                String fecha = "";
                for(int i = 0; i < dlmOrdenGINSATEC.getSize(); i++, id++){
                    fecha = hstFechaOrdenGINSATEC.get(dlmOrdenGINSATEC.get(i));
                    sql = "insert into ordenescompraginsatec values(" + id + ",'" + dlmOrdenGINSATEC.get(i) + "'," + folioVenta + ",'" + fecha + "')";
                    comando.executeUpdate(sql);
                }
                
                sql = "select max(idfactura) from facturas";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                for(int i = 0; i < dlmFactura.getSize(); i++, id++){
                    fecha = hstFechaFactura.get(dlmFactura.get(i));
                    sql = "insert into facturas values(" + id + ",'" + dlmFactura.get(i) + "'," + folioVenta + ",'" + fecha + "')";
                    comando.executeUpdate(sql);
                }
                JOptionPane.showMessageDialog(null, "Registro Exitoso");
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarRegistro(){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                int id;
                comando = c.createStatement();
                String sql = "update ventas set fechaautorizacion='" + dccFechaAutorizacion.getText() + "',nombrecliente='" 
                        + cmbCliente.getSelectedItem() + "',fechaentrega='" + dccFechaEntrega.getText() + "',fecharecibido='" + dccFechaRecibido.getText() 
                        + "',materialrecibido='" + chkRecibido.isSelected() + "',requiereinstalacion='" + chkInstalacion.isSelected() 
                        + "',requierecanalizacion='" + chkCanalizacion.isSelected() + "',vendedor='" + cmbVendedor.getSelectedItem() + "',importe=" 
                        + txtImporte.getText() + ",importependiente=" + txtImportePendiente.getText() + ",importefacturado=" + txtImporteFacturado.getText()
                        + ",tipomoneda='" + cmbMoneda.getSelectedItem() + "',tasacambio=" + txtTasaCambio.getText() + ",gm=" 
                        + txtGM.getText() + ",diascredito=" + txtDiasCredito.getText() + ",fechavencimientopago='" + dccFechaVencimiento.getText() 
                        + "',status='" + cmbStatus.getSelectedItem() + "',notas='" + txtaNotas.getText() + "' where folioventa=" + folio;
                comando.executeUpdate(sql);
                modificarFolios();
                modificarCotizaciones();
                modificarOrdenes();
                modificarOrdenesGINSATEC();
                modificarFacturas();
                JOptionPane.showMessageDialog(null, "Se ha modificado con exito");
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante la modificacion");
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarFolios(){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                Vector<Integer> id = new Vector<Integer>();
                String sql = "select idfolio from foliosservicio where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idfolio"));
                }
                int i;
                if(id.size() >= dlmFolio.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from foliosservicio where idfolio=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < dlmFolio.size(); i++){
                        sql = "insert into foliosservicio values(" + id.elementAt(i) + ",'" + dlmFolio.elementAt(i) + "'," + folio + ")";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from foliosservicio where idfolio=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < id.size(); i++){
                        sql = "insert into foliosservicio values(" + id.elementAt(i) + ",'" + dlmFolio.elementAt(i) + "'," + folio + ")";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idfolio) from foliosservicio";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmFolio.size(); i++, newId++){
                        sql = "insert into foliosservicio values(" + newId + ",'" + dlmFolio.elementAt(i) + "'," + folio + ")";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante la modificación");
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarCotizaciones(){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                int i = 0;
                String ruta = "";
                Vector<Integer> id = new Vector<Integer>();
                String sql = "select idcotizacion from cotizaciones where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idcotizacion"));
                }
                if(id.size() >= dlmCotizacion.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from cotizaciones where idcotizacion=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < dlmCotizacion.size(); i++){
                        ruta = hstArchivoCotizacion.get(dlmCotizacion.elementAt(i));
                        if(ruta.equals(noHayRuta)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into cotizaciones values(" + id.elementAt(i) + ",'" + dlmCotizacion.elementAt(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from cotizaciones where idcotizacion=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < id.size(); i++){
                        ruta = hstArchivoCotizacion.get(dlmCotizacion.elementAt(i));
                        if(ruta.equals(noHayRuta)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into cotizaciones values(" + id.elementAt(i) + ",'" + dlmCotizacion.elementAt(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idcotizacion) from cotizaciones";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmCotizacion.size(); i++, newId++){
                        ruta = hstArchivoCotizacion.get(dlmCotizacion.elementAt(i));
                        if(ruta.equals(noHayRuta)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into cotizaciones values(" + newId + ",'" + dlmCotizacion.elementAt(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarOrdenes(){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                int i = 0;
                Vector<Integer> id = new Vector<Integer>();
                String ruta = "";
                String sql = "select idorden from ordenescompra where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idorden"));
                }
                if(id.size() >= dlmOrdenCompra.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from ordenescompra where idorden=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    ruta = "";
                    for(i = 0; i < dlmOrdenCompra.size(); i++){
                        ruta = hstArchivoOrdenCompra.get(dlmOrdenCompra.elementAt(i));
                        if(ruta.equals(noHayRuta)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into ordenescompra values(" + id.elementAt(i) + ",'" + dlmOrdenCompra.elementAt(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from ordenescompra where idorden=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    ruta = "";
                    for(i = 0; i < id.size(); i++){
                        ruta = hstArchivoOrdenCompra.get(dlmOrdenCompra.elementAt(i));
                        if(ruta.equals(noHayRuta)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into ordenescompra values(" + id.elementAt(i) + ",'" + dlmOrdenCompra.elementAt(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idorden) from ordenescompra";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmOrdenCompra.size(); i++, newId++){
                        ruta = hstArchivoOrdenCompra.get(dlmOrdenCompra.elementAt(i));
                        if(ruta.equals(noHayRuta)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into ordenescompra values(" + newId + ",'" + dlmOrdenCompra.elementAt(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarOrdenesGINSATEC(){
        Connection c = ConectarDB();
        Statement comando = null, update = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                int i = 0;
                Vector<Integer> id = new Vector<Integer>();
                String fecha = "";
                String sql = "select idorden from ordenescompraginsatec where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idorden"));
                }
                if(id.size() >= dlmOrdenGINSATEC.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from ordenescompraginsatec where idorden=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    fecha = "";
                    for(i = 0; i < dlmOrdenGINSATEC.size(); i++){
                        fecha = hstFechaOrdenGINSATEC.get(dlmOrdenGINSATEC.elementAt(i));
                        sql = "insert into ordenescompraginsatec values(" + id.elementAt(i) + ",'" + dlmOrdenGINSATEC.elementAt(i) + "'," + folio + ",'" + fecha + "')";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from ordenescompraginsatec where idorden=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    fecha = "";
                    for(i = 0; i < id.size(); i++){
                        fecha = hstFechaOrdenGINSATEC.get(dlmOrdenGINSATEC.elementAt(i));
                        sql = "insert into ordenescompraginsatec values(" + id.elementAt(i) + ",'" + dlmOrdenGINSATEC.elementAt(i) + "'," + folio + ",'" + fecha + "')";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idorden) from ordenescompraginsatec";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmOrdenGINSATEC.size(); i++, newId++){
                        fecha = hstFechaOrdenGINSATEC.get(dlmOrdenGINSATEC.elementAt(i));
                        sql = "insert into ordenescompraginsatec values(" + newId + ",'" + dlmOrdenGINSATEC.elementAt(i) + "'," + folio + ",'" + fecha + "')";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarFacturas(){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                int i = 0;
                Vector<Integer> id = new Vector<Integer>();
                String fecha = "";
                String sql = "select idfactura from facturas where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idfactura"));
                }
                if(id.size() >= dlmFactura.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from facturas where idfactura=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < dlmFactura.size(); i++){
                        fecha = hstFechaFactura.get(dlmFactura.elementAt(i));
                        sql = "insert into facturas values(" + id.elementAt(i) + ",'" + dlmFactura.elementAt(i) + "'," + folio + ",'" + fecha + "')";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from facturas where idfactura=" + id.elementAt(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < id.size(); i++){
                        fecha = hstFechaFactura.get(dlmFactura.elementAt(i));
                        sql = "insert into facturas values(" + id.elementAt(i) + ",'" + dlmFactura.elementAt(i) + "'," + folio + ",'" + fecha + "')";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idfactura) from facturas";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmFactura.size(); i++, newId++){
                        fecha = hstFechaFactura.get(dlmFactura.elementAt(i));
                        sql = "insert into facturas values(" + newId + ",'" + dlmFactura.elementAt(i) + "'," + folio + ",'" + fecha + "')";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean validarCampo(JTextField f, String tipo){
        if(!f.getText().isEmpty()){
            if(tipo.equals("float")){
                try{
                    Float.parseFloat(f.getText());
                    return true;
                }
                catch(Exception ex){
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public boolean validarNumero(JTextField f){
        if(f.getText().isEmpty()){
            f.setText("0");
            return true;
        }
        else{
            try{
                Float.parseFloat(f.getText());
                return true;
            }
            catch(Exception ex){
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
    
    public void inicializarFila(){
        try{
            f.FolioVenta = folio;
            f.FechaAutorizacion = dccFechaAutorizacion.getSelectedDate().getTime();
            f.NombreCliente = String.valueOf(cmbCliente.getSelectedItem());
            f.FechaEntrega = dccFechaEntrega.getSelectedDate().getTime();
            f.FechaRecibido = dccFechaRecibido.getSelectedDate().getTime();
            if(chkRecibido.isSelected()){
                f.MaterialRecibido = "Si";
            }
            else{
                f.MaterialRecibido = "No";
            }
            if(chkInstalacion.isSelected()){
                f.RequiereInstalacion = "Si";
            }
            else{
                f.RequiereInstalacion = "No";
            }
            if(chkCanalizacion.isSelected()){
                f.RequiereCanalizacion = "Si";
            }
            else{
                f.RequiereCanalizacion = "No";
            }
            f.Vendedor = String.valueOf(cmbVendedor.getSelectedItem());
            f.Importe = Float.parseFloat(txtImporte.getText());
            f.ImportePendiente = Float.parseFloat(txtImportePendiente.getText());
            f.ImporteFacturado = Float.parseFloat(txtImporteFacturado.getText());
            f.TipoMoneda = String.valueOf(cmbMoneda.getSelectedItem());
            f.TasaCambio = Float.parseFloat(txtTasaCambio.getText());
            f.GM = Float.parseFloat(txtGM.getText());
            f.DiasCredito = Integer.parseInt(txtDiasCredito.getText());
            f.FechaVencimientoPago = dccFechaVencimiento.getSelectedDate().getTime();
            f.Status = String.valueOf(cmbStatus.getSelectedItem());
            f.Notas = txtaNotas.getText();
            f.Capturo = Integer.toString(ventas.idUsuario);
            f.FechaFactura = "";
            f.Factura = "";
            for(int i = 0; i < dlmFactura.size(); i++){
                f.FechaFactura += hstFechaFactura.get(dlmFactura.get(i)) + "-";
                f.Factura += dlmFactura.get(i) + "-";
            }
            f.OrdenCompraGINSATEC = "";
            f.FechaOrdenCompraGINSATEC = "";
            for(int i = 0; i < dlmOrdenGINSATEC.size(); i++){
                f.OrdenCompraGINSATEC += hstFechaOrdenGINSATEC.get(dlmOrdenGINSATEC.get(i)) + "-";
                f.OrdenCompraGINSATEC += dlmOrdenGINSATEC.get(i) + "-";
            }
            f.FolioServicio = "";
            for(int i = 0; i < dlmFolio.size(); i++){
                f.FolioServicio += dlmFolio.get(i) + "-";
            }
            f.Cotización = "";
            for(int i = 0; i < dlmCotizacion.size(); i++){
                f.Cotización += dlmCotizacion.get(i) + "-";
            }
            f.OrdenCompra = "";
            for(int i = 0; i < dlmOrdenCompra.size(); i++){
                f.OrdenCompra += dlmOrdenCompra.get(i) + "-";
            }
        }
        catch(Exception ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object accion = ae.getSource();
        if(accion == btnGuardar){
            if(cmbCliente.getSelectedItem().equals("")){
                JOptionPane.showMessageDialog(null, "El campo CLIENTE no debe estar vacio");
            }
            else if(!validarCampo(txtImporte, "float")){
                JOptionPane.showMessageDialog(null, "Asegurate de llenar adecuadamente el campo IMPORTE");
            }
            else if(!validarCampo(txtTasaCambio, "float")){
                JOptionPane.showMessageDialog(null, "Asegurate de llenar adecuadamente el campo TASA DE CAMBIO");
            }
            else if(!validarCampo(txtGM, "float")){
                JOptionPane.showMessageDialog(null, "Asegurate de llenar adecuadamente el campo GM");
            }
            else{
                if(!validarNumero(txtImportePendiente)){
                    JOptionPane.showMessageDialog(null, "Asegurate de llenar adecuadamente los campos númericos");
                }
                else if(!validarNumero(txtImporteFacturado)){
                    JOptionPane.showMessageDialog(null, "Asegurate de llenar adecuadamente los campos númericos");
                }
                else if(!validarNumero(txtDiasCredito)){
                    JOptionPane.showMessageDialog(null, "Asegurate de llenar adecuadamente los campos númericos");
                }
                else{
                    if(nuevo){
                        nuevoRegistro();
                    }
                    else{
                        modificarRegistro();    
                    }
                    ventas.actualizarFiltro();
                    inicializarFila();
                    if(ventas.modeloRegistrar == null){
                        ventas.modeloRegistrar = new SortTable(ventas.columnasRegistrar, new ArrayList<Filas>());
                        ventas.tblTablaRegistrar = new JTable(ventas.modeloRegistrar);
                        ventas.modeloRegistrar.modificar(folio, f);
                        
                        ventas.pnlRegistrar.remove(ventas.pnlTablaRegistrar);
                        ventas.pnlTablaRegistrar = new JScrollPane(ventas.tblTablaRegistrar);
                        ventas.tblTablaRegistrar.setFillsViewportHeight(true);
                        ventas.tblTablaRegistrar.setDefaultEditor(Object.class, null);
                        ventas.tblTablaRegistrar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        ventas.tcaRegistrar = new TableColumnAdjuster(ventas.tblTablaRegistrar);
                        ventas.tcaRegistrar.adjustColumns();
                        ventas.tblTablaRegistrar.setAutoCreateRowSorter(true);
                        ventas.tblTablaRegistrar.getTableHeader().setReorderingAllowed(false);
                        ventas.tblTablaRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                ventas.abrirArchivo(ventas.tblTablaRegistrar);
                            }
                        });
                        ventas.pnlRegistrar.add(ventas.pnlTablaRegistrar, BorderLayout.CENTER);
                        ventas.pnlRegistrar.validate();
                        ventas.pnlRegistrar.repaint();
                    }
                    else{
                        ventas.modeloRegistrar.modificar(folio, f);
                        ventas.tcaRegistrar.adjustColumns();
                    }
                    if(ventas.modeloMostrar == null){
                        ventas.modeloMostrar = new SortTable(ventas.columnasMostrar, new ArrayList<Filas>());
                        ventas.tblTablaMostrar = new JTable(ventas.modeloMostrar);
                        ventas.modeloMostrar.modificar(folio, f);
                        
                        ventas.pnlMostrar.remove(ventas.pnlTablaMostrar);
                        ventas.crearTablas(ventas.tblTablaMostrar);
                        ventas.pnlMostrar.add(ventas.pnlTablaMostrar, BorderLayout.CENTER);
                        ventas.pnlMostrar.validate();
                        ventas.pnlMostrar.repaint();
                        ventas.cmbFiltros.setEnabled(true);
                    }
                    else{
                        ventas.modeloMostrar.modificar(folio, f);
                        ventas.tcaMostrar.adjustColumns();
                    }
                    this.dispose();
                }
            }
        }
        else if(accion == btnCancelar){
            ventas.actualizarFiltro();
            this.dispose();
        }
        else if(accion == btnNuevoCliente){
            String cliente = JOptionPane.showInputDialog(
                    this, 
                    "Nombre del nuevo cliente:", 
                    "Agregar Nuevo Cliente", 
                    JOptionPane.INFORMATION_MESSAGE);
            if(cliente != null){
                cliente = cliente.toUpperCase();
                boolean iguales = false;
                for(int i = 0; i < opcionesCliente.size(); i++){
                    if(opcionesCliente.get(i).equals(cliente)){
                        iguales = true;
                    }
                }
                
                if(cliente.equals("")){
                    JOptionPane.showMessageDialog(null, "No es un nombre de cliente valido");
                }
                else if(iguales){
                    JOptionPane.showMessageDialog(null, "El cliente ya existe");
                }
                else{
                    nuevoCliente(cliente);
                    opcionesCliente.add(cliente);
                    model = new DefaultComboBoxModel(opcionesCliente);
                    cmbCliente.setModel(model);
                    ventas.filtrosCliente.add(cliente);
                    ventas.actualizarFiltro();
                }
            }
        }
        else if(accion == btnModificarCliente){
            if(cmbCliente.getSelectedItem().equals("")){
                JOptionPane.showMessageDialog(null, "No has seleccionado un cliente");
            }
            else{
                String cliente = JOptionPane.showInputDialog(
                    this, 
                    "Nuevo nombre del nuevo cliente:", 
                    "Modificar Cliente", 
                    JOptionPane.INFORMATION_MESSAGE);
                if(cliente != null){
                    cliente = cliente.toUpperCase();
                    boolean iguales = false;
                    for(int i = 0; i < opcionesCliente.size(); i++){
                        if(opcionesCliente.get(i).equals(cliente)){
                            iguales = true;
                        }
                    }

                    if(cliente.equals("")){
                        JOptionPane.showMessageDialog(null, "No es un nombre de cliente valido");
                    }
                    else if(iguales){
                        JOptionPane.showMessageDialog(null, "El cliente ya existe");
                    }
                    else{
                        modificarCliente(String.valueOf(cmbCliente.getSelectedItem()), cliente);
                        ventas.filtrosCliente.remove(cmbCliente.getSelectedItem());
                        ventas.filtrosCliente.add(cliente);
                        ventas.actualizarFiltro();
                        opcionesCliente.remove(cmbCliente.getSelectedItem());
                        opcionesCliente.add(cliente);
                        model = new DefaultComboBoxModel(opcionesCliente);
                        cmbCliente.setModel(model);
                    }
                }
            }
        }
        else if(accion == btnArchivoOrdenCompra){
            JFileChooser dialogoArchivo = new JFileChooser();
            dialogoArchivo.setVisible(true);
            if(dialogoArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                txtArchivoOrdenCompra.setText(dialogoArchivo.getSelectedFile().getAbsolutePath());
            }
            else{
                txtArchivoOrdenCompra.setText(noHayRuta);
            }
        }
        else if(accion == btnAgregarOrdenCompra){
            if(!txtOrdenCompra.getText().isEmpty()){
                hstArchivoOrdenCompra.put(txtOrdenCompra.getText(), txtArchivoOrdenCompra.getText());
                dlmOrdenCompra.addElement(txtOrdenCompra.getText());
                txtOrdenCompra.setText("");
                txtArchivoOrdenCompra.setText(noHayRuta);
            }
        }
        else if(accion == btnEliminarOrdenCompra){
            if(!lstOrdenCompra.isSelectionEmpty()){
                hstArchivoOrdenCompra.remove(dlmOrdenCompra.get(lstOrdenCompra.getSelectedIndex()));
                dlmOrdenCompra.remove(lstOrdenCompra.getSelectedIndex());
            }
        }
        else if(accion == btnAgregarOrdenGINSATEC){
            if(!txtOrdenGINSATEC.getText().isEmpty()){
                hstFechaOrdenGINSATEC.put(txtOrdenGINSATEC.getText(), dccFechaOrdenGINSATEC.getText());
                dlmOrdenGINSATEC.addElement(txtOrdenGINSATEC.getText());
                txtOrdenGINSATEC.setText("");
            }
        }
        else if(accion == btnEliminarOrdenGINSATEC){
            if(!lstOrdenGINSATEC.isSelectionEmpty()){
                hstFechaOrdenGINSATEC.remove(dlmOrdenGINSATEC.get(lstOrdenGINSATEC.getSelectedIndex()));
                dlmOrdenGINSATEC.remove(lstOrdenGINSATEC.getSelectedIndex());
            }
        }
        else if(accion == btnAgregarFactura){
            if(!txtFactura.getText().isEmpty()){
                hstFechaFactura.put(txtFactura.getText(), dccFechaFactura.getText());
                dlmFactura.addElement(txtFactura.getText());
                txtFactura.setText("");
            }
        }
        else if(accion == btnEliminarFactura){
            if(!lstFactura.isSelectionEmpty()){
                hstFechaFactura.remove(dlmFactura.get(lstFactura.getSelectedIndex()));
                dlmFactura.remove(lstFactura.getSelectedIndex());
            }
        }
        else if(accion == btnAgregarFolio){
            if(!txtFolio.getText().isEmpty()){
                dlmFolio.addElement(txtFolio.getText());
                txtFolio.setText("");
            }
        }
        else if(accion == btnEliminarFolio){
            if(!lstFolio.isSelectionEmpty()){
                dlmFolio.remove(lstFolio.getSelectedIndex());
            }
        }
        else if(accion == btnArchivoCotizacion){
            JFileChooser dialogoArchivo = new JFileChooser();
            dialogoArchivo.setVisible(true);
            if(dialogoArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                txtArchivoCotizacion.setText(dialogoArchivo.getSelectedFile().getAbsolutePath());
            }
            else{
                txtArchivoCotizacion.setText(noHayRuta);
            }
        }
        else if(accion == btnAgregarCotizacion){
            if(!txtCotizacion.getText().isEmpty()){
                hstArchivoCotizacion.put(txtCotizacion.getText(), txtArchivoCotizacion.getText());
                dlmCotizacion.addElement(txtCotizacion.getText());
                txtCotizacion.setText("");
                txtArchivoCotizacion.setText(noHayRuta);
            }
        }
        else if(accion == btnEliminarCotizacion){
            if(!lstCotizacion.isSelectionEmpty()){
                hstArchivoCotizacion.remove(dlmCotizacion.get(lstCotizacion.getSelectedIndex()));
                dlmCotizacion.remove(lstCotizacion.getSelectedIndex());
            }
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
