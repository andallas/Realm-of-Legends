package models;

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
	
	public RawModel GetRawModel()
	{
		return this.rawModel;
	}
	
	public ModelTexture GetTexture()
	{
		return this.texture;
	}
}