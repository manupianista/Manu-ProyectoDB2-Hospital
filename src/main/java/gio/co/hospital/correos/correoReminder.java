/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gio.co.hospital.correos;


import gio.co.hospital.ws.cita.Citas;
import gio.co.hospitales.JavaConnectDb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import org.json.JSONObject;

/**
 *
 * @author manu
 */

@WebServlet("/correoReminder")

public class correoReminder extends HttpServlet{
    
    //private static final long serialVersionUID = 1L;
    private static final long serialVersionUID = 1L;
    private static int hospitalNum = JavaConnectDb.getHospNum();
    
       
    public correoReminder(){
        super();
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Response info
        
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            //var query sql
            
            String sql;
              
            
                sql = "SELECT P.NOMBRE, P.APELLIDO, P.EMAIL, C.CITA_ID, C.DOC_ID, C.PACIENTE_ID, C.FECHA, U.NOMBRE AS MIDOC, U.APELLIDO AS MIDOCTOR, U.USUARIO_ID, S.SUBCAT FROM CITAS C INNER JOIN PACIENTES P ON C.PACIENTE_ID=P.PACIENTE_ID  INNER JOIN USUARIO U ON C.DOC_ID=U.USUARIO_ID INNER JOIN SUBCATEGORIA S ON C.ID_SUBCAT=S.ID_SUBCAT WHERE C.FECHA>SYSDATE AND C.FECHA<SYSDATE+3";
                
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
              
                String nombreP = rs.getString("nombre");
                String apellidoP = rs.getString("apellido");
                String emailP = rs.getString("email");
                //String fechaC = rs.getString("fecha");
                String nombreDoc = rs.getString("midoc");
                //String apellidoDoc = rs.getString("midoctor");
                String subcat = rs.getString("subcat");
                
                String[] parts = emailP.split("@");
                String user, serverno, serversi, dotque;
                user = parts[0];
                serverno = parts[1];
                //String[] parts2 = serverno.split(".");
                //serversi = parts2[0];
                //dotque = parts2[1];
                
                
                StringBuilder dataBuilder = new StringBuilder();
                     dataBuilder.append(URLEncoder.encode("nombrep", "UTF-8")).append('=').append(URLEncoder.encode(nombreP, "UTF-8")).append("&").
                    append(URLEncoder.encode("apellidop", "UTF-8")).append('=').append(URLEncoder.encode(apellidoP, "UTF-8")).append("&").
                    append(URLEncoder.encode("correop", "UTF-8")).append('=').append(URLEncoder.encode(user, "UTF-8")).append("&").
                             append(URLEncoder.encode("correoPserver", "UTF-8")).append('=').append(URLEncoder.encode(serverno, "UTF-8")).append("&").
                                
                    //append(URLEncoder.encode("fecha", "UTF-8")).append('=').append(URLEncoder.encode(fechaC, "UTF-8")).append("&").
                    append(URLEncoder.encode("docName", "UTF-8")).append('=').append(URLEncoder.encode(nombreDoc, "UTF-8")).append("&").
                    append(URLEncoder.encode("subcat", "UTF-8")).append('=').append(URLEncoder.encode(subcat, "UTF-8"));
               
                        // Send data
              URL url = new URL("http://192.168.69.4:8888/proyectoDB2-Hospital1/sendEmailP");
              HttpURLConnection conn2 = (HttpURLConnection) url.openConnection();
              conn2.setDoOutput(true);
              
              conn2.setRequestMethod("POST");
              OutputStreamWriter wr = new OutputStreamWriter(conn2.getOutputStream());
              wr.write(dataBuilder.toString());
              //out.println(wr);
              wr.flush();
              
              
              
                         
              }
                rs.close();
              pst.close();
              conn.close();
              
              
                             
           

             } catch(SQLException e){
                  //response.sendRedirect("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp");
                     System.err.println(e);
                 }
    
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    
}
    



















































