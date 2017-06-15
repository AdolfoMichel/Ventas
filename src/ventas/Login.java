/*
 * Autor: Adolfo Michel
 * Clase y ventana Login
 * 
 */
package ventas;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;

public class Login extends JFrame implements ActionListener{

    int usuario;
    
    JLabel lblTitulo = new JLabel("Login");
    JLabel lblUser = new JLabel("Usuario:");
    JLabel lblPass = new JLabel("Contraseña:");
    JTextField txtUser = new JTextField("");
    JPasswordField txtPass = new JPasswordField("");
    JButton btnEntrar = new JButton("Entrar");
    
    Login(){
        super("Registro de Ventas - Login");
        this.setSize(600,300);
    	this.setLocationRelativeTo(null);
        this.setLayout(null);
        
        lblTitulo.setBounds(265, 30, 70, 30);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(20.0f));
        lblUser.setBounds(150, 75, 50, 30);
        lblPass.setBounds(150, 125, 75, 30);
        txtUser.setBounds(250, 75, 200, 30);
        txtPass.setBounds(250, 125, 200, 30);
        btnEntrar.setBounds(225, 200, 150, 30);
        
        this.add(lblTitulo);
        this.add(lblUser);
        this.add(lblPass);
        this.add(txtUser);
        this.add(txtPass);
        this.add(btnEntrar);
        
    	this.setResizable(false);
        
        btnEntrar.addActionListener(this);
        txtPass.addActionListener(this);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        //GenerarPrueba x = new GenerarPrueba();
        //x.nuevoRegistro();
        Login inicio = new Login();
    }
    
    public void actionPerformed(ActionEvent ae) {
        Object accion = ae.getSource();
        if( accion == btnEntrar ){
            if(txtUser.getText().isEmpty() || txtPass.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Debes llenar ambos campos");
            }
            else{
                if(Logear(txtUser.getText(), txtPass.getText())){
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new Thread(new WaitWindow(usuario)).start();
                        }
                    });
                    
                    this.dispose();
                }
            }   
        }
        if( accion == txtPass ){
            if(txtUser.getText().isEmpty() || txtPass.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Debes llenar ambos campos");
            }
            else{
                if(Logear(txtUser.getText(), txtPass.getText())){
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new Thread(new WaitWindow(usuario)).start();
                        }
                    });
                    
                    this.dispose();
                }
            }   
        }
    }
    
    public Connection ConectarDB(){
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
    
    public boolean Logear(String user, String password){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
            return false;
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "select idusuario, password from usuarios where usuario='" + user + "'";
                ResultSet consulta = comando.executeQuery(sql);
                if(consulta.next()){
                    usuario = consulta.getInt("idusuario");
                    String p = consulta.getString("password");
                    if(password.equals(p)){
                        return true;
                    }
                }
                usuario = -1;
                JOptionPane.showMessageDialog(null, "Usuario o Contraseña incorrecta");
                comando.close();
                c.close();
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
    
}
