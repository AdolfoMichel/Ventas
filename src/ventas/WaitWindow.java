/*
 * Autor: Adolfo Michel
 * Ventana de espera
 */
package ventas;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class WaitWindow extends JFrame implements Runnable{

    private volatile boolean running = true;
    int user;
    WaitWindow w;

    WaitWindow(int usuario){
        super("Registro de Ventas - Cargando Registros");
        this.setSize(600,300);
    	
        
        w = this;
        user = usuario;
        
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
            @Override
            public void run(){
                new Ventas(user, w);
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(WaitWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        t.start();
    }
    
    @Override
    public void run() {    
        iniciar();
        while(running){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WaitWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
