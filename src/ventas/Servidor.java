/*
 * Autor: Adolfo Michel
 * Arrancar y Terminar servidor
 */
package ventas;

import java.io.BufferedReader;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sun.rmi.runtime.Log;

public class Servidor {
    
    Process p = null;
    
    public void arrancarServidor(){
        try{
            String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\ArranqueServidor.bat",""};
            //String[] command = {System.getProperty("user.dir") + "\\src\\BatchFiles\\ArranqueServidor.bat", ""};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles\\"));
            //builder.directory(new File(System.getProperty("user.dir") + "\\src\\BatchFiles"));
            Process p = builder.start();
            BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s = null;
            while((s = stdOut.readLine()) != null){
                System.out.println("Output: " + s);
                if(s.equals("LOG:  autovacuum launcher started")) break;
            }
            System.out.println("Arrancó el servidor");
            //p.waitFor();
        }
        catch(IOException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cerrarServidor(){
        try{
            String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\CerrarServidor.bat",""};
            //String[] command = {System.getProperty("user.dir") + "\\src\\BatchFiles\\CerrarServidor.bat", ""};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles"));
            //builder.directory(new File(System.getProperty("user.dir") + "\\src\\BatchFiles"));
            Process p = builder.start();
            BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s = null;
            while((s = stdOut.readLine())!=null){
                System.out.println("\"Output: " + s);
            }
            //p.waitFor();
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
