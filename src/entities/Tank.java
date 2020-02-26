package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import engineTester.MainGameLoop;
import models.TexturedModel;
import terrains.Terrain;

public class Tank extends Entity{
	private List<Bullet> bullets = new ArrayList<>();
	private List<Bomb> bombs = new ArrayList<>();
	private int max_bullets = 50;
	private int max_bombs = 10;
	private int reload_time = 1000;
	private boolean isEnemy = false;
	private double health = 100;
	private int missle_clip = max_bombs;
	private int bullet_clip = max_bullets;
	Audio gun;
	Audio missle;
	private List<Terrain> terrains = new ArrayList<>();
	public Tank(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale, List<Terrain> terrains) {
		super(model, index, position, rotX, rotY, rotZ, scale);
		this.terrains = terrains;
		try{
		      gun = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/gun.wav"));
		      missle = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/tank.wav"));
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void move(Player player){
		
	}
	
	public void move(){
		super.getPosition().y = terrains.get((int) Math.floor(super.getPosition().x / Terrain.SIZE)).getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
	}
	
	Timer gunTimer = null;
	private void soundPlayer(){
		if(gunTimer == null){
			gunTimer = new Timer();
			gunTimer.schedule(new TimerTask(){
				public void run(){
					try{
				
					} catch(Exception e){}
					gunTimer = null;
				}
			}, 500);
		}
	}
	
	Timer reload_timer = null;
	private void reload(){
		if(reload_timer == null){
			reload_timer = new Timer();
			reload_timer.schedule(new TimerTask(){
				public void run(){
				//reload bullets	
					System.out.println("reladnig");
					reload_timer = null;
				}
			}, reload_time);
		}
	}
	
	Timer missle_timer = null;
	public void shoot_missle(){
		if(missle_timer == null){
			missle_timer = new Timer();
			missle_timer.schedule(new TimerTask(){
				public void run(){
					//shoot bullets
					fire();
					missle_clip -=1;
					if(missle_clip <= 0){
						reload();
					}
					missle_timer = null;
				}
			}, 2000);
		}
	}
	
	Timer bullet_timer = null;
	public void shoot_bullet(){
		if(bullet_timer == null){
			bullet_timer = new Timer();
			bullet_timer.schedule(new TimerTask(){
				public void run(){
					//shoot bullets
					shoot();
					bullet_clip -= 1;
					if(bullet_clip <= 0){
						reload();
					}
					bullet_timer = null;
				}
			}, 500);
		}
	}
	private void shoot(){
		gun.playAsSoundEffect(1.0f, 1.0f, false);
		SoundStore.get().poll(0);
		Bullet bullet = new Bullet(MainGameLoop.cube_model, 1, new Vector3f(super.getPosition()), 0,super.getRotY(),0,1,terrains);
		bullets.add(bullet);
		System.out.println("bullet");
		MainGameLoop.addToEntities(bullet);
		MainGameLoop.addToBullets(bullet);
		
	}
	private void fire(){
		missle.playAsMusic(1.0f, 1.0f, false);
		SoundStore.get().poll(0);
		Bomb missle = new Bomb(MainGameLoop.cube_model, 1, new Vector3f(super.getPosition()), 0,0,0,1,terrains);
		bombs.add(missle);
		MainGameLoop.addToEntities(missle);
		MainGameLoop.addToBombs(missle);
	}
	public List<Bullet> getBullets(){
		return bullets;
	}
	public List<Bomb> getBombs(){
		return bombs;
	}
}
