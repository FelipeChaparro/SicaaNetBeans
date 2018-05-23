/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dao.parametrosSistemaDao;
import JavaBean.BeanNubePalabras;
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
 * @author hariasor
 */
@WebServlet(name = "NubePalabrasServlet", urlPatterns = {"/NubePalabrasServlet"})
public class NubePalabrasServlet extends HttpServlet {

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
            out.println("<title>Servlet NubePalabrasServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NubePalabrasServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        JSONObject respuesta = new JSONObject();
        JSONObject respuesta_parametrosSistemaDao = new JSONObject();
        JSONObject respuesta_bean_nube_palabras = new JSONObject();

        try {
            
            parametrosSistemaDao parametros_sistema_dao = new parametrosSistemaDao();
            BeanNubePalabras bean_nube_palabras = new BeanNubePalabras();
            
            String departamento = request.getParameter("Departamento").isEmpty()?null:request.getParameter("Departamento");
            String facultad = request.getParameter("Facultad").isEmpty()?null:request.getParameter("Facultad");
            String nombre = request.getParameter("Nombre").isEmpty()?null:request.getParameter("Nombre");
            boolean usarDpto = Boolean.parseBoolean(request.getParameter("UsarDpto"));
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
                        
            respuesta_parametrosSistemaDao = parametros_sistema_dao.getParametrosSistema();
            
            if ((int) respuesta_parametrosSistemaDao.get("code") == 0) {
                //(int) respuesta_parametrosSistemaDao.get("maximoNubePalabras")
                respuesta_bean_nube_palabras = bean_nube_palabras.getAllPalabarsByFiltrosBean(departamento, facultad, nombre, usarDpto, 100);
                respuesta = respuesta_bean_nube_palabras;
            }
            else
                respuesta = respuesta_parametrosSistemaDao;

            System.out.println("FIN NubePalabrasServlet: "+respuesta);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(respuesta);
            out.flush(); 
            
        }catch (SQLException ex) {
            response.setContentType("application/json");
            respuesta.put("code", 9998);
            respuesta.put("description", "Error en base de datos");
            System.out.println("retorno: "+respuesta);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(respuesta);        
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            response.setContentType("application/json");
            respuesta.put("code", 9996);
            respuesta.put("description", "Error en base de IOException");
            System.out.println("retorno: "+respuesta);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(respuesta);     
            Logger.getLogger(NubePalabrasServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            response.setContentType("application/json");
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("retorno: "+respuesta);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(respuesta);     
            Logger.getLogger(NubePalabrasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        processRequest(request, response);
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
