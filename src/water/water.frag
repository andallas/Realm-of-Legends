#version 330 core

out vec4 out_Color;

in vec4 clipSpaceCoords;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;


void main(void)
{
	vec2 refractUVCoords = (clipSpaceCoords.xy / clipSpaceCoords.w) / 2.0 + 0.5;
	vec2 reflectUvCoords = vec2(refractUVCoords.x, -refractUVCoords.y);
	
	vec4 reflectColor = texture(reflectionTexture, reflectUvCoords);
	vec4 refractColor = texture(refractionTexture, refractUVCoords);
	
	out_Color = mix(reflectColor, refractColor, 0.5);
}