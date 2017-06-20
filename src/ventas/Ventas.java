/*
*Autor: Adolfo Michel
*Clase principal
 */
package ventas;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.TableModel;

public class Ventas extends JFrame implements ActionListener, Runnable{

    int idUsuario;
    
    public SortTable modeloMostrar = null;
    public SortTable modeloRegistrar = null;
    
    TableColumnAdjuster tcaMostrar;
    TableColumnAdjuster tcaRegistrar;
    
    String[] columnasRegistrar = {"ID", "Fecha Autorización", "Orden de Compra", "Cotización", "Folio Servicio",
        "Cliente", "Fecha Orden GINSATEC", "Orden de compra GINSATEC", "Fecha Aproximada Entrega",
        "Fecha Recibido", "Material Recibido", "Requiere Instalación", "Requiere Canalización",
        "Fecha Factura", "Factura", "Vendedor", "Importe", "ImportePendiente", "ImporteFacturado", "Tipo Moneda", 
        "Tasa de cambio", "GM", "Días de Credito", "Fecha Vencimiento Pago", "Status", "Notas"};
    
    String[] columnasMostrar = {"ID", "Fecha Autorización", "Orden de Compra", "Cotización", "Folio Servicio",
        "Cliente", "Fecha Orden GINSATEC", "Orden de compra GINSATEC", "Fecha Aproximada Entrega",
        "Fecha Recibido", "Material Recibido", "Requiere Instalación", "Requiere Canalización",
        "Fecha Factura", "Factura", "Vendedor", "Importe", "ImportePendiente", "ImporteFacturado", "Tipo Moneda", 
        "Tasa de cambio", "GM", "Días de Credito", "Fecha Vencimiento Pago", "Status", "Notas", "Capturó"};
    
    int minYear = 0;
    WaitWindow window;
    
    String[] filtros = {"Sin Filtro", "Mes", "Trimestre", "Año", "Vendedor-Mensual", "Vendedor-Trimestral", "Vendedor-Anual", "Vendedor", "Cliente-Mensual", "Cliente-Trimestral", "Cliente-Anual", "Cliente", "Status"};
    String[] filtrosMes = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    String[] filtrosTrimestre = {"Enero-Marzo", "Abril-Junio", "Julio-Septiembre", "Octubre-Diciembre"};
    String[] filtrosStatus = {"Pendiente", "Activa", "Pagada", "No pagada", "Cancelada"};
    Vector<String> filtrosVendedor; // = {"Mario Gonzalez", "Marco Padilla", "Daniel Martinez", "Rosario Arellano", "Alberto Lomeli", "Mariano Ruiz"};
    Vector<String> filtrosAnio;
    Vector<String> filtrosCliente;
    
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
    
    DefaultComboBoxModel modeloFiltros = new DefaultComboBoxModel(filtros);
    DefaultComboBoxModel modeloMes = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloAnio = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloExtra = new DefaultComboBoxModel();
    
    JLabel lblFiltros = new JLabel("Filtros:");
    JComboBox cmbFiltros = new JComboBox(modeloFiltros);
    JLabel lblOpcionesFiltros = new JLabel("Opciones:");
    JComboBox cmbOpcionesMes = new JComboBox(modeloMes);
    JComboBox cmbOpcionesAnio = new JComboBox(modeloAnio);
    JComboBox cmbOpcionesExtra = new JComboBox(modeloExtra);
    JPanel pnlFiltros = new JPanel(new GridLayout(1,5,50,0));
    
    JLabel lblDolares = new JLabel("Dolares");
    JLabel lblPesos = new JLabel("Pesos");
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

    Ventas(int idUser, WaitWindow w){
        super("Registro de Ventas");
        this.setSize(800,600);
    	this.setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);

        idUsuario = idUser;
        window = w;
        crearTablas();
        buscarAnioFiltros();
        filtrosVendedor = cargarVendedores();
        filtrosCliente = cargarClientes();
        
        window.dispose();
        window.terminate();
        
        pnlFiltros.add(lblFiltros);
        pnlFiltros.add(cmbFiltros);
        pnlFiltros.add(lblOpcionesFiltros);
        pnlFiltros.add(cmbOpcionesMes);
        pnlFiltros.add(cmbOpcionesAnio);
        pnlFiltros.add(cmbOpcionesExtra);

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
        if(tblTablaMostrar.getRowCount() == 0){
            cmbFiltros.setEnabled(false);
        }
        cmbOpcionesMes.setVisible(false);
        cmbOpcionesAnio.setVisible(false);
        cmbOpcionesExtra.setVisible(false);
        
