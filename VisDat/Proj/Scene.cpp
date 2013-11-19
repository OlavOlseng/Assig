#include "Scene.hpp"

#include <math.h>
#include <cstdlib>
#include <iostream>
#include <fstream>
#include <ctime>
#include <GL/glew.h>
#include <GL/freeglut.h>
#include <gtc/matrix_transform.hpp>
#include <time.h>
#include <vector>
#include <string>

#include "shader.hpp"
#include "ModelStruct.h"


#define PI 3.14159265359

#define mouseSpeed 0.00002f

#define cursor_speed 0.718f
// units per second


// This will be used with shader
//GLuint VertexArrayID;
GLuint vertexbuffer;
GLuint* numbers;
GLuint programID_1;

int last_time, current_time;

GLuint MatrixID; // Handler Matrix for moving the cam
GLuint colorID;  //Shader handle for color vec3

glm::vec3 color;
glm::mat4 MVP; // Final Homogeneous Matrix

glm::mat4 Projection, View;
std::vector<ModelStruct*> models;
std::vector<ModelStruct*> numberModels;

// Variables for moving camera with mouse
int mouse_x = 800/2;
int mouse_y = 600/2;
int mouse_button =  GLUT_LEFT_BUTTON;
int mouse_state = GLUT_UP;
int sp_key = 0;

int colors[6] = {0, 0, 0, 0, 0, 0};

float counter=0;
// Initial position : on +Z
glm::vec3 position = glm::vec3(50.0f, 30.0f, 50.0f);
// Initial horizontal angle : toward -Z
float horizontalAngle = 0.0f;
// Initial vertical angle : none
float verticalAngle = 0.0f;
// Initial Field of View
float initialFoV = 80.0f;
glm::vec3 direction =  glm::vec3(position.x, position.y, 0.0f) - position;
glm::vec3 right  = glm::vec3(1.0f, 0, 0) ;
glm::vec3 up = glm::vec3(0, 1, 0);



void MouseGL(int button, int state, int x, int y)
{
	if ( state == GLUT_DOWN) // if key is pressed
	{
		mouse_state = state; // save in global memomry

		// move point to the midle of screen
		glutWarpPointer(800/2, 600/2);
	}
	else
	{
		mouse_state = GLUT_UP;

	}
}

void Mouse_active(int x, int y)
{
	//save values in global memory
	mouse_x = x;
	mouse_y = y;

}


void Idle()
{

	current_time = glutGet(GLUT_ELAPSED_TIME);

	int dt = current_time - last_time;
	counter += dt;

		if(mouse_state ==  GLUT_DOWN)
		{

			horizontalAngle += mouseSpeed * float( 800/2 - mouse_x); // 800 = window width
			verticalAngle   += mouseSpeed * float(600/2 - mouse_y );// 600 = window height


			// Direction : Spherical coordinates to Cartesian coordinates conversion
			direction= glm::vec3(
				cos(verticalAngle) * sin(horizontalAngle),
				sin(verticalAngle),
				cos(verticalAngle) * cos(horizontalAngle)
				);

			// Right vector
			right = glm::vec3(
				sin(horizontalAngle - PI/2.0f),
				0,
				cos(horizontalAngle - PI/2.0f)
				);

			// Up vector
			up = glm::cross( right, direction );
		}

		//            // Move forward
		if (sp_key == GLUT_KEY_UP)
		{
			position += direction * (dt * cursor_speed);
		}
		// Move backward
		if (sp_key == GLUT_KEY_DOWN)
		{

			position -= direction * (dt * cursor_speed);
		}
		// Strafe right
		if (sp_key == GLUT_KEY_RIGHT)
		{

			position += right * (dt * cursor_speed);
		}
		// Strafe left
		if (sp_key== GLUT_KEY_LEFT)
		{

			position -= right * (dt * cursor_speed);
		}
		
		sp_key =0;

		// Camera matrix


		View       = glm::lookAt(
			position,
			position+direction,
			up
			);
	

	glutPostRedisplay();
	last_time = current_time;// update when the last timer;
}


