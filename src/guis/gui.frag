#version 330

in vec2 uvCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main(void)
{
	out_Color = texture(guiTexture, uvCoords);
}