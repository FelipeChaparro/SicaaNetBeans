package Controller;

import JavaBean.BeanLogin;

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
 * Servlet implementation class login_servelet
 */
@WebServlet("/login_servelet")
public class login_servelet extends HttpServlet {
  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login_servelet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // TODO Auto-generated method stub
            response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        JSONObject respuesta_loginVO = new JSONObject();
                
        BeanLogin beanLogin = new BeanLogin();
        
        try {
            String json=request.getReader().lines().collect(Collectors.joining());
            JSONParser parser = new JSONParser();
            JSONObject user_data = (JSONObject) parser.parse(json);
            
            System.out.println("Data: "+user_data.get("user_name"));
            
            if (user_data.get("action").toString().equals("LOGIN"))
                respuesta_loginVO = beanLogin.loginInByMail(user_data);
            else if (user_data.get("action").toString().equals("LOAD_INFORMACION_BASICA"))
                respuesta_loginVO = beanLogin.cargarInformacionBasicaByToken(user_data);
            
            System.out.println("Respuesta login_servelet:: "+respuesta_loginVO);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(respuesta_loginVO);
            out.flush();                
        } catch (ParseException ex) {
            response.setContentType("application/json");
            respuesta_loginVO.put("code", 9998);
            respuesta_loginVO.put("description", "Error de parser respuesta");
            System.out.println("retrno: "+respuesta_loginVO);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(respuesta_loginVO);        
            Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception ex) {
            response.setContentType("application/json");
            respuesta_loginVO.put("code", 9997);
            respuesta_loginVO.put("description", "Error de sistema");

            System.out.println("retrno: "+respuesta_loginVO);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(respuesta_loginVO);        
            Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
