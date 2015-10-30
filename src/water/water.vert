#version 330 core

in vec2 position;

out vec4 clipSpaceCoords;
out vec2 uvCoords;
out vec3 toCameraVector;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;

const float tiling = 6.0;


void main(void)
{
	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	clipSpaceCoords = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpaceCoords;
	uvCoords = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tiling;
	
	toCameraVector = cameraPosition - worldPosition.xyz;
}