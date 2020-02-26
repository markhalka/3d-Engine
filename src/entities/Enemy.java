package entities;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import terrains.Terrain;

public class Enemy extends Entity{


	private float speed = 0.2f;
	private List<Terrain> terrains;
	private double health = 100.0;
	public Enemy(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale, List<Terrain> terrains) {
		super(model, index, position, rotX, rotY, rotZ, scale);
		this.terrains = terrains;
	}
	
	public void update(){
		//super.getPosition().z-=speed;
	//	super.getPosition().x+=speed;
		
		super.getPosition().y = terrains.get((int)(Math.floor(super.getPosition().x / Terrain.SIZE))).getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
	}
	
	public double getHealth(){
		return health;
	}
	
	public Bullet collisionDetection(List<Bullet> bullets){
		//collision detection
		float width = 1.7f;

		for(Bullet bullet : bullets){
		if(bullet.getPosition().x + width> super.getPosition().x && 
				bullet.getPosition().x < super.getPosition().x + width &&
				bullet.getPosition().y + width > super.getPosition().y &&
				bullet.getPosition().y < super.getPosition().y + width &&
				bullet.getPosition().z + width > super.getPosition().z &&
				bullet.getPosition().z < super.getPosition().z + width){
			System.out.println(health);
			health-=1;
			return bullet;
			//COLLISION
		}
		}
		return null;
		//health goes down
	}

}
