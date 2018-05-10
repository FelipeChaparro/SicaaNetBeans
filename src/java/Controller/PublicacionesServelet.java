/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

import JavaBean.BeanConcordancia;
import JavaBean.BeanPublicaciones;
import JavaBean.BeasSesionesUsuarios;
import java.sql.SQLException;
import java.util.stream.Collectors;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author hariasor
 */
@WebServlet(name = "PublicacionesServelet", urlPatterns = {"/PublicacionesServelet"})
public class PublicacionesServelet extends HttpServlet {
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PublicacionesServelet() {
        super();
        // TODO Auto-generated constructor stub
    }

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
        
        JSONObject respuesta_validarTokenVO = new JSONObject();
        //JSONObject respuesta_publicacionesVO = new JSONObject();
        JSONObject respuesta = new JSONObject();


        try {
            
            BeasSesionesUsuarios bean_user_session = new BeasSesionesUsuarios();
            BeanPublicaciones bean_publicaciones = new BeanPublicaciones();
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
            
            System.out.println("User_token: "+request.getParameter("token"));
            String user_token = request.getParameter("token");
            String action = request.getParameter("action") ;
                        
            
            respuesta_validarTokenVO = bean_user_session.validar_token(user_token);
            
            if ((int) respuesta_validarTokenVO.get("code") == 0)
                respuesta = bean_publicaciones.getAllPublicacionesByPersonaID(respuesta_validarTokenVO.get("id").toString(),action);
            else
                respuesta = respuesta_validarTokenVO;
            
            System.out.println("Respuesta bean_user_session: "+respuesta);
            response.setStatus(HttpServletResponse.SC_OK);
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
            response.getWriter().print(respuesta);        
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject respuestaServicio =  new JSONObject();
        try {
            String json=request.getReader().lines().collect(Collectors.joining());
            JSONParser parser = new JSONParser();
            JSONObject user_data = (JSONObject) parser.parse(json);
            
            String accion = user_data.get("accion").toString();
            if ("prueba".equals(accion)){
                System.out.println("A");
                respuestaServicio = prueba(user_data);
            }
            else {
                System.out.println("B");
            }
            
           
        } catch (ParseException ex) {
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(respuestaServicio);
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
    private JSONObject prueba(JSONObject request) throws ServletException, IOException {
        JSONObject respuesta = new JSONObject();
        BeanConcordancia concordancia = new BeanConcordancia();
        
        Double porcentajeConfianza = concordancia.getSimilarity(request.get("palabrauno").toString(),request.get("palabrados").toString(),"NOMBRE");
        respuesta.put("concuerdan", porcentajeConfianza);
        
        return respuesta;
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
