/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import JavaBean.BeanDesactivarPublicacion;
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
@WebServlet(name = "DesactivarPublicacionServlet", urlPatterns = {"/DesactivarPublicacionServlet"})
public class DesactivarPublicacionServlet extends HttpServlet {

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
            out.println("<title>Servlet DesactivarPublicacionServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DesactivarPublicacionServlet at " + request.getContextPath() + "</h1>");
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
        JSONObject mInfo;
        JSONObject respuesta_servlet = new JSONObject();
        JSONParser mParser = new JSONParser();
        try {
            BeanDesactivarPublicacion mBean = new BeanDesactivarPublicacion();
            mInfo = (JSONObject) mParser.parse(request.getReader());
            respuesta_servlet = mBean.desactivarPublicacion(mInfo);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
        } catch(IOException | ParseException e) {
            respuesta_servlet.put("code", 9999);
            response.setCharacterEncoding("UTF-8");
            respuesta_servlet.put("description", "Error en Servlet");
            System.out.println("Error: " + e);
        } finally {
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
