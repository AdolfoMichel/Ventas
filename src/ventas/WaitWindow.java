/*
 * Autor: Adolfo Michel
 * Ventana de espera
 */
package ventas;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.logging.*;
import javax.swing.*;

public class WaitWindow extends JFrame implements Runnable{

    private volatile boolean running = true;
    private int user;
    private String rol;
    WaitWindow w;

    WaitWindow(int usuario, String rol){
        super("Registro de Ventas - Cargando Registros");
        this.setSize(600,300);
    	
        
        w = this;
        user = usuario;
        this.rol = rol;
        try{
            URL url = this.getClass().getResource("images/loading.gif");
            Icon icon = new ImageIcon(url);
            JLabel lblLoading = new JLabel(icon);
            this.getContentPane().add(lblLoading);
            this.pack();
        }
        catch(Exception ex){
            Logger.getLogger(WaitWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e)
            {
                new Servidor().cerrarServidor();
            }
        });
        
        //this.add(new LoadingImage());
        
        this.setLocationRelativeTo(null);
    	this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void terminate() {
        running = false;
    }
    
    public void iniciar(){
        Thread t = new Thread(){
            Ventas v;
            @Override
            public void run(){
                v = new Ventas(user, rol, w);
                while(true){
                    /*Nothing*/
                }
            }
        };
        t.start();
    }
    
    @Override
    public void run() {    
        iniciar();
        while(running){
            /*Nothing*/
        }
    }
    
}