void ReshapeGL( int w, int h )
{
	std::cout << "ReshapeGL( " << w << ", " << h << " );" << std::endl;

	if (h == 0)		// Prevent a divide-by-zero error
	{
		h = 1;		// Making Height Equal One
	}
	g_iWindowWidth = w;
	g_iWindowHeight = h;
	glViewport( 0, 0, w, h);

}

void Specialkey(int c, int x, int y )
{
	sp_key = c;
}

void KeyboardGL( unsigned char c, int x, int y )
{
	// Store the current scene so we can test if it has changed later.

	switch ( c )
	{
	case 's':
	case 'S':
		{
			std::cout << "Shade Model: GL_SMOOTH" << std::endl;
			// Switch to smooth shading model
			glShadeModel( GL_SMOOTH );
		}
		break;
	case 'f':
	case 'F':
		{
			std::cout << "Shade Model: GL_FLAT" << std::endl;
			// Switch to flat shading model
			glShadeModel( GL_FLAT );
		}
		break;
	case '\033': // escape quits
	case '\015': // Enter quits
	case 'Q':    // Q quits
	case 'q':    // q (or escape) quits
		{
			// Cleanup up and quit
			Cleanup(0);
		}
		break;

	case '\72': //arrow up
		{

		}
		break;
	}
	glutPostRedisplay();
}


void Cleanup( int errorCode, bool bExit )
{
	if ( g_iGLUTWindowHandle != 0 )
	{
		glutDestroyWindow( g_iGLUTWindowHandle );
		g_iGLUTWindowHandle = 0;
	}

	if ( bExit )
	{
		exit( errorCode );
	}
}


void DisplayGL()
{

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  // Clean up the colour of the window
	// and the depth buffer

	RenderImage();

	glutSwapBuffers();
}

void RenderImage()
{    // Disable depth test
	// Projection matrix : 45° Field of View, 4:3 ratio, display range : 0.1 unit <-> 100 units
	// Camera matrix
	View = glm::lookAt(
		position, // Camera
		position + direction, // look at coord
		up  // up-vec
		);

	for (int i = 0; i < 6; i++)
	{
		glm::mat4 Model = glm::mat4(1.0f);
		glm::vec3 temp = *models[i] -> position + glm::vec3(0.0f, -5.0f, 0.0f);
		RenderNumber(colors[i], temp, counter/12.0f);
	}

	for (int i = 0; i < models.size(); i++ )
	{
		glm::mat4 Model = glm::mat4(1.0f);
		//glm::rotate(Model, *models[i] -> rotation, up);
		Model = glm::translate(Model, *(models[i] -> position));

		MVP        = Projection * View * Model; // Remember, matrix multiplication is the other way around
		// Use our shader
		glUseProgram(programID_1);
		// Send our transformation to the currently bound shader,
		// in the "MVP" uniform
		color = *(models[i] -> color);
		glUniform3fv(colorID, 1, &color[0]);
		glUniformMatrix4fv(MatrixID, 1, GL_FALSE, &MVP[0][0]);
		// 1rst attribute buffer : vertices
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, *models[i] -> modelHandle);
		glVertexAttribPointer(
			0,                  // attribute. No particular reason for 0, but must match the layout in the shader.
			3,                  // size
			GL_FLOAT,           // type
			GL_FALSE,           // normalized?
			0,                  // stride
			(void*)0            // array buffer offset
			);

		// Draw the triangles
		glDrawArrays(GL_TRIANGLES, 0, models[i]->vertices);
		glDisableVertexAttribArray(0);
	}
}

