package gio.co.hospitales.citas;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import gio.co.hospitales.JavaConnectDb;

/**
 * Servlet implementation class AddCita
 */
@WebServlet("/AddCita")
public class AddCita extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static int hospitalNum = JavaConnectDb.getHospNum();

    ;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCita() {
        super();
        // TODO Auto-generated constructor stub
    }

    // Generar jsons
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Response info
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            //Obtener parametros
            String pId, dateCita, hora, sId, seg, docId;
            pId = request.getParameter("pId");
            dateCita = request.getParameter("fechaCita");
            hora = request.getParameter("hora");
            sId = request.getParameter("servicioId");
            docId = request.getParameter("docId");
            seg = request.getParameter("seg");
            // Construct data
            StringBuilder dataBuilder = new StringBuilder();
            dataBuilder.append(URLEncoder.encode("pId", "UTF-8")).append('=').append(URLEncoder.encode(pId, "UTF-8")).append("&").
                    append(URLEncoder.encode("fechaCita", "UTF-8")).append('=').append(URLEncoder.encode(dateCita, "UTF-8")).append("&").
                    append(URLEncoder.encode("hora", "UTF-8")).append('=').append(URLEncoder.encode(hora, "UTF-8")).append("&").
                    append(URLEncoder.encode("servicioId", "UTF-8")).append('=').append(URLEncoder.encode(sId, "UTF-8")).append("&").
                    append(URLEncoder.encode("seg", "UTF-8")).append('=').append(URLEncoder.encode(seg, "UTF-8")).append("&").
                    append(URLEncoder.encode("docId", "UTF-8")).append('=').append(URLEncoder.encode(docId, "UTF-8"));
            // Send data
            URL url = new URL("http://192.168.69.4:8888/proyectoDB2-Hospital1/restC/cita/addCita");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(dataBuilder.toString());
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer response2 = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response2.append(line);
            }
            JSONObject obj = new JSONObject(response2.toString());
            int answ = obj.getInt("in");
            if (answ == 1) {
                response.sendRedirect("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?in=1");
            } else {
                response.sendRedirect("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?in=0");
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            response.sendRedirect("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?err=1");
            System.err.println(e);
        }
    }
}


