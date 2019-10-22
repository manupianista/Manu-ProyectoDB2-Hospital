package gio.co.hospitales.citas;

import gio.co.hospitales.pacientes.*;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Servlet implementation class UpdateCita
 */
@WebServlet("/UpdateCita")
public class UpdateCita extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateCita() {
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
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            //Obtener parametros
            String citaId,dateCita,hora,sId,diag,pasos,res,obsrv,meds,docId;
            citaId = request.getParameter("citaId");
            dateCita = request.getParameter("fechaCita");
            hora = request.getParameter("hora");
            sId = request.getParameter("servicioId");
            diag = request.getParameter("diag");
            pasos = request.getParameter("pasosASeguir");
            res = request.getParameter("res");
            obsrv = request.getParameter("obsrv");
            meds = request.getParameter("meds");
            docId = request.getParameter("docId");
            // Construct data
            StringBuilder dataBuilder = new StringBuilder();
            dataBuilder.append(URLEncoder.encode("citaId", "UTF-8")).append('=').append(URLEncoder.encode(citaId, "UTF-8")).append("&").
                    append(URLEncoder.encode("fechaCita", "UTF-8")).append('=').append(URLEncoder.encode(dateCita, "UTF-8")).append("&").
                    append(URLEncoder.encode("hora", "UTF-8")).append('=').append(URLEncoder.encode(hora, "UTF-8")).append("&").
                    append(URLEncoder.encode("servicioId", "UTF-8")).append('=').append(URLEncoder.encode(sId, "UTF-8")).append("&").
                    append(URLEncoder.encode("diag", "UTF-8")).append('=').append(URLEncoder.encode(diag, "UTF-8")).append("&").
                    append(URLEncoder.encode("pasosASeguir", "UTF-8")).append('=').append(URLEncoder.encode(pasos, "UTF-8")).append("&").
                    append(URLEncoder.encode("res", "UTF-8")).append('=').append(URLEncoder.encode(res, "UTF-8")).append("&").
                    append(URLEncoder.encode("obsrv", "UTF-8")).append('=').append(URLEncoder.encode(obsrv, "UTF-8")).append("&").
                    append(URLEncoder.encode("meds", "UTF-8")).append('=').append(URLEncoder.encode(meds, "UTF-8")).append("&").
                    append(URLEncoder.encode("docId", "UTF-8")).append('=').append(URLEncoder.encode(docId, "UTF-8"));
            // Send data
            URL url = new URL("http://192.168.69.4:8888/proyectoDB2-Hospital1/restC/cita/updateCita");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
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
            int answ = obj.getInt("up");

            if (answ == 1) {
                response.sendRedirect("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?up=1");
            } else {
                response.sendRedirect("http://192.168.69.4:8888/proyectoDB2-Hospital1/citas_h.jsp?up=0");
            }

            //out.println(answ);
            //out.println(response2.toString());
            wr.close();
            rd.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
