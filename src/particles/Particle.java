package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Player;
import renderEngine.DisplayManager;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	private boolean useAlphaBlending = false;
	private float elapsedTime = 0;

	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	
	float blendFactor;
	private float distance;
	private Vector3f reusableVector = new Vector3f();
	private ParticleTexture texture;
	public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale, ParticleTexture texture) {
		super();
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}
	
	public boolean getUseAlphaBlending(){
		return useAlphaBlending;
	}
	
		public Vector2f getTexOffset1() {
		return texOffset1;
	}

		public float getDistance(){
			return distance;
		}


	public void setTexOffset1(Vector2f texOffset1) {
		this.texOffset1 = texOffset1;
	}
	
	private void updateTextureCoordInfo(){
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount -1 ? index1 + 1: index1;
		this.blendFactor = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	
	}
	
	private  void setTextureOffset(Vector2f offset, int index){
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}
	
	



	public Vector2f getTexOffset2() {
		return texOffset2;
	}



	public void setTexOffset2(Vector2f texOffset2) {
		this.texOffset2 = texOffset2;
	}



	public float getBlendFactor() {
		return blendFactor;
	}



	public void setBlendFactor(float blendFactor) {
		this.blendFactor = blendFactor;
	}



	public ParticleTexture getTexture(){
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	boolean update(Camera camera){
		velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		reusableVector.set(velocity);
		reusableVector.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(reusableVector, position, position);
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		
		updateTextureCoordInfo();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return (elapsedTime < lifeLength);

	}
}
