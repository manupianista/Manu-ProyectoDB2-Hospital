/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gio.co.hospitales;

/**
 *
 * @author migue
 */
import gio.co.hospital.correos.correoReminder;
import gio.co.hospital.ws.cita.Citas;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import gio.co.hospital.correos.sendStuff;
import java.net.HttpURLConnection;
import java.net.URL;
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
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import org.json.JSONObject;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
  

public class Scheduler implements ServletContextListener {
    private Thread t = null;
    private ServletContext context;
    private static final int hospitalNum = JavaConnectDb.getHospNum();
    protected List<Citas> citasList = new ArrayList<>();


    public void contextInitialized(ServletContextEvent contextEvent) {
	//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        t =  new Thread(){
            //task
            public void run(){  
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                try {
                    while(true){
                        Date date = new Date();
                        String formated = dateFormat.format(date).toString();
                        Date date2 = new Date("2016/11/16 18:27:00");
                        String formated2 = dateFormat.format(date2).toString();
                        if(formated.equals(formated2)){
                            //Aqui va el metodo en vez del println
                            System.out.println("Funcionaaaa!!! WUJUUUUU");
                            getCitas();
                            //Cada 24 horas 86400 segundos
                            Thread.sleep(86400);
                        }
                        Thread.sleep(15000);
                        Date date3 = new Date();
                        String formated3 = dateFormat.format(date3).toString();
                        Date date4 = new Date("2016/11/16 16:30:00");
                        String formated4 = dateFormat.format(date4).toString();
                        if(formated3.equals(formated4)){
                           String a = "A";
                             try {
                                 
                                 sendStuff.enviar();
                               

                                Thread.sleep(60000);
                           } catch (Exception e){
                                System.err.println(e);
                           }
                        }
                    }
                } catch (InterruptedException e) {}
            }            
        };
        t.start();
        context = contextEvent.getServletContext();
        // you can set a context variable just like this
        context.setAttribute("TEST", "TEST_VALUE");
    }
    public void contextDestroyed(ServletContextEvent contextEvent) {
        // context is destroyed interrupts the thread
        t.interrupt();
    }
    
    public void getCitas(){
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            Date fechaA = new Date();
            DateFormat diaF = new SimpleDateFormat("dd");
            DateFormat mesF = new SimpleDateFormat("MM");
            //String dia = diaF.format(fechaA).toString();
            Calendar c = Calendar.getInstance();
            c.setTime(fechaA);
            c.add(Calendar.DATE, -1);
            fechaA.setTime( c.getTime().getTime() );
            String dia = diaF.format(fechaA).toString();
            String mes = mesF.format(fechaA).toString();
            sql = "select * from citas_full where (to_char(fecha, 'DD')='"+dia+"' AND to_char(fecha, 'MM')='"+mes+"')";
            //sql = "select * from citas_full where paciente_id =" + pId + " order by CITA_ID";
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                //obtener parametros
                int id = rs.getInt("CITA_ID");
                String diag = rs.getString("Diagnostico");
                String res = rs.getString("resultados");
                String meds = rs.getString("medicinas");
                String pasos = rs.getString("pasosaseguir");
                String observ = rs.getString("observaciones");
                String fecha = rs.getString(7);
                fecha = fecha.substring(0, fecha.indexOf(' '));
                int docId = rs.getInt("doc_id");
                String docName = rs.getString("nombre_doc");
                String docLastName = rs.getString("apellido_doc");
                String docFN = docName+"-"+docLastName;
                int paId = rs.getInt("paciente_id");
                String pName = rs.getString("nombre");
                try{pName = pName.substring(0, pName.indexOf(' '));}catch(Exception e){}
                String pLastName = rs.getString("apellido");
                try{pLastName = pLastName.substring(0, pLastName.indexOf(' '));}catch(Exception e){}
                int subCatId = rs.getInt("ID_SUBCAT");
                String subCat = rs.getString("subcat");
                try{subCat = subCat.substring(0, subCat.indexOf(' '));}catch(Exception e){}
                int catId = rs.getInt("ID_CAT");
                String cat = rs.getString("categoria");
                try{cat = cat.substring(0, cat.indexOf(' '));}catch(Exception e){}
                double dpid = rs.getDouble("DPI");
                long dpil = (long) dpid;
                String dpi = Long.toString(dpil);
                //String dpi = Double.toString(rs.getDouble("DPI"));
                //Crear clase paciente
                Citas citas = new Citas(id, diag, res, meds, pasos, observ, fecha, docId, docFN, docLastName, paId, pName, pLastName, subCatId, subCat, catId, cat);
                //enviar historial a la aseguradora
                int resu = mongoInsert(fecha,docFN, diag, res, meds, pasos, observ, dpi, cat, subCat, id, pName, pLastName);
                String a = "a";
                //Agregar paciente a la lista
                citasList.add(citas);
            }
        } catch(SQLException e){
            System.err.println(e);
        }
    }
    
    public int mongoInsert(String fechaCita, String doctor, String diagnostico, String resultados, String medicinas, String pasos, String observaciones, String dpi, String categoria, String subcat, int idCita, String pName, String pLastName){
        int res = 0;
        try {//b
            // Send data
            String rStmt ="http://192.168.69.4:8888/proyectoDB2-seguro/restHist/hist/addHist?hospital="+hospitalNum+"&fecha="+fechaCita+"&doctor="+doctor+"&diagnostico="+diagnostico+"&resultados="+resultados+"&medicinas="+medicinas+"&pasos="+pasos+"&observaciones="+observaciones+"&dpi="+dpi+"&categoria="+categoria+"&subcat="+subcat+"&idCita="+idCita+"&nCliete="+pName+"&aCliente="+pLastName;
            //String rStmt = "http://192.168.69.4:8888/proyectoDB2-seguro/restHist/hist/addHist?hospital=" + hospitalNum + "&fecha=" + fechaCita + "&doctor=" + doctor + "&diagnostico=" + diagnostico 
            //        + "&resultados=" + resultados + "&medicinas=" + medicinas + "&pasos=" + pasos + "&observaciones="+observaciones+"&dpi="+dpi+"&categoria="+categoria+"&subcat="+subcat+"&idCita="+idCita;
            URL urlr = new URL(rStmt);
            HttpURLConnection connr = (HttpURLConnection) urlr.openConnection();
            connr.setRequestMethod("POST");
            connr.setDoOutput(true);
            String a = "d";

            // Get the response
            BufferedReader rdr = new BufferedReader(new InputStreamReader(connr.getInputStream()));
            a = "e";
            String liner;
            StringBuffer responser = new StringBuffer();
            while ((liner = rdr.readLine()) != null) {
                responser.append(liner);
            }
            //JSONArray arrObjr = new JSONArray(responser.toString());
            //JSONObject objr = arrObjr.getJSONObject(0);
            JSONObject objr = new JSONObject(responser.toString());
            //JSONObject objr = arrObjr.getJSONObject(0);
            res = objr.getInt("in");
            rdr.close();
            a = "f";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}


















































































































































































