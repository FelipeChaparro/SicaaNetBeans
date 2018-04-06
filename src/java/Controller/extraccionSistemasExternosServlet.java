/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import JavaBean.BeanExtracionInformacion;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author JUANCHO
 */
@WebServlet(name = "extraccionSistemasExternosServlet", urlPatterns = {"/extraccionSistemasExternosServlet"})
public class extraccionSistemasExternosServlet extends HttpServlet {

    public extraccionSistemasExternosServlet() {
        super();
        // TODO Auto-generated constructor stub
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // TODO Auto-generated method stub
            BeanExtracionInformacion bean=new BeanExtracionInformacion();
            String url = request.getParameter("urlcvlac");
            System.out.println("la url es"+url);
            JSONObject retorno=bean.obtenerInformacion(url);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // TODO Auto-generated method stub
            doGet(request, response);
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
