package entities;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Bullet extends Entity{
	List<Terrain> terrains;
	private boolean collided = false;
	public Bullet(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale, List<Terrain> terrains) {
		super(model, index, position, rotX, rotY, rotZ, scale);
		this.terrains = terrains;
	}
	
	public boolean getCollided(){
		return collided;
	}
	public void update(){
			float distance = 12 * DisplayManager.getFrameTimeSeconds();
			float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
			 float dz = (float) (distance * Math.cos(Math.toRadians(-super.getRotY())));
			super.setPosition(new Vector3f(super.getPosition().x+=dx,super.getPosition().y,super.getPosition().z+=dz));
			collision();
	}
	
	public void collision(){
		float current_x = (super.getPosition().x > 0) ? super.getPosition().x : 1;
		
		Terrain current_terrain = terrains.get((int) Math.floor(current_x / Terrain.SIZE));
		if(super.getPosition().y <= current_terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z)){
			collided = true;
		}
	}

}
