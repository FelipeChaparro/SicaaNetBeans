/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import JavaBean.BeanGuardarPublicacion;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author juanchaparro
 */
@WebServlet(name = "GuardarPublicacion", urlPatterns = {"/GuardarPublicacion"})
public class GuardarPublicacion extends HttpServlet {
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
        JSONObject respuestaServlet = new JSONObject();
        try {
            String myJson = request.getReader().lines().collect(Collectors.joining());
            BeanGuardarPublicacion beansp = new BeanGuardarPublicacion();
            respuestaServlet = beansp.guardarPublicacionDB(myJson); 
            System.out.println("Respuesta de BeanGuardarPublicacion: " + respuestaServlet);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(respuestaServlet);
            out.flush();
            
        } catch (ParseException ex) {
            response.setContentType("application/json");
            respuestaServlet.put("code", 9998);
            respuestaServlet.put("description", "Error en base de datos");
            System.out.println("Servlet GuardarPublicacion: " + respuestaServlet);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(respuestaServlet);  
            Logger.getLogger(GuardarPublicacion.class.getName()).log(Level.SEVERE, null, ex);
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
