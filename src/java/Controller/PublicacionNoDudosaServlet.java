/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import JavaBean.BeanPublicacionesDudosas;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
 * @author Administrador
 */
@WebServlet(name = "PublicacionNoDudosaServlet", urlPatterns = {"/PublicacionNoDudosaServlet"})
public class PublicacionNoDudosaServlet extends HttpServlet {

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
            out.println("<title>Servlet PublicacionNoDudosaServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PublicacionNoDudosaServlet at " + request.getContextPath() + "</h1>");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject respuesta = new JSONObject();
        
        try {
            String json=request.getReader().lines().collect(Collectors.joining());
            BeanPublicacionesDudosas pubDudo = new BeanPublicacionesDudosas();
            JSONParser parser = new JSONParser();
            JSONObject data_servicio = (JSONObject) parser.parse(json);
          
            System.out.println("Data llegada: "+data_servicio);
            
            respuesta = pubDudo.publicaiconDudosaDiferente(data_servicio);

        } catch (ParseException ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error en Data Parser");
            Logger.getLogger(PublicacionNoDudosaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(respuesta);  
        out.flush();
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
