package gio.co.hospitales.citas;

import gio.co.hospitales.pacientes.*;
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
 * Servlet implementation class GetCita
 */
@WebServlet("/GetCita")
public class GetCita extends HttpServlet {

    private static final long serialVersionUID = 1L;
    //public static String hospitalNum = "1";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCita() {
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
            String parPId = request.getParameter("pId");
            String parCitaId = request.getParameter("citaId");
            String pardocId = request.getParameter("docId");
            if (((parPId != null) && !(parPId.equals("")))) {
                int pId = Integer.parseInt(parPId);
                url = "http://192.168.69.4:8888/proyectoDB2-Hospital1/restC/cita/getCita?pId=" + pId;
            } else if (((parCitaId != null) && !(parCitaId.equals("")))) {
                int citaId = Integer.parseInt(parCitaId);
                url = "http://192.168.69.4:8888/proyectoDB2-Hospital1/restC/cita/getCita?citaId=" + citaId;
            }else if (((pardocId != null) && !(pardocId.equals("")))) {
                int docId = Integer.parseInt(pardocId);
                url = "http://192.168.69.4:8888/proyectoDB2-Hospital1/restC/cita/getCita?docId=" + docId;
            } else {
                url = "http://192.168.69.4:8888/proyectoDB2-Hospital1/restC/cita/getCita";
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

