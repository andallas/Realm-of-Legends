package models;

import renderEngine.TextureLoader;
import textures.ModelTexture;

public class TexturedModel
{
	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel model, ModelTexture texture)
	{
		this.rawModel = model;
		this.texture = texture;
	}
	
	public TexturedModel(RawModel model)
	{
		this.rawModel = model;
		this.texture = new ModelTexture(TextureLoader.LoadTexture("white.png"));;
	}
	
	public RawModel GetRawModel()
	{
		return this.rawModel;
	}
	
	public ModelTexture GetTexture()
	{
		return this.texture;
	}
}