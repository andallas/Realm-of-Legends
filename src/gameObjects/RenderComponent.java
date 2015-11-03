package gameObjects;

import models.TexturedModel;

public class RenderComponent extends Component
{
	private TexturedModel _model;
	private int _textureIndex = 0;
	
	public RenderComponent(GameObject gameObject)
	{
		super(gameObject);
		_type = ComponentType.Render;
	}

	@Override
	public void Receive(ComponentMessage message)
	{
		// TODO: Implement Receive method for RenderComponent
		
	}

	@Override
	public String ToString()
	{
		// TODO: Implement ToString method for RenderComponent
		return null;
	}
	
	
	public float GetTextureXOffset()
	{
		int numRows = _model.GetTexture().GetNumberOfRows();
		int column = _textureIndex % numRows ;
		return (float)column / (float)numRows;
	}
	
	public float GetTextureYOffset()
	{
		int numCols = _model.GetTexture().GetNumberOfRows();
		int row = _textureIndex / numCols ;
		return (float)row / (float)numCols;
	}

	public TexturedModel GetModel() { return _model; }
	public void SetModel(TexturedModel model) { _model = model; }
	public void SetIndex(int index) { _textureIndex = index; }
}