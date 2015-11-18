package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.ModelLoader;


public class TextMaster
{
	private static Map<FontType, List<GUIText>> _texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer _renderer;
	
	public static void Init()
	{
		_renderer = new FontRenderer();
	}
	
	public static void Render()
	{
		_renderer.Render(_texts);
	}
	
	public static void LoadText(GUIText text)
	{
		FontType font = text.GetFont();
		TextMeshData data = font.LoadText(text);
		int vao = ModelLoader.LoadToVAO(data.GetVertexPositions(), data.GetTextureCoords());
		text.SetMeshInfo(vao, data.GetVertexCount());
		
		List<GUIText> textBatch = _texts.get(font);
		if (textBatch == null)
		{
			textBatch = new ArrayList<GUIText>();
			_texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void RemoveText(GUIText text)
	{
		List<GUIText> textBatch = _texts.get(text.GetFont());
		textBatch.remove(text);
		
		if (textBatch.isEmpty())
		{
			_texts.remove(text.GetFont());
			// TODO: Delete VAO and related VBOs for text
		}
	}
	
	public static void CleanUp()
	{
		_renderer.CleanUp();
	}
	
}
