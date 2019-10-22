/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gio.co.hospital.ws.usuario;

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
import java.net.URI;
import javax.ws.rs.PUT;
import gio.co.hospitales.JavaConnectDb;

@Path("/usuarios")
public class UsuariosResource {

    private static int hospitalNum = JavaConnectDb.getHospNum();
    ;                                //Este va a estar cambiado para cada hospital
    protected List<Usuarios> usuariosList = new ArrayList<Usuarios>();
    protected List<Usuarios2> usuariosListDocs = new ArrayList<Usuarios2>();

    //Realizar una consulta
    @GET
    @Path("/getUsuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios(
            @QueryParam("uId") String uId) {                                //Aquí uso @QueryParam para recibir los parametros como query

        makeList(uId);                                                      //Crear la lista de la info solicitada
        return Response.status(200).entity(usuariosList).build();
    }
    
    @GET
    @Path("/getUsuariosTypes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuariosTypes(
            @QueryParam("typeId") String typeId) {                                //Aquí uso @QueryParam para recibir los parametros como query

        makeList2(typeId);                                                      //Crear la lista de la info solicitada
        return Response.status(200).entity(usuariosListDocs).build();
    }

    //Insertar o Actualizar un paciente
    @POST
    @Path("/addUsuarios")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addUsuarios(
            @FormParam("uId") int uId, //Aquí obtengo los parametros del formulario
            @FormParam("username") String username,
            @FormParam("name") String name, //Aquí uso @FormParam para recibir los parametros de un form
            @FormParam("lastName") String lastName,
            @FormParam("usType") int usType,
            @FormParam("usSpecial") int usSpecial,
            @FormParam("tel") int tel,
            @FormParam("passw") String passw) {

        Boolean answ;                                                       //Respuesta del addUpdatePatient
        answ = false;
        answ = addUpdateUsuarios(uId, username, name, lastName, usType, usSpecial, tel, passw);
        if (uId != 1) {
            if (answ) {
                return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?in=1")).build();
            } else {
                return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?in=0")).build();
            }
        } else {
            if (answ) {
                return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?up=1")).build();
            } else {
                return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?up=0")).build();
            }
        }
    }

    //Inhabilitar un paciente
    @POST
    @Path("/deleteUsuario")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUsuario(
            @FormParam("delId") int uId) {
        Boolean answ;                                                       //Respuesta del delPatient
        answ = false;
        answ = delUsuario(uId);
        if (answ) {
            return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?del=1")).build();
        } else {
            return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?del=0")).build();
        }
    }

    //Habilitar un paciente
    @POST
    @Path("/readdUsuario")
    @Produces(MediaType.TEXT_PLAIN)
    public Response readdUsuario(
            @FormParam("readdId") int uId) {
        Boolean answ;                                                       //Respuesta del delPatient
        answ = false;
        answ = habilitarUsuario(uId);
        if (answ) {
            return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?readd=1")).build();
        } else {
            return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?readd=0")).build();
        }
    }

    @PUT                                                                    //Insertar un paciente pero jsp ni html5 funcionan con put
    @Path("/updateUsuario")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateUsuario(
            @QueryParam("uId") int uId, //Aquí obtengo los parametros 
            @QueryParam("username") String username,
            @QueryParam("name") String name, //Aquí uso @QueryParam para recibir los parametros como query
            @QueryParam("lastName") String lastName,
            @QueryParam("usType") int usType,
            @QueryParam("uSpecal") int usSpecial,
            @QueryParam("tel") int tel,
            @QueryParam("passw") String passw) {

        //Respuesta del addPatient
        Boolean answ;
        answ = false;
        answ = addUpdateUsuarios(uId, username, name, lastName, usType, usSpecial, tel, passw);
        if (answ) {
            //return Response.status(200).entity("Success").build();
            return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?up=1")).build();
        } else {
            //return Response.status(200).entity("Failure").build();
            return Response.temporaryRedirect(URI.create("http://192.168.69.4:8888/proyectoDB2-Hospital1/usuarios_h.jsp?up=0")).build();
        }
    }

