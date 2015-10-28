#version 330

in vec2 uvCoordinate;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float fogVisibility;

out vec4 out_color;

uniform sampler2D splat1;
uniform sampler2D splat2;
uniform sampler2D splat3;
uniform sampler2D splat4;
uniform sampler2D splatMap;

uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
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
	vec3 unitCameraVector = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for (int i = 0; i < 4; i++)
	{	
		float distance = length(toLightVector[i]);
		float attenuationFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		vec3 lightDirection = -unitLightVector;
		float normalDifference = dot(unitSurfaceNormal, unitLightVector);
		vec3 reflectedLightDirection = reflect(lightDirection, unitSurfaceNormal);
		float specularFactor = dot(reflectedLightDirection, unitCameraVector);
		specularFactor = max(specularFactor, 0.0);
		float dampeningFactor = pow(specularFactor, shineDampening);
		float brightness = max(normalDifference, 0.);
		
		totalDiffuse += (brightness * lightColor[i]) / attenuationFactor;
		totalSpecular += (dampeningFactor * reflectivity * lightColor[i]) / attenuationFactor;
	}
	totalDiffuse = max(totalDiffuse, 0.2);
	
	out_color = vec4(totalDiffuse, 1.0) * totalSplatColor + vec4(totalSpecular, 1.0);
	out_color = mix(vec4(skyColor, 1.0), out_color, fogVisibility);
}