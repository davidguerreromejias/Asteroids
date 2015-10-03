package com.davidguerrero.asteroids;

public class Puntuacion {
    
	private String puntos;
	private String nombre;
	
	public interface AfegirPuntuacio{
		public void afegirPuntuacio(String puntuacio);
		public void afegirNom(String nom);
		public void getPuntuacio();
		public void getNom();
	}
	
	public Puntuacion(){};
	
	public Puntuacion(String nombre, String puntos){
		this.puntos = puntos;
		this.nombre = nombre;
	}

	public String getPuntos() {
		return puntos;
	}

	public void setPuntos(String puntos) {
		this.puntos = puntos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
