package World;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.EntityShadowList;
import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.Maths;

public class NatureFoundation extends TileContent{
	
	private float xpos, ypos;
	private int rotation;
	
	private Entity[] foliage;
	private Entity foundationEntity;
	
	private String buildingID;

	public NatureFoundation(int xtile, int ytile, int foliageCount) {
		super(xtile, ytile);
		
		this.xpos = (xtile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		this.ypos = (ytile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		
		buildingID = "8";
		
		foundationEntity = new Entity(MeshContainer.natureFoundation, new Vector3f(xpos, 0, ypos), 0, rotation, 0, 1);
		
		if(foliageCount > 4) {
			foliageCount = 4;
		}
		foliage = new Entity[foliageCount];
		
		generateFoliageEntities();
		
	}
	
	private void generateFoliageEntities() {
		int foliageXPos = 0;
		int foliageYPos = 0;
		int foliageRot = 0;
		float foliageSize = 1.f;
		for(int i = 0; i < foliage.length; i++) {
			foliageXPos = Maths.getRandomBetween(1 + (int)(10/foliage.length) * i, (int)(10/foliage.length) * i + 1);
			foliageYPos = Maths.getRandomBetween(1, 9);
			foliageRot = Maths.getRandomBetween(0, 360);
			foliageSize = Maths.getRandomBetween(7, 10) / 10.f;
			
			foliage[i] = new Entity(getRandomFoliageMesh(), new Vector3f(xpos + foliageXPos - 5, 0, ypos + foliageYPos - 5),
					0, foliageRot, 0, foliageSize);
			EntityShadowList.addEntity(foliage[i]);
		}
		
	}
	
	private Mesh getRandomFoliageMesh() {
		int randomNumber = Maths.getRandomBetween(0, 3);
		switch(randomNumber) {
			case 0:
				return MeshContainer.great_tree_green;
			case 1:
				return MeshContainer.tree_green_2;
			case 2:
				return MeshContainer.stone_1;
			default:
				return MeshContainer.tree_green_2;
		}
	}
	
	

	@Override
	public void destroy() {
		for(int i = 0; i < foliage.length; i++) {
			EntityShadowList.removeEntity(foliage[i]);
		}
	}

	@Override
	public void render(MasterRenderer renderer) {
		renderer.processEntity(foundationEntity);
		for(int i = 0; i < foliage.length; i++) {
			renderer.processEntity(foliage[i]);
		}
	}

	@Override
	public void increaseRotation() {
	}

	@Override
	public String getBuildingID() {
		// TODO Auto-generated method stub
		return buildingID;
	}
}
