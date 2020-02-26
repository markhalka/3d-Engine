package particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = "src/particles/particleFShader.txt";


	private int location_projectionMatrix;
	private int location_numberOfRows;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_numberOfRows = super.getUniformLocation("numOfRows");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "textureOffsets");
		super.bindAttribute(6, "blendFactor");
	}
	
	void loadNumberOfRows(float num){
		super.loadFloat(location_numberOfRows, num);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
