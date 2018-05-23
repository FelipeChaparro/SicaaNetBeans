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
@WebServlet(name = "PublicacionDudosaServlet", urlPatterns = {"/PublicacionDudosaServlet"})
public class PublicacionDudosaServlet extends HttpServlet {


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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        BeanPublicacionesDudosas bean = new BeanPublicacionesDudosas();
        
	JSONObject respueta_publicacionesDudosas = bean.obtenerPublicacionDudosa(request.getParameter("token"));
        
        System.out.println("Respuesta PublicacionDudosaServlet:: "+respueta_publicacionesDudosas);
        response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(respueta_publicacionesDudosas);
        out.flush();       
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
       
        JSONObject respuestaServicio =  new JSONObject();
        BeanPublicacionesDudosas publicacionesDudosas_bean = new BeanPublicacionesDudosas();
        JSONParser parser = new JSONParser();

        try {
            String json=request.getReader().lines().collect(Collectors.joining());
            JSONObject publicacion = (JSONObject) parser.parse(json);
            
            System.out.println("Publciaicon llega: "+publicacion);
            respuestaServicio = publicacionesDudosas_bean.remplazarPublicacion(publicacion,publicacion.get("publicacionID").toString());
            
        } catch (ParseException ex) {response.setContentType("application/json");
            respuestaServicio.put("code", 9997);
            respuestaServicio.put("description", "Error en sistema");
            System.out.println("retorno: "+respuestaServicio);
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(respuestaServicio);
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
