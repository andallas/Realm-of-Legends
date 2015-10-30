#version 330 core

out vec4 out_Color;

in vec4 clipSpaceCoords;
in vec2 uvCoords;
in vec3 toCameraVector;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dUdVmap;
uniform float waveFactor;

const float waveStrength = 0.02;


void main(void)
{
	vec2 refractUVCoords = (clipSpaceCoords.xy / clipSpaceCoords.w) / 2.0 + 0.5;
	vec2 reflectUVCoords = vec2(refractUVCoords.x, -refractUVCoords.y);
	
	vec2 distortion1 = (texture(dUdVmap, vec2(uvCoords.x + waveFactor, uvCoords.y)).rg * 2.0 - 1.0) * waveStrength;
	vec2 distortion2 = (texture(dUdVmap, vec2(-uvCoords.x + waveFactor, uvCoords.y + waveFactor)).rg * 2.0 - 1.0) * waveStrength;
	vec2 totalDistortion = distortion1 + distortion2;
	
	refractUVCoords += totalDistortion;
	refractUVCoords = clamp(refractUVCoords, 0.001, 0.999);
	
	reflectUVCoords += totalDistortion;
	reflectUVCoords.x = clamp(reflectUVCoords.x, 0.001, 0.999);
	reflectUVCoords.y = clamp(reflectUVCoords.y, -0.999, -0.001);
	
	vec4 refractColor = texture(refractionTexture, refractUVCoords);
	vec4 reflectColor = texture(reflectionTexture, reflectUVCoords);
	
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, vec3(0.0, 1.0, 0.0));
	refractiveFactor = pow(refractiveFactor, 2.0);
	
	vec4 waterColor = mix(reflectColor, refractColor, refractiveFactor);
	out_Color = mix(waterColor, vec4(0.0, 0.3, 0.5, 1.0), 0.2);
}