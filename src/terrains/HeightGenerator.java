package terrains;

import java.util.Random;

public class HeightGenerator {
private static final float AMPLITUDE = 70;
private static final int OCTAVES = 3;
private static final float ROUGHNESS = 0.3f;
private Random random = new Random();
private int seed = random.nextInt(1000000000);
public HeightGenerator(){

}

public float generateHeight(int x, int z){
	//returns the height
	float total = getInterplatedNoise(x/8f,z/8f) * AMPLITUDE;
	total += getInterplatedNoise(x/4f,z/4f) * AMPLITUDE/3;
	total += getInterplatedNoise(x/2f,z/2f) * AMPLITUDE/9;
	return total;
	
}

private float getInterplatedNoise(float x, float z){
	int intX = (int) x;
	int intZ = (int) z;
	float fracX = x - intX;
	float fracZ = z - intZ;
	
	float v1 = getSmoothNoise(intX, intZ);
	float v2 = getSmoothNoise(intX + 1, intZ);
	float v3 = getSmoothNoise(intX, intZ + 1);
	float v4 = getSmoothNoise(intX + 1, intZ + 1);
	float i1 = interpolate(v1, v2, fracX);
	float i2 = interpolate(v3, v4, fracX);
	return interpolate(i1,i2,fracZ);
	
}

private float interpolate(float a, float b, float blend){
	double theta = blend * Math.PI;
	float f = (float) (1f - Math.cos(theta)) * 0.5f;
	return a * (1f-f) + b * f;
}

private float getSmoothNoise(int x, int z){
	float corners = (getNoise(x-1,z-1) + getNoise(x+1,z-1) + getNoise(x-1,z+1) + getNoise(x+1,z+1)) / 16;
	float sides = (getNoise(x-1,z) + getNoise(x+1,z) + getNoise(x,z+1) + getNoise(x,z-1)) / 8;
	float middle = (getNoise(x,z)) / 4;
	return corners + sides + middle;
}

public float getNoise(int x, int z){
	random.setSeed(x * 49632 + z * 325176 + seed);
	return random.nextFloat() * 2f -1f;
}
}
