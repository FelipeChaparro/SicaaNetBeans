/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import JavaBean.BeanActualizarPublicacion;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author juanchaparro
 */
@WebServlet(name = "ActualizarPublicacionServlet", urlPatterns = {"/ActualizarPublicacionServlet"})
public class ActualizarPublicacionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ActualizarInformacionServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ActualizarInformacionServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject respuesta_servlet = new JSONObject();
        JSONObject mObject;
        BeanActualizarPublicacion mBean = new BeanActualizarPublicacion();
        try {
            JSONParser mParser = new JSONParser();
            mObject = (JSONObject) mParser.parse(request.getReader());
            respuesta_servlet = mBean.actualizarPublicacion(mObject);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException | ParseException e) {
            System.out.println(e);
            respuesta_servlet.put("code", 9999);
            response.setCharacterEncoding("UTF-8");
            respuesta_servlet.put("description","Error del Servlet");
        } finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(respuesta_servlet);
            out.flush();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
