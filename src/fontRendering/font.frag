#version 330

in vec2 pass_textureCoords;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;

const float width = 0.45;
const float edge = 0.1;

// Normal border effect
//const float borderWidth = 0.0;
//const float borderEdge = 0.1;

// Glow effect
const float borderWidth = 0.0;
const float borderEdge = 0.4;

const vec3 borderColor = vec3(	0.1176470588235294,
								0.7725490196078431,
								0.0117647058823529);

const vec2 shadowOffset = vec2(0, 0);

void main(void)
{
	float textureAlpha = texture(fontAtlas, pass_textureCoords).a;
	float distance = 1.0 - textureAlpha;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);
	
	float offsetTextureAlpha = texture(fontAtlas, pass_textureCoords + shadowOffset).a;
	float borderDistance = 1.0 - offsetTextureAlpha;
	float borderAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, borderDistance);
	
	float totalAlpha = alpha + (1.0 - alpha) * borderAlpha;
	vec3 totalColor = mix(borderColor, color, alpha / totalAlpha);
	
	out_color = vec4(totalColor, totalAlpha);
}