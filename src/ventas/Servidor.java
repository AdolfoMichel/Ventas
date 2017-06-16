/*
 * Autor: Adolfo Michel
 * Arrancar y Terminar servidor
 */
package ventas;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.rmi.runtime.Log;

public class Servidor {
    public void arrancarServidor(){
        try{
            //String[] command = {"C:\\Users\\Sersitec-Laboratorio\\Documents\\NetBeansProjects\\VentasV2\\src\\ventas\\ArranqueServidor.bat",""};
            String[] command = {System.getProperty("user.dir") + "\\src\\BatchFiles\\ArranqueServidor.bat", ""};
            ProcessBuilder builder = new ProcessBuilder(command);
            //builder.directory(new File("C:\\Users\\Sersitec-Laboratorio\\Documents\\NetBeansProjects\\VentasV2\\src\\ventas"));
            builder.directory(new File(System.getProperty("user.dir") + "\\src\\BatchFiles"));
            Process p = builder.start();
            p.waitFor();
        }
        catch(IOException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cerrarServidor(){
        try{
            //String[] command = {"C:\\Users\\Sersitec-Laboratorio\\Documents\\NetBeansProjects\\VentasV2\\src\\ventas\\ArranqueServidor.bat",""};
            String[] command = {System.getProperty("user.dir") + "\\src\\BatchFiles\\CerrarServidor.bat", ""};
            ProcessBuilder builder = new ProcessBuilder(command);
            //builder.directory(new File("C:\\Users\\Sersitec-Laboratorio\\Documents\\NetBeansProjects\\VentasV2\\src\\ventas"));
            builder.directory(new File(System.getProperty("user.dir") + "\\src\\BatchFiles"));
            Process p = builder.start();
        }
        catch(IOException ex){
        
        }
    }
}
