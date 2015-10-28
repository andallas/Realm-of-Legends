package textures;

import java.nio.ByteBuffer;

public class TextureData
{
	private int width;
	private int height;
	private ByteBuffer buffer;
	
	public TextureData(ByteBuffer buffer, int width, int height)
	{
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}

	public int GetWidth() { return width; }
	public int GetHeight() { return height; }
	public ByteBuffer GetBuffer() { return buffer; }
	
}