        pnlMostrar.add(pnlFiltros, BorderLayout.NORTH);
        pnlMostrar.add(pnlTablaMostrar, BorderLayout.CENTER);
        pnlMostrar.add(pnlTotales, BorderLayout.SOUTH);
        
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
        cmbFiltros.addActionListener(this);
        cmbOpcionesMes.addActionListener(this);
        cmbOpcionesAnio.addActionListener(this);
        cmbOpcionesExtra.addActionListener(this);
        
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                new Servidor().cerrarServidor();
            }
        });
        
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void crearTablas(){
        tblTablaRegistrar = CargarDatos(idUsuario);
        pnlTablaRegistrar = new JScrollPane(tblTablaRegistrar);
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
        pnlTablaMostrar = new JScrollPane(tabla);
        tblTablaMostrar.setFillsViewportHeight(true);
        tblTablaMostrar.setDefaultEditor(Object.class, null);
        tblTablaMostrar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnAdjuster tcaMostrar = new TableColumnAdjuster(tblTablaMostrar);
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
        DecimalFormat df = new DecimalFormat("#.00");
        double total, pendiente, facturado, totalPesos, pendientePesos, facturadoPesos, utilidadDolares, utilidadPesos, GMProm;
        total = pendiente = facturado = totalPesos = pendientePesos = facturadoPesos = utilidadDolares = utilidadPesos = GMProm = 0;
        for(int i = 0; i < tblTablaMostrar.getRowCount(); i++){
            if(tblTablaMostrar.getValueAt(i, 19).equals("Pesos")){
                total += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 16))) / Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)));
                pendiente += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 17))) / Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)));
                facturado += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18))) / Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)));
                
                totalPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 16)));
                pendientePesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 17)));
                facturadoPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18)));
                
                utilidadDolares += (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 16))) / Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)))) * (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 21)))/100);
                utilidadPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 16))) * (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 21)))/100);
            }
            else{
                total += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 16)));
                pendiente += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 17)));
                facturado += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18)));
                
                totalPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 16))) * Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)));
                pendientePesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 17))) * Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)));
                facturadoPesos += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 18))) * Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)));
                
                utilidadPesos += (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 16))) * Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 20)))) * (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 21)))/100);
                utilidadDolares += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 16))) * (Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 21)))/100);
            }
            GMProm += Float.parseFloat(String.valueOf(tblTablaMostrar.getValueAt(i, 21)));
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
    
    public Connection ConectarDB(){
        //new Servidor().arrancarServidor();
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver"); //jdbc:postgresql://localhost:5432/sistemabasedatos
            //c = DriverManager.getConnection("jdbc:postgresql://localhost:5344/SistemaBaseDatos", "Sersitec-Laboratorio", "");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:3389/VentasDB", "usuario", "Sersitec886");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return c;
    }
    
    public void buscarAnioFiltros(){
        Connection c = ConectarDB();
        Statement comando = null;
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
                filtrosAnio = new Vector<String>();
                for(int i = minYear; i <= minYear + max; i++){
                    filtrosAnio.add(Integer.toString(i));
                }
                comando.close();
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public JTable CargarDatos(int usuario){
        Connection c = ConectarDB();
        Statement comando = null;

        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
            return new JTable(null);
        }
        else{
            try {
                int x;
                comando = c.createStatement();
                String sql = "";
                if(usuario != 0){
                    sql = "select count(1) from ventas where capturo=" + usuario;
                }
                else{
                    sql = "select count(1) from ventas";
                }
                ResultSet consulta = comando.executeQuery(sql);
                if(consulta.next()){
                    x = consulta.getInt("count");
                    if(x == 0){
                        if(usuario != 0){
                            //JOptionPane.showMessageDialog(null, "No hay registros en este usuario");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "No hay registros en la base de datos");
                        }
                        return new JTable(null);
                    }
                }
                else{
                    if(usuario != 0){
                        return new JTable(null, columnasRegistrar);
                    }
                    else{
                        return new JTable(null, columnasMostrar);
                    }
                }
                sql = "select ventas.*,\n" +
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
                
                //Prueba
                ArrayList<Filas> lista = new ArrayList<Filas>();
                ArrayList<Object> fila;
                while(consulta.next()){
                    fila = new ArrayList<Object>();
                    int folioVenta = consulta.getInt("folioventa");
                    fila.add(folioVenta);
                    //java.sql.Date FechaAutorizacion = consulta.getDate("fechaautorizacion");
                    fila.add(consulta.getDate("fechaautorizacion"));
                    //String NombreCliente = consulta.getString("nombrecliente");
                    fila.add(consulta.getString("nombrecliente"));
                    //java.sql.Date FechaEntrega = consulta.getDate("fechaentrega");
                    fila.add(consulta.getDate("fechaentrega"));
                    //java.sql.Date FechaRecibido = consulta.getDate("fecharecibido");
                    fila.add(consulta.getDate("fecharecibido"));
                    String MaterialRecibido = "";
                    if(consulta.getBoolean("materialrecibido")){
                        MaterialRecibido = "Si";
                    }
                    else{
                        MaterialRecibido = "No";
                    }
                    fila.add(MaterialRecibido);
                    String RequiereInstalacion = "";
                    if(consulta.getBoolean("requiereinstalacion")){
                        RequiereInstalacion = "Si";
                    }
                    else{
                        RequiereInstalacion = "No";
                    }
                    fila.add(RequiereInstalacion);
                    String RequiereCanalizacion = "";
                    if(consulta.getBoolean("requierecanalizacion")){
                        RequiereCanalizacion = "Si";
                    }
                    else{
                        RequiereCanalizacion = "No";
                    }
                    fila.add(RequiereCanalizacion);
                    //String Vendedor = consulta.getString("vendedor");
                    fila.add(consulta.getString("vendedor"));
                    //float Importe = consulta.getFloat("importe");
                    fila.add(consulta.getFloat("importe"));
                    //float Importe = consulta.getFloat("importependiente");
                    fila.add(consulta.getFloat("importependiente"));
                    //float Importe = consulta.getFloat("importefacturado");
                    fila.add(consulta.getFloat("importefacturado"));
                    //String TipoMoneda = consulta.getString("tipomoneda");
                    fila.add(consulta.getString("tipomoneda"));
                    //float TasaCambio = consulta.getFloat("tasacambio");
                    fila.add(consulta.getFloat("tasacambio"));
                    //float GM = consulta.getFloat("gm");
                    fila.add(consulta.getFloat("gm"));
                    //int DiasCredito = consulta.getInt("diascredito");
                    fila.add(consulta.getInt("diascredito"));
                    //java.sql.Date FechaVencimientoPago = consulta.getDate("fechavencimientopago");
                    fila.add(consulta.getDate("fechavencimientopago"));
                    //String Status = consulta.getString("status");
                    fila.add(consulta.getString("status"));
                    //String Notas = consulta.getString("notas");
                    fila.add(consulta.getString("notas"));
                    consulta.getString("capturo");
                    String FolioServicio = consulta.getString("folioservicio");
                    //fila.add(cargarFoliosServicio(folioVenta));
                    if(FolioServicio == null) FolioServicio = "";
                    fila.add(FolioServicio);
                    String Factura = consulta.getString("factura");
                    //fila.add(cargarFacturas(folioVenta));
                    if(Factura == null) Factura = "";
                    fila.add(Factura);
                    String FechaFactura = consulta.getString("fechafactura");
                    //fila.add(cargarFechasFacturas(folioVenta));
                    if(FechaFactura == null) FechaFactura = "";
                    fila.add(FechaFactura);
                    String OrdenCompraGINSATEC = consulta.getString("ordencompraginsatec");
                    //fila.add(cargarOrdenesCompraGINSATEC(folioVenta));
                    if(OrdenCompraGINSATEC == null) OrdenCompraGINSATEC = "";
                    fila.add(OrdenCompraGINSATEC);
                    String FechaOrdenGINSATEC = consulta.getString("fechaordenginsatec");
                    //fila.add(cargarFechasOrdenesCompraGINSATEC(folioVenta));
                    if(FechaOrdenGINSATEC == null) FechaOrdenGINSATEC = "";
                    fila.add(FechaOrdenGINSATEC);
                    String Cotizacion = consulta.getString("cotizacion");
                    //fila.add(cargarCotizaciones(folioVenta));
                    if(Cotizacion == null) Cotizacion = "";
                    fila.add(Cotizacion);
                    String OrdenCompra = consulta.getString("ordencompra");
                    //fila.add(cargarOrdenesCompra(folioVenta));
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
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                if(usuario != 0){
                    return new JTable(null);
                }
                else{
                    return new JTable(null);
                }
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
    
    public void actualizarTabla(JTable tabla){
        pnlMostrar.remove(pnlTablaMostrar);
        crearTablas(tabla);
        pnlMostrar.add(pnlTablaMostrar, BorderLayout.CENTER);
        pnlMostrar.validate();
        pnlMostrar.repaint();
    }
    
    public void abrirArchivo(JTable tabla){
        int x = tabla.getSelectedRow(), y = tabla.getSelectedColumn();
        if(y == 2 || y == 3){
            Connection c = ConectarDB();
            Statement comando = null;
            if( c == null ){
                JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
            }
            else{
                try {
                    int folio = Integer.parseInt(String.valueOf(tabla.getValueAt(x, 0)));
                    comando = c.createStatement();
                    String sql = "";
                    if(y == 2){
                        sql = "select ruta from ordenescompra where folioventa=" + folio + " and ruta is not null";
                    }
                    else if(y == 3){
                        sql = "select ruta from cotizaciones where folioventa=" + folio + " and ruta is not null";
                    }
                    String path = "";
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
                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void actualizarFiltro(){
        buscarAnioFiltros();
        if(     cmbFiltros.getSelectedItem().equals("Mes") || 
                cmbFiltros.getSelectedItem().equals("Trimestre") ||
                cmbFiltros.getSelectedItem().equals("Año") ||
                cmbFiltros.getSelectedItem().equals("Vendedor-Mensual") ||
                cmbFiltros.getSelectedItem().equals("Vendedor-Trimestral") ||
                cmbFiltros.getSelectedItem().equals("Vendedor-Anual") ||
                cmbFiltros.getSelectedItem().equals("Cliente-Mensual") ||
                cmbFiltros.getSelectedItem().equals("Cliente-Anual") ||
                cmbFiltros.getSelectedItem().equals("Cliente-Trimestral")){
            modeloAnio = new DefaultComboBoxModel(filtrosAnio);
            cmbOpcionesAnio.setModel(modeloAnio);
        }
        else if(cmbFiltros.getSelectedItem().equals("Cliente-Mensual") ||
                cmbFiltros.getSelectedItem().equals("Cliente-Trimestral") ||
                cmbFiltros.getSelectedItem().equals("Cliente-Anual") ||
                cmbFiltros.getSelectedItem().equals("Cliente")){
            modeloExtra = new DefaultComboBoxModel(filtrosCliente);
            cmbOpcionesExtra.setModel(modeloExtra);
        }
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
        else if(accion == cmbFiltros){
            if(cmbFiltros.getSelectedItem().equals("Sin Filtro")){
                tblTablaMostrar = new JTable(modeloMostrar);
                actualizarTabla(tblTablaMostrar);
                cmbOpcionesMes.setVisible(false);
                cmbOpcionesAnio.setVisible(false);
                cmbOpcionesExtra.setVisible(false);
            }
            else if(cmbFiltros.getSelectedItem().equals("Mes")){
                modeloMes = new DefaultComboBoxModel(filtrosMes);
                cmbOpcionesMes.setModel(modeloMes);
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                cmbOpcionesMes.setVisible(true);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(false);
            }
            else if(cmbFiltros.getSelectedItem().equals("Trimestre")){
                modeloMes = new DefaultComboBoxModel(filtrosTrimestre);
                cmbOpcionesMes.setModel(modeloMes);
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                cmbOpcionesMes.setVisible(true);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(false);
            }
            else if(cmbFiltros.getSelectedItem().equals("Año")){
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                cmbOpcionesMes.setVisible(false);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(false);
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Mensual")){
                modeloMes = new DefaultComboBoxModel(filtrosMes);
                cmbOpcionesMes.setModel(modeloMes);
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                modeloExtra = new DefaultComboBoxModel(filtrosVendedor);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(true);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(true);
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Trimestral")){
                modeloMes = new DefaultComboBoxModel(filtrosTrimestre);
                cmbOpcionesMes.setModel(modeloMes);
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                modeloExtra = new DefaultComboBoxModel(filtrosVendedor);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(true);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(true);
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Anual")){
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                modeloExtra = new DefaultComboBoxModel(filtrosVendedor);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(false);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(true);
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor")){
                modeloExtra = new DefaultComboBoxModel(filtrosVendedor);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(false);
                cmbOpcionesAnio.setVisible(false);
                cmbOpcionesExtra.setVisible(true);
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Mensual")){
                modeloMes = new DefaultComboBoxModel(filtrosMes);
                cmbOpcionesMes.setModel(modeloMes);
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                modeloExtra = new DefaultComboBoxModel(filtrosCliente);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(true);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(true);
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Trimestral")){
                modeloMes = new DefaultComboBoxModel(filtrosTrimestre);
                cmbOpcionesMes.setModel(modeloMes);
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                modeloExtra = new DefaultComboBoxModel(filtrosCliente);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(true);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(true);
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Anual")){
                modeloAnio = new DefaultComboBoxModel(filtrosAnio);
                cmbOpcionesAnio.setModel(modeloAnio);
                modeloExtra = new DefaultComboBoxModel(filtrosCliente);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(false);
                cmbOpcionesAnio.setVisible(true);
                cmbOpcionesExtra.setVisible(true);
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente")){
                modeloExtra = new DefaultComboBoxModel(filtrosCliente);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(false);
                cmbOpcionesAnio.setVisible(false);
                cmbOpcionesExtra.setVisible(true);
            }
            else if(cmbFiltros.getSelectedItem().equals("Status")){
                modeloExtra = new DefaultComboBoxModel(filtrosStatus);
                cmbOpcionesExtra.setModel(modeloExtra);
                cmbOpcionesMes.setVisible(false);
                cmbOpcionesAnio.setVisible(false);
                cmbOpcionesExtra.setVisible(true);
            }
        }
        else if(accion == cmbOpcionesMes){
            if(cmbFiltros.getSelectedItem().equals("Mes")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(cmbOpcionesMes.getSelectedIndex() + 1, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Trimestre")){
                switch(cmbOpcionesMes.getSelectedIndex()){
                    case 0:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(1, 3, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 1:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(4, 6, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 2:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(7, 9, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 3:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(10, 12, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                }
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Mensual")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), cmbOpcionesMes.getSelectedIndex() + 1, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Trimestral")){
                switch(cmbOpcionesMes.getSelectedIndex()){
                    case 0:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 1, 3, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 1:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 4, 6, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 2:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 7, 9, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 3:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 10, 12, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                }
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Mensual")){
                tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), cmbOpcionesMes.getSelectedIndex() + 1, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Trimestral")){
                switch(cmbOpcionesMes.getSelectedIndex()){
                    case 0:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 1, 3, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 1:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 4, 6, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 2:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 7, 9, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 3:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 10, 12, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                }
            }
            actualizarTabla(tblTablaMostrar);
            
        }
        else if(accion == cmbOpcionesAnio){
            if(cmbFiltros.getSelectedItem().equals("Mes")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(cmbOpcionesMes.getSelectedIndex() + 1, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Trimestre")){
                switch(cmbOpcionesMes.getSelectedIndex()){
                    case 0:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(1, 3, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 1:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(4, 6, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 2:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(7, 9, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 3:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(10, 12, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                }
            }
            else if(cmbFiltros.getSelectedItem().equals("Año")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Mensual")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), cmbOpcionesMes.getSelectedIndex() + 1, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Trimestral")){
                switch(cmbOpcionesMes.getSelectedIndex()){
                    case 0:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 1, 3, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 1:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 4, 6, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 2:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 7, 9, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 3:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 10, 12, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                }
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Anual")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Mensual")){
                tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), cmbOpcionesMes.getSelectedIndex() + 1, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Trimestral")){
                switch(cmbOpcionesMes.getSelectedIndex()){
                    case 0:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 1, 3, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 1:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 4, 6, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 2:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 7, 9, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 3:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 10, 12, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                }
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Anual")){
                tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            actualizarTabla(tblTablaMostrar);
        }
        else if(accion == cmbOpcionesExtra){
            if(cmbFiltros.getSelectedItem().equals("Vendedor-Mensual")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), cmbOpcionesMes.getSelectedIndex() + 1, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Trimestral")){
                switch(cmbOpcionesMes.getSelectedIndex()){
                    case 0:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 1, 3, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 1:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 4, 6, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 2:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 7, 9, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 3:
                        tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 10, 12, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                }
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor-Anual")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem()), Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Vendedor")){
                tblTablaMostrar = new JTable(modeloMostrar.Filtrar(String.valueOf(cmbOpcionesExtra.getSelectedItem())));
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Mensual")){
                tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), cmbOpcionesMes.getSelectedIndex() + 1, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Trimestral")){
                switch(cmbOpcionesMes.getSelectedIndex()){
                    case 0:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 1, 3, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 1:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 4, 6, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 2:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 7, 9, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                    case 3:
                        tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), 10, 12, Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
                        break;
                }
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente-Anual")){
                tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem()), Integer.parseInt(String.valueOf(cmbOpcionesAnio.getSelectedItem()))));
            }
            else if(cmbFiltros.getSelectedItem().equals("Cliente")){
                tblTablaMostrar = new JTable(modeloMostrar.FiltrarCliente(String.valueOf(cmbOpcionesExtra.getSelectedItem())));
            }
            else if(cmbFiltros.getSelectedItem().equals("Status")){
                tblTablaMostrar = new JTable(modeloMostrar.FiltrarStatus(String.valueOf(cmbOpcionesExtra.getSelectedItem())));
            }
            actualizarTabla(tblTablaMostrar);
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
