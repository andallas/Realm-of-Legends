#version 330 core

out vec4 out_Color;

in vec4 clipSpaceCoords;
in vec2 uvCoords;
in vec3 toCameraVector;
in vec3 fromLightVector;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dUdVMap;
uniform sampler2D normalMap;
uniform float waveFactor;
uniform vec3 lightColor;

const float waveStrength = 0.02;
const float shineDampening = 20.0;
const float reflectivity = 0.6;


void main(void)
{
	vec2 refractUVCoords = (clipSpaceCoords.xy / clipSpaceCoords.w) / 2.0 + 0.5;
	vec2 reflectUVCoords = vec2(refractUVCoords.x, -refractUVCoords.y);
	
	vec2 distortedUVCoords = texture(dUdVMap, vec2(uvCoords.x + waveFactor, uvCoords.y)).rg * 0.1;
	distortedUVCoords = uvCoords + vec2(distortedUVCoords.x, distortedUVCoords.y + waveFactor);
	vec2 totalDistortion = (texture(dUdVMap, distortedUVCoords).rg * 2.0 - 1.0) * waveStrength;
	
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
	
	vec4 normalMapColor = texture(normalMap, distortedUVCoords);
	vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b, normalMapColor.g * 2.0 - 1.0);
	normal = normalize(normal);
	
	vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.0);
	specular = pow(specular, shineDampening);
	vec3 specularHighlights = lightColor * specular * reflectivity * (1 - refractiveFactor);
	
	vec4 fresnelColor = mix(reflectColor, refractColor, refractiveFactor);
	vec4 waterColor = vec4(0.0, 0.3, 0.5, 1.0);
	out_Color = mix(fresnelColor, waterColor, 0.2) + vec4(specularHighlights, 0.0);
}