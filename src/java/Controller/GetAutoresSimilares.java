/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import JavaBean.BeanConcordanciaAutores;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author juanchaparro
 */
@WebServlet(name = "GetAutoresSimilares", urlPatterns = {"/GetAutoresSimilares"})
public class GetAutoresSimilares extends HttpServlet {

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
        BeanConcordanciaAutores bca = new BeanConcordanciaAutores();
        JSONObject respuesta_servlet = new JSONObject();
        String autorName = request.getParameter("autor");
        JSONObject autor = new JSONObject(); 
        JSONArray arreglo = new JSONArray();
        autor.put("Nombre", autorName);
        arreglo.add(autor);
        JSONObject autores = new JSONObject();
        autores.put("autor", arreglo);
        JSONArray arregloUsuarios = bca.buscarAutoresPorNombre(autores);
        System.out.println(arregloUsuarios);
        respuesta_servlet.put("code", 0);
        respuesta_servlet.put("description", "Todo salió bien");
        respuesta_servlet.put("autores", arregloUsuarios);
     
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(respuesta_servlet);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
