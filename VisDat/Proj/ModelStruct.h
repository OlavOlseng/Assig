#pragma once
#include <glm.hpp>

struct ModelStruct
{
public:
	glm::vec3* position;
	glm::vec3* color;
	GLuint* modelHandle;
	int vertices;
	float rotation;
};

