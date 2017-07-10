/*
 * Autor: Adolfo Michel
 * Arrancar y Terminar servidor
 */
package ventas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Servidor {
    
    public void arrancarServidor(){
        try{
            if(!buscarArchivo()){
                registrarServidor();
                String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\ArranqueServidor.bat",""};
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles\\"));
                Process p = builder.start();
                BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s;
                while((s = stdOut.readLine()) != null){
                    System.out.println("Output: " + s);
                    if(s.equals("LOG:  autovacuum launcher started")) break;
                }
                System.out.println("Arrancó el servidor");
            }
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cerrarServidor(){
        try{
            if(buscarArchivo()){
                if(removerServidor()){
                    String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\CerrarServidor.bat",""};
                    ProcessBuilder builder = new ProcessBuilder(command);
                    builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles"));
                    Process p = builder.start();
                    BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String s;
                    while((s = stdOut.readLine()) != null){
                        System.out.println("\"Output: " + s);
                    }
                }
            }
            else{
                String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\CerrarServidor.bat",""};
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles"));
                Process p = builder.start();
                BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s;
                while((s = stdOut.readLine()) != null){
                    System.out.println("\"Output: " + s);
                }
            }
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean reiniciarServidor(){
        String ip;
        
        JOptionPane.showMessageDialog(null, "No es posible acceder al servidor de base de datos.\n"
                                            + "Reiniciando servidor.\n"
                                            + "Este proceso puede tomar algunos minutos.");
        if(buscarArchivo()){
            ip = obtenerIP();
        }
        else{
            ip = "localhost";
        }
        if(checarIP()){
            while(!verificarServidor(ip)){
                removerArchivo();
                cerrarServidor();
                arrancarServidor();
            }
            return true;
        }
        else{
            while(!verificarServidor(ip)){
                removerArchivo();
                cerrarServidor();
                arrancarServidor();
                ip = "localhost";
            }
            return true;
        }
    }
    
    private boolean verificarServidor(String ip){
        try {
            String[] command = {"C:\\SistemaVentas\\src\\ventas\\BatchFiles\\VerificarServidor.bat",ip};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File("C:\\SistemaVentas\\src\\ventas\\BatchFiles"));
            Process p = builder.start();
            BufferedReader stdOut=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            System.out.println("Verificando Servidor en: " + ip);
            while((s = stdOut.readLine())!=null){
                if(s.equals(ip + ":3389 - sin respuesta")){
                    return false;
                }
                else if(s.equals(ip + ":3389 - aceptando conexiones")){
                    return true;
                }
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public String obtenerIP(){
        boolean disponible = buscarArchivo();
        String path = "\\\\192.168.110.1\\servicio\\Prueba DB\\Check.txt";
        String ip = "";
        if(disponible){
            try (RandomAccessFile file = new RandomAccessFile(path, "rw")) {
                while(file.getFilePointer() < file.length()){
                    ip += file.readChar();
                }
            return ip;
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public boolean buscarArchivo(){
        String path = "\\\\192.168.110.1\\servicio\\Prueba DB\\Check.txt";
        File check = new File(path);
        return check.exists();
    }
    
    public void registrarServidor(){
        String ip = "localhost";
        try{
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            for (
                final Enumeration< NetworkInterface > interfaces =
                    NetworkInterface.getNetworkInterfaces( );
                interfaces.hasMoreElements( );
            )
            {
                final NetworkInterface cur = interfaces.nextElement( );

                if ( cur.isLoopback( ) )
                {
                    continue;
                }

                System.out.println( "interface " + cur.getName( ) );

                for ( final InterfaceAddress addr : cur.getInterfaceAddresses( ) )
                {
                    final InetAddress inet_addr = addr.getAddress( );

                    if ( !( inet_addr instanceof Inet4Address ) )
                    {
                        continue;
                    }

                    System.out.println(
                        "  address: " + inet_addr.getHostAddress( ) +
                        "/" + addr.getNetworkPrefixLength( )
                    );
                    ip = inet_addr.getHostAddress();
                    System.out.println(
                        "  broadcast address: " +
                            addr.getBroadcast( ).getHostAddress( )
                    );
                }
            }
            try (RandomAccessFile file = new RandomAccessFile("\\\\192.168.110.1\\servicio\\Prueba DB\\Check.txt", "rw")) {
                file.seek(file.length());
                for(int i = 0; i < ip.length(); i++){
                    file.writeChar(ip.charAt(i));
                }
            }
            System.out.println("Servidor Registrado");
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean removerServidor(){
        if(checarIP()){
            removerArchivo();
            return true;
        }
        else{
            System.out.println("No iguales");
        }
        return false;
    }

    private void removerArchivo() {
        String path = "\\\\192.168.110.1\\servicio\\Prueba DB\\Check.txt";
        File archivo = new File(path);
        if(archivo.delete()){
            System.out.println("Se borro el archivo");
        }
        else{
            System.out.println("No se borro");
        }
    }
    
    private boolean checarIP(){
        String ip = "";
        try{
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            for (
                final Enumeration< NetworkInterface > interfaces =
                    NetworkInterface.getNetworkInterfaces( );
                interfaces.hasMoreElements( );
            )
            {
                final NetworkInterface cur = interfaces.nextElement( );

                if ( cur.isLoopback( ) )
                {
                    continue;
                }

                System.out.println( "interface " + cur.getName( ) );

                for ( final InterfaceAddress addr : cur.getInterfaceAddresses( ) )
                {
                    final InetAddress inet_addr = addr.getAddress( );

                    if ( !( inet_addr instanceof Inet4Address ) )
                    {
                        continue;
                    }

                    System.out.println(
                        "  address: " + inet_addr.getHostAddress( ) +
                        "/" + addr.getNetworkPrefixLength( )
                    );
                    ip = inet_addr.getHostAddress();
                    System.out.println(
                        "  broadcast address: " +
                            addr.getBroadcast( ).getHostAddress( )
                    );
                }
            }
            return ip.contentEquals(obtenerIP());
        }
        catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
