package com.davidguerrero.asteroids;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

class Grafic {
	
	private View view;

	private Drawable drawable;
	private double tiempoMisil;
	private double posX;
	private double posY;
	private double incX;
	private int radioColision;
	private double incY;
	private int angle;
	private int rotacio;
	private int ample;
	private int alcada;
	private int tipus;
	private static int MAX_VELOCIDAD = 20;

	public void dibuixaGrafic(Canvas canvas) {
		canvas.save();
		int x = (int) (posX + ample / 2);
		int y = (int) (posY + alcada / 2);
		canvas.rotate((float) angle, (float) x, (float) y);
		drawable.setBounds((int) posX, (int) posY, (int) posX + ample,
				(int) posY + alcada);
		drawable.draw(canvas);
		canvas.restore();
		int rInval = (int) Math.hypot(ample, alcada) / 2 + MAX_VELOCIDAD;
		view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval);
	}

	public Grafic(View view, Drawable drawable, int tip) {
		this.view = view;
		this.drawable = drawable;
		ample = drawable.getIntrinsicWidth();
		alcada = drawable.getIntrinsicHeight();
		radioColision = (alcada + ample) / 4;
		tipus = tip;
	}

	public void incrementaPosNave(double plus, double duracion) {
		posX = posX / duracion + plus;
		if (posX < -ample / 2)
			posX = view.getWidth() - ample / 2;
		if (posX > view.getWidth() - ample / 2)
			posX = -ample / 2;
		posY += incY * plus;
		if (posY < -alcada / 2)
			posY = view.getHeight() - alcada / 2;
		if (posY > view.getHeight() - alcada / 2)
			posY = -alcada / 2;
		angle += rotacio * plus;
	}

	public void incrementaPos(double plus) {
		posX += incX * plus;
		if (posX < -ample / 2)
			posX = view.getWidth() - ample / 2;
		if (posX > view.getWidth() - ample / 2)
			posX = -ample / 2;
		posY += incY * plus;
		if (posY < -alcada / 2)
			posY = view.getHeight() - alcada / 2;
		if (posY > view.getHeight() - alcada / 2)
			posY = -alcada / 2;
		//angle += rotacio * plus; 
	}

	public double distancia(Grafic g) {
		return Math.hypot(posX - g.posX, posY - g.posY);
	}

	public boolean verificaColision(Grafic g) {
		return (distancia(g) < (radioColision + g.radioColision));
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getIncX() {
		return incX;
	}

	public void setIncX(double incX) {
		this.incX = incX;
	}

	public double getIncY() {
		return incY;
	}

	public void setIncY(double incY) {
		this.incY = incY;
	}

	public int getAngulo() {
		return angle;
	}

	public void setAngulo(int angulo) {
		this.angle = angulo;
	}

	public int getRotacion() {
		return rotacio;
	}

	public void setRotacion(int rotacion) {
		this.rotacio = rotacion;
	}

	public int getAncho() {
		return ample;
	}

	public void setAncho(int ancho) {
		this.ample = ancho;
	}

	public int getAlto() {
		return alcada;
	}

	public void setAlto(int alto) {
		this.alcada = alto;
	}

	public int getRadioColision() {
		return radioColision;
	}

	public void setRadioColision(int radioColision) {
		this.radioColision = radioColision;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public void settiempoMisil(double tmp) {
		this.tiempoMisil = tmp;
	}

	public double gettiempoMisil() {
		return tiempoMisil;
	}

	public static int getMaxVelocidad() {
		return MAX_VELOCIDAD;
	}

	public void setMaxVelocidad(int maxvelocidad) {
		MAX_VELOCIDAD = maxvelocidad;
	}

	public int getTipus() {
		return tipus;
	}

	public void setTipus(int tip) {
		this.tipus = tip;
	}
}