#version 330

in vec2 uvCoordinate;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float fogVisibility;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDampening;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void)
{
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
	
	vec4 textureColor = texture(textureSampler, uvCoordinate);
	if (textureColor.a < 0.5)
	{
		discard;
	}
	
	out_color = vec4(diffuse, 1.0) * textureColor + vec4(specularValue, 1.0);
	out_color = mix(vec4(skyColor, 1.0), out_color, fogVisibility);
}