/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import JavaBean.BeanProfesores;
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

/**
 *
 * @author hp
 */
@WebServlet(name = "ProfesoresServlet", urlPatterns = {"/ProfesoresServlet"})
public class ProfesoresServlet extends HttpServlet {

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
            out.println("<title>Servlet ProfesoresServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfesoresServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject respuesta = new JSONObject();
        
        try {
            BeanProfesores bean_profesores = new BeanProfesores();
            
            String action = request.getParameter("action").isEmpty()?null:request.getParameter("action");
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
                 
            switch (action) {
                case "INFO_BASICA_DPTO":
                    String idDepartamento = request.getParameter("idDepartamento").isEmpty()?null:request.getParameter("idDepartamento");
                    
                    if (idDepartamento != null) 
                        respuesta = bean_profesores.getInfoBasicaProfesoresByDepartamento(idDepartamento);
                    else {
                        respuesta.put("code", 9995);
                        respuesta.put("description", "No se ingresaron los parametros requeridos");
                    }
                break;
                case "INFO_BASICA_FACU":
                    String idFacultad = request.getParameter("idFacultad").isEmpty()?null:request.getParameter("idFacultad");
                    
                    if (idFacultad != null) 
                        respuesta = bean_profesores.getInfoBasicaProfesoresByFacultad(idFacultad);
                    else {
                        respuesta.put("code", 9995);
                        respuesta.put("description", "No se ingresaron los parametros requeridos");
                    }
                break;
                case "INFO_BASICA_NAME":
                    String nombre = request.getParameter("nombre").isEmpty()?null:request.getParameter("nombre");
                    
                    if (nombre != null) 
                        respuesta = bean_profesores.getInfoBasicaProfesoresByName(nombre);
                    else {
                        respuesta.put("code", 9995);
                        respuesta.put("description", "No se ingresaron los parametros requeridos");
                    }
                break;
                case "INFO_BASICA_TOKEN":
                    String token = request.getParameter("token").isEmpty()?null:request.getParameter("token");

                    if (token != null) 
                        respuesta = bean_profesores.getInfoBasicaProfesoresByToken(token);
                    else {
                        respuesta.put("code", 9995);
                        respuesta.put("description", "No se ingresaron los parametros requeridos");
                    }
                break;
                case "INFO_DETALLE":
                    String idProfesor = request.getParameter("idProfesor").isEmpty()?null:request.getParameter("idProfesor");

                    if (idProfesor != null) 
                        respuesta = bean_profesores.getDetalleProfesorById(idProfesor);
                    else {
                        respuesta.put("code", 9995);
                        respuesta.put("description", "No se ingresaron los parametros requeridos");
                    }
                break;
            }
             
            System.out.println("FIN ProfesoresServlet: "+respuesta);
            
        }
        catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("FIN ProfesoresServlet: "+respuesta);   
            Logger.getLogger(NubePalabrasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(respuesta);
            out.flush(); 
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
