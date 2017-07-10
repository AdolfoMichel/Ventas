/*
 * Autor: Adolfo Michel
 * Formulario de captura
 */
package ventas;

import ventas.Utilerias.TableColumnAdjuster;
import datechooser.beans.DateChooserCombo;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import ventas.Utilerias.SpinnerEditor;

public class Capturar extends JFrame implements ActionListener, Runnable{

    Boolean nuevo;
    Ventas ventas;
    int folio;
    Filas f = new Filas();
    
    private static final String NOHAYRUTA = "No hay archivo seleccionado";

    Hashtable<Integer,Integer> hstCliente = new Hashtable<>();
    Hashtable<String,String> hstCotizacion = new Hashtable<>();
    Hashtable<String,String> hstOrdenCompra = new Hashtable<>();
    Hashtable<String,String> hstFolio = new Hashtable<>();
    Hashtable<String,String[]> hstOrdenGINSATEC = new Hashtable<>();
    Hashtable<String,String[]> hstFactura = new Hashtable<>();
    
    JPanel pnlPrimero = new JPanel();
    JLabel lblFechaAutorizacion = new JLabel("Fecha Autorizacion:");
    DateChooserCombo dccFechaAutorizacion = new DateChooserCombo();
    
    JLabel lblOrdenCompra = new JLabel("<html><p align=\"right\">Orden de<br>Compra:</p></html>", SwingConstants.RIGHT);
    JTextField txtOrdenCompra = new JTextField();
    JTextField txtArchivoOrdenCompra = new JTextField(NOHAYRUTA);
    JButton btnArchivoOrdenCompra = new JButton("Agregar Archivo");
    JButton btnAgregarOrdenCompra = new JButton("Agregar");
    JButton btnEliminarOrdenCompra = new JButton("Eliminar");
    DefaultListModel dlmOrdenCompra = new DefaultListModel();
    JList lstOrdenCompra = new JList(dlmOrdenCompra);
    JScrollPane pnlListaOrdenCompra = new JScrollPane(lstOrdenCompra);
    
    JLabel lblCotizacion = new JLabel("Cotización:", SwingConstants.RIGHT);
    JTextField txtCotizacion = new JTextField();
    JTextField txtArchivoCotizacion = new JTextField(NOHAYRUTA);
    JButton btnArchivoCotizacion = new JButton("Agregar Archivo");
    JButton btnAgregarCotizacion = new JButton("Agregar");
    JButton btnEliminarCotizacion = new JButton("Eliminar");
    DefaultListModel dlmCotizacion = new DefaultListModel();
    JList lstCotizacion = new JList(dlmCotizacion);
    JScrollPane pnlListaCotizacion = new JScrollPane(lstCotizacion);
    
    JLabel lblFolio = new JLabel("<html><p align=\"right\">Folio de<br>servicio:</p></html>", SwingConstants.RIGHT);
    JTextField txtFolio = new JTextField();
    JTextField txtArchivoFolio = new JTextField(NOHAYRUTA);
    JButton btnArchivoFolio = new JButton("Agregar Archivo");
    JButton btnAgregarFolio = new JButton("Agregar");
    JButton btnEliminarFolio = new JButton("Eliminar");
    DefaultListModel dlmFolio = new DefaultListModel();
    JList lstFolio = new JList(dlmFolio);
    JScrollPane pnlListaFolio = new JScrollPane(lstFolio);
    
    JLabel lblFactura = new JLabel("Factura:", SwingConstants.RIGHT);
    JTextField txtFactura = new JTextField();
    JLabel lblFechaFactura = new JLabel("<html><p align=\"right\">Fecha<br>Factura:</p></html>", SwingConstants.RIGHT);
    DateChooserCombo dccFechaFactura = new DateChooserCombo();
    JTextField txtArchivoFactura = new JTextField(NOHAYRUTA);
    JButton btnArchivoFactura = new JButton("Agregar Archivo");
    JButton btnAgregarFactura = new JButton("Agregar");
    JButton btnEliminarFactura = new JButton("Eliminar");
    DefaultListModel dlmFactura = new DefaultListModel();
    JList lstFactura = new JList(dlmFactura);
    JScrollPane pnlListaFactura = new JScrollPane(lstFactura);
    
    JLabel lblOrdenGINSATEC = new JLabel("<html><p align=\"right\">Orden<br>GINSATEC:</p></html>", SwingConstants.RIGHT);
    JTextField txtOrdenGINSATEC = new JTextField();
    JLabel lblFechaOrdenGINSATEC = new JLabel("<html><p align=\"right\">Fecha Orden<br>GINSATEC:</p></html>", SwingConstants.RIGHT);
    DateChooserCombo dccFechaOrdenGINSATEC = new DateChooserCombo();
    JTextField txtArchivoOrdenGINSATEC = new JTextField(NOHAYRUTA);
    JButton btnArchivoOrdenGINSATEC = new JButton("Agregar Archivo");
    JButton btnAgregarOrdenGINSATEC = new JButton("Agregar");
    JButton btnEliminarOrdenGINSATEC = new JButton("Eliminar");
    DefaultListModel dlmOrdenGINSATEC = new DefaultListModel();
    JList lstOrdenGINSATEC = new JList(dlmOrdenGINSATEC);
    JScrollPane pnlListaOrdenGINSATEC = new JScrollPane(lstOrdenGINSATEC);
    
    Vector<String> opcionesCliente;
    Vector<String> opcionesNombreComercial;
    Vector<String> opcionesRazonSocial;
    JLabel lblCliente = new JLabel("Nombre Comercial:");
    DefaultComboBoxModel modelCliente;
    JComboBox cmbCliente;
    JButton btnBuscarCliente = new JButton("Buscar");
    JButton btnNuevoCliente = new JButton("Agregar Nuevo");
    JButton btnModificarCliente = new JButton("Modificar");
    
    JLabel lblRecibido = new JLabel("Material Recibido:", SwingConstants.RIGHT);
    JCheckBox chkRecibido = new JCheckBox();
    
    JLabel lblInstalacion = new JLabel("Requiere Instalación:", SwingConstants.RIGHT);
    JCheckBox chkInstalacion = new JCheckBox();
    
    JLabel lblCanalizacion = new JLabel("Requiere Canalización:", SwingConstants.RIGHT);
    JCheckBox chkCanalizacion = new JCheckBox();
    
    JLabel lblGM = new JLabel("GM:", SwingConstants.RIGHT);
    JTextField txtGM = new JTextField();
    JLabel lblGMporciento = new JLabel("%");
    
    JLabel lblDiasCredito = new JLabel("Días de Credito:", SwingConstants.RIGHT);
    JTextField txtDiasCredito = new JTextField();
    
    JLabel lblFechaEntrega = new JLabel("Fecha Entrega:", SwingConstants.RIGHT);
    DateChooserCombo dccFechaEntrega = new DateChooserCombo();
    
    JLabel lblFechaRecibido = new JLabel("Fecha Recibido:", SwingConstants.RIGHT);
    DateChooserCombo dccFechaRecibido = new DateChooserCombo();
    
    Vector<String> opcionesVendedor;
    JLabel lblVendedor = new JLabel("Vendedor:", SwingConstants.RIGHT);
    JComboBox cmbVendedor;
    
    String[] opcionesMoneda = {"Dólares", "Pesos"};
    JLabel lblImporte = new JLabel("Importe:", SwingConstants.RIGHT);
    JTextField txtImporte = new JTextField();
    JLabel lblImportePendiente = new JLabel("Importe Pendiente:", SwingConstants.RIGHT);
    JTextField txtImportePendiente = new JTextField();
    JLabel lblImporteFacturado = new JLabel("Importe Facturado:", SwingConstants.RIGHT);
    JTextField txtImporteFacturado = new JTextField();
    JComboBox cmbMoneda = new JComboBox(opcionesMoneda);
    
    JLabel lblTasaCambio = new JLabel("Tasa de cambio:", SwingConstants.RIGHT);
    JTextField txtTasaCambio = new JTextField();
    
    JLabel lblFechaVencimiento = new JLabel("Fecha Vencimiento Pago:", SwingConstants.RIGHT);
    DateChooserCombo dccFechaVencimiento = new DateChooserCombo();
    
