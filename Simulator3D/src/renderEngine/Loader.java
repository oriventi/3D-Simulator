package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.Mesh;
import models.RawModel;

/**
 * loads data
 * @author Oriventi
 *
 */
public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	/**
	 * loads data out of vbos and creates vao
	 * @param positions of vertices
	 * @param textureCoords 
	 * @param normals of vertices
	 * @param indices of positions
	 * @return a rawModel
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeFloatInAttributeList(0, 3, positions);
		storeFloatInAttributeList(1, 2, textureCoords);
		storeFloatInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * loads HUD Vao 
	 * @param positions of hud vertices
	 * @param textureCoords of hud
	 * @return id of vao in OpenGL Buffer
	 */
	public int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeFloatInAttributeList(0, 2, positions);
		storeFloatInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	/**
	 * loads rawModel only from positions
	 * @param positions of vertices
	 * @return rawModel out of data
	 */
	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		this.storeFloatInAttributeList(0, 2, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/2);
	}
	
	/**
	 * loads a texture
	 * @param fileName
	 * @return returns id of texture in openGL buffer
	 */
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/textures/" + fileName + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	/**
	 * creates a vao and binds it
	 * @return id of vao from OpenGLs Buffer
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		vaos.add(vaoID);
		return vaoID;
	}
	
	/**
	 * stores a float in vbo
	 * @param attributeNumber
	 * @param coordinateSize
	 * @param data
	 */
	private void storeFloatInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = parseToFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * cleans all vaos and vbos from openGL buffer in ram
	 */
	public void cleanUP() {
		for(int vao: vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo: vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for(int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
	}
	
	/**
	 * unbinds a vao from OpenGLs Buffer
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * creates indices vbo
	 * @param indices
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = parseToIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	/**
	 * parses int to buffer
	 * @param data
	 * @return bufferdata
	 */
	private IntBuffer parseToIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * parses float to buffer
	 * @param data
	 * @return bufferdata
	 */
	private FloatBuffer parseToFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}