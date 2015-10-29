#version 330 core

in vec3 uvCoords;

out vec4 out_Color;

uniform samplerCube cubeMap1;
uniform samplerCube cubeMap2;
uniform float cubeMapBlendFactor;
uniform vec3 fogColor;

const float lowerFogLimit = 0.0;
const float upperFogLimit = 30.0;

void main(void)
{
	vec4 texture1 = texture(cubeMap1, uvCoords);
	vec4 texture2 = texture(cubeMap2, uvCoords);
	
	vec4 finalColor = mix(texture1, texture2, cubeMapBlendFactor);
	
	float fogFactor = (uvCoords.y - lowerFogLimit) / (upperFogLimit - lowerFogLimit);
	fogFactor = clamp(fogFactor, 0.0, 1.0);
	out_Color = mix(vec4(fogColor, 1.0), finalColor, fogFactor);
}