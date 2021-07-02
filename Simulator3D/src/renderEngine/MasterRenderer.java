package renderEngine;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.LightManager;
import models.Mesh;
import shaders.ShaderProgram;
import shaders.StaticShader;
import shadows.ShadowMapMasterRenderer;

public class MasterRenderer {

	private StaticShader shader =new StaticShader();
	private Renderer renderer;
	private ShadowMapMasterRenderer shadowMapRenderer;

	
	private Vector3f skyColor = new Vector3f(0, 0.5f, 1);
	
	private Map<Mesh, List<Entity>> entities = new HashMap<Mesh, List<Entity>>();
	
	
	public Matrix4f projectionMatrix;
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;
	
	public MasterRenderer(Camera cam, Loader loader) {
		createProjectionMatrix();
		renderer = new Renderer(shader, loader);
		shadowMapRenderer = new ShadowMapMasterRenderer(cam);
	}
	
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	public void render(List<Light> lights, Camera camera) {
		prepare();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix());
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
	
	public void renderShadowMap(List<Entity> entityList) {
		for(int i = 0; i < entityList.size(); i++) {
			processEntity(entityList.get(i));
		}
		shadowMapRenderer.render(entities, LightManager.getSun());
		entities.clear();
	}

	public int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}
	
	public void cleanUp() {
		shader.cleanUp();
		shadowMapRenderer.cleanUp();
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