    //Metodo para crear la lista de pacientes
    protected void makeList(String uId) {
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        //Response info
        try {
            //var query sql
            String sql;
            //Revisar si hay un request
            if (uId != null) {
                //Query con el filtro para seleccionar un paciente
                //sql = "select * from pacientes where paciente_id ="+pId+" order by paciente_id";
                sql = "select * from usuario where usuario_id =" + uId + " order by usuario_id";
            } else {
                //Query de todos los pacientes
                //sql = "select * from pacientes order by paciente_id";
                sql = "select * from usuario order by usuario_id";
            }
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                //obtener parametros
                int id = rs.getInt("USUARIO_ID");
                String username = rs.getString("USUARIO");
                String firstName = rs.getString("NOMBRE");
                String lastName = rs.getString("APELLIDO");
                int usType = rs.getInt("TIPO_USUARIO_ID");
                int usSpecial = rs.getInt("ESPECIALIDAD_ID");
                int phone = rs.getInt("TELEFONO");
                String pass = rs.getString("PASS");
                int state = rs.getInt("ESTADO");

                //Crear clase paciente
                Usuarios usuarios = new Usuarios(id, username, firstName, lastName, usType, usSpecial, phone, pass, state);
                //Agregar paciente a la lista
                usuariosList.add(usuarios);
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
     protected void makeList2(String typeId) {
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        //Response info
        try {
            //var query sql
            String sql;
            //Revisar si hay un request
            if (typeId != null) {
                //Query con el filtro para seleccionar un paciente
                //sql = "select * from pacientes where paciente_id ="+pId+" order by paciente_id";
                sql = "select * from usuario where tipo_usuario_id =" + typeId + " order by usuario_id";
            } else {
                //Query de todos los pacientes
                //sql = "select * from pacientes order by paciente_id";
                sql = "select * from usuario order by usuario_id";
            }
            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sql);
            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                //obtener parametros
                int id = rs.getInt("USUARIO_ID");
                String username = rs.getString("USUARIO");
                String firstName = rs.getString("NOMBRE");
                String lastName = rs.getString("APELLIDO");
                int usType = rs.getInt("TIPO_USUARIO_ID");
                int usSpecial = rs.getInt("ESPECIALIDAD_ID");
                int phone = rs.getInt("TELEFONO");
                
                int state = rs.getInt("ESTADO");

                //Crear clase docs
                Usuarios2 usuariosDocs = new Usuarios2(id, username, firstName, lastName, usType, usSpecial, phone, state);
                //Agregar paciente a la lista
                usuariosListDocs.add(usuariosDocs);
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //Metodo para realizar un insert o un update dependiendo del caso
    private Boolean addUpdateUsuarios(int uId, String username, String name, String lastName, int usType, int usSpecial, int tel, String passw) {
        Boolean respuesta = false;
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            int num1 = 1;
            //Armar el query
            if (uId != 0) {
                //Query con el filtro
                sql = "UPDATE USUARIO SET USUARIO = '" + username + "', NOMBRE = '" + name + "', APELLIDO = '" + lastName + "', TIPO_USUARIO_ID = '" + usType + "', ESPECIALIDAD_ID = " + usSpecial + ", TELEFONO = " + tel + ", PASS = '" + passw + "' WHERE usuario_id = '" + uId + "'";
            } else {
                //Query
                sql = "INSERT INTO USUARIO (USUARIO, NOMBRE, APELLIDO, TIPO_USUARIO_ID, ESPECIALIDAD_ID, TELEFONO, PASS, ESTADO) VALUES ('" + username + "', '" + name + "', '" + lastName + "', '" + usType + "', '" + usSpecial + "', '" + tel + "', '" + passw + "', " + num1 + ")";
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

    private Boolean delUsuario(int uId) {
        Boolean respuesta = false;
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            //Query
            sql = "CALL inhabilitarUser(" + uId + ")";
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

    private Boolean habilitarUsuario(int uId) {
        Boolean respuesta = false;
        //Conexion con db oracle
        Connection conn = gio.co.hospitales.JavaConnectDb.connectDbH(hospitalNum);
        try {
            //var query sql
            String sql;
            //Query
            sql = "CALL habilitarUser(" + uId + ")";
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











