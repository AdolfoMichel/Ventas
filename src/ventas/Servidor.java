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
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sun.rmi.runtime.Log;

public class Servidor {
    
    Process p = null;
    
    public void arrancarServidor(){
        try{
            if(!verificarServidor()){
                String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\ArranqueServidor.bat",""};
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles\\"));
                Process p = builder.start();
                BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s = null;
                while((s = stdOut.readLine()) != null){
                    System.out.println("Output: " + s);
                    if(s.equals("LOG:  autovacuum launcher started")) break;
                }
                System.out.println("Arrancó el servidor");
            }
            registrarServidor();
        }
        catch(IOException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cerrarServidor(){
        try{
            if(!removerServidor()){
                String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\CerrarServidor.bat",""};
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles"));
                Process p = builder.start();
                BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s = null;
                while((s = stdOut.readLine())!=null){
                    System.out.println("\"Output: " + s);
                }
            }
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean verificarServidor(){
        boolean disponible = false;
        try{
            
            String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\VerificarServidor.bat",""};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles"));
            Process p = builder.start();
            BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s = null;
            while((s = stdOut.readLine())!=null){
                System.out.println("\"Output: " + s);
                if(s.equals("/tmp:3389 - sin respuesta")){
                    disponible = false;
                    break;
                }
                else if(s.equals("/tmp:3389 - aceptando conexiones")){
                    disponible = true;
                    break;
                }
            }
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return disponible;
    }
    
    public void registrarServidor(){
        try{
            RandomAccessFile file= new RandomAccessFile("\\\\192.168.110.1\\servicio\\Prueba DB\\Check.txt", "rw");
            file.seek(file.length());
            file.writeBoolean(true);
            file.close();
            System.out.println("Servidor Registrado");
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean removerServidor(){
        try{
            RandomAccessFile file= new RandomAccessFile("\\\\192.168.110.1\\servicio\\Prueba DB\\Check.txt", "rw");
            if(file.length() == 1){
                System.out.println("Servidor vacio");
                file.setLength(0);
                file.close();
                return false;
            }
            System.out.println("Servidor lleno");
            file.setLength(file.length() - 1);
            file.close();
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    
}