    String[] opcionesConceptoVenta = {  "Automatización", "Control de Accesos", "CCTV", "Detección de Incendios", "Sistemas de Seguridad", 
                                        "Paciente-Enfermera", "Supresión de Incendios", "Viaticos", "Canalizaciones y cableados", 
                                        "Programación y puesta en marcha", "Mano de Obra", "Poliza", "Refacciones", "Reparaciones", "Varios"};
    JLabel lblConceptoVenta = new JLabel("Concepto de Venta:");
    JTable tblConceptoVenta;
    
    String[] opcionesStatus = {"Pendiente", "Activa", "Pagada", "No pagada", "Cancelada"};
    JLabel lblStatus = new JLabel("Status:", SwingConstants.RIGHT);
    JComboBox cmbStatus = new JComboBox(opcionesStatus);
    
    JLabel lblNotas = new JLabel("Notas:");
    JTextArea txtaNotas = new JTextArea();
    
    JButton btnGuardar = new JButton("Guardar");
    JButton btnCancelar = new JButton("Cancelar");
    
    Capturar(Ventas ref){
        super("Registro de Ventas - Nuevo Registro");
        inicializarVentana();
        nuevo = true;
        //txtArchivoOrdenCompra.setText(NOHAYRUTA);
        //txtArchivoCotizacion.setText(NOHAYRUTA);
        txtTasaCambio.setText("20.00");
        ventas = ref;
    }
    
    Capturar(Ventas ref, int folioVenta){
        super("Registro de Ventas - Modificar Registro");
        inicializarVentana();
        CargarDatos(folioVenta);
        nuevo = false;
        //txtArchivoOrdenCompra.setText(NOHAYRUTA);
        //txtArchivoCotizacion.setText(NOHAYRUTA);
        ventas = ref;
        folio = folioVenta;
    }
    
    public final void inicializarVentana(){
    	this.setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        
        opcionesVendedor = cargarVendedores();
        cmbVendedor = new JComboBox( opcionesVendedor);
        cargarClientes();
        modelCliente = new DefaultComboBoxModel(opcionesCliente);
        cmbCliente = new JComboBox(modelCliente);
        
        pnlPrimero.add(lblFechaAutorizacion);
        pnlPrimero.add(dccFechaAutorizacion);
        pnlPrimero.add(lblCliente);
        pnlPrimero.add(cmbCliente);
        pnlPrimero.add(btnBuscarCliente);
        pnlPrimero.add(btnNuevoCliente);
        pnlPrimero.add(btnModificarCliente);
        cmbCliente.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3, 30));
        cmbCliente.setMaximumRowCount(20);
        
        JPanel pnlOrdenes = new JPanel(new BorderLayout());
        JPanel pnlComplementarioOrdenes = new JPanel(new GridLayout(1,2,30,10));
        pnlComplementarioOrdenes.setBorder(new EmptyBorder(30,0,30,0));
        pnlComplementarioOrdenes.add(lblOrdenCompra);
        pnlComplementarioOrdenes.add(txtOrdenCompra);
        JPanel pnlArchivoOrdenes = new JPanel(new BorderLayout());
        pnlArchivoOrdenes.add(txtArchivoOrdenCompra, BorderLayout.CENTER);
        txtArchivoOrdenCompra.setEditable(false);
        pnlArchivoOrdenes.add(btnArchivoOrdenCompra, BorderLayout.EAST);
        JPanel pnlBotonesOrdenes = new JPanel(new GridLayout(2,1,0,10));
        pnlBotonesOrdenes.add(btnAgregarOrdenCompra);
        pnlBotonesOrdenes.add(btnEliminarOrdenCompra);
        pnlOrdenes.add(pnlComplementarioOrdenes, BorderLayout.CENTER);
        pnlOrdenes.add(pnlArchivoOrdenes, BorderLayout.SOUTH);
        JPanel pnlListaOrdenes = new JPanel(new GridLayout(1,3,10,10));
        pnlListaOrdenes.add(pnlListaOrdenCompra);
        pnlListaOrdenes.add(pnlBotonesOrdenes);
        JPanel pnlCentralOrdenes = new JPanel(new GridLayout(1,2,10,10));
        pnlCentralOrdenes.add(pnlOrdenes);
        pnlCentralOrdenes.add(pnlListaOrdenes);
        
        JPanel pnlCotizaciones = new JPanel(new BorderLayout());
        JPanel pnlComplementarioCotizaciones = new JPanel(new GridLayout(1,2,30,10));
        pnlComplementarioCotizaciones.setBorder(new EmptyBorder(30,0,30,0));
        pnlComplementarioCotizaciones.add(lblCotizacion);
        pnlComplementarioCotizaciones.add(txtCotizacion);
        JPanel pnlArchivoCotizaciones = new JPanel(new BorderLayout());
        pnlArchivoCotizaciones.add(txtArchivoCotizacion, BorderLayout.CENTER);
        txtArchivoCotizacion.setEditable(false);
        pnlArchivoCotizaciones.add(btnArchivoCotizacion, BorderLayout.EAST);
        JPanel pnlBotonesCotizaciones = new JPanel(new GridLayout(2,1,0,10));
        pnlBotonesCotizaciones.add(btnAgregarCotizacion);
        pnlBotonesCotizaciones.add(btnEliminarCotizacion);
        pnlCotizaciones.add(pnlComplementarioCotizaciones, BorderLayout.CENTER);
        pnlCotizaciones.add(pnlArchivoCotizaciones, BorderLayout.SOUTH);
        JPanel pnlListaCotizaciones = new JPanel(new GridLayout(1,2,10,10));
        pnlListaCotizaciones.add(pnlListaCotizacion);
        pnlListaCotizaciones.add(pnlBotonesCotizaciones);
        JPanel pnlCentralCotizaciones = new JPanel(new GridLayout(1,2,10,10));
        pnlCentralCotizaciones.add(pnlCotizaciones);
        pnlCentralCotizaciones.add(pnlListaCotizaciones);
        
        JPanel pnlFolios = new JPanel(new BorderLayout());
        JPanel pnlComplementarioFolios = new JPanel(new GridLayout(1,2,30,10));
        pnlComplementarioFolios.setBorder(new EmptyBorder(30,0,30,0));
        pnlComplementarioFolios.add(lblFolio);
        pnlComplementarioFolios.add(txtFolio);
        JPanel pnlArchivoFolios = new JPanel(new BorderLayout());
        pnlArchivoFolios.add(txtArchivoFolio, BorderLayout.CENTER);
        txtArchivoFolio.setEditable(false);
        pnlArchivoFolios.add(btnArchivoFolio, BorderLayout.EAST);
        JPanel pnlBotonesFolios = new JPanel(new GridLayout(2,1,10,10));
        pnlBotonesFolios.add(btnAgregarFolio);
        pnlBotonesFolios.add(btnEliminarFolio);
        pnlFolios.add(pnlComplementarioFolios, BorderLayout.CENTER);
        pnlFolios.add(pnlArchivoFolios, BorderLayout.SOUTH);
        JPanel pnlListaFolios = new JPanel(new GridLayout(1,2,10,10));
        pnlListaFolios.add(pnlListaFolio);
        pnlListaFolios.add(pnlBotonesFolios);
        JPanel pnlCentralFolios = new JPanel(new GridLayout(1,2,10,10));
        pnlCentralFolios.add(pnlFolios);
        pnlCentralFolios.add(pnlListaFolios);
        
        JPanel pnlFacturas = new JPanel(new BorderLayout());
        JPanel pnlComplementarioFacturas = new JPanel(new GridLayout(2,2,30,10));
        pnlComplementarioFacturas.setBorder(new EmptyBorder(10,0,10,0));
        pnlComplementarioFacturas.add(lblFactura);
        pnlComplementarioFacturas.add(txtFactura);
        // pnlFechaFacturas = new JPanel(new GridLayout(1,2,30,10));
        pnlComplementarioFacturas.add(lblFechaFactura);
        pnlComplementarioFacturas.add(dccFechaFactura);
        JPanel pnlArchivoFacturas = new JPanel(new BorderLayout());
        pnlArchivoFacturas.add(txtArchivoFactura, BorderLayout.CENTER);
        txtArchivoFactura.setEditable(false);
        pnlArchivoFacturas.add(btnArchivoFactura, BorderLayout.EAST);
        JPanel pnlBotonesFacturas = new JPanel(new GridLayout(2,1,10,10));
        pnlBotonesFacturas.add(btnAgregarFactura);
        pnlBotonesFacturas.add(btnEliminarFactura);
        pnlFacturas.add(pnlComplementarioFacturas, BorderLayout.CENTER);
        //pnlFacturas.add(pnlFechaFacturas, BorderLayout.CENTER);
        pnlFacturas.add(pnlArchivoFacturas, BorderLayout.SOUTH);
        JPanel pnlListaFacturas = new JPanel(new GridLayout(1,2,10,10));
        pnlListaFacturas.add(pnlListaFactura);
        pnlListaFacturas.add(pnlBotonesFacturas);
        JPanel pnlCentralFacturas = new JPanel(new GridLayout(1,2,10,10));
        pnlCentralFacturas.add(pnlFacturas);
        pnlCentralFacturas.add(pnlListaFacturas);
        
        JPanel pnlOrdenesGINSATEC = new JPanel(new BorderLayout());
        JPanel pnlComplementarioOrdenesGINSATEC = new JPanel(new GridLayout(2,2,30,10));
        pnlComplementarioOrdenesGINSATEC.setBorder(new EmptyBorder(10,0,10,0));
        pnlComplementarioOrdenesGINSATEC.add(lblOrdenGINSATEC);
        pnlComplementarioOrdenesGINSATEC.add(txtOrdenGINSATEC);
        //JPanel pnlFechaOrdenesGINSATEC = new JPanel(new GridLayout(1,2,30,10));
        pnlComplementarioOrdenesGINSATEC.add(lblFechaOrdenGINSATEC);
        pnlComplementarioOrdenesGINSATEC.add(dccFechaOrdenGINSATEC);
        JPanel pnlArchivoOrdenesGINSATEC = new JPanel(new BorderLayout());
        pnlArchivoOrdenesGINSATEC.add(txtArchivoOrdenGINSATEC, BorderLayout.CENTER);
        txtArchivoOrdenGINSATEC.setEditable(false);
        pnlArchivoOrdenesGINSATEC.add(btnArchivoOrdenGINSATEC, BorderLayout.EAST);
        JPanel pnlBotonesOrdenesGINSATEC = new JPanel(new GridLayout(2,1,10,10));
        pnlBotonesOrdenesGINSATEC.add(btnAgregarOrdenGINSATEC);
        pnlBotonesOrdenesGINSATEC.add(btnEliminarOrdenGINSATEC);
        pnlOrdenesGINSATEC.add(pnlComplementarioOrdenesGINSATEC, BorderLayout.CENTER);
        //pnlOrdenesGINSATEC.add(pnlFechaOrdenesGINSATEC, BorderLayout.CENTER);
        pnlOrdenesGINSATEC.add(pnlArchivoOrdenesGINSATEC, BorderLayout.SOUTH);
        JPanel pnlListaOrdenesGINSATEC = new JPanel(new GridLayout(1,2,10,10));
        pnlListaOrdenesGINSATEC.add(pnlListaOrdenGINSATEC);
        pnlListaOrdenesGINSATEC.add(pnlBotonesOrdenesGINSATEC);
        JPanel pnlCentralOrdenesGINSATEC = new JPanel(new GridLayout(1,2,10,10));
        pnlCentralOrdenesGINSATEC.add(pnlOrdenesGINSATEC);
        pnlCentralOrdenesGINSATEC.add(pnlListaOrdenesGINSATEC);
        
        JPanel pnlFinanciero = new JPanel(new GridLayout(5,2,10,5));
        pnlFinanciero.add(lblVendedor);
        pnlFinanciero.add(cmbVendedor);
        pnlFinanciero.add(lblImporte);
        JPanel pnlImporte = new JPanel(new GridLayout(1,2,10,0));
        pnlImporte.add(txtImporte);
        pnlImporte.add(cmbMoneda);
        pnlFinanciero.add(pnlImporte);
        pnlFinanciero.add(lblImportePendiente);
        pnlFinanciero.add(txtImportePendiente);
        pnlFinanciero.add(lblImporteFacturado);
        pnlFinanciero.add(txtImporteFacturado);
        pnlFinanciero.add(lblTasaCambio);
        pnlFinanciero.add(txtTasaCambio);
        txtImportePendiente.setEditable(false);
        
        JPanel pnlCheckBox = new JPanel(new GridLayout(4,2,50,0));
        pnlCheckBox.add(lblStatus);
        pnlCheckBox.add(cmbStatus);
        pnlCheckBox.add(lblRecibido);
        pnlCheckBox.add(chkRecibido);
        pnlCheckBox.add(lblInstalacion);
        pnlCheckBox.add(chkInstalacion);
        pnlCheckBox.add(lblCanalizacion);
        pnlCheckBox.add(chkCanalizacion);
        JPanel pnlDivision = new JPanel(new GridLayout(1,2,10,0));
        
        int rows = opcionesConceptoVenta.length;
        Object[][] lista = new Object[rows][2];
        for(int i = 0; i < rows; i++){
            lista[i][0] = opcionesConceptoVenta[i];
            lista[i][1] = 0;
        }
        Object[] encabezados = {"Sistema", "Valor (%)"};
        tblConceptoVenta = new JTable(lista, encabezados);
        tblConceptoVenta.setDefaultEditor(Object.class, null);
        
        TableColumnModel tcm = tblConceptoVenta.getColumnModel();
        TableColumn tc = tcm.getColumn(1);
        tc.setCellEditor(new SpinnerEditor());
        
        JPanel pnlConcepto = new JPanel(new BorderLayout());
        pnlConcepto.add(lblConceptoVenta, BorderLayout.PAGE_START);
        pnlConcepto.add(new JScrollPane(tblConceptoVenta), BorderLayout.CENTER);
        
        
        pnlDivision.add(pnlCheckBox);
        pnlDivision.add(pnlConcepto);
        
        JPanel pnlFechas = new JPanel(new GridLayout(5,2,10,5));
        pnlFechas.add(lblGM);
        JPanel pnlGM = new JPanel(new GridLayout(1,2,10,0));
        pnlGM.add(txtGM);
        pnlGM.add(lblGMporciento);
        pnlFechas.add(pnlGM);
        pnlFechas.add(lblDiasCredito);
        pnlFechas.add(txtDiasCredito);
        pnlFechas.add(lblFechaEntrega);
        pnlFechas.add(dccFechaEntrega);
        pnlFechas.add(lblFechaRecibido);
        pnlFechas.add(dccFechaRecibido);
        pnlFechas.add(lblFechaVencimiento);
        pnlFechas.add(dccFechaVencimiento);
        
        JPanel pnlFormulariosListas = new JPanel(new GridLayout(4,2,10,10));
        pnlFormulariosListas.add(pnlCentralOrdenes);
        pnlFormulariosListas.add(pnlCentralFacturas);
        pnlFormulariosListas.add(pnlCentralCotizaciones);
        pnlFormulariosListas.add(pnlCentralOrdenesGINSATEC);
        pnlFormulariosListas.add(pnlCentralFolios);
        pnlFormulariosListas.add(pnlFinanciero);
        pnlFormulariosListas.add(pnlDivision);
        pnlFormulariosListas.add(pnlFechas);
        
        JPanel pnlPrincipal = new JPanel(new BorderLayout());
        pnlPrincipal.add(pnlFormulariosListas, BorderLayout.CENTER);
        
        JPanel pnlNotas = new JPanel(new BorderLayout());
        JScrollPane scrollNotas = new JScrollPane(txtaNotas);
        txtaNotas.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 60));
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
        btnBuscarCliente.addActionListener(this);
        btnNuevoCliente.addActionListener(this);
        btnArchivoOrdenCompra.addActionListener(this);
        btnAgregarOrdenCompra.addActionListener(this);
        btnEliminarOrdenCompra.addActionListener(this);
        btnArchivoCotizacion.addActionListener(this);
        btnAgregarCotizacion.addActionListener(this);
        btnEliminarCotizacion.addActionListener(this);
        btnArchivoFactura.addActionListener(this);
        btnAgregarFactura.addActionListener(this);
        btnEliminarFactura.addActionListener(this);
        btnArchivoOrdenGINSATEC.addActionListener(this);
        btnAgregarOrdenGINSATEC.addActionListener(this);
        btnEliminarOrdenGINSATEC.addActionListener(this);
        btnArchivoFolio.addActionListener(this);
        btnAgregarFolio.addActionListener(this);
        btnEliminarFolio.addActionListener(this);
        btnModificarCliente.addActionListener(this);
        txtImporte.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent e){
                warn();
            }
            @Override
            public void removeUpdate(DocumentEvent e){
                warn();
            }
            @Override
            public void insertUpdate(DocumentEvent e){
                warn();
            }

            public void warn() {
                 try{
                    txtImportePendiente.setText(String.valueOf(Float.parseFloat(txtImporte.getText()) - Float.parseFloat(txtImporteFacturado.getText())));
                }
                catch(NumberFormatException ex){
                    Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
                    txtImportePendiente.setText("0");
                }
            }
        
        });
        txtImporteFacturado.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent e){
                warn();
            }
            @Override
            public void removeUpdate(DocumentEvent e){
                warn();
            }
            @Override
            public void insertUpdate(DocumentEvent e){
                warn();
            }

            public void warn() {
                 try{
                    if(!txtImporte.getText().isEmpty() && !txtImporteFacturado.getText().isEmpty()) 
                    txtImportePendiente.setText(String.valueOf(Float.parseFloat(txtImporte.getText()) - Float.parseFloat(txtImporteFacturado.getText())));
                }
                catch(NumberFormatException ex){
                    Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
                    txtImportePendiente.setText("0");
                }
            }
        
        });
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e)
            {
                ventas.actualizarFiltro();
            }
        });
        
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public Connection ConectarDB(){
        Servidor s = new Servidor();
        s.arrancarServidor();
        String ip = s.obtenerIP();
        System.out.println("IP obtenida: " + ip);
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + ip + ":3389/VentasDB", "usuario", "Sersitec886");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            if(s.reiniciarServidor()){
                c = ConectarDB();
            }
        }
        return c;
    }
    
    public final void CargarDatos(int folioVenta){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql =    "select ventas.*,"
                        +       "(select nombrecomercial from clientes where clientes.idcliente=ventas.cliente) as nombrecomercial,"
                        +       "(select razonsocial from clientes where clientes.idcliente=ventas.cliente) as razonsocial"
                        +       " from ventas where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                if(consulta.next()){
                    Calendar calendario = Calendar.getInstance();
                    calendario.setTime(consulta.getDate("fechaautorizacion"));
                    dccFechaAutorizacion.setSelectedDate(calendario);
                    cargarOrdenesCompra(folioVenta);
                    cargarCotizaciones(folioVenta);
                    cargarFoliosServicio(folioVenta);
                    cmbCliente.setSelectedItem(consulta.getString("nombrecomercial") + "-" + consulta.getString("razonsocial"));
                    cargarDetalleVenta(folioVenta);
                    cargarOrdenesCompraGINSATEC(folioVenta);
                    calendario.setTime(consulta.getDate("fechaentrega"));
                    dccFechaEntrega.setSelectedDate(calendario);
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
                    cargarFacturas(folioVenta);
                    cmbVendedor.setSelectedItem(consulta.getString("vendedor"));
                    txtImporte.setText(Float.toString(consulta.getFloat("importe")));
                    txtImportePendiente.setText(Float.toString(consulta.getFloat("importependiente")));
                    txtImporteFacturado.setText(Float.toString(consulta.getFloat("importefacturado")));
                    cmbMoneda.setSelectedItem(consulta.getString("tipomoneda"));
                    txtTasaCambio.setText(Float.toString(consulta.getFloat("tasacambio")));
                    txtGM.setText(Float.toString(consulta.getFloat("gm")));
                    txtDiasCredito.setText(Integer.toString(consulta.getInt("diascredito")));
                    calendario.setTime(consulta.getDate("fechavencimientopago"));
                    dccFechaVencimiento.setSelectedDate(calendario);
                    cmbStatus.setSelectedItem(consulta.getString("status"));
                    txtaNotas.setText(consulta.getString("notas"));
                    comando.close();
                    c.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Vector<String> cargarVendedores(){
        Vector<String> vendedores = new Vector<>();
        Connection c = ConectarDB();
        Statement comando;

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
            catch(SQLException ex){
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return vendedores;
    }
    
    public void cargarClientes(){
        opcionesCliente = new Vector<>();
        opcionesCliente.add("");
        opcionesNombreComercial = new Vector<>();
        opcionesNombreComercial.add("");
        opcionesRazonSocial = new Vector<>();
        opcionesRazonSocial.add("");
        Connection c = ConectarDB();
        Statement comando;

        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try{
                comando = c.createStatement();
                String sql = "select * from clientes order by nombrecomercial";
                ResultSet consulta = comando.executeQuery(sql);
                int i = 1;
                while(consulta.next()){
                    hstCliente.put(i, consulta.getInt("idcliente"));
                    opcionesNombreComercial.add(consulta.getString("nombrecomercial"));
                    opcionesRazonSocial.add(consulta.getString("razonsocial"));
                    opcionesCliente.add(opcionesNombreComercial.lastElement() + "-" + opcionesRazonSocial.lastElement());
                    i++;
                }
                comando.close();
                c.close();
            }
            catch(SQLException ex){
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void cargarDetalleVenta(int folioVenta){
        Connection c = ConectarDB();
        Statement comando;
        if(c == null){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try{
                comando = c.createStatement();
                String sql = "select * from detalleventa where folioventa=" + folioVenta + ";";
                ResultSet consulta = comando.executeQuery(sql);
                if(consulta.next()){
                    tblConceptoVenta.setValueAt(consulta.getFloat("automatizacion"), 0, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("controlaccesos"), 1, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("cctv"), 2, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("deteccionincendios"), 3, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("sistemasseguridad"), 4, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("pacienteenfermera"), 5, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("supresionincendios"), 6, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("viaticos"), 7, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("canalizacionescableados"), 8, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("programacionpuestaenmarcha"), 9, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("manoobra"), 10, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("poliza"), 11, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("refacciones"), 12, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("reparaciones"), 13, 1);
                    tblConceptoVenta.setValueAt(consulta.getFloat("varios"), 14, 1);
                }
                comando.close();
                c.close();
            }
            catch(SQLException ex){
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void cargarFoliosServicio(int folioVenta){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select folioservicio, ruta from foliosservicio where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String folioServicio, ruta;
                while(consulta.next()){
                    folioServicio = consulta.getString("folioservicio");
                    ruta = consulta.getString("ruta");
                    if(ruta == null){
                        ruta = NOHAYRUTA;
                    }
                    hstFolio.put(folioServicio, ruta);
                    dlmFolio.addElement(folioServicio);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarFacturas(int folioVenta){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select factura, fecha, ruta from facturas where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String factura; 
                String[] auxFactura = new String[2];
                while(consulta.next()){
                    factura = consulta.getString("factura");
                    auxFactura[0] = String.valueOf(consulta.getDate("fecha"));
                    auxFactura[1] = consulta.getString("ruta");
                    if(auxFactura[1] == null){
                        auxFactura[1] = NOHAYRUTA;
                    }
                    hstFactura.put(factura, auxFactura);
                    dlmFactura.addElement(factura);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarOrdenesCompra(int folioVenta){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select ordencompra, ruta from ordenescompra where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String orden, ruta;
                while(consulta.next()){
                    orden = consulta.getString("ordencompra");
                    ruta = consulta.getString("ruta");
                    if(ruta == null){
                        ruta = NOHAYRUTA;
                    }
                    hstOrdenCompra.put(orden, ruta);
                    dlmOrdenCompra.addElement(orden);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarOrdenesCompraGINSATEC(int folioVenta){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select ordencompra, fecha, ruta from ordenescompraginsatec where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String orden;
                String[] auxOrden = new String[2];
                while(consulta.next()){
                    orden = consulta.getString("ordencompra");
                    auxOrden[0] = String.valueOf(consulta.getDate("fecha"));
                    auxOrden[1] = consulta.getString("ruta");
                    if(auxOrden[1] == null){
                        auxOrden[1] = NOHAYRUTA;
                    }
                    hstOrdenGINSATEC.put(orden, auxOrden);
                    dlmOrdenGINSATEC.addElement(orden);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarCotizaciones(int folioVenta){
        Connection c = ConectarDB();
        Statement comando;

        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select cotizacion, ruta from cotizaciones where folioventa=" + folioVenta;
                ResultSet consulta = comando.executeQuery(sql);
                String cotizacion, ruta;
                while(consulta.next()){
                    cotizacion = consulta.getString("cotizacion");
                    ruta = consulta.getString("ruta");
                    if(ruta == null){
                        ruta = NOHAYRUTA;
                    }
                    hstCotizacion.put(cotizacion, ruta);
                    dlmCotizacion.addElement(cotizacion);
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void nuevoCliente(String nombreComercial, String razonSocial){
        Connection c = ConectarDB();
        Statement comando;
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
                hstCliente.put(cmbCliente.getItemCount()-1, id);
                sql = "insert into clientes values(" + id + ",'" + nombreComercial + "','" + razonSocial + "')";
                comando.executeUpdate(sql);
                comando.close();
                c.close();
            }
            catch(SQLException ex){
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarCliente(String nombreComercial, String razonSocial){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            int id = hstCliente.get(cmbCliente.getSelectedIndex());
            try{
                comando = c.createStatement();
                String sql = "update clientes set nombrecomercial='" + nombreComercial + "', razonsocial='" + razonSocial +"' where idcliente=" + id + ";";
                comando.executeUpdate(sql);
                comando.close();
                c.close();
            }
            catch(SQLException ex){
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void nuevoRegistro(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                int id;
                int folioVenta;
                String ruta;
                String[] aux;
                String sql = "select max(folioventa) from ventas";
                comando = c.createStatement();
                ResultSet consulta = comando.executeQuery(sql);
                folioVenta = 0;
                if(consulta.next()){
                    folioVenta = consulta.getInt("max") + 1;
                }
                folio = folioVenta;
                
                sql = "insert into ventas values(" + folioVenta + ",'" + dccFechaAutorizacion.getText() + "'," 
                        + hstCliente.get(cmbCliente.getSelectedIndex()) + ",'detalle','" + dccFechaEntrega.getText() + "','" + dccFechaRecibido.getText() 
                        + "','" + chkRecibido.isSelected() + "','" + chkInstalacion.isSelected() + "','" + chkCanalizacion.isSelected() 
                        + "','" + cmbVendedor.getSelectedItem() + "'," + txtImporte.getText() + "," + txtImportePendiente.getText() + ","
                        + txtImporteFacturado.getText() + ",'" + cmbMoneda.getSelectedItem() + "'," + txtTasaCambio.getText() + "," 
                        + txtGM.getText() + "," + txtDiasCredito.getText() + ",'" + dccFechaVencimiento.getText() + "','" 
                        + cmbStatus.getSelectedItem() + "','" + txtaNotas.getText() + "'," + ventas.idUsuario + ")";
                comando.executeUpdate(sql);
                
                sql = "select max(iddetalle) from detalleventa;";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                sql = "insert into detalleventa values(" + id + ", " + folioVenta;
                for(int i = 0; i < opcionesConceptoVenta.length; i++){
                    sql += ", " + tblConceptoVenta.getValueAt(i, 1);
                }
                sql += ");";
                comando.executeUpdate(sql);
                
                sql = "select max(idfolio) from foliosservicio";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                for(int i = 0; i < dlmFolio.getSize(); i++, id++){
                    ruta = hstFolio.get(dlmFolio.get(i).toString());
                    if(ruta.equals(NOHAYRUTA)){
                        ruta = "null";
                    }
                    else{
                        ruta = "'" + ruta + "'";
                    }
                    sql = "insert into foliosservicio values(" + id + ",'" + dlmFolio.get(i) + "'," + folioVenta + ", " + ruta + ");";
                    comando.executeUpdate(sql);
                }
                
                sql = "select max(idcotizacion) from cotizaciones";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                for(int i = 0; i < dlmCotizacion.getSize(); i++, id++){
                    ruta = hstCotizacion.get(dlmCotizacion.get(i).toString());
                    if(ruta.equals(NOHAYRUTA)){
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
                    ruta = hstOrdenCompra.get(dlmOrdenCompra.get(i).toString());
                    if(ruta.equals(NOHAYRUTA)){
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
                for(int i = 0; i < dlmOrdenGINSATEC.getSize(); i++, id++){
                    aux = hstOrdenGINSATEC.get(dlmOrdenGINSATEC.get(i).toString());
                    if(aux[1].equals(NOHAYRUTA)){
                        aux[1] = "null";
                    }
                    else{
                        aux[1] = "'" + aux[1] +"'";
                    }
                    sql = "insert into ordenescompraginsatec values(" + id + ",'" + dlmOrdenGINSATEC.get(i) + "'," + folioVenta + ",'" + aux[0] + "'," + aux[1] + ")";
                    comando.executeUpdate(sql);
                }
                
                sql = "select max(idfactura) from facturas";
                consulta = comando.executeQuery(sql);
                id = 0;
                if(consulta.next()){
                    id = consulta.getInt("max") + 1;
                }
                for(int i = 0; i < dlmFactura.getSize(); i++, id++){
                    aux = hstFactura.get(dlmFactura.get(i).toString());
                    if(aux[1].equals(NOHAYRUTA)){
                        aux[1] = "null";
                    }
                    else{
                        aux[1] = "'" + aux[1] + "'";
                    }
                    sql = "insert into facturas values(" + id + ",'" + dlmFactura.get(i) + "'," + folioVenta + ",'" + aux[0] + "'," + aux[1] + ");";
                    comando.executeUpdate(sql);
                }
                JOptionPane.showMessageDialog(null, "Registro Exitoso");
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarRegistro(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "update ventas set fechaautorizacion='" + dccFechaAutorizacion.getText() + "',cliente=" 
                        + hstCliente.get(cmbCliente.getSelectedIndex()) + ", fechaentrega='" + dccFechaEntrega.getText() + "',fecharecibido='" + dccFechaRecibido.getText() 
                        + "',materialrecibido='" + chkRecibido.isSelected() + "',requiereinstalacion='" + chkInstalacion.isSelected() 
                        + "',requierecanalizacion='" + chkCanalizacion.isSelected() + "',vendedor='" + cmbVendedor.getSelectedItem() + "',importe=" 
                        + txtImporte.getText() + ",importependiente=" + txtImportePendiente.getText() + ",importefacturado=" + txtImporteFacturado.getText()
                        + ",tipomoneda='" + cmbMoneda.getSelectedItem() + "',tasacambio=" + txtTasaCambio.getText() + ",gm=" 
                        + txtGM.getText() + ",diascredito=" + txtDiasCredito.getText() + ",fechavencimientopago='" + dccFechaVencimiento.getText() 
                        + "',status='" + cmbStatus.getSelectedItem() + "',notas='" + txtaNotas.getText() + "' where folioventa=" + folio;
                comando.executeUpdate(sql);
                modificarDetalle();
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
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarDetalle(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql =    "update detalleventa set automatizacion=" + tblConceptoVenta.getValueAt(0, 1) + ", controlaccesos=" + tblConceptoVenta.getValueAt(1, 1) + 
                                ", cctv=" + tblConceptoVenta.getValueAt(2, 1) + ", deteccionincendios=" + tblConceptoVenta.getValueAt(3, 1) + ", sistemasseguridad=" + 
                                tblConceptoVenta.getValueAt(4, 1) + ", pacienteenfermera=" + tblConceptoVenta.getValueAt(5, 1) + ", supresionincendios=" + tblConceptoVenta.getValueAt(6, 1) + 
                                ", viaticos=" + tblConceptoVenta.getValueAt(7, 1) + ", canalizacionescableados=" + tblConceptoVenta.getValueAt(8, 1) + ", programacionpuestaenmarcha=" + 
                                tblConceptoVenta.getValueAt(9, 1) + ", manoobra=" + tblConceptoVenta.getValueAt(10, 1) + ", poliza=" + tblConceptoVenta.getValueAt(11, 1) + 
                                ", refacciones=" + tblConceptoVenta.getValueAt(12, 1) + ", reparaciones=" + tblConceptoVenta.getValueAt(13, 1) + ", varios=" + 
                                tblConceptoVenta.getValueAt(14, 1) + " where folioventa=" + folio;
                comando.executeUpdate(sql);
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante la modificacion");
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarFolios(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                Vector<Integer> id = new Vector<>();
                String ruta;
                String sql = "select idfolio from foliosservicio where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idfolio"));
                }
                int i;
                if(id.size() >= dlmFolio.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from foliosservicio where idfolio=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < dlmFolio.size(); i++){
                        ruta = hstFolio.get(dlmFolio.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into foliosservicio values(" + id.get(i) + ",'" + dlmFolio.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from foliosservicio where idfolio=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < id.size(); i++){
                        ruta = hstFolio.get(dlmFolio.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into foliosservicio values(" + id.get(i) + ",'" + dlmFolio.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idfolio) from foliosservicio";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmFolio.size(); i++, newId++){
                        ruta = hstFolio.get(dlmFolio.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into foliosservicio values(" + newId + ",'" + dlmFolio.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante la modificación");
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarCotizaciones(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                int i;
                String ruta;
                Vector<Integer> id = new Vector<>();
                String sql = "select idcotizacion from cotizaciones where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idcotizacion"));
                }
                if(id.size() >= dlmCotizacion.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from cotizaciones where idcotizacion=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < dlmCotizacion.size(); i++){
                        ruta = hstCotizacion.get(dlmCotizacion.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into cotizaciones values(" + id.get(i) + ",'" + dlmCotizacion.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from cotizaciones where idcotizacion=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < id.size(); i++){
                        ruta = hstCotizacion.get(dlmCotizacion.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into cotizaciones values(" + id.get(i) + ",'" + dlmCotizacion.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idcotizacion) from cotizaciones";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmCotizacion.size(); i++, newId++){
                        ruta = hstCotizacion.get(dlmCotizacion.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into cotizaciones values(" + newId + ",'" + dlmCotizacion.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarOrdenes(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                int i;
                Vector<Integer> id = new Vector<>();
                String ruta;
                String sql = "select idorden from ordenescompra where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idorden"));
                }
                if(id.size() >= dlmOrdenCompra.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from ordenescompra where idorden=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < dlmOrdenCompra.size(); i++){
                        ruta = hstOrdenCompra.get(dlmOrdenCompra.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into ordenescompra values(" + id.get(i) + ",'" + dlmOrdenCompra.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from ordenescompra where idorden=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < id.size(); i++){
                        ruta = hstOrdenCompra.get(dlmOrdenCompra.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into ordenescompra values(" + id.get(i) + ",'" + dlmOrdenCompra.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idorden) from ordenescompra";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmOrdenCompra.size(); i++, newId++){
                        ruta = hstOrdenCompra.get(dlmOrdenCompra.get(i).toString());
                        if(ruta.equals(NOHAYRUTA)){
                            ruta = "null";
                        }
                        else{
                            ruta = "'" + ruta + "'";
                        }
                        sql = "insert into ordenescompra values(" + newId + ",'" + dlmOrdenCompra.get(i) + "'," + folio + "," + ruta + ")";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarOrdenesGINSATEC(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                int i;
                Vector<Integer> id = new Vector<>();
                String[] aux;
                String sql = "select idorden from ordenescompraginsatec where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idorden"));
                }
                if(id.size() >= dlmOrdenGINSATEC.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from ordenescompraginsatec where idorden=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < dlmOrdenGINSATEC.size(); i++){
                        aux = hstOrdenGINSATEC.get(dlmOrdenGINSATEC.get(i).toString());
                        if(aux[1].equals(NOHAYRUTA)){
                            aux[1] = "null";
                        }
                        else{
                            aux[1] = "'" + aux[1] + "'";
                        }
                        sql = "insert into ordenescompraginsatec values(" + id.get(i) + ",'" + dlmOrdenGINSATEC.get(i) + "'," + folio + ",'" + aux[0] + "'," + aux[1] + ")";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from ordenescompraginsatec where idorden=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < id.size(); i++){
                        aux = hstOrdenGINSATEC.get(dlmOrdenGINSATEC.get(i).toString());
                        if(aux[1].equals(NOHAYRUTA)){
                            aux[1] = "null";
                        }
                        else{
                            aux[1] = "'" + aux[1] + "'";
                        }
                        sql = "insert into ordenescompraginsatec values(" + id.get(i) + ",'" + dlmOrdenGINSATEC.get(i) + "'," + folio + ",'" + aux[0] + "'," + aux[1] + ")";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idorden) from ordenescompraginsatec";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmOrdenGINSATEC.size(); i++, newId++){
                        aux = hstOrdenGINSATEC.get(dlmOrdenGINSATEC.get(i).toString());
                        if(aux[1].equals(NOHAYRUTA)){
                            aux[1] = "null";
                        }
                        else{
                            aux[1] = "'" + aux[1] + "'";
                        }
                        sql = "insert into ordenescompraginsatec values(" + newId + ",'" + dlmOrdenGINSATEC.get(i) + "'," + folio + ",'" + aux[0] + "'," + aux[1] + ")";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modificarFacturas(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                int i;
                Vector<Integer> id = new Vector<>();
                String[] aux;
                String sql = "select idfactura from facturas where folioventa=" + folio;
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    id.add(consulta.getInt("idfactura"));
                }
                if(id.size() >= dlmFactura.size()){
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from facturas where idfactura=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < dlmFactura.size(); i++){
                        aux = hstFactura.get(dlmFactura.get(i).toString());
                        if(aux[1].equals(NOHAYRUTA)){
                            aux[1] = "null";
                        }
                        else{
                            aux[1] = "'" + aux[1] + "'";
                        }
                        sql = "insert into facturas values(" + id.get(i) + ",'" + dlmFactura.get(i) + "'," + folio + ",'" + aux[0] + "'," + aux[1] + ")";
                        comando.executeUpdate(sql);
                    }
                }
                else{
                    for(i = 0; i < id.size(); i++){
                        sql = "delete from facturas where idfactura=" + id.get(i);
                        comando.executeUpdate(sql);
                    }
                    for(i = 0; i < id.size(); i++){
                        aux = hstFactura.get(dlmFactura.get(i).toString());
                        if(aux[1].equals(NOHAYRUTA)){
                            aux[1] = "null";
                        }
                        else{
                            aux[1] = "'" + aux[1] + "'";
                        }
                        sql = "insert into facturas values(" + id.get(i) + ",'" + dlmFactura.get(i) + "'," + folio + ",'" + aux[0] + "'," + aux[1] + ")";
                        comando.executeUpdate(sql);
                    }
                    sql = "select max(idfactura) from facturas";
                    consulta = comando.executeQuery(sql);
                    int newId = 0;
                    if(consulta.next()){
                        newId = consulta.getInt("max") + 1;
                    }
                    for(; i < dlmFactura.size(); i++, newId++){
                        aux = hstFactura.get(dlmFactura.get(i).toString());
                        if(aux[1].equals(NOHAYRUTA)){
                            aux[1] = "null";
                        }
                        else{
                            aux[1] = "'" + aux[1] + "'";
                        }
                        sql = "insert into facturas values(" + newId + ",'" + dlmFactura.get(i) + "'," + folio + ",'" + aux[0] + "'," + aux[1] + ")";
                        comando.executeUpdate(sql);
                    }
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
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
                catch(NumberFormatException ex){
                    Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
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
            catch(NumberFormatException ex){
                Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
    
    public void inicializarFila(){
        String[] aux;
        try{
            f.FolioVenta = folio;
            f.FechaAutorizacion = dccFechaAutorizacion.getSelectedDate().getTime();
            f.NombreComercial = opcionesNombreComercial.get(cmbCliente.getSelectedIndex());
            f.RazonSocial = opcionesRazonSocial.get(cmbCliente.getSelectedIndex());
            f.ConceptoVenta = "Detalle";
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
                aux = hstFactura.get(dlmFactura.get(i).toString());
                f.FechaFactura += aux[0] + "-";
                f.Factura += dlmFactura.get(i) + "-";
            }
            f.OrdenCompraGINSATEC = "";
            f.FechaOrdenCompraGINSATEC = "";
            for(int i = 0; i < dlmOrdenGINSATEC.size(); i++){
                aux = hstOrdenGINSATEC.get(dlmOrdenGINSATEC.get(i).toString());
                f.FechaOrdenCompraGINSATEC += aux[0] + "-";
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
        catch(NumberFormatException ex){
            Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
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
                    int sum = 0;
                    boolean valido = true;
                    ArrayList<Float> conceptos = new ArrayList<>();
                    conceptos.add(Float.valueOf(String.valueOf(txtImporte.getText())));
                    for(int i = 0; i < opcionesConceptoVenta.length; i++){
                        if(Float.valueOf(String.valueOf(tblConceptoVenta.getValueAt(i, 1))) < 0) valido = false;
                        sum += Float.valueOf(String.valueOf(tblConceptoVenta.getValueAt(i, 1)));
                        conceptos.add(Float.valueOf(String.valueOf(tblConceptoVenta.getValueAt(i, 1))));
                    }
                    if(!valido){
                        JOptionPane.showMessageDialog(this, "No se permiten números negativos para el detalle de la venta", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                    else if(sum < 100){
                        JOptionPane.showMessageDialog(this, "El total de los conceptos de venta es menor al 100%");
                    }
                    else if(sum > 100){
                        JOptionPane.showMessageDialog(this, "El total de los conceptos de venta es mayor al 100%");
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
                        ventas.hstDetalleVenta.replace(folio, conceptos);
                        if(ventas.modeloRegistrar == null){
                            if(ventas.admin){
                                ventas.modeloRegistrar = new SortTable(ventas.columnasMostrar, ventas.modeloMostrar.datos);
                            }
                            else{
                                ventas.modeloRegistrar = new SortTable(ventas.columnasRegistrar, new ArrayList<>());
                            }
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
                            System.out.println("No es igual a null");
                            ventas.modeloRegistrar.modificar(folio, f);
                            ventas.tcaRegistrar.adjustColumns();
                        }
                        if(ventas.modeloMostrar == null){
                            ventas.modeloMostrar = new SortTable(ventas.columnasMostrar, new ArrayList<>());
                            ventas.tblTablaMostrar = new JTable(ventas.modeloMostrar);
                            ventas.modeloMostrar.modificar(folio, f);

                            ventas.pnlMostrar.remove(ventas.pnlTablaMostrar);
                            ventas.crearTablas(ventas.tblTablaMostrar);
                            ventas.pnlMostrar.add(ventas.pnlTablaMostrar, BorderLayout.CENTER);
                            ventas.pnlMostrar.validate();
                            ventas.pnlMostrar.repaint();
                        }
                        else{
                            ventas.modeloMostrar.modificar(folio, f);
                            ventas.tcaMostrar.adjustColumns();
                        }
                        this.dispose();
                    }
                }
            }
        }
        else if(accion == btnCancelar){
            ventas.actualizarFiltro();
            this.dispose();
        }
        else if(accion == btnBuscarCliente){
            String buscar = JOptionPane.showInputDialog(
                    this,
                    "Buscar Cliente",
                    "Escribe el Cliente a buscar",
                    JOptionPane.INFORMATION_MESSAGE);
            if(buscar != null){
                int index = buscar(buscar);
                cmbCliente.setSelectedIndex(index);
            }
        }
        else if(accion == btnNuevoCliente){
            String nombreComercial = null;
            do{
                nombreComercial = JOptionPane.showInputDialog(
                    this, 
                    "Nombre Comercial del nuevo cliente:", 
                    "Agregar Nuevo Nombre Comercial", 
                    JOptionPane.INFORMATION_MESSAGE);
                if(nombreComercial != null){
                    if(nombreComercial.contains("'")){
                        JOptionPane.showMessageDialog(null, "El caracter ' puede afectar el funcionamiento de la base de datos.\n"
                            + "Por favor evita usarlo");
                    }
                }
                else{
                    break;
                }
            }while(nombreComercial.contains("'"));
            String razonSocial = null;
            do{
            razonSocial = JOptionPane.showInputDialog(
                    this, 
                    "Razón Social del nuevo cliente:", 
                    "Agregar Nueva Razón Social", 
                    JOptionPane.INFORMATION_MESSAGE);
            if(razonSocial != null){
                if(razonSocial.contains("'")){
                    JOptionPane.showMessageDialog(null, "El caracter ' puede afectar el funcionamiento de la base de datos.\n"
                            + "Por favor evita usarlo");
                }
            }
            else{
                break;
            }
            }while(razonSocial.contains("'"));
            if(nombreComercial != null && razonSocial != null){
                nombreComercial = nombreComercial.toUpperCase();
                razonSocial = razonSocial.toUpperCase();
                boolean iguales = false;
                for(int i = 0; i < opcionesCliente.size(); i++){
                    if(opcionesNombreComercial.get(i).equals(nombreComercial) && opcionesRazonSocial.get(i).equals(razonSocial)){
                        iguales = true;
                    }
                }
                if(nombreComercial.equals("") && razonSocial.equals("")){
                    JOptionPane.showMessageDialog(null, "Debes capturar al menos uno de los dos.\n"
                            + "Nombre Comercial o Razón Social");
                }
                else if(iguales){
                    JOptionPane.showMessageDialog(null, "Ese conjunto de Nombre Comercial y Razón Social ya existen");
                }
                else{
                    if(razonSocial.equals("")){
                        razonSocial = "RS";
                    }
                    if(nombreComercial.equals("")){
                        nombreComercial = "NC";
                    }
                    opcionesNombreComercial.add(nombreComercial);
                    opcionesRazonSocial.add(razonSocial);
                    opcionesCliente.add(nombreComercial + "-" + razonSocial);
                    nuevoCliente(nombreComercial, razonSocial);
                    modelCliente = new DefaultComboBoxModel(opcionesCliente);
                    cmbCliente.setModel(modelCliente);
                    ventas.filtrosCliente.add(nombreComercial);
                    ventas.actualizarFiltro();
                }
            }
        }
        else if(accion == btnModificarCliente){
            if(cmbCliente.getSelectedItem().equals("")){
                JOptionPane.showMessageDialog(null, "No has seleccionado un cliente");
            }
            else{
                String nombreComercial = opcionesNombreComercial.get(cmbCliente.getSelectedIndex());
                do{
                    nombreComercial = JOptionPane.showInputDialog(
                            this, 
                            "Nuevo Nombre Comercial del cliente:", 
                            "Agregar Nuevo Nombre Comercial", 
                            JOptionPane.INFORMATION_MESSAGE, 
                            null, 
                            null, 
                            opcionesNombreComercial.get(cmbCliente.getSelectedIndex())).toString();
                    
                    /*nombreComercial = JOptionPane.showInputDialog(
                    this, 
                    "Nuevo Nombre Comercial del cliente:", 
                    "Agregar Nuevo Nombre Comercial", 
                    JOptionPane.INFORMATION_MESSAGE);*/
                    
                    if(nombreComercial != null){
                        if(nombreComercial.contains("'")){
                            JOptionPane.showMessageDialog(null, "El caracter ' puede afectar el funcionamiento de la base de datos.\n"
                                    + "Por favor evita usarlo");
                        }
                    }
                    else{
                        break;
                    }
                }while(nombreComercial.contains("'"));
                String razonSocial = null;
                do{
                    razonSocial = JOptionPane.showInputDialog(
                            this, 
                            "Nueva Razón Social del cliente:", 
                            "Agregar Nueva Razón Social", 
                            JOptionPane.INFORMATION_MESSAGE, 
                            null, 
                            null, 
                            opcionesRazonSocial.get(cmbCliente.getSelectedIndex())).toString();
                    
                    /*razonSocial = JOptionPane.showInputDialog(
                        this, 
                        "Nueva Razón Social del cliente:", 
                        "Agregar Nueva Razón Social", 
                        JOptionPane.INFORMATION_MESSAGE);*/
                    
                    if(razonSocial != null){
                        if(razonSocial.contains("'")){
                            JOptionPane.showMessageDialog(null, "El caracter ' puede afectar el funcionamiento de la base de datos.\n"
                                    + "Por favor evita usarlo");
                        }
                    }
                    else{
                        break;
                    }
                }while(razonSocial.contains("'"));
                if(nombreComercial != null && razonSocial != null){
                    nombreComercial = nombreComercial.toUpperCase();
                    razonSocial = razonSocial.toUpperCase();
                    boolean iguales = false;
                    for(int i = 0; i < opcionesCliente.size(); i++){
                        if(opcionesNombreComercial.get(i).equals(nombreComercial) && opcionesRazonSocial.get(i).equals(razonSocial)){
                            iguales = true;
                        }
                    }
                    if(nombreComercial.equals("") && razonSocial.equals("")){
                        JOptionPane.showMessageDialog(null, "Debes capturar al menos uno de los dos.\n"
                            + "Nombre Comercial o Razón Social");
                    }
                    else if(iguales){
                        JOptionPane.showMessageDialog(null, "Ese conjunto de Nombre Comercial y Razón Social ya existen");
                    }
                    else{
                        if(razonSocial.equals("")){
                            razonSocial = "RS";
                        }
                        if(nombreComercial.equals("")){
                            nombreComercial = "NC";
                        }
                        modificarCliente(nombreComercial, razonSocial);
                        ventas.filtrosCliente.remove(cmbCliente.getSelectedItem().toString());
                        ventas.filtrosCliente.add(nombreComercial);
                        ventas.actualizarFiltro();
                        opcionesNombreComercial.remove(cmbCliente.getSelectedIndex());
                        opcionesNombreComercial.add(nombreComercial);
                        opcionesRazonSocial.remove(cmbCliente.getSelectedIndex());
                        opcionesRazonSocial.add(razonSocial);
                        opcionesCliente.remove(cmbCliente.getSelectedItem().toString());
                        opcionesCliente.add(nombreComercial + "-" + razonSocial);
                        modelCliente = new DefaultComboBoxModel(opcionesCliente);
                        cmbCliente.setModel(modelCliente);
                    }
                }
            }
        }
        else if(accion == btnArchivoOrdenCompra){
            JFileChooser dialogoArchivo = new JFileChooser();
            dialogoArchivo.setVisible(true);
            if(dialogoArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                try {
                    txtArchivoOrdenCompra.setText(dialogoArchivo.getSelectedFile().getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                txtArchivoOrdenCompra.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnAgregarOrdenCompra){
            int option = JOptionPane.YES_OPTION;
            String[] ordenes;
            for(Filas fila : ventas.modeloMostrar.datos){
                ordenes = fila.OrdenCompra.split(",");
                for(String orden : ordenes){
                    if(orden.equals(txtOrdenCompra.getText()) && (!orden.equals("NA") && !orden.equals("N/A") && !orden.equals(""))){
                        option = JOptionPane.showConfirmDialog(this, "La orden que deseas agregar ya fue agregada en otro registro.\nDeseas continuar?", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
            if(!txtOrdenCompra.getText().isEmpty() && option == JOptionPane.YES_OPTION){
                hstOrdenCompra.put(txtOrdenCompra.getText(), txtArchivoOrdenCompra.getText());
                dlmOrdenCompra.addElement(txtOrdenCompra.getText());
                txtOrdenCompra.setText("");
                txtArchivoOrdenCompra.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnEliminarOrdenCompra){
            if(!lstOrdenCompra.isSelectionEmpty()){
                hstOrdenCompra.remove(dlmOrdenCompra.remove(lstOrdenCompra.getSelectedIndex()).toString());
            }
        }
        else if(accion == btnArchivoOrdenGINSATEC){
            JFileChooser dialogoArchivo = new JFileChooser();
            dialogoArchivo.setVisible(true);
            if(dialogoArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                try {
                    txtArchivoOrdenGINSATEC.setText(dialogoArchivo.getSelectedFile().getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                txtArchivoOrdenGINSATEC.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnAgregarOrdenGINSATEC){
            String[] aux = new String[2];
            if(!txtOrdenGINSATEC.getText().isEmpty()){
                aux[0] = dccFechaOrdenGINSATEC.getText();
                aux[1] = txtArchivoOrdenGINSATEC.getText();
                hstOrdenGINSATEC.put(txtOrdenGINSATEC.getText(), aux);
                dlmOrdenGINSATEC.addElement(txtOrdenGINSATEC.getText());
                txtOrdenGINSATEC.setText("");
                txtArchivoOrdenGINSATEC.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnEliminarOrdenGINSATEC){
            if(!lstOrdenGINSATEC.isSelectionEmpty()){
                hstOrdenGINSATEC.remove(dlmOrdenGINSATEC.remove(lstOrdenGINSATEC.getSelectedIndex()).toString());
            }
        }
        else if(accion == btnArchivoFactura){
            JFileChooser dialogoArchivo = new JFileChooser();
            dialogoArchivo.setVisible(true);
            if(dialogoArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                try {
                    txtArchivoFactura.setText(dialogoArchivo.getSelectedFile().getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                txtArchivoFactura.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnAgregarFactura){
            String[] aux = new String[2];
            if(!txtFactura.getText().isEmpty()){
                aux[0] = dccFechaFactura.getText();
                aux[1] = txtArchivoFactura.getText();
                hstFactura.put(txtFactura.getText(), aux);
                dlmFactura.addElement(txtFactura.getText());
                txtFactura.setText("");
                txtArchivoFactura.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnEliminarFactura){
            if(!lstFactura.isSelectionEmpty()){
                hstFactura.remove(dlmFactura.remove(lstFactura.getSelectedIndex()).toString());
            }
        }
        else if(accion == btnArchivoFolio){
            JFileChooser dialogoArchivo = new JFileChooser();
            dialogoArchivo.setVisible(true);
            if(dialogoArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                try {
                    txtArchivoFolio.setText(dialogoArchivo.getSelectedFile().getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                txtArchivoFolio.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnAgregarFolio){
            if(!txtFolio.getText().isEmpty()){
                hstFolio.put(txtFolio.getText(), txtArchivoFolio.getText());
                dlmFolio.addElement(txtFolio.getText());
                txtFolio.setText("");
                txtArchivoFolio.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnEliminarFolio){
            if(!lstFolio.isSelectionEmpty()){
                hstFolio.remove(dlmFolio.remove(lstFolio.getSelectedIndex()).toString());
            }
        }
        else if(accion == btnArchivoCotizacion){
            JFileChooser dialogoArchivo = new JFileChooser();
            dialogoArchivo.setVisible(true);
            if(dialogoArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                try {
                    txtArchivoCotizacion.setText(dialogoArchivo.getSelectedFile().getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Capturar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                txtArchivoCotizacion.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnAgregarCotizacion){
            if(!txtCotizacion.getText().isEmpty()){
                hstCotizacion.put(txtCotizacion.getText(), txtArchivoCotizacion.getText());
                dlmCotizacion.addElement(txtCotizacion.getText());
                txtCotizacion.setText("");
                txtArchivoCotizacion.setText(NOHAYRUTA);
            }
        }
        else if(accion == btnEliminarCotizacion){
            if(!lstCotizacion.isSelectionEmpty()){
                hstCotizacion.remove(dlmCotizacion.remove(lstCotizacion.getSelectedIndex()).toString());
            }
        }
    }

    @Override
    public void run() {
        while(true){
            /*Nothing*/
        }
    }

    private int buscar(String regex){ 
        Pattern p = Pattern.compile(".*" + regex.toUpperCase() + ".*");
        int i;
        for (i = 0; i < opcionesCliente.size(); i++){
            if (p.matcher(opcionesNombreComercial.get(i)).matches() || p.matcher(opcionesRazonSocial.get(i)).matches()) {
              return i;
            }
        }
        return 0;
    }
    
}
