/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gio.co.hospital.ws.ClienteSeguro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author C.V
 */

@WebServlet("/GetCliente")

public class GetCliente extends HttpServlet {
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCliente() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    // Generar jsons
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Response info
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String url;
            String parClienteId = request.getParameter("dpi");
            if ((parClienteId != null) && !(parClienteId.equals(""))) {
                long cId = Long.parseLong(request.getParameter("dpi"));
                url = "http://25.65.236.60:8080/proyectoDB2-seguro/restC/cliente/getCliente?dpi=" + cId;
                //url = "http://192.168.69.4:8888/proyectoDB2-seguro/restC/cliente/getCliente?dpi=" + cId;
            } else {
                url = "http://25.65.236.60:8080/proyectoDB2-seguro/restC/cliente/getCliente/";
                //url = "http://192.168.69.4:8888/proyectoDB2-seguro/restC/cliente/getCliente/";
            }
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response2 = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response2.append(inputLine);
            }
            in.close();
            //print in String
            out.println(response2.toString());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    
}













