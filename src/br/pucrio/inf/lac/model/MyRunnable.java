package br.pucrio.inf.lac.model;

import br.pucrio.inf.lac.controller.FilterCEP;

public class MyRunnable implements Runnable {
	private float up;
	private float down;
	private float right;
	private float left;
	private int id;

	public MyRunnable(int id, float pos_y, float in_pos_y, float pos_x, float in_pos_x) {
		this.up = pos_y;
		this.down = in_pos_y;
		this.right = pos_x;
		this.left = in_pos_x;
		this.id = id;
		System.out.println("[Thread - "+id+"]Criando Filtro para a seguinte coordenadas "+this.up+" "+this.down+" "+this.right+" "+this.left);
	}
	
	public void run() {
		new FilterCEP(this.up,this.down,this.right,this.left);
	}
}