void RenderNumber(int number, glm::vec3 position, float rotation)
{
	glDisable(GL_CULL_FACE);
	
	bool big = (number >= 10);
	int ones = number % 10; 
	int tens = number / 10;

	glm::mat4 Offset = glm::mat4(1.0f);
	if(big) 
	{
	Offset = glm::translate(Offset, glm::vec3(1.0f, 0.0f, 0.0f));
	}
	
	glm::mat4 Model = glm::mat4(1.0f);
	Model = glm::translate(Model, position);
	Model = glm::rotate(Model, rotation, glm::vec3(0.0f, 1.0f, 0.0f));

	glUseProgram(programID_1);
	color = glm::vec3(1.0f, 1.0f, 1.0f);
	glUniform3fv(colorID, 1, &color[0]);

	MVP        = Projection * View * Model * Offset;
	glUniformMatrix4fv(MatrixID, 1, GL_FALSE, &MVP[0][0]);
	glEnableVertexAttribArray(0);
	glBindBuffer(GL_ARRAY_BUFFER, *numberModels[ones] -> modelHandle);
	glVertexAttribPointer(
		0,                  // attribute. No particular reason for 0, but must match the layout in the shader.
		3,                  // size
		GL_FLOAT,           // type
		GL_FALSE,           // normalized?
		0,                  // stride
		(void*)0            // array buffer offset
		);

	glDrawArrays(GL_TRIANGLES, 0, numberModels[ones]->vertices);
	glDisableVertexAttribArray(0);

	if(!big) return;

	Offset = glm::translate(glm::mat4(1.0f), glm::vec3(-1.0f, 0.0f, 0.0f));
	MVP        = Projection * View * Model * Offset;

	glUniformMatrix4fv(MatrixID, 1, GL_FALSE, &MVP[0][0]);
	glEnableVertexAttribArray(0);
	glBindBuffer(GL_ARRAY_BUFFER, *numberModels[tens] -> modelHandle);
	glVertexAttribPointer(
		0,                  // attribute. No particular reason for 0, but must match the layout in the shader.
		3,                  // size
		GL_FLOAT,           // type
		GL_FALSE,           // normalized?
		0,                  // stride
		(void*)0            // array buffer offset
		);

	glDrawArrays(GL_TRIANGLES, 0, numberModels[tens]->vertices);

	glEnable(GL_CULL_FACE);
}

