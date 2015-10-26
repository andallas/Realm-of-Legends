#version 330 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 uvCoordinate;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float fogVisibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform float fakeLighting;

const float fogDensity = 0.007;
const float fogGradient = 1.5;

void main(void)
{
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	uvCoordinate = textureCoord;
	
	vec3 actualNormal = normal;
	if (fakeLighting > 0.5)
	{
		actualNormal = vec3(0.0, 1.0, 0.0);
	}
	
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	float fogDistance = length(positionRelativeToCamera.xyz);
	fogVisibility = exp(-pow((fogDistance * fogDensity), fogGradient));
	fogVisibility = clamp(fogVisibility, 0.0, 1.0);
}