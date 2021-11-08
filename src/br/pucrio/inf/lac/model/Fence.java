package br.pucrio.inf.lac.model;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

public class Fence {
	private float up;
	private float down;
	private float right;
	private float left;

	public Fence(float pos_y, float in_pos_y, float pos_x, float in_pos_x) {
		this.up = pos_y;
		this.down = in_pos_y;
		this.right = pos_x;
		this.left = in_pos_x;
	}
	
	public float GetPosUp() {
		return this.up;
	}
	
	public float GetPosDown() {
		return this.down;
	}
	
	public float GetPosLeft() {
		return this.left;
	}
	
	public float GetPosRight() {
		return this.right;
	}
}
