/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import java.util.List;

/**
 *
 * @author JUANCHO
 */
public class Publicacion {
  private String titulo;
   private String fecha;
   private List<String>autores;
   private String tipo;
   
	public String getTitulo() {
	return titulo;
}

public void setTitulo(String titulo) {
	this.titulo = titulo;
}

public String getFecha() {
	return fecha;
}

public void setFecha(String fecha) {
	this.fecha = fecha;
}

public List<String> getAutores() {
	return autores;
}

public void setAutores(List<String> autores) {
	this.autores = autores;
}

public String getTipo() {
	return tipo;
}

public void setTipo(String tipo) {
	this.tipo = tipo;
}

	public Publicacion() {
		// TODO Auto-generated constructor stub
	}

}
