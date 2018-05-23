/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;


import JavaBean.BeanExtraccionResearch;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "ExtraccionResearchServlet", urlPatterns = {"/ExtraccionResearchServlet"})
public class ExtraccionResearchServlet extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExtraccionResearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            JSONObject retorno = new JSONObject();
            
            try {
                // TODO Auto-generated method stub
                BeanExtraccionResearch bean= new BeanExtraccionResearch();
                String url = request.getParameter("urlresearch");
                System.out.println("la url es"+url);
                retorno=bean.obtenerInformacion(url);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(retorno);
            } catch (SQLException ex) {
                retorno.put("code", 9998);
                retorno.put("description", "Error en base de datos");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(retorno);
                Logger.getLogger(ExtraccionResearchServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
