#version 330 core

in vec2 position;

out vec2 uvCoords;

uniform mat4 transformationMatrix;

void main(void)
{
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	uvCoords = vec2((position.x + 1.0) * 0.5, 1 - (position.y + 1.0) * 0.5);
}