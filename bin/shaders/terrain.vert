#version 330 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 uvCoordinate;
out vec3 surfaceNormal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float fogVisibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4];
uniform vec4 clipPlane;

const float fogDensity = 0.002;
const float fogGradient = 1.5;

void main(void)
{
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	uvCoordinate = textureCoord;
	
	
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	for (int i = 0; i < 4; i++)
	{
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	float fogDistance = length(positionRelativeToCamera.xyz);
	fogVisibility = exp(-pow((fogDistance * fogDensity), fogGradient));
	fogVisibility = clamp(fogVisibility, 0.0, 1.0);
}