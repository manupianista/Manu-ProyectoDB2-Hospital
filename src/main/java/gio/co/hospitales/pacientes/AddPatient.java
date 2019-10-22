package gio.co.hospitales.pacientes;

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
import gio.co.hospitales.JavaConnectDb;

/**
 * Servlet implementation class AddPatient
 */
@WebServlet("/AddPatient")
public class AddPatient extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static int hospitalNum = JavaConnectDb.getHospNum();

    ;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddPatient() {
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
            String name, lastName, dir, bDate, segNum, tel, dpi, docId, asegNum, asegType;
            name = request.getParameter("nameP");
            lastName = request.getParameter("lastNameP");
            dir = request.getParameter("dir");
            tel = request.getParameter("tel");
            bDate = request.getParameter("bDate");
            dpi = request.getParameter("dpi");
            segNum = request.getParameter("segNum");
            docId = request.getParameter("docId");
            asegNum = request.getParameter("asegNum");
            asegType = request.getParameter("asegType");
            // Construct data
            StringBuilder dataBuilder = new StringBuilder();
            dataBuilder.append(URLEncoder.encode("nameP", "UTF-8")).append('=').append(URLEncoder.encode(name, "UTF-8")).append("&").
                    append(URLEncoder.encode("lastNameP", "UTF-8")).append('=').append(URLEncoder.encode(lastName, "UTF-8")).append("&").
                    append(URLEncoder.encode("dir", "UTF-8")).append('=').append(URLEncoder.encode(dir, "UTF-8")).append("&").
                    append(URLEncoder.encode("tel", "UTF-8")).append('=').append(URLEncoder.encode(tel, "UTF-8")).append("&").
                    append(URLEncoder.encode("bDate", "UTF-8")).append('=').append(URLEncoder.encode(bDate, "UTF-8")).append("&").
                    append(URLEncoder.encode("dpi", "UTF-8")).append('=').append(URLEncoder.encode(dpi, "UTF-8")).append("&").
                    append(URLEncoder.encode("segNum", "UTF-8")).append('=').append(URLEncoder.encode(segNum, "UTF-8")).append("&").
                    append(URLEncoder.encode("docId", "UTF-8")).append('=').append(URLEncoder.encode(docId, "UTF-8")).append("&").
                    append(URLEncoder.encode("asegNum", "UTF-8")).append('=').append(URLEncoder.encode(asegNum, "UTF-8")).append("&").
                    append(URLEncoder.encode("asegType", "UTF-8")).append('=').append(URLEncoder.encode(asegType, "UTF-8"));
            // Send data
            URL url = new URL("http://192.168.69.4:8888/proyectoDB2-Hospital1/restP/patient/addPatient");
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
                response.sendRedirect("http://192.168.69.4:8888/proyectoDB2-Hospital1/pacientes_h.jsp?in=1");
            } else {
                response.sendRedirect("http://192.168.69.4:8888/proyectoDB2-Hospital1/pacientes_h.jsp?in=0");
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    protected void getInfoCookies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookiesInf = request.getCookies();
        if (cookiesInf != null) {
            for (Cookie cookie : cookiesInf) {
                if (cookie.getName().equals("hospNum")) {
                    hospitalNum = Integer.parseInt(cookie.getValue());
                }
            }
        }
    }
}

