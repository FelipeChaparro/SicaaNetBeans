/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import JavaBean.BeanExtracionInformacion;
import JavaBean.BeanPublicacionesDudosas;
import JavaBean.GuardarInformacion;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                BeanPublicacionesDudosas bean=new BeanPublicacionesDudosas();
		String url = request.getParameter("token");
                String id="1";
		JSONObject retorno=bean.obtenerPublicacionDudosa(id);
		//response.setContentType("text/plain");
		//System.out.println("retorno  :"+retorno);
		response.setContentType("application/json");
		response.getWriter().print(retorno);
        
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
        
      
             System.out.println("Estoy en post");
             String json=request.getReader().lines().collect(Collectors.joining());
             BeanPublicacionesDudosas pubDudo = new BeanPublicacionesDudosas();
             JSONObject obj=pubDudo.insertarPublicacion(json);
             response.setStatus(HttpServletResponse.SC_OK);
             response.setContentType("application/json");
             PrintWriter out = response.getWriter();
             out.print(obj);
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
