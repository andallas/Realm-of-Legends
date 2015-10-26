#version 330

in vec2 uvCoordinate;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float fogVisibility;

out vec4 out_color;

uniform sampler2D splat1;
uniform sampler2D splat2;
uniform sampler2D splat3;
uniform sampler2D splat4;
uniform sampler2D splatMap;

uniform vec3 lightColor;
uniform float shineDampening;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void)
{
	vec4 splatMapColor = texture(splatMap, uvCoordinate);
	float splatAmount = 1 - (splatMapColor.r + splatMapColor.g + splatMapColor.b);
	vec2 tiledUVCoords = uvCoordinate * 40.0;
	
	vec4 splat1Color = texture(splat1, tiledUVCoords) * splatAmount;
	vec4 splat2Color = texture(splat2, tiledUVCoords) * splatMapColor.r;
	vec4 splat3Color = texture(splat3, tiledUVCoords) * splatMapColor.g;
	vec4 splat4Color = texture(splat4, tiledUVCoords) * splatMapColor.b;
	
	vec4 totalSplatColor = splat1Color + splat2Color + splat3Color + splat4Color;

	vec3 unitSurfaceNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float normalDifference = dot(unitSurfaceNormal, unitLightVector);
	float brightness = max(normalDifference, 0.2);
	vec3 diffuse = brightness * lightColor;
	
	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitSurfaceNormal);
	
	float specularFactor = dot(reflectedLightDirection, unitCameraVector);
	specularFactor = max(specularFactor, 0.0);
	float dampeningFactor = pow(specularFactor, shineDampening);
	vec3 specularValue = dampeningFactor * reflectivity * lightColor;
	
	out_color = vec4(diffuse, 1.0) * totalSplatColor + vec4(specularValue, 1.0);
	out_color = mix(vec4(skyColor, 1.0), out_color, fogVisibility);
}