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
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BeanExtracionInformacion bean=new BeanExtracionInformacion();
		String url = request.getParameter("urlcvlac");
		System.out.println("la url es"+url);
		JSONObject retorno=bean.obtenerInformacion(url);
		//response.setContentType("text/plain");
		//System.out.println("retorno  :"+retorno);
		response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
		response.getWriter().print(retorno);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
