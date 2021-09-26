package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.TextMeshData;
import hud.HUDText;
import mainPackage.MainGameLoop;
import renderEngine.Loader;

public class TextMaster {
	
	private static Loader loader;
	private static Map<FontType, List<HUDText>> texts = new HashMap<FontType, List<HUDText>>();
	private static FontRenderer renderer;
	
	public static void init() {
		loader = MainGameLoop.loader;
		renderer = new FontRenderer();
		
	}
	
	public static void loadText(HUDText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(),  data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<HUDText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<HUDText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void render() {
		renderer.render(texts);
	}
	
	public static void removeText(HUDText text) {
		List<HUDText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
}
