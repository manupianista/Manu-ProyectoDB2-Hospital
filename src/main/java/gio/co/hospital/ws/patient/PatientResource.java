/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gio.co.hospital.ws.patient;

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
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import gio.co.hospitales.JavaConnectDb;

/**
 *
 * @author migue
 */
@Path("/patient")
public class PatientResource {
    //private static int hospitalNum = 1;
    private static int hospitalNum = JavaConnectDb.getHospNum();                                //Este va a estar cambiado para cada hospital
    protected List<Patients> patientsList = new ArrayList<Patients>();
    
    protected List<Patients> patientsList2 = new ArrayList<Patients>();

    //Realizar una consulta
    @GET
    @Path("/getPatient")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatient(
            @QueryParam("pId") String pId) {                                //Aquí uso @QueryParam para recibir los parametros como query

        makeList(pId);                                                      //Crear la lista de la info solicitada
        return Response.status(200).entity(patientsList).build();
    }
    
    //AGREGUE ESTO PARA HACER LA CONSULTA DEL DPI
     //Realizar una consulta con DPI
    @GET
    @Path("/getPatientDPI")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatientDPI(
            @QueryParam("dpi") String dpiP) {                                //Aquí uso @QueryParam para recibir los parametros como query

        makeList2(dpiP);                                                      //Crear la lista de la info solicitada
        return Response.status(200).entity(patientsList2).build();
    }

    //Insertar un paciente
    @POST
    @Path("/addPatient")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addPatient(
            @FormParam("pId") int pId, //Aquí obtengo los parametros del formulario
            @FormParam("nameP") String name, //Aquí uso @FormParam para recibir los parametros de un form
            @FormParam("lastNameP") String lastName,
            @FormParam("dir") String dir,
            @FormParam("tel") int tel,
            @FormParam("bDate") String bDate,
            @FormParam("dpi") double dpi,
            @FormParam("segNum") String segNum,
            @FormParam("docId") int docId,  
            @FormParam("asegNum") int asegNum,
            @FormParam("asegType") int asegType) {

        Boolean answ;                                                       //Respuesta del addUpdatePatient
        answ = false;
        //answ = addUpdatePatient(pId, name, lastName, dir, tel, bDate, dpi, segNum, docId, asegNum,asegType);
        answ = addUpdatePatient(pId, name, lastName, dir, tel, bDate, dpi, dpi, docId, asegNum,asegType);
        if (answ) {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/pacientes_h.jsp?in=1")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"in\":1}").build();
        } else {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/pacientes_h.jsp?in=0")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"in\":0}").build();
        }
    }

    //Eliminar un paciente
    @DELETE
    @Path("/deletePatient")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePatient(
            @FormParam("delId") int pId) {
        Boolean answ;                                                       //Respuesta del delPatient
        answ = false;
        answ = delPatient(pId);
        if (answ) {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/pacientes_h.jsp?del=1")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"del\":1}").build();
        } else {
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/pacientes_h.jsp?del=0")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"del\":0}").build();
        }
    }

    @PUT                                                                    //Update un paciente pero jsp ni html5 funcionan con put
    @Path("/updatePatient")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePatient(
            @FormParam("pId") int pId,                                         //Aquí obtengo los parametros 
            @FormParam("nameP") String name,                                   //Aquí uso @QueryParam para recibir los parametros como query
            @FormParam("lastNameP") String lastName,
            @FormParam("dir") String dir,
            @FormParam("tel") int tel,
            @FormParam("bDate") String bDate,
            @FormParam("dpi") double dpi,
            @FormParam("segNum") String segNum,
            @FormParam("docId") int docId,
            @FormParam("asegType") int asegType,
            @FormParam("asegNum") int asegNum) {

        //Respuesta del addPatient
        Boolean answ;
        answ = false;
        //answ = addUpdatePatient(pId, name, lastName, dir, tel, bDate, dpi, segNum, docId, asegNum,asegType);
        answ = addUpdatePatient(pId, name, lastName, dir, tel, bDate, dpi, dpi, docId, asegNum,asegType);
        if (answ) {
            //return Response.status(200).entity("Success").build();
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/pacientes_h.jsp?up=1")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"up\":1}").build();
        } else {
            //return Response.status(200).entity("Failure").build();
            //return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/pacientes_h.jsp?up=0")).build();
            return Response.status(200).type(MediaType.APPLICATION_JSON).entity("{\"up\":0}").build();
        }
    }

    //Metodo para crear la lista de pacientes
    protected void makeList(String pId) {
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
            //Response info
            try{
                //var query sql
                String sql;
                //Revisar si hay un request
                if(pId!=null){
                    //Query con el filtro para seleccionar un paciente
                    //sql = "select * from pacientes where paciente_id ="+pId+" order by paciente_id";
                    sql = "select * from pacientes p join aseguradora a on p.aseguradora_id = a.id_aseguradora join tipo_seguro ts on p.id_tipo_seguro=ts.id_tipo_seguro where paciente_id ="+pId+" order by paciente_id";
                }
                else{
                    //Query de todos los pacientes
                    //sql = "select * from pacientes order by paciente_id";
                    sql = "select * from pacientes p join aseguradora a on p.aseguradora_id = a.id_aseguradora join tipo_seguro ts on p.id_tipo_seguro=ts.id_tipo_seguro order by paciente_id";
                }
                OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
                OracleResultSet rs = (OracleResultSet) pst.executeQuery();                    
                while(rs.next()){
                    //obtener parametros
                    int id = rs.getInt("PACIENTE_ID");
                    String name = rs.getString("Nombre");
                    String lastN = rs.getString("Apellido");
                    int tel = rs.getInt("TEL");
                    double dpi = rs.getDouble("DPI");
                    String segNum = rs.getString("num_seguro");
                    String fNacimiento = rs.getString("f_nacimiento");
                    String dir = rs.getString("dir");
                    int docId = rs.getInt("doctor_id");
                    //id de la aseguradora
                    int asegNum = rs.getInt("ASEGURADORA_ID");
                    String asegName = rs.getString("ASEGURADORA");
                    int asegType = rs.getInt("ID_TIPO_SEGURO");
                    String asegTypeName = rs.getString("TIPO_SEGURO");
                    //Crear clase paciente
                    Patients patients = new Patients(id,name,lastN,tel,dpi,segNum,fNacimiento,dir,asegNum,asegName, asegType, asegTypeName,docId);
                    //Agregar paciente a la lista
                    patientsList.add(patients);
                }
                rs.close ();
                pst.close ();
                conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    //AGREGUE ESTO PARA HACER LA CONSULTA DEL DPI
    //Metodo para crear la lista de pacientes con DPI
    protected void makeList2(String dpiP) {
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
            //Response info
            try{
                //var query sql
                String sql;
                //Revisar si hay un request
                if(dpiP!=null){
                    //Query con el filtro para seleccionar un paciente
                    //sql = "select * from pacientes where paciente_id ="+pId+" order by paciente_id";
                    sql = "select * from pacientes p join aseguradora a on p.aseguradora_id = a.id_aseguradora join tipo_seguro ts on p.id_tipo_seguro=ts.id_tipo_seguro where dpi ="+dpiP+" order by dpi";
                }
                else{
                    //Query de todos los pacientes
                    //sql = "select * from pacientes order by paciente_id";
                    sql = "select * from pacientes p join aseguradora a on p.aseguradora_id = a.id_aseguradora join tipo_seguro ts on p.id_tipo_seguro=ts.id_tipo_seguro order by dpi";
                }
                OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
                OracleResultSet rs = (OracleResultSet) pst.executeQuery();                    
                while(rs.next()){
                    //obtener parametros
                    int id = rs.getInt("PACIENTE_ID");
                    String name = rs.getString("Nombre");
                    String lastN = rs.getString("Apellido");
                    int tel = rs.getInt("TEL");
                    double dpi = rs.getDouble("DPI");
                    String segNum = rs.getString("num_seguro");
                    String fNacimiento = rs.getString("f_nacimiento");
                    String dir = rs.getString("dir");
                    int docId = rs.getInt("doctor_id");
                    //id de la aseguradora
                    int asegNum = rs.getInt("ASEGURADORA_ID");
                    String asegName = rs.getString("ASEGURADORA");
                    int asegType = rs.getInt("ID_TIPO_SEGURO");
                    String asegTypeName = rs.getString("TIPO_SEGURO");
                    //Crear clase paciente
                    Patients patients = new Patients(id,name,lastN,tel,dpi,segNum,fNacimiento,dir,asegNum,asegName, asegType, asegTypeName,docId);
                    //Agregar paciente a la lista
                    patientsList2.add(patients);
                }
                rs.close ();
                pst.close ();
                conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //Metodo para realizar un insert o un update dependiendo del caso
    private Boolean addUpdatePatient(int pId, String name, String lastName, String dir, int tel, String bDate, double dpi, double segNum, int docId, int asegNum, int asegType) {
        Boolean respuesta = false;
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            //Armar el query
            if (pId != 0) {
                //Query con el filtro
                sql = "UPDATE PACIENTES SET NOMBRE = '"+name+"', APELLIDO = '"+lastName+"', DIR = '"+dir+"', TEL = "+tel+", F_NACIMIENTO = TO_DATE('"+bDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), DPI = "+dpi+", NUM_SEGURO = "+segNum+", DOCTOR_ID = "+docId+",aseguradora_id = "+asegNum+", ID_TIPO_SEGURO = "+asegType+" WHERE paciente_id = "+pId;
                //sql = "UPDATE PACIENTES SET NOMBRE = '"+name+"', APELLIDO = '"+lastName+"', DIR = '"+dir+"', TEL = "+tel+", F_NACIMIENTO = TO_DATE('"+bDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), DPI = "+dpi+", NUM_SEGURO = "+segNum+", DOCTOR_ID = "+docId+",aseguradora_id = "+asegNum+" WHERE paciente_id = "+pId;
            }
            else{
                //Query
                sql = "INSERT INTO PACIENTES (NOMBRE, APELLIDO, DIR, TEL, F_NACIMIENTO, DPI, NUM_SEGURO, DOCTOR_ID,aseguradora_id, ID_TIPO_SEGURO) VALUES ('"+name+"', '"+lastName+"', '"+dir+"', '"+tel+"', TO_DATE('"+bDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), '"+dpi+"', '"+segNum+"', '"+docId+"','"+asegNum+"', '"+asegType+"')";
                //sql = "INSERT INTO PACIENTES (NOMBRE, APELLIDO, DIR, TEL, F_NACIMIENTO, DPI, NUM_SEGURO, DOCTOR_ID,aseguradora_id) VALUES ('"+name+"', '"+lastName+"', '"+dir+"', '"+tel+"', TO_DATE('"+bDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), '"+dpi+"', '"+segNum+"', '"+docId+"','"+asegNum+"')";
            }
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            rs.close();
            pst.close();
            conn.close();
            respuesta = true;
        } catch (Exception e) {
            System.err.println(e);
            respuesta = false;
        }
        return respuesta;
    }

    private Boolean delPatient(int pId) {
        Boolean respuesta = false;
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            //Query
            sql = "DELETE FROM PACIENTES WHERE paciente_id=" + pId;
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            rs.close();
            pst.close();
            conn.close();
            respuesta = true;
        } catch (Exception e) {
            System.err.println(e);
            respuesta = false;
        }
        return respuesta;
    }
}

