void SetupGL() //
{

	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH);
	//Parameter handling
	glShadeModel (GL_SMOOTH);
	numbers = new GLuint[10];
	Projection = glm::perspective(initialFoV, 4.0f / 3.0f, 0.1f, 100.0f);

	// Accept fragment if it closer to the camera than the former one
	glDepthFunc(GL_LESS);
	// polygon rendering mode
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial( GL_FRONT, GL_AMBIENT_AND_DIFFUSE );

	//Set up light source
	GLfloat light_position[] = { 0.0, 30.0,-50.0,0.0 };

	glLightfv(GL_LIGHT0, GL_POSITION, light_position);
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);

	// Black background
	glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );   // Black background

	// Register GLUT callbacks
	glutDisplayFunc(DisplayGL);
	glutKeyboardFunc(KeyboardGL);
	glutSpecialFunc(Specialkey);
	glutReshapeFunc(ReshapeGL);
	glutMouseFunc(MouseGL);
	glutMotionFunc(Mouse_active);

	//Call to the drawing function
	glutIdleFunc(Idle);
	last_time = glutGet(GLUT_ELAPSED_TIME);

	// Setup initial GL State
	glClearDepth( 1.0f );

	// Init GLEW
	if ( glewInit() != GLEW_OK )
	{
		std::cerr << "Failed to initialize GLEW." << std::endl;
		exit(-1);
	}

	std::cout << "Initialise OpenGL: Success!" << std::endl;

	//VAO
	//Create and compile our GLSL program from the shaders
	programID_1 = LoadShaders("TransformVertexShader.vertexshader", "ColorFragmentShader.fragmentshader");
	MatrixID = glGetUniformLocation(programID_1, "MVP");
	colorID = glGetUniformLocation(programID_1, "colorIN");

	//VBO -- VERTEX
	static const GLfloat g_nonstop_buffer_data[] = {
		//Front
		0.0f, 0.0f, 0.3f,
		0.6f, 0.3f, 0.0f,
		0.3f, 0.6f, 0.0f,

		0.0f, 0.0f, 0.3f,
		0.3f, 0.6f, 0.0f,
		-0.3f, 0.6f, 0.0f,

		0.0f, 0.0f, 0.3f,
		-0.3f, 0.6f, 0.0f,
		-0.6f, 0.3f, 0.0f,

		0.0f, 0.0f, 0.3f,
		-0.6f, 0.3f, 0.0f,
		-0.6f, -0.3f, 0.0f,

		0.0f, 0.0f, 0.3f,
		-0.6f, -0.3f, 0.0f,
		-0.3f, -0.6f, 0.0f,

		0.0f, 0.0f, 0.3f,
		-0.3f, -0.6f, 0.0f,
		0.3f, -0.6f, 0.0f,

		0.0f, 0.0f, 0.3f,
		0.3f, -0.6f, 0.0f,
		0.6f, -0.3f, 0.0f,

		0.0f, 0.0f, 0.3f,
		0.6f, -0.3f, 0.0f,
		0.6f, 0.3f, 0.0f,

		//Back
		0.0f, 0.0f, -0.3f,
		0.3f, 0.6f, 0.0f,
		0.6f, 0.3f, 0.0f,

		0.0f, 0.0f, -0.3f,
		-0.3f, 0.6f, 0.0f,
		0.3f, 0.6f, 0.0f,

		0.0f, 0.0f, -0.3f,
		-0.6f, 0.3f, 0.0f,
		-0.3f, 0.6f, 0.0f,

		0.0f, 0.0f, -0.3f,
		-0.6f, -0.3f, 0.0f,
		-0.6f, 0.3f, 0.0f,

		0.0f, 0.0f, -0.3f,
		-0.3f, -0.6f, 0.0f,
		-0.6f, -0.3f, 0.0f,

		0.0f, 0.0f, -0.3f,
		0.3f, -0.6f, 0.0f,
		-0.3f, -0.6f, 0.0f,

		0.0f, 0.0f, -0.3f,
		0.6f, -0.3f, 0.0f,
		0.3f, -0.6f, 0.0f,

		0.0f, 0.0f, -0.3f,
		0.6f, 0.3f, 0.0f,
		0.6f, -0.3f, 0.0f,

	};

	glGenBuffers(1, &vertexbuffer);
	glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_nonstop_buffer_data), g_nonstop_buffer_data, GL_STATIC_DRAW);
	
	static const GLfloat g_zero_buffer_data[] = 
	{
		//Horziontals
		//super_
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.6f, 0.0f,
		0.5f, 0.8, 0.0f,

		-0.5f, 0.6f, 0.0f,
		0.5f, 0.6f, 0.0f,
		0.5f, 0.8f, 0.0f,

		//sub_
		-0.5f, -0.6, 0.0f,
		-0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,

		-0.5f, -0.8f, 0.0f,
		0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,


		//Verticals
		//Ltop |
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		//Lbot |
		-0.5f, -0.8, 0.0f,
		-0.5f, -0.1f, 0.0f,
		-0.3f, -0.8f, 0.0f,

		-0.5f, -0.1f, 0.0f,
		-0.3f, -0.1f, 0.0f,
		-0.3f, -0.8f, 0.0f,

		//Rtop |
		0.5f, 0.8, 0.0f,
		0.5f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		0.5f, 0.1f, 0.0f,
		0.3f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[0]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[0]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_zero_buffer_data), g_zero_buffer_data, GL_STATIC_DRAW);

	static const GLfloat g_one_buffer_data[] = 
	{
		
		//Verticals
		//Rtop |
		0.5f, 0.8, 0.0f,
		0.5f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		0.5f, 0.1f, 0.0f,
		0.3f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

	};

	glGenBuffers(1, &numbers[1]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[1]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_one_buffer_data), g_one_buffer_data, GL_STATIC_DRAW);

	static const GLfloat g_two_buffer_data[] = 
	{
		//Horziontals
		//super_
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.6f, 0.0f,
		0.5f, 0.8, 0.0f,

		-0.5f, 0.6f, 0.0f,
		0.5f, 0.6f, 0.0f,
		0.5f, 0.8f, 0.0f,

		//mid _
		-0.5f, 0.1, 0.0f,
		-0.5f, -0.1f, 0.0f,
		0.5f, 0.1, 0.0f,

		-0.5f, -0.1f, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.5f, 0.1f, 0.0f,

		//sub_
		-0.5f, -0.6, 0.0f,
		-0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,

		-0.5f, -0.8f, 0.0f,
		0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,


		//Verticals

		//Lbot |
		-0.5f, -0.8, 0.0f,
		-0.5f, -0.1f, 0.0f,
		-0.3f, -0.8f, 0.0f,

		-0.5f, -0.1f, 0.0f,
		-0.3f, -0.1f, 0.0f,
		-0.3f, -0.8f, 0.0f,

		//Rtop |
		0.5f, 0.8, 0.0f,
		0.5f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		0.5f, 0.1f, 0.0f,
		0.3f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[2]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[2]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_two_buffer_data), g_two_buffer_data, GL_STATIC_DRAW);
	
	static const GLfloat g_three_buffer_data[] = 
	{
		//Horziontals
		//super_
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.6f, 0.0f,
		0.5f, 0.8, 0.0f,

		-0.5f, 0.6f, 0.0f,
		0.5f, 0.6f, 0.0f,
		0.5f, 0.8f, 0.0f,

		//mid _
		-0.5f, 0.1, 0.0f,
		-0.5f, -0.1f, 0.0f,
		0.5f, 0.1, 0.0f,

		-0.5f, -0.1f, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.5f, 0.1f, 0.0f,

		//sub_
		-0.5f, -0.6, 0.0f,
		-0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,

		-0.5f, -0.8f, 0.0f,
		0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,


		//Verticals
		//Rtop |
		0.5f, 0.8, 0.0f,
		0.5f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		0.5f, 0.1f, 0.0f,
		0.3f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[3]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[3]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_three_buffer_data), g_three_buffer_data, GL_STATIC_DRAW);
	
	static const GLfloat g_four_buffer_data[] = 
	{
		//Horziontals
		//mid _
		-0.5f, 0.1, 0.0f,
		-0.5f, -0.1f, 0.0f,
		0.5f, 0.1, 0.0f,

		-0.5f, -0.1f, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.5f, 0.1f, 0.0f,

		//Verticals
		//Ltop |
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		//Rtop |
		0.5f, 0.8, 0.0f,
		0.5f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		0.5f, 0.1f, 0.0f,
		0.3f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[4]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[4]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_four_buffer_data), g_four_buffer_data, GL_STATIC_DRAW);

	static const GLfloat g_five_buffer_data[] = 
	{
		//Horziontals
		//super_
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.6f, 0.0f,
		0.5f, 0.8, 0.0f,

		-0.5f, 0.6f, 0.0f,
		0.5f, 0.6f, 0.0f,
		0.5f, 0.8f, 0.0f,

		//mid _
		-0.5f, 0.1, 0.0f,
		-0.5f, -0.1f, 0.0f,
		0.5f, 0.1, 0.0f,

		-0.5f, -0.1f, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.5f, 0.1f, 0.0f,

		//sub_
		-0.5f, -0.6, 0.0f,
		-0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,

		-0.5f, -0.8f, 0.0f,
		0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,


		//Verticals
		//Ltop |
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[5]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[5]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_five_buffer_data), g_five_buffer_data, GL_STATIC_DRAW);

	static const GLfloat g_six_buffer_data[] = 
	{
		//Horziontals
		//super_
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.6f, 0.0f,
		0.5f, 0.8, 0.0f,

		-0.5f, 0.6f, 0.0f,
		0.5f, 0.6f, 0.0f,
		0.5f, 0.8f, 0.0f,

		//mid _
		-0.5f, 0.1, 0.0f,
		-0.5f, -0.1f, 0.0f,
		0.5f, 0.1, 0.0f,

		-0.5f, -0.1f, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.5f, 0.1f, 0.0f,

		//sub_
		-0.5f, -0.6, 0.0f,
		-0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,

		-0.5f, -0.8f, 0.0f,
		0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,


		//Verticals
		//Ltop |
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		//Lbot |
		-0.5f, -0.8, 0.0f,
		-0.5f, -0.1f, 0.0f,
		-0.3f, -0.8f, 0.0f,

		-0.5f, -0.1f, 0.0f,
		-0.3f, -0.1f, 0.0f,
		-0.3f, -0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[6]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[6]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_six_buffer_data), g_six_buffer_data, GL_STATIC_DRAW);

	static const GLfloat g_seven_buffer_data[] = 
	{
		//Horziontals
		//super_
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.6f, 0.0f,
		0.5f, 0.8, 0.0f,

		-0.5f, 0.6f, 0.0f,
		0.5f, 0.6f, 0.0f,
		0.5f, 0.8f, 0.0f,

		//Verticals
		//Ltop |
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		//Rtop |
		0.5f, 0.8, 0.0f,
		0.5f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		0.5f, 0.1f, 0.0f,
		0.3f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[7]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[7]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_seven_buffer_data), g_seven_buffer_data, GL_STATIC_DRAW);

	static const GLfloat g_eight_buffer_data[] = 
	{
		//Horziontals
		//super_
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.6f, 0.0f,
		0.5f, 0.8, 0.0f,

		-0.5f, 0.6f, 0.0f,
		0.5f, 0.6f, 0.0f,
		0.5f, 0.8f, 0.0f,

		//mid _
		-0.5f, 0.1, 0.0f,
		-0.5f, -0.1f, 0.0f,
		0.5f, 0.1, 0.0f,

		-0.5f, -0.1f, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.5f, 0.1f, 0.0f,

		//sub_
		-0.5f, -0.6, 0.0f,
		-0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,

		-0.5f, -0.8f, 0.0f,
		0.5f, -0.8f, 0.0f,
		0.5f, -0.6f, 0.0f,


		//Verticals
		//Ltop |
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		//Lbot |
		-0.5f, -0.8, 0.0f,
		-0.5f, -0.1f, 0.0f,
		-0.3f, -0.8f, 0.0f,

		-0.5f, -0.1f, 0.0f,
		-0.3f, -0.1f, 0.0f,
		-0.3f, -0.8f, 0.0f,

		//Rtop |
		0.5f, 0.8, 0.0f,
		0.5f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		0.5f, 0.1f, 0.0f,
		0.3f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[8]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[8]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_eight_buffer_data), g_eight_buffer_data, GL_STATIC_DRAW);

	static const GLfloat g_nine_buffer_data[] = 
	{
		//Horziontals
		//super_
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.6f, 0.0f,
		0.5f, 0.8, 0.0f,

		-0.5f, 0.6f, 0.0f,
		0.5f, 0.6f, 0.0f,
		0.5f, 0.8f, 0.0f,

		//mid _
		-0.5f, 0.1, 0.0f,
		-0.5f, -0.1f, 0.0f,
		0.5f, 0.1, 0.0f,

		-0.5f, -0.1f, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.5f, 0.1f, 0.0f,

		//Verticals
		//Ltop |
		-0.5f, 0.8, 0.0f,
		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		-0.5f, 0.1f, 0.0f,
		-0.3f, 0.1f, 0.0f,
		-0.3f, 0.8f, 0.0f,

		//Rtop |
		0.5f, 0.8, 0.0f,
		0.5f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		0.5f, 0.1f, 0.0f,
		0.3f, 0.1f, 0.0f,
		0.3f, 0.8f, 0.0f,

		//Rbot |
		0.5f, -0.8, 0.0f,
		0.5f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,

		0.5f, -0.1f, 0.0f,
		0.3f, -0.1f, 0.0f,
		0.3f, -0.8f, 0.0f,
	};

	glGenBuffers(1, &numbers[9]);
	glBindBuffer(GL_ARRAY_BUFFER, numbers[9]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_nine_buffer_data), g_nine_buffer_data, GL_STATIC_DRAW);



	
	genScene("counter.txt");
}

