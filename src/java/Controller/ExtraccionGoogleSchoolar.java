package Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import JavaBean.BeanExtraerInformacionGoogleSchoolar;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet implementation class ExtraccionGoogleSchoolar
 */
@WebServlet("/ExtraccionGoogleSchoolar")
public class ExtraccionGoogleSchoolar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExtraccionGoogleSchoolar() {
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
                BeanExtraerInformacionGoogleSchoolar bean=new BeanExtraerInformacionGoogleSchoolar();
                String url = request.getParameter("urlschoolar");
                System.out.println("la url de google schoolar es: "+url);
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
                Logger.getLogger(ExtraccionGoogleSchoolar.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
