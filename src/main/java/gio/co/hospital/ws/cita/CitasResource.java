/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gio.co.hospital.ws.cita;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import gio.co.hospitales.JavaConnectDb;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author migue
 */
@Path("/cita")
public class CitasResource {

    private static final int hospitalNum = JavaConnectDb.getHospNum();               //Este va a estar cambiado para cada hospital
    protected List<Citas> citasList = new ArrayList<>();

    //Realizar una consulta
    @GET
    @Path("/getCita")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCita(
            @QueryParam("pId") String pId,
            @QueryParam("docId") String docId,
            @QueryParam("citaId") String citaId) {                                  //Aquí uso @QueryParam para recibir los parametros como query

        makeList(pId, citaId, docId);                                                       //Crear la lista de la info solicitada
        return Response.status(200).entity(citasList).build();
    }

    //Indicar disponibilidad del horario
    @GET
    @Path("/getDisp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDisp(
            @QueryParam("fecha") String fecha,
            @QueryParam("docId") int docId) {
        List<Horario> horarios = checkDisp(fecha, docId);                                                      //Crear la lista de los horarios
        return Response.status(200).entity(horarios).build();
    }

    //Insertar una cita 
    @POST
    @Path("/addCita")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addCita(
            @FormParam("pId") int pId, //Aquí obtengo los parametros del formulario
            @FormParam("fechaCita") String dateCita, //Aquí uso @FormParam para recibir los parametros de un form
            @FormParam("hora") String hora,
            @FormParam("servicioId") int sId,
            @FormParam("citaId") int citaId,
            @FormParam("seg") int seg,
            @FormParam("docId") int docId) {

        Boolean answ;                                                               //Respuesta del addUpdateCita
        answ = addNewCita(pId, dateCita, hora, sId, docId, citaId, seg);
        if (answ) {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?in=1")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"in\":1}").build();
        } else {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?in=0")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"in\":0}").build();
        }

    }

    //Insertar una cita
    @PUT
    @Path("/updateCita")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateCita(
            @FormParam("citaId") int citaId, //Aquí obtengo los parametros del formulario
            @FormParam("fechaCita") String dateCita, //Aquí uso @FormParam para recibir los parametros de un form
            @FormParam("hora") String hora,
            @FormParam("servicioId") int sId,
            @FormParam("diag") String diag,
            @FormParam("pasosASeguir") String pasos,
            @FormParam("res") String res,
            @FormParam("obsrv") String obsrv,
            @FormParam("meds") String meds,
            @FormParam("docId") int docId) {

        Boolean answ;                                                               //Respuesta del addUpdateCita
        answ = upCita(citaId, dateCita, hora, sId, diag, pasos, res, obsrv, meds, docId);
        if (answ) {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?up=1")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"up\":1}").build();
        } else {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?up=0")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"up\":0}").build();
        }
    }

    //Eliminar un paciente
    @DELETE
    @Path("/deleteCita")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCita(
            @FormParam("delId") int citaId) {
        Boolean answ;                                                               //Respuesta del delCita
        answ = delCita(citaId);
        if (answ) {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?del=1")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"del\":1}").build();
        } else {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?del=0")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"del\":0}").build();
        }
    }

    //Metodo para crear la lista de citas
    protected void makeList(String pId, String citaId, String pDocId) {
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        //Response info
        try {
            //var query sql
            String sql;
            //Revisar si hay un request
            if (pId != null) {
                //Query con el filtro
                sql = "select * from citas_full where paciente_id =" + pId + " order by CITA_ID";
            } else if (citaId != null) {
                //Query con el filtro
                sql = "select * from citas_full where cita_id =" + citaId + " order by CITA_ID";
            } else if (pDocId != null) {
                //Query con el filtro
                sql = "select * from citas_full where doc_id =" + pDocId;
            } else {
                //Query
                sql = "select * from citas_full order by CITA_ID";
            }
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            //Array jsons
            while (rs.next()) {
                //obtener parametros
                int id = rs.getInt("CITA_ID");
                String diag = rs.getString("Diagnostico");
                String res = rs.getString("resultados");
                String meds = rs.getString("medicinas");
                String pasos = rs.getString("pasosaseguir");
                String observ = rs.getString("observaciones");
                String fecha = rs.getString(7);
                int docId = rs.getInt("doc_id");
                String docName = rs.getString("nombre_doc");
                String docLastName = rs.getString("apellido_doc");
                int paId = rs.getInt("paciente_id");
                String pName = rs.getString("nombre");
                String pLastName = rs.getString("apellido");
                int subCatId = rs.getInt("ID_SUBCAT");
                String subCat = rs.getString("subcat");
                int catId = rs.getInt("ID_CAT");
                String cat = rs.getString("categoria");
                //Crear clase paciente
                Citas citas = new Citas(id, diag, res, meds, pasos, observ, fecha, docId, docName, docLastName, paId, pName, pLastName, subCatId, subCat, catId, cat);
                //Agregar paciente a la lista
                citasList.add(citas);
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    private Boolean delCita(int citaId) {
        Boolean respuesta = false;
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            //Query
            sql = "DELETE FROM CITAS WHERE cita_id=" + citaId;
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            rs.close();
            pst.close();
            conn.close();
            respuesta = true;
        } catch (SQLException e) {
            System.err.println(e);
            respuesta = false;
        }
        return respuesta;
    }

    //Metodo para realizar un insert
    private Boolean addNewCita(int pId, String dateCita, String hora, int sId, int docId, int citaId, int seg) {
        Boolean respuesta = false;
        int res;
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            sql = "INSERT INTO CITAS (FECHA, DOC_ID, PACIENTE_ID, ID_SUBCAT) VALUES (TO_DATE('" + dateCita + " " + hora + "', 'YYYY-MM-DD HH24:MI:SS'), '" + docId + "', '" + pId + "', '" + sId + "')";
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            rs.close();
            pst.close();
            conn.close();
            respuesta = true;
            //Facturacion
            String[] datos = datosCita(pId, dateCita, hora, sId);
            int cId = Integer.parseInt(datos[0]);
            String servicio = datos[1];
            int monto = Integer.parseInt(datos[2]);
            long DPI = Long.parseLong(datos[3]);
            String a = "a";
            String[] porcentajes = getPorcentaje(DPI);
            double pct = Double.parseDouble(porcentajes[1]);
            String porcentaje = porcentajes[0];
            a = "b";
            if (seg == 1) {
                res = instertAuth(dateCita, servicio, DPI, monto, porcentaje, cId);
            } else {
                res = 0;
            }
            a = "c";
            insertFactura(res, cId, monto, pct);

            a = "d";
            //select * from citas_full where paciente_id =" + pId + " order by CITA_ID"

        } catch (SQLException e) {
            System.err.println(e);
            respuesta = false;
        }
        return respuesta;
    }

    private Boolean upCita(int citaId, String dateCita, String hora, int sId, String diag, String pasos, String res, String obsrv, String meds, int docId) {
        Boolean respuesta = false;
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            //Armar el query
            sql = "UPDATE CITAS SET DIAGNOSTICO = '" + diag + "', RESULTADOS = '" + res + "', MEDICINAS = '" + meds + "', PASOSASEGUIR = '" + pasos + "', OBSERVACIONES = '" + obsrv + "', FECHA = TO_DATE('" + dateCita + " " + hora + "', 'YYYY-MM-DD HH24:MI:SS'), DOC_ID = '" + docId + "', ID_SUBCAT = '" + sId + "' WHERE cita_id=" + citaId;
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            rs.close();
            pst.close();
            conn.close();
            respuesta = true;
        } catch (SQLException e) {
            System.err.println(e);
            respuesta = false;
        }
        return respuesta;
    }

    private List<Horario> checkDisp(String fecha, int doc_id) {
        List<Horario> horariosList = new ArrayList<>();
        try {
            //Conexion con db oracle
            Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
            //var query sql
            String sql;
            if (fecha != null && doc_id > 0) {
                sql = "select id_horario,to_char(horario,'hh24:mi:ss'),to_char(fecha,'hh24:mi:ss') "
                        + "from horarios h "
                        + "left join "
                        + "("
                        + "select * from citas where fecha between TO_DATE('" + fecha + " 00:00', 'YYYY-MM-DD HH24:MI') and TO_DATE('" + fecha + " 23:59', 'YYYY-MM-DD HH24:MI') and doc_id =" + doc_id
                        + ")"
                        + "on to_char(h.horario,'hh24:mi:ss') = to_char(fecha,'hh24:mi:ss')  "
                        + "order by to_char(horario,'hh24:mi:ss') asc";
            } else {
                sql = "select id_horario,to_char(horario,'hh24:mi:ss') from horarios order by horario";
            }
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            //Array jsons
            while (rs.next()) {
                //obtener parametros
                int idHorario = rs.getInt(1);
                String horario = rs.getString(2);
                String fechaOcupada = null;
                if (fecha != null && doc_id > 0) {
                    fechaOcupada = rs.getString(3);
                }
                Horario horarioObj;
                if (fechaOcupada == null) {
                    horarioObj = new Horario(idHorario, horario);
                    horariosList.add(horarioObj);
                }
                //citasList.add(citas);
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return horariosList;
    }

    private String[] datosCita(int pId, String dateCita, String hora, int sId) {
        String[] datos = new String[4];
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            String sql = "Select * from CITAS_FULL where PACIENTE_ID =" + pId + " AND FECHA = TO_DATE('" + dateCita + " " + hora + "', 'YYYY-MM-DD HH24:MI:SS') and ID_SUBCAT =" + sId;
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                //obtener parametros
                int cId = rs.getInt("CITA_ID");
                String servicio = rs.getString("SUBCAT");
                int monto = rs.getInt("COSTO");
                long DPI = rs.getLong("DPI");

                datos[0] = Integer.toString(cId);
                datos[1] = servicio;
                datos[2] = Integer.toString(monto);
                datos[3] = Long.toString(DPI);

                String a = "a";
            }
            String a = "b";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

    private String[] getPorcentaje(long DPI) {
        String[] porcentajes = new String[2];
        try {//a
            // Send data
            URL url = new URL("http://192.168.69.4:8888/proyectoDB2-Hospital1/GetCliente?dpi=" + DPI);
            HttpURLConnection conn2 = (HttpURLConnection) url.openConnection();
            conn2.setDoOutput(true);

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
            String line;
            StringBuffer response2 = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response2.append(line);
            }
            if (response2.toString().equals("[]")) {
                porcentajes[0] = "0";
                porcentajes[1] = "0";
                return porcentajes;
            }
            JSONArray arrObj = new JSONArray(response2.toString());
            JSONObject obj = arrObj.getJSONObject(0);
            String porcentaje = obj.getString("cobertura");
            rd.close();
            double pct = DecimalFormat.getNumberInstance().parse(porcentaje).doubleValue() / 100;
            porcentaje = porcentaje.substring(0, porcentaje.length() - 1);
            porcentajes[0] = porcentaje;
            porcentajes[1] = Double.toString(pct);

            String a = "c";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return porcentajes;
    }

    private int instertAuth(String dateCita, String servicio, long DPI, int monto, String porcentaje, int cId) {
        int res = 0;
        if (porcentaje.equals("0")) {
            return res;
        }
        try {//b
            // Send data
            String rStmt = "http://192.168.69.4:8888/proyectoDB2-seguro/restAuth/auth/addAuth?hospital=" + hospitalNum + "&fecha=" + dateCita + "&servicio=" + servicio + "&dpi=" + DPI + "&monto=" + monto + "&porcentaje=" + porcentaje + "&idCita=" + cId;
            //String rStmt="http://192.168.69.4:8888/proyectoDB2-Hospital1/GetCliente?dpi=" + DPI;
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

    private void insertFactura(int res, int cId, int monto, double pct) {
        int hospitalNum = JavaConnectDb.getHospNum();
        if (res == 1) {
            try {
                // Send data
                URL urlauth = new URL("http://192.168.69.4:8888/proyectoDB2-seguro/restAuth/auth/getAuth?idCita=" + cId + "&hospNum=" + hospitalNum);
                HttpURLConnection connauth = (HttpURLConnection) urlauth.openConnection();
                connauth.setDoOutput(true);
                // Get the response
                BufferedReader rdauth = new BufferedReader(new InputStreamReader(connauth.getInputStream()));
                String lineauth;
                StringBuffer responseauth = new StringBuffer();
                while ((lineauth = rdauth.readLine()) != null) {
                    responseauth.append(lineauth);
                }
                JSONArray arrObjAuth = new JSONArray(responseauth.toString());
                JSONObject objauth = arrObjAuth.getJSONObject(0);
                int aId = objauth.getInt("_id");
                String a = "a";
                rdauth.close();
                Connection connFac = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
                String sqlFac;
                double cobroCliente = monto - (monto * pct);
                sqlFac = "INSERT INTO facturas (CITA_ID, MONTO, AUTORIZACION, COBRO_CLIENTE) VALUES ('" + cId + "','" + monto + "','" + aId + "','" + cobroCliente + "')";
                OraclePreparedStatement pstFac = (OraclePreparedStatement) connFac.prepareStatement(sqlFac);
                OracleResultSet rsFac = (OracleResultSet) pstFac.executeQuery();
                rsFac.close();
                pstFac.close();
                connFac.close();
                a = "a";

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Connection connFac = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
                String sqlFac;
                sqlFac = "INSERT INTO facturas (CITA_ID, MONTO, COBRO_CLIENTE) VALUES ('" + cId + "','" + monto + "','" + monto + "')";
                OraclePreparedStatement pstFac = (OraclePreparedStatement) connFac.prepareStatement(sqlFac);
                OracleResultSet rsFac = (OracleResultSet) pstFac.executeQuery();
                rsFac.close();
                pstFac.close();
                connFac.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
