#version 330 core

in vec2 uvCoordinate;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float fogVisibility;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDampening;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void)
{
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
		
		float brightness = max(normalDifference, 0.0);
		
		totalDiffuse += (brightness * lightColor[i]) / attenuationFactor;
		totalSpecular += (dampeningFactor * reflectivity * lightColor[i]) / attenuationFactor;
	}
	totalDiffuse = max(totalDiffuse, 0.2);
	
	vec4 textureColor = texture(textureSampler, uvCoordinate);
	if (textureColor.a < 0.5)
	{
		discard;
	}
	
	out_color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	out_color = mix(vec4(skyColor, 1.0), out_color, fogVisibility);
}