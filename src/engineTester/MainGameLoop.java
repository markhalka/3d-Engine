package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Bomb;
import entities.Bullet;
import entities.Camera;
import entities.Enemy;
import entities.Entity;
import entities.Light;
import entities.Plane;
import entities.Player;
import entities.Tank;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.OBJFileLoader;
import particles.ComplexParticleSystem;
import particles.ParticleMaster;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {
	private static List<Entity> entities;
	private static List<Bullet> bullets;
	private static List<Terrain> terrains;
	private static List<Bomb> bombs;
	private static List<Plane> planes = new ArrayList<>();
	private static List<Tank> tanks = new ArrayList<>();
	public static TexturedModel cube_model;
	private static List<Enemy> enemies;
	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		TextMaster.init(loader);
		
		
		FontType font = new FontType(loader.loadTexture("candara"), new File("res/candara.fnt"));
		GUIText text = new GUIText("This is some text!", 3f, font, new Vector2f(0f, 0f), 1f, true);
		text.setColour(1, 0, 0);
		GuiTexture hand_held = new GuiTexture(loader.loadTexture("weapons_menu"), new Vector2f(0,0), new Vector2f(1,1));
		List<GuiTexture> weapons_menu = new ArrayList<>();
		weapons_menu.add(hand_held);
		// *********TERRAIN TEXTURE STUFF**********
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("trench_blendMap"));

		// *****************************************

		TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader),
				new ModelTexture(loader.loadTexture("rocks")));

		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);

		TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
				fernTextureAtlas);

		TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader),
				new ModelTexture(loader.loadTexture("pine")));
		bobble.getTexture().setHasTransparency(true);

		fern.getTexture().setHasTransparency(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "trench_heightmap");
		Terrain terrain2 = new Terrain(1, -1, loader, texturePack, blendMap, "trench_heightmap");
		
		 terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		//terrains.add(terrain2);

		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);

		 entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		
		//******************NORMAL MAP MODELS************************
		
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
				new ModelTexture(loader.loadTexture("crate")));
		crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
		crateModel.getTexture().setShineDamper(10);
		crateModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
				new ModelTexture(loader.loadTexture("boulder")));
		boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
		boulderModel.getTexture().setShineDamper(10);
		boulderModel.getTexture().setReflectivity(0.5f);
		
		
		//************ENTITIES*******************
		
		Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
		Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
		Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
		normalMapEntities.add(entity);
		normalMapEntities.add(entity2);
		normalMapEntities.add(entity3);
		
		Random random = new Random();
		//Random random2 = new Random();
		for(int i = 0; i < 60; i++){
			float x = random.nextFloat() * 1000;
			float z = random.nextFloat() * -500;
			float y =0;
			if(x < Terrain.SIZE){
			y = terrain.getHeightOfTerrain(x, z);
			} else {
			y = terrain2.getHeightOfTerrain(x, z);
			}
			Entity temp_entity = new Entity(fern,3, new Vector3f(x,y,z), 0,
					random.nextFloat() * 360,0,1);
			entities.add(temp_entity);
			
			float x2 = random.nextFloat() * 1000;
			float z2 = random.nextFloat() * -500;
			float y2 =0;
			if(x2 < Terrain.SIZE){
			y2 = terrain.getHeightOfTerrain(x2, z2);
			} else {
			y2 = terrain2.getHeightOfTerrain(x2, z2);
			}
			Entity temp_entity2 = new Entity(bobble,1, new Vector3f(x2,y2,z2), 0,
					random.nextFloat() * 360,0,1);
			entities.add(temp_entity2);
		}
		//TexturedModel cube = new TexturedModel(OBJLoader.loadObjModel("cube", loader), new ModelTexture(loader.loadTexture("cube_thing")));
		//entities.add(new Entity(cube,1,new Vector3f(0,0,0),0,0,0,1));
		
		//REMEMBER that the order of the models is important!
		 cube_model = new TexturedModel(OBJFileLoader.loadOBJ("cube", loader), new ModelTexture(loader.loadTexture("cube_thing")));
		List<TexturedModel> weapon_models = new ArrayList<>();
		weapon_models.add(cube_model);
		entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));
		
		//*******************OTHER SETUP***************

		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1000000, 1000000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);

		

		RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
				loader.loadTexture("playerTexture")));

		Player player = new Player(stanfordBunny, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f,weapon_models,terrains);
		entities.add(player);
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(loader, camera);

		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
	
		//**********Water Renderer Set-up************************
		
		WaterFrameBuffers buffers = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		for(int i = 0; i < 6; i ++){
			for(int j = 0; j < 6; j++){
				waters.add(new WaterTile(i * WaterTile.TILE_SIZE * 2, -j * WaterTile.TILE_SIZE * 2, 0));
			}
		}
		
		
		//****************Game Loop Below*********************
bullets = new ArrayList<>();
bombs = new ArrayList<>();
enemies = new ArrayList<>();

Enemy enemy = new Enemy(cube_model,1,new Vector3f(10,0,-100),0,0,0,1,terrains);
enemies.add(enemy);
entities.add(enemy);

Tank tank1 = new Tank(cube_model,1,new Vector3f(200,0,100),0,0,0,1,terrains);
tanks.add(tank1);
entities.add(tank1);

Plane plane1 = new Plane(cube_model,1,new Vector3f(100,0,100),0,0,0,1,terrains);
planes.add(plane1);
entities.add(plane1);
ParticleTexture texture = new ParticleTexture(loader.loadTexture("particleAtlas"),4);

ComplexParticleSystem complexParticle = new ComplexParticleSystem(50,10,0.1f,2,2f,texture);
complexParticle.setDirection(new Vector3f(0,1,0), 0.1f);
complexParticle.setLifeError(0.1f);
complexParticle.setScaleError(0.5f);
complexParticle.setSpeedError(0.25f);
complexParticle.randomizeRotation();
		while (!Display.isCloseRequested()) {
			renderer.renderShadowMap(entities, sun);
if(Mouse.isButtonDown(0)){
//shoot(player,500, cube_model);
}

complexParticle.generateParticles(new Vector3f(player.getPosition()));
//particleSystem.generateParticles(new Vector3f(player.getPosition()));
		
			player.move();
			camera.move();
			picker.update();
			ParticleMaster.update(camera);
			entity.increaseRotation(0, 1, 0);
			entity2.increaseRotation(0, 1, 0);
			entity3.increaseRotation(0, 1, 0);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			//update bullets and missles
		
		update();
		
		
		//check collisions
			Iterator<Enemy> entity_it = enemies.iterator();
			while(entity_it.hasNext()){
				Enemy entity1 = entity_it.next();
			
					if(entity1.getHealth() <= 0){
						entities.remove(entity1);
						entity_it.remove();
						System.out.print("removing");
				}
			}
			Iterator<Bullet>  bullet_it = bullets.iterator();
			while(bullet_it.hasNext()){
				Bullet bullet = bullet_it.next();
				if(bullet.getCollided()){
				//	entities.remove(bullet);
				//	bullet_it.remove();
				}
			}
			
			
			
			//weird ass shit
			buffers.bindReflectionFrameBuffer();
			
			float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -waters.get(0).getHeight()+1));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			//render refraction texture
			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, waters.get(0).getHeight()));
			
			//render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFrameBuffer();	
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));	
			waterRenderer.render(waters, camera, sun);
			
			ParticleMaster.renderParticles(camera);
			
			guiRenderer.render(guiTextures);
			if(player.render_menu){
				guiRenderer.render(weapons_menu);
			}
			TextMaster.render();
			
			DisplayManager.updateDisplay();
		}

		//*********Clean Up Below**************
		
		TextMaster.cleanUp();
		buffers.cleanUp();
		ParticleMaster.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	private static Timer timer = null;
	public static void shoot(Player player, int delay, TexturedModel cube_model){
		if(timer == null){
			timer = new Timer();
			timer.schedule(new TimerTask(){
				public void run(){
					switch(player.current_weapon){
					case 0:
						Bullet bullet = new Bullet(cube_model,1,new Vector3f(player.getPosition().x, player.getPosition().y , player.getPosition().z),0,player.getRotY(),0,1,terrains);
						entities.add(bullet);
						bullets.add(bullet);
						break;
					case 1:
						System.out.println("bomb");
						Bomb bomb = new Bomb(cube_model, 1, new Vector3f(player.getPosition().x, player.getPosition().y + 10, player.getPosition().z),0,0,0,1,terrains);
						entities.add(bomb);
						bombs.add(bomb);
						break;
					}
				
					timer = null;
				}
				
			}, delay);
		
		}
	}
	
	
	public static void addToEntities(Entity entity){
		entities.add(entity);
	}
	
	public static void removeFromEntities(Entity entity){
		entities.remove(entity);
	}
	
	public static void addToBombs(Bomb bomb){
		bombs.add(bomb);
	}
	
	public static void removeFromBombs(Bomb bomb){
		bombs.remove(bomb);
	}
	
	public static void addToBullets(Bullet bullet){
		bullets.add(bullet);
	}
	
	public static void removeFromBullets(Bullet bullet){
		bullets.remove(bullet);
	}
	
	private static void update(){
		
		for(Bullet bullet : bullets){
			bullet.update();
		}
		for(Bomb bomb : bombs){
			bomb.update();
		}
			
		for(Enemy enemy1 : enemies){
			enemy1.update();
			Bullet temp = enemy1.collisionDetection(bullets);
			if(temp != null){
				bullets.remove(temp);
			}
		}
		
		for(Tank tank : tanks){
			tank.move();
			if(Mouse.isButtonDown(0)){
			//	tank.shoot_missle();
			}
		
		}
		
		for(Plane plane : planes){
			plane.move();
		}
	}


}