void genScene(const char* path) 
{
	float distScale = 1.0f/12.0f;
	float px, py;
	
	std::string color;
	std::ifstream file;
	
	//Create the counter
	file.open("counter.txt");
	
	while(file >> px >> py >> color)
	{
		ModelStruct* m = new ModelStruct;
		m -> position = new glm::vec3(px * distScale, py * distScale, 0.0f);

		switch(color[0]) 
		{
		case 'r':
			{
			m -> color = new glm::vec3(1.0f, 0.0f, 0.0f);
			}
		break;
		case 'g':
			{
			m -> color = new glm::vec3(0.0f, 1.0f, 0.0f);
			}
		break;
		case 'b':
			{
			m -> color = new glm::vec3(0.0f, 0.0f, 1.0f);
			}
		break;
		case 'y':
			{
			m -> color = new glm::vec3(1.0f, 1.0f, 0.0f);
			}
		break;
		case 'o':
			{
			m -> color = new glm::vec3(1.0f, 0.5f, 0.0f);
			}
		break;
		case 'p':
			{
			m -> color = new glm::vec3(1.0f, 0.0f, 1.0f);
			}
		break;
		}

		m -> rotation = 0;
		m -> vertices = 16*3*3;
		m -> modelHandle = &vertexbuffer;
		models.push_back(m);	
		std::cout << "X: " << px << " Y: " << py << " C: " << color <<  std::endl;
	}
	file.close();

	ModelStruct* m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[0];
	m -> vertices = 3*2*6;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[1];
	m -> vertices = 3*2*2;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[2];
	m -> vertices = 3*2*5;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[3];
	m -> vertices = 3*2*7;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[4];
	m -> vertices = 3*2*4;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[5];
	m -> vertices = 3*2*5;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[6];
	m -> vertices = 3*2*6;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[7];
	m -> vertices = 3*2*4;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[8];
	m -> vertices = 3*2*7;
	numberModels.push_back(m);

	m = new ModelStruct;
	m -> color = new glm::vec3(1.0f, 1.0f, 1.0f);
	m -> position = new glm::vec3(position);
	m -> rotation = 0;
	m -> modelHandle = &numbers[9];
	m -> vertices = 3*2*5;
	numberModels.push_back(m);

	file.open("data.txt");
	
	while(file >> px >> py >> color)
	{
		ModelStruct* m = new ModelStruct;
		m -> position = new glm::vec3(px * distScale, 50 -py * distScale, 0.0f);
		switch(color[0]) 
		{
		case 'r':
			{
			colors[0]++;
			m -> color = new glm::vec3(1.0f, 0.0f, 0.0f);
			}
		break;
		case 'g':
			{
			colors[1]++;
			m -> color = new glm::vec3(0.0f, 1.0f, 0.0f);
			}
		break;
		case 'b':
			{
			colors[2]++;
			m -> color = new glm::vec3(0.0f, 0.0f, 1.0f);
			}
		break;
		case 'y':
			{
			colors[3]++;
			m -> color = new glm::vec3(1.0f, 1.0f, 0.0f);
			}
		break;
		case 'o':
			{
			colors[4]++;
			std::cout << colors[4] << std::endl; 
			m -> color = new glm::vec3(1.0f, 0.5f, 0.0f);
			}
		break;
		case 'p':
			{
			colors[5]++;
			m -> color = new glm::vec3(1.0f, 0.0f, 1.0f);
			}
		break;
		}

		m -> rotation = 0;
		m -> vertices = 16*3*3;
		m -> modelHandle = &vertexbuffer;
		models.push_back(m);	
		std::cout << "X: " << px << " Y: " << py << " C: " << color <<  std::endl;
	}
	file.close();
}