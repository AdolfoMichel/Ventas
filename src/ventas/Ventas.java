/*
*Autor: Adolfo Michel
*Clase principal
 */
package ventas;

import ventas.Utilerias.TableColumnAdjuster;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class Ventas extends JFrame implements ActionListener, Runnable{

    int idUsuario;
    boolean admin = false;
    
    Hashtable<Integer,ArrayList> hstDetalleVenta = new Hashtable<>();
    
    public SortTable modeloMostrar = null;
    public SortTable modeloRegistrar = null;
    
    TableColumnAdjuster tcaMostrar;
    TableColumnAdjuster tcaRegistrar;
    
    String[] columnasRegistrar = {"<html>ID<br>&nbsp;</html>", "<html>Fecha <br>Autorización</html>", "<html>Orden de <br>Compra</html>", "Cotización", "<html>Folio de <br>Servicio</html>",
        "Nombre Comercial", "Razón Social", "<html>Concepto <br>de Venta<html>", "<html>Fecha de Orden <br>GINSATEC</html>", "<html>Orden de compra <br>GINSATEC</html>", "<html>Fecha Aproximada <br>de Entrega</html>",
        "<html>Fecha de <br>Recibido</html>", "<html>Material <br>Recibido</html>", "<html>Requiere <br>Instalación</html>", "<html>Requiere <br>Canalización</html>",
        "<html>Fecha <br>Factura</html>", "Factura", "Vendedor", "Importe", "<html>Importe <br>Pendiente</html>", "<html>Importe <br>Facturado</html>", "<html>Tipo <br>Moneda</html>", 
        "<html>Tasa de <br>cambio</html>", "GM", "<html>Días de <br>Credito</html>", "<html>Fecha de <br>Vencimiento Pago</html>", "Status", "Notas"};
    
    String[] columnasMostrar = {"<html>ID<br>&nbsp;</html>", "<html>Fecha <br>Autorización</html>", "<html>Orden de <br>Compra</html>", "Cotización", "<html>Folio de <br>Servicio</html>",
        "Nombre Comercial", "Razón Social", "<html>Concepto <br>de Venta<html>", "<html>Fecha de Orden <br>GINSATEC</html>", "<html>Orden de compra <br>GINSATEC</html>", "<html>Fecha Aproximada <br>de Entrega</html>",
        "<html>Fecha de <br>Recibido</html>", "<html>Material <br>Recibido</html>", "<html>Requiere <br>Instalación</html>", "<html>Requiere <br>Canalización</html>",
        "<html>Fecha <br>Factura</html>", "Factura", "Vendedor", "Importe", "<html>Importe <br>Pendiente</html>", "<html>Importe <br>Facturado</html>", "<html>Tipo <br>Moneda</html>", 
        "<html>Tasa de <br>cambio</html>", "GM", "<html>Días de <br>Credito</html>", "<html>Fecha de <br>Vencimiento Pago</html>", "Status", "Notas", "Capturó"};
    
    int minYear = 0;
    WaitWindow window;
    
    String[] filtrosStatus =    {   "Todos", "Pendiente", "Activa", "Pagada", "No pagada", "Cancelada"};
    String[] filtrosMeses =     {   "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre", "Todos"};
    String[] filtrosConcepto =  {   "Automatización", "Control de Accesos", "CCTV", "Detección de Incendios", "Sistemas de Seguridad", 
                                    "Paciente-Enfermera", "Supresión de Incendios", "Viaticos", "Canalizaciones y cableados", 
                                    "Programación y puesta en marcha", "Mano de Obra", "Poliza", "Refacciones", "Reparaciones", "Varios", "Todos"};
    ArrayList<String> filtrosVendedor;
    ArrayList<String> filtrosAnio;
    ArrayList<String> filtrosCliente;
    
    JTabbedPane principal = new JTabbedPane();
    JPanel pnlMostrar = new JPanel(new BorderLayout());
    JScrollPane pnlTablaRegistrar;
    JScrollPane pnlTablaMostrar;
    JTable tblTablaRegistrar;
    JTable tblTablaMostrar;
    JButton btnNuevo = new JButton("Capturar Nuevo");
    JButton btnModificar = new JButton("Modificar");
    JPanel pnlBotones = new JPanel();
    JPanel pnlRegistrar = new JPanel();
    
    ArrayList<JCheckBox> chkMeses = new ArrayList<>();
    
    DefaultComboBoxModel modeloStatus = new DefaultComboBoxModel(filtrosStatus);
    DefaultComboBoxModel modeloAnio;
    DefaultComboBoxModel modeloCliente;
    
    JComboBox cmbAnio;
    JComboBox cmbStatus;
    JComboBox cmbCliente;
    
    ArrayList<JCheckBox> chkConceptos = new ArrayList<>();
    
    ArrayList<JCheckBox> chkVendedores = new ArrayList<>();
    
    JPanel pnlMeses = new JPanel(new GridLayout(7,2));
    JPanel pnlConceptos = new JPanel(new GridLayout(8,2));
    JPanel pnlComboBox = new JPanel(new GridLayout(6,1,10,5));
    JPanel pnlVendedor = new JPanel(new GridLayout(4,2));
    JPanel pnlFiltros = new JPanel(new GridLayout(1,4,10,0));
    
    ArrayList<JTextField> txtTotalesConceptos = new ArrayList<>();
    
    JLabel lblDolares = new JLabel("Dolares", SwingConstants.RIGHT);
    JLabel lblPesos = new JLabel("Pesos", SwingConstants.RIGHT);
    JLabel lblTotal = new JLabel("Total");
    JTextField txtTotalDolares = new JTextField();
    JTextField txtTotalPesos = new JTextField();
    JLabel lblPendiente = new JLabel("Importe Pendiente");
    JTextField txtPendienteDolares = new JTextField();
    JTextField txtPendientePesos = new JTextField();
    JLabel lblFacturado = new JLabel("Facturado");
    JTextField txtFacturadoDolares = new JTextField();
    JTextField txtFacturadoPesos = new JTextField();
    JLabel lblUtilidad = new JLabel("Utilidad");
    JTextField txtUtilidadDolares = new JTextField();
    JTextField txtUtilidadPesos = new JTextField();
    JLabel lblGM = new JLabel("GM Promedio");
    JTextField txtGM = new JTextField();
    
    JPanel pnlTotales = new JPanel(new GridLayout(3,6,50,0));
    JPanel pnlTotalesConcepto = new JPanel(new GridLayout(4,8,5,5));
    JPanel pnlSumatoria = new JPanel(new BorderLayout());

    Ventas(int idUser, String rol, WaitWindow w){
        super("Registro de Ventas");
        this.setSize(800,600);
    	this.setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);

        idUsuario = idUser;
        window = w;
        
        switch (rol) {
            case "admin":
                admin = true;
                for(int i = 0; i < filtrosConcepto.length - 1; i++){
                    pnlTotalesConcepto.add(new JLabel(filtrosConcepto[i], SwingConstants.RIGHT));
                    txtTotalesConceptos.add(new JTextField());
                    txtTotalesConceptos.get(i).setEditable(false);
                    txtTotalesConceptos.get(i).setHorizontalAlignment(SwingConstants.RIGHT);
                    pnlTotalesConcepto.add(txtTotalesConceptos.get(i));
                }   inicializarAdmin();
                break;
            case "administracion":
                inicializarAdministrador();
                break;
            case "ventas":
                inicializarVendedor();
                break;
            default:
                break;
        }
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e)
            {
                new Servidor().cerrarServidor();
            }
        });
        
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public final void inicializarAdmin(){
        crearTablas();
        buscarAnioFiltros();
        filtrosVendedor = cargarVendedores();
        filtrosCliente = cargarClientes();
        
        window.dispose();
        window.terminate();
        
        inicializarTotales();
        
        inicializarFiltros();
        
        pnlMostrar.add(pnlFiltros, BorderLayout.NORTH);
        pnlMostrar.add(pnlTablaMostrar, BorderLayout.CENTER);
        pnlMostrar.add(pnlSumatoria, BorderLayout.SOUTH);
        
        pnlBotones.add(btnNuevo, BorderLayout.WEST);
        pnlBotones.add(btnModificar, BorderLayout.EAST);
        pnlRegistrar.setLayout(new BorderLayout());
        pnlRegistrar.add(pnlTablaRegistrar, BorderLayout.CENTER);
        pnlRegistrar.add(pnlBotones, BorderLayout.SOUTH);

        principal.addTab("Registrar", pnlRegistrar);
        principal.addTab("Ver", pnlMostrar);

        this.setLayout(new BorderLayout());
        this.getContentPane().add(principal);

        btnNuevo.addActionListener(this);
        btnModificar.addActionListener(this);
        
    }
    
    public final void inicializarAdministrador(){
        crearTablas();
        buscarAnioFiltros();
        filtrosVendedor = cargarVendedores();
        filtrosCliente = cargarClientes();
        
        window.dispose();
        window.terminate();
        
        pnlBotones.add(btnNuevo, BorderLayout.WEST);
        pnlBotones.add(btnModificar, BorderLayout.EAST);
        pnlRegistrar.setLayout(new BorderLayout());
        pnlRegistrar.add(pnlTablaRegistrar, BorderLayout.CENTER);
        pnlRegistrar.add(pnlBotones, BorderLayout.SOUTH);
        
        inicializarFiltros();
        
        pnlMostrar.add(pnlFiltros, BorderLayout.NORTH);
        pnlMostrar.add(pnlTablaMostrar, BorderLayout.CENTER);

        principal.addTab("Registrar", pnlRegistrar);
        principal.addTab("Ver", pnlMostrar);
        
        this.setLayout(new BorderLayout());
        this.getContentPane().add(principal);

        btnNuevo.addActionListener(this);
        btnModificar.addActionListener(this);
    }
    
    public final void inicializarVendedor(){
        crearTablas();
        buscarAnioFiltros();
        filtrosVendedor = cargarVendedores();
        filtrosCliente = cargarClientes();
        
        window.dispose();
        window.terminate();
        
        inicializarFiltros();
        
        pnlMostrar.add(pnlFiltros, BorderLayout.NORTH);
        pnlMostrar.add(pnlTablaMostrar, BorderLayout.CENTER);
        
        principal.addTab("Ver", pnlMostrar);

        this.setLayout(new BorderLayout());
        this.getContentPane().add(principal);
    }
    
    public void inicializarTotales(){
        
        pnlTotales.add(new JLabel(""));
        pnlTotales.add(lblTotal);
        pnlTotales.add(lblPendiente);
        pnlTotales.add(lblFacturado);
        pnlTotales.add(lblUtilidad);
        pnlTotales.add(lblGM);
        pnlTotales.add(lblDolares);
        pnlTotales.add(txtTotalDolares);
        pnlTotales.add(txtPendienteDolares);
        pnlTotales.add(txtFacturadoDolares);
        pnlTotales.add(txtUtilidadDolares);
        pnlTotales.add(txtGM);
        pnlTotales.add(lblPesos);
        pnlTotales.add(txtTotalPesos);
        pnlTotales.add(txtPendientePesos);
        pnlTotales.add(txtFacturadoPesos);
        pnlTotales.add(txtUtilidadPesos);
        
        txtTotalDolares.setEditable(false);
        txtTotalPesos.setEditable(false);
        txtPendienteDolares.setEditable(false);
        txtPendientePesos.setEditable(false);
        txtFacturadoDolares.setEditable(false);
        txtFacturadoPesos.setEditable(false);
        txtUtilidadDolares.setEditable(false);
        txtUtilidadPesos.setEditable(false);
        txtGM.setEditable(false);
        
        txtTotalDolares.setHorizontalAlignment(SwingConstants.RIGHT);
        txtTotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
        txtPendienteDolares.setHorizontalAlignment(SwingConstants.RIGHT);
        txtPendientePesos.setHorizontalAlignment(SwingConstants.RIGHT);
        txtFacturadoDolares.setHorizontalAlignment(SwingConstants.RIGHT);
        txtFacturadoPesos.setHorizontalAlignment(SwingConstants.RIGHT);
        txtUtilidadDolares.setHorizontalAlignment(SwingConstants.RIGHT);
        txtUtilidadPesos.setHorizontalAlignment(SwingConstants.RIGHT);
        txtGM.setHorizontalAlignment(SwingConstants.RIGHT);
        
        pnlTotales.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        pnlTotalesConcepto.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        
        pnlSumatoria.add(pnlTotalesConcepto, BorderLayout.SOUTH);
        pnlSumatoria.add(pnlTotales, BorderLayout.NORTH);
        pnlSumatoria.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().width-20, 170/*(int) Toolkit.getDefaultToolkit().getScreenSize().height/5*/));
    }
    
    public void inicializarFiltros(){
        for(int i = 0; i < filtrosMeses.length; i++){
            chkMeses.add(new JCheckBox(filtrosMeses[i]));
            chkMeses.get(i).addActionListener(this);
            chkMeses.get(i).setSelected(true);
            pnlMeses.add(chkMeses.get(i));
        }
        filtrosAnio.add(0, "Todos");
        modeloAnio = new DefaultComboBoxModel(filtrosAnio.toArray());
        filtrosCliente.add(0, "Todos");
        modeloCliente = new DefaultComboBoxModel(filtrosCliente.toArray());
        cmbAnio = new JComboBox(modeloAnio);
        cmbCliente = new JComboBox(modeloCliente);
        cmbStatus = new JComboBox(modeloStatus);
        
        pnlComboBox.add(new JLabel("Año", SwingConstants.LEFT));
        pnlComboBox.add(cmbAnio);
        pnlComboBox.add(new JLabel("Status", SwingConstants.LEFT));
        pnlComboBox.add(cmbStatus);
        pnlComboBox.add(new JLabel("Cliente", SwingConstants.LEFT));
        pnlComboBox.add(cmbCliente);
        
        for(int i = 0; i < filtrosConcepto.length; i++){
            String con = filtrosConcepto[i];
            for(int j = (con.length()/2) - 1; j < con.length(); j++){
                if(con.charAt(j) == ' ' || con.charAt(j) == '-'){
                    con = "<html>" + con.substring(0,j) + "<br>" + con.substring(j+1, con.length()) + "</html>";
                    break;
                }
            }
            chkConceptos.add(new JCheckBox(con));
            chkConceptos.get(i).addActionListener(this);
            chkConceptos.get(i).setSelected(true);
            pnlConceptos.add(chkConceptos.get(i));
        }
        
        filtrosVendedor.add("Todos");
        for(int i = 0; i < filtrosVendedor.size(); i++){
            chkVendedores.add(new JCheckBox("<html>" + filtrosVendedor.get(i).replaceAll(" ", "<br>") + "</html>"));
            chkVendedores.get(i).addActionListener(this);
            chkVendedores.get(i).setSelected(true);
            pnlVendedor.add(chkVendedores.get(i));
        }
        
        pnlMeses.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(3, 20, 3, 20)));
        pnlConceptos.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(3, 20, 3, 20)));
        pnlComboBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(3, 20, 3, 20)));
        pnlVendedor.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(3, 20, 3, 20)));
        
        pnlFiltros.add(pnlMeses);
        pnlFiltros.add(pnlComboBox);
        pnlFiltros.add(pnlVendedor);
        pnlFiltros.add(new JScrollPane(pnlConceptos));
        
        cmbAnio.addActionListener(this);
        cmbStatus.addActionListener(this);
        cmbCliente.addActionListener(this);
        
        pnlFiltros.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 30, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/5));
        cmbAnio.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().width/8, 30));
        cmbCliente.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().width/8, 20));
        cmbStatus.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().width/8, 10));
    }
    
    public void crearTablas(){
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        if(admin){
            tblTablaRegistrar = CargarDatos(0);
        }
        else{
            tblTablaRegistrar = CargarDatos(idUsuario);
        }
        pnlTablaRegistrar = new JScrollPane(tblTablaRegistrar);
        tblTablaRegistrar.getColumnModel().getColumn(18).setCellRenderer(rightRenderer);
        tblTablaRegistrar.getColumnModel().getColumn(19).setCellRenderer(rightRenderer);
        tblTablaRegistrar.getColumnModel().getColumn(20).setCellRenderer(rightRenderer);
        tblTablaRegistrar.getColumnModel().getColumn(22).setCellRenderer(rightRenderer);
        tblTablaRegistrar.getColumnModel().getColumn(23).setCellRenderer(rightRenderer);
        tblTablaRegistrar.setFillsViewportHeight(true);
        tblTablaRegistrar.setDefaultEditor(Object.class, null);
        tblTablaRegistrar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tcaRegistrar = new TableColumnAdjuster(tblTablaRegistrar);
        tcaRegistrar.adjustColumns();
        tblTablaRegistrar.setAutoCreateRowSorter(true);
        tblTablaRegistrar.getTableHeader().setReorderingAllowed(false);
        tblTablaRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirArchivo(tblTablaRegistrar);
            }
        });
        
        tblTablaMostrar = CargarDatos(0);
        pnlTablaMostrar = new JScrollPane(tblTablaMostrar);
        tblTablaMostrar.getColumnModel().getColumn(18).setCellRenderer(rightRenderer);
        tblTablaMostrar.getColumnModel().getColumn(19).setCellRenderer(rightRenderer);
        tblTablaMostrar.getColumnModel().getColumn(20).setCellRenderer(rightRenderer);
        tblTablaMostrar.getColumnModel().getColumn(22).setCellRenderer(rightRenderer);
        tblTablaMostrar.getColumnModel().getColumn(23).setCellRenderer(rightRenderer);
        tblTablaMostrar.setFillsViewportHeight(true);
        tblTablaMostrar.setDefaultEditor(Object.class, null);
        tblTablaMostrar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tcaMostrar = new TableColumnAdjuster(tblTablaMostrar);
        tcaMostrar.adjustColumns();
        tblTablaMostrar.setAutoCreateRowSorter(true);
        tblTablaMostrar.getTableHeader().setReorderingAllowed(false);
        tblTablaMostrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirArchivo(tblTablaMostrar);
            }
        });
        calcularTotales();
    }

    public void crearTablas(JTable tabla){
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlTablaMostrar = new JScrollPane(tabla);
        tblTablaMostrar.getColumnModel().getColumn(18).setCellRenderer(rightRenderer);
        tblTablaMostrar.getColumnModel().getColumn(19).setCellRenderer(rightRenderer);
        tblTablaMostrar.getColumnModel().getColumn(20).setCellRenderer(rightRenderer);
        tblTablaMostrar.getColumnModel().getColumn(22).setCellRenderer(rightRenderer);
        tblTablaMostrar.getColumnModel().getColumn(23).setCellRenderer(rightRenderer);
        tblTablaMostrar.setFillsViewportHeight(true);
        tblTablaMostrar.setDefaultEditor(Object.class, null);
        tblTablaMostrar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tcaMostrar = new TableColumnAdjuster(tblTablaMostrar);
        tcaMostrar.adjustColumns();
        tblTablaMostrar.setAutoCreateRowSorter(true);
        tblTablaMostrar.getTableHeader().setReorderingAllowed(false);
        tblTablaMostrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirArchivo(tblTablaMostrar);
            }
        });
        calcularTotales();
    }
    
    public void calcularTotales(){
        //Importe = 18
        //Importe Pendiente = 19
        //Importe Facturado = 20
        //Tipo Moneda = 21
        //Tasa de Cambio = 22
        //GM = 23
        calcularSumatoria();
        DecimalFormat df = new DecimalFormat("#,###.00");
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        double total, pendiente, facturado, totalPesos, pendientePesos, facturadoPesos, utilidadDolares, utilidadPesos, GMProm;
        total = pendiente = facturado = totalPesos = pendientePesos = facturadoPesos = utilidadDolares = utilidadPesos = GMProm = 0;
        for(int i = 0; i < tblTablaMostrar.getRowCount(); i++){
            if(tblTablaMostrar.getValueAt(i, 21).equals("Pesos")){
                total += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18))) / Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)));
                pendiente += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 19))) / Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)));
                facturado += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20))) / Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)));
                
                totalPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18)));
                pendientePesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 19)));
                facturadoPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)));
                
                utilidadDolares += (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18))) / Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)))) * (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 23)))/100);
                utilidadPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18))) * (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 23)))/100);
            }
            else{
                total += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18)));
                pendiente += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 19)));
                facturado += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)));
                
                totalPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18))) * Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)));
                pendientePesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 19))) * Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)));
                facturadoPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20))) * Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)));
                
                utilidadPesos += (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18))) * Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)))) * (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 23)))/100);
                utilidadDolares += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18))) * (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 23)))/100);
            }
            GMProm += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 23)));
        }
        GMProm /= tblTablaMostrar.getRowCount();
        
        txtTotalDolares.setText("$ " + df.format(total));
        txtPendienteDolares.setText("$ " + df.format(pendiente));
        txtFacturadoDolares.setText("$ " + df.format(facturado));
        txtUtilidadDolares.setText("$ " + df.format(utilidadDolares));
        txtTotalPesos.setText("$ " + df.format(totalPesos));
        txtPendientePesos.setText("$ " + df.format(pendientePesos));
        txtFacturadoPesos.setText("$ " + df.format(facturadoPesos));
        txtUtilidadPesos.setText("$ " + df.format(utilidadPesos));
        txtGM.setText(df.format(GMProm) + "%");
    }
    
    public void calcularSumatoria(){
        //Importe = 18
        //Importe Pendiente = 19
        //Importe Facturado = 20
        //Tipo Moneda = 21
        //Tasa de Cambio = 22
        //GM = 23
        DecimalFormat df = new DecimalFormat("#,###.00");
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        Float[] sum = new Float[filtrosConcepto.length - 1];
        float importe;
        float aux = 0;
        for(int i = 0; i < filtrosConcepto.length - 1; i++){
            sum[i] = aux;
        }
        try{
            ArrayList<Float> lista;
            for(int i = 0; i < tblTablaMostrar.getRowCount(); i++){
                lista = hstDetalleVenta.get(Integer.valueOf(tblTablaMostrar.getValueAt(i, 0).toString()));
                if(lista == null) break;
                importe = lista.get(0);
                for(int j = 1; j < lista.size(); j++){
                    if(tblTablaMostrar.getValueAt(i, 21).equals("Pesos")){
                        aux = Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 22)));
                        sum[j-1] += ((importe/aux) * (lista.get(j)/100));
                    }
                    else{
                        sum[j-1] += (importe * (lista.get(j)/100));
                    }
                }
            }
        }
        catch(NumberFormatException ex){
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < txtTotalesConceptos.size(); i++){
            txtTotalesConceptos.get(i).setText("$ " + df.format(sum[i]));
        }
    }
    
    public String crearDetalle(int folio){
        DecimalFormat df = new DecimalFormat("#,###.00");
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        String detalle;
        float aux, importe;
        ArrayList<Float> lista = hstDetalleVenta.get(folio);
        detalle = "";
        importe = lista.get(0);
        aux = lista.get(1);
        if(aux > 0) detalle += "Automatización:\t\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(2);
        if(aux > 0) detalle += "Control de Accesos:\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(3);
        if(aux > 0) detalle += "CCTV:\t\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(4);
        if(aux > 0) detalle += "Detección de Incendios:\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(5);
        if(aux > 0) detalle += "Sistemas de Seguridad:\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(6);
        if(aux > 0) detalle += "Paciente-Enfermera:\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(7);
        if(aux > 0) detalle += "Supresión de Incendios:\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(8);
        if(aux > 0) detalle += "Viaticos:\t\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(9);
        if(aux > 0) detalle += "Canalización y cableado:\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(10);
        if(aux > 0) detalle += "Puesta en marcha:\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(11);
        if(aux > 0) detalle += "Mano de Obra:\t\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(12);
        if(aux > 0) detalle += "Poliza:\t\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(13);
        if(aux > 0) detalle += "Refacciones:\t\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(14);
        if(aux > 0) detalle += "Reparaciones:\t\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        aux = lista.get(15);
        if(aux > 0) detalle += "Varios:\t\t$" + df.format(importe*(aux/100)) + "\t" + aux + "%\n";
        return detalle;
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
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
            if(s.reiniciarServidor()){
                c = ConectarDB();
            }
        }
        return c;
    }
    
    public void buscarAnioFiltros(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select extract(year from min(fechaautorizacion)) from ventas";
                ResultSet consulta = comando.executeQuery(sql);
                if(consulta.next()){
                    minYear = consulta.getInt("date_part");
                }
                int max = 0; 
                sql = "select extract(year from age(max(fechaautorizacion),min(fechaautorizacion))) from ventas";
                consulta = comando.executeQuery(sql);
                if(consulta.next()){
                    max = consulta.getInt("date_part") + 1;
                }
                filtrosAnio = new ArrayList<>();
                for(int i = minYear; i <= minYear + max; i++){
                    filtrosAnio.add(Integer.toString(i));
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public JTable CargarDatos(int usuario){
        Connection c = ConectarDB();
        Statement comando;

        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
            return new JTable(null);
        }
        else{
            try {
                int x = 0;
                comando = c.createStatement();
                String sql;
                if(usuario != 0){
                    sql = "select count(1) from ventas where capturo=" + usuario;
                }
                else{
                    sql = "select count(1) from ventas";
                }
                ResultSet consulta = comando.executeQuery(sql);
                if(consulta.next()){
                    x = consulta.getInt("count");
                }
                if(x == 0){
                    if(usuario == 0){
                        JOptionPane.showMessageDialog(null, "No hay registros en la base de datos");
                    }
                    return new JTable(null);
                }
                sql = "select ventas.*,\n" +
                        "    (select nombrecomercial from clientes where clientes.idcliente=ventas.cliente),\n" +
                        "    (select razonsocial from clientes where clientes.idcliente=ventas.cliente),\n" +
                        "    (select string_agg(folioservicio,',') from foliosservicio where foliosservicio.folioventa=ventas.folioventa group by foliosservicio.folioventa) as folioservicio,\n" +
                        "    (select string_agg(factura,',') from facturas where facturas.folioventa=ventas.folioventa group by facturas.folioventa) as factura,\n" +
                        "    (select string_agg(fecha::character varying,',') from facturas where facturas.folioventa=ventas.folioventa group by facturas.folioventa) as fechafactura,\n" +
                        "    (select string_agg(ordencompra,',') from ordenescompraginsatec where ordenescompraginsatec.folioventa=ventas.folioventa group by ordenescompraginsatec.folioventa) as ordencompraginsatec,\n" +
                        "    (select string_agg(fecha::character varying,',') from ordenescompraginsatec where ordenescompraginsatec.folioventa=ventas.folioventa group by ordenescompraginsatec.folioventa) as fechaordenginsatec,\n" +
                        "    (select string_agg(cotizacion,',') from cotizaciones where cotizaciones.folioventa=ventas.folioventa group by cotizaciones.folioventa) as cotizacion,\n" +
                        "    (select string_agg(ordencompra,',') from ordenescompra where ordenescompra.folioventa=ventas.folioventa group by ordenescompra.folioventa) as ordencompra,\n" +
                        "    (select usuario from usuarios where idusuario=ventas.capturo) as autor\n" +
                        "from ventas";
                if(usuario != 0){
                    sql += " where capturo=" + usuario;
                }
                else{
                    sql += " where capturo is not null";
                }
                consulta = comando.executeQuery(sql);
                int i = 0;

                ArrayList<Filas> lista = new ArrayList<>();
                ArrayList<Object> fila;
                while(consulta.next()){
                    fila = new ArrayList<>();
                    int folioVenta = consulta.getInt("folioventa");
                    fila.add(folioVenta);
                    fila.add(consulta.getDate("fechaautorizacion"));
                    fila.add(consulta.getString("nombrecomercial"));
                    fila.add(consulta.getString("razonsocial"));
                    cargarDetalleVenta();
                    fila.add("Detalle");
                    fila.add(consulta.getDate("fechaentrega"));
                    fila.add(consulta.getDate("fecharecibido"));
                    String MaterialRecibido;
                    if(consulta.getBoolean("materialrecibido")){
                        MaterialRecibido = "Si";
                    }
                    else{
                        MaterialRecibido = "No";
                    }
                    fila.add(MaterialRecibido);
                    String RequiereInstalacion;
                    if(consulta.getBoolean("requiereinstalacion")){
                        RequiereInstalacion = "Si";
                    }
                    else{
                        RequiereInstalacion = "No";
                    }
                    fila.add(RequiereInstalacion);
                    String RequiereCanalizacion;
                    if(consulta.getBoolean("requierecanalizacion")){
                        RequiereCanalizacion = "Si";
                    }
                    else{
                        RequiereCanalizacion = "No";
                    }
                    fila.add(RequiereCanalizacion);
                    fila.add(consulta.getString("vendedor"));
                    fila.add(consulta.getFloat("importe"));
                    fila.add(consulta.getFloat("importependiente"));
                    fila.add(consulta.getFloat("importefacturado"));
                    fila.add(consulta.getString("tipomoneda"));
                    fila.add(consulta.getFloat("tasacambio"));
                    fila.add(consulta.getFloat("gm"));
                    fila.add(consulta.getInt("diascredito"));
                    fila.add(consulta.getDate("fechavencimientopago"));
                    fila.add(consulta.getString("status"));
                    fila.add(consulta.getString("notas"));
                    consulta.getString("capturo");
                    String FolioServicio = consulta.getString("folioservicio");
                    if(FolioServicio == null) FolioServicio = "";
                    fila.add(FolioServicio);
                    String Factura = consulta.getString("factura");
                    if(Factura == null) Factura = "";
                    fila.add(Factura);
                    String FechaFactura = consulta.getString("fechafactura");
                    if(FechaFactura == null) FechaFactura = "";
                    fila.add(FechaFactura);
                    String OrdenCompraGINSATEC = consulta.getString("ordencompraginsatec");
                    if(OrdenCompraGINSATEC == null) OrdenCompraGINSATEC = "";
                    fila.add(OrdenCompraGINSATEC);
                    String FechaOrdenGINSATEC = consulta.getString("fechaordenginsatec");
                    if(FechaOrdenGINSATEC == null) FechaOrdenGINSATEC = "";
                    fila.add(FechaOrdenGINSATEC);
                    String Cotizacion = consulta.getString("cotizacion");
                    if(Cotizacion == null) Cotizacion = "";
                    fila.add(Cotizacion);
                    String OrdenCompra = consulta.getString("ordencompra");
                    if(OrdenCompra == null) OrdenCompra = "";
                    fila.add(OrdenCompra);
                    if(usuario == 0){
                        fila.add(consulta.getString("autor"));
                    }
                    else{
                        fila.add(null);
                    }
                    lista.add(new Filas(fila));
                }
                comando.close();
                c.close();
                if(usuario != 0){
                    modeloRegistrar = new SortTable(columnasRegistrar, lista);
                    return new JTable(modeloRegistrar);
                }
                else{
                    modeloMostrar = new SortTable(columnasMostrar, lista);
                    return new JTable(modeloMostrar);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
                if(usuario != 0){
                    return new JTable(null);
                }
                else{
                    return new JTable(null);
                }
            }
        }
    }
    
    public ArrayList<String> cargarVendedores(){
        ArrayList<String> vendedores = new ArrayList<>();
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
                Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return vendedores;
    }
    
    public ArrayList<String> cargarClientes(){
        ArrayList<String> clientes = new ArrayList<>();
        Set setClientes = new HashSet();
        Connection c = ConectarDB();
        Statement comando;

        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try{
                comando = c.createStatement();
                String sql = "select nombrecomercial from clientes order by nombrecomercial";
                ResultSet consulta = comando.executeQuery(sql);
                while(consulta.next()){
                    setClientes.add(consulta.getString("nombrecomercial"));
                }
                clientes.addAll(new TreeSet(setClientes));
                comando.close();
                c.close();
            }
            catch(SQLException ex){
                Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return clientes;
    }
    
    public void cargarDetalleVenta(){
        Connection c = ConectarDB();
        Statement comando;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try{
                comando = c.createStatement();
                String sql = "select ventas.importe, detalleventa.* from ventas, detalleventa where ventas.folioventa=detalleventa.folioventa";
                ResultSet consulta = comando.executeQuery(sql);
                int folio;
                ArrayList<Float> lista;
                while(consulta.next()){
                    lista = new ArrayList<>();
                    folio = consulta.getInt("folioventa");
                    lista.add(consulta.getFloat("importe"));
                    lista.add(consulta.getFloat("automatizacion"));
                    lista.add(consulta.getFloat("controlaccesos"));
                    lista.add(consulta.getFloat("cctv"));
                    lista.add(consulta.getFloat("deteccionincendios"));
                    lista.add(consulta.getFloat("sistemasseguridad"));
                    lista.add(consulta.getFloat("pacienteenfermera"));
                    lista.add(consulta.getFloat("supresionincendios"));
                    lista.add(consulta.getFloat("viaticos"));
                    lista.add(consulta.getFloat("canalizacionescableados"));
                    lista.add(consulta.getFloat("programacionpuestaenmarcha"));
                    lista.add(consulta.getFloat("manoobra"));
                    lista.add(consulta.getFloat("poliza"));
                    lista.add(consulta.getFloat("refacciones"));
                    lista.add(consulta.getFloat("reparaciones"));
                    lista.add(consulta.getFloat("varios"));
                    hstDetalleVenta.put(folio, lista);
                }
                comando.close();
                c.close();
            }
            catch(SQLException ex){
                Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void actualizarTabla(JTable tabla){
        pnlMostrar.remove(pnlTablaMostrar);
        crearTablas(tabla);
        pnlMostrar.add(pnlTablaMostrar, BorderLayout.CENTER);
        pnlMostrar.validate();
        pnlMostrar.repaint();
    }
    
    public void abrirArchivo(JTable tabla){
        int x = tabla.getSelectedRow(), y = tabla.getSelectedColumn();
        if( y == 2 || 
            y == 3 || 
            y == 4 ||
            y == 9 ||
            y == 16 ){
            Connection c = ConectarDB();
            Statement comando;
            if( c == null ){
                JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
            }
            else{
                try {
                    int folio = Integer.parseInt(String.valueOf(tabla.getValueAt(x, 0)));
                    comando = c.createStatement();
                    String sql = "";
                    switch (y) {
                        case 2:
                            sql = "select ruta from ordenescompra where folioventa=" + folio + " and ruta is not null";
                            break;
                        case 3:
                            sql = "select ruta from cotizaciones where folioventa=" + folio + " and ruta is not null";
                            break;
                        case 4:
                            sql = "select ruta from foliosservicio where folioventa=" + folio + " and ruta is not null";
                            break;
                        case 9:
                            sql = "select ruta from ordenescompraginsatec where folioventa=" + folio + " and ruta is not null";
                            break;
                        case 16:
                            sql = "select ruta from facturas where folioventa=" + folio + " and ruta is not null";
                            break;
                        default:
                            break;
                    }
                    String path;
                    ResultSet consulta = comando.executeQuery(sql);
                    while(consulta.next()){
                        path = consulta.getString("ruta");
                        if ((new File(path)).exists()) {
                            Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + path);
                            p.waitFor();
                        } else {
                            JOptionPane.showMessageDialog(null, "La ruta al archivo a cambiado o el archivo no existe");
                        }
                    }
                    comando.close();
                    c.close();
                } catch (SQLException | InterruptedException | IOException ex) {
                    Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else if(y == 7){
            JTextArea area = new JTextArea(crearDetalle(Integer.parseInt(String.valueOf(tabla.getValueAt(x, 0)))));
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, area, "Detalle de Venta", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void actualizarFiltro(){
        buscarAnioFiltros();
        modeloAnio = new DefaultComboBoxModel(filtrosAnio.toArray());
        cmbAnio.setModel(modeloAnio);
        modeloCliente = new DefaultComboBoxModel(filtrosCliente.toArray());
        cmbCliente.setModel(modeloCliente);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object accion = ae.getSource();
        if(accion == btnNuevo){
            new Thread(new Capturar(this)).start();
        }
        else if(accion == btnModificar){
            if(!tblTablaRegistrar.getSelectionModel().isSelectionEmpty()){
                new Thread(new Capturar(this, (int) tblTablaRegistrar.getValueAt(tblTablaRegistrar.getSelectedRow(), 0))).start();
            }
        }
        else if(    chkMeses.contains((JCheckBox) accion) || 
                    chkVendedores.contains((JCheckBox) accion) || 
                    chkConceptos.contains((JCheckBox) accion) ||
                    accion == cmbAnio ||
                    accion == cmbStatus ||
                    accion == cmbCliente){
            
            if(accion == chkMeses.get(12)){
                if(chkMeses.get(12).isSelected()){
                    chkMeses.forEach((JCheckBox mes) -> {
                        mes.setSelected(true);
                    });
                }
                else{
                    chkMeses.forEach((JCheckBox mes) -> {
                        mes.setSelected(false);
                    });
                }
            }
            else if(accion == chkVendedores.get(6)){
                if(chkVendedores.get(6).isSelected()){
                    chkVendedores.forEach((JCheckBox vendedor) -> {
                        vendedor.setSelected(true);
                    });
                }
                else{
                    chkVendedores.forEach((JCheckBox vendedor) -> {
                        vendedor.setSelected(false);
                    });
                }
            }
            else if(accion == chkConceptos.get(15)){
                if(chkConceptos.get(15).isSelected()){
                    chkConceptos.forEach((JCheckBox concepto) -> {
                        concepto.setSelected(true);
                    });
                }
                else{
                    chkConceptos.forEach((JCheckBox concepto) -> {
                        concepto.setSelected(false);
                    });
                }
            }
            
            System.out.println("Filtrar");
            ArrayList<String> mes = new ArrayList<>();
            for(int i = 0; i < filtrosMeses.length - 1; i++){
                if(chkMeses.get(i).isSelected()){
                    mes.add(filtrosMeses[i]);
                }
            }
            ArrayList<Integer> concepto = new ArrayList<>();
            for(int i = 0; i < filtrosConcepto.length - 1; i++){
                if(chkConceptos.get(i).isSelected()){
                    concepto.add(i);
                }
            }
            ArrayList<String> vendedor = new ArrayList<>();
            for(int i = 0; i < filtrosVendedor.size() - 1; i++){
                if(chkVendedores.get(i).isSelected()){
                    vendedor.add(filtrosVendedor.get(i));
                }
            }
            System.out.println("Size: " + modeloMostrar.datos.size());
            tblTablaMostrar = new JTable(modeloMostrar.Filtrar(mes, concepto, hstDetalleVenta, String.valueOf(cmbAnio.getSelectedItem()), String.valueOf(cmbStatus.getSelectedItem()), String.valueOf(cmbCliente.getSelectedItem()), vendedor));
            actualizarTabla(tblTablaMostrar);
        }
    }

    @Override
    public void run() {
        while(true){
            /*Nothing*/
        }
    }

}
