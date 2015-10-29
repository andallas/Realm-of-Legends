#version 330 core

in vec2 position;

out vec4 clipSpaceCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;


void main(void)
{
	clipSpaceCoords = projectionMatrix * viewMatrix * modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	gl_Position = clipSpaceCoords;
}