package entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import guis.GuiTexture;
import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 40;
	private static final float TURN_SPEED = 160;
	public static final float GRAVITY = -50;
	private static final float JUMP_POWER = 18;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private int weapon_size = 1;
	public boolean render_menu = false;

	private List<Bullet> bullets = new ArrayList<>();
	private List<Bomb> bombs = new ArrayList<>();

	public int current_weapon = 0;
	private boolean isInAir = false;
	private List<TexturedModel> texturedModels;
	private List<Terrain> terrains;
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale, List<TexturedModel> texturedModels, List<Terrain> terrains) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.texturedModels = texturedModels;
		this.terrains = terrains;
	
	}
	public void loadUpWeapons(){
		
	}
	
	public List<Bullet> getBullets(){
		return bullets;
	}
	
	public List<Bomb> getBombs(){
		return bombs;
	}
	


	public void move() {
		
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrains.get((int) (Math.floor(super.getPosition().x / Terrain.SIZE))).getHeightOfTerrain(getPosition().x, getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			render_menu = true;
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
		switch_weapon(200,1);
			
			} else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
		switch_weapon(200,-1);
		}
			} else {
			render_menu = false;
		}
		
	}
	Timer timer = null;
	private void switch_weapon(int delay,int num){
		if(timer == null){
			timer = new Timer();
			timer.schedule(new TimerTask(){
				public void run(){
					current_weapon += num;
					if(current_weapon < 0){
						current_weapon = weapon_size;
					}
					timer = null;
			}
		}, delay);
		}
	}
	
	
}
	
	


