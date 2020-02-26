package entities;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import terrains.Terrain;

public class Bomb extends Entity {
	List<Terrain> terrains;
	boolean exploded = false;
	public Bomb(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale, List<Terrain> terrains) {
		super(model, index, position, rotX, rotY, rotZ, scale);
		this.terrains = terrains;
	}
	
	public void update(){
		//super.getPosition().y += Player.GRAVITY * 0.01f;
		super.getPosition().x+=1;
		explode();
	}
	
	public void explode(){
float current_x = (super.getPosition().x > 0) ? super.getPosition().x : 1;
		
		Terrain current_terrain = terrains.get((int) Math.floor(current_x / Terrain.SIZE));
		if(super.getPosition().y <= current_terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z)){
			exploded = true;
		}
	}

}
