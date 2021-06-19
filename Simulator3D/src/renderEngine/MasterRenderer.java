package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.Mesh;
import shaders.StaticShader;
import shadows.ShadowMapMasterRenderer;

public class MasterRenderer {

	private StaticShader shader =new StaticShader();
	private Renderer renderer;
	
	private Map<Mesh, List<Entity>> entities = new HashMap<Mesh, List<Entity>>();
	
	
	public Matrix4f projectionMatrix;
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;
	
	public MasterRenderer(Camera cam, Loader loader) {
		createProjectionMatrix();
		renderer = new Renderer(shader, loader);
	}
	
	public void render(List<Light> lights, Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	public void processEntity(Entity entity) {
		Mesh entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!= null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void renderShadowMap(List<Entity> entityList, Light sun) {
		for(Entity entity : entityList) {
			processEntity(entity);
		}
		
	}
	
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		
	}
}
