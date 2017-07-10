/*
 * Autor: Adolfo Michel
 * Crear 100 registros para probar la funcionalidad del programa y eficiencia de los filtros
 */
package PruebaRegistros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ventas.Login;

public class GenerarPrueba {
    
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
    
    public void nuevoRegistro(){
        Connection c = ConectarDB();
        Statement comando = null;
        if( c == null ){
            JOptionPane.showMessageDialog(null, "No se puede acceder a la base de datos");
        }
        else{
            try {
                comando = c.createStatement();
                String sql = "";
                String vendedor = "";
                String cliente = "CLIENTE ";
                String status = "";
                String[] opcionesStatus = {"Pendiente", "Activa", "Pagada", "No pagada", "Cancelada", "Pagada"};
                for(int i = 0, op = 0; i < 1000; i++, op++){
                    status = opcionesStatus[op];
                    switch(op){
                        case 0:
                            vendedor = "Mario Gonzalez";
                            break;
                        case 1:
                            vendedor = "Marco Padilla";
                            break;
                        case 2:
                            vendedor = "Daniel Martinez";
                            break;
                        case 3:
                            vendedor = "Rosario Arellano";
                            break;
                        case 4:
                            vendedor = "Alberto Lomeli";
                            break;
                        case 5:
                            vendedor = "Mariano Ruiz";
                            op = -1;
                            break;
                    }
                    sql = "";
                    sql = "insert into ventas values(" + i + ",(now() + interval '" + i + "' day),'" + cliente + (op + 2) +"',(now() + interval '" + i + "' day),(now() + interval '" + i + "' day),";
                    sql += "'true','true','true','" + vendedor + "',1000,0,1000,'Dolares',20,100,30,(now() + interval '" + i + "' day),'" + status + "','NOTA',1)";
                    comando.executeUpdate(sql);
                }
                JOptionPane.showMessageDialog(null, "Registro Exitoso");
                comando.close();
                c.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error durante el registro");
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
