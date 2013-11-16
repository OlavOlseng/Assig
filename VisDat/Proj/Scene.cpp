#include "Scene.hpp"
#include <math.h>
#include <iostream>
#include <ctime>
#include <GL/glew.h>
#include <GL/freeglut.h>
#include <glm.hpp>
#include <gtc/matrix_transform.hpp>

#include "shader.hpp"
#include <time.h>
#include <vector>



#define PI 3.14159265359

#define mouseSpeed 0.00005f

#define cursor_speed 0.0218f
// units per second


// This will be used with shader
//GLuint VertexArrayID;
GLuint vertexbuffer, colorbuffer, footbuffer;
GLuint objvertexbuffer; // for obj
GLuint programID_1, programID_2;

int last_time, current_time;

GLuint MatrixID; // Handler Matrix for moving the cam
GLuint colorID;

glm::vec3 color;
glm::mat4 MVP; // FInal Homogeneous Matrix

glm::mat4 MVP1;
glm::mat4 Projection, View, Model;

// Variables for moving camera with mouse
int mouse_x = 800/2;
int mouse_y = 600/2;
int mouse_button =  GLUT_LEFT_BUTTON;
int mouse_state = GLUT_UP;
int sp_key =0;

float counter=0;
// Initial position : on +Z
glm::vec3 position = glm::vec3(0.0f, 0.0f, -5);
// Initial horizontal angle : toward -Z
float horizontalAngle = 0.0f;
// Initial vertical angle : none
float verticalAngle = 0.0f;
// Initial Field of View
float initialFoV = 90.0f;
glm::vec3 direction =  glm::vec3(0, 0, 0) - position;
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

	Render();

	glutSwapBuffers();
}

void Render()
{    // Disable depth test
	// Projection matrix : 45° Field of View, 4:3 ratio, display range : 0.1 unit <-> 100 units
	// Camera matrix
	View       = glm::lookAt(
		position, // Camera
		position + direction, // look at coord
		up  // up-vec
		);

	// Model matrix : an identity matrix (model will be at the origin)
	Model      = glm::mat4(1.0f);
	// Our ModelViewProjection : multiplication of our 3 matrices
	MVP        = Projection * View * Model; // Remember, matrix multiplication is the other way around
	// Use our shader
	glUseProgram(programID_1);
	// Send our transformation to the currently bound shader,
	// in the "MVP" uniform
	glUniformMatrix4fv(MatrixID, 1, GL_FALSE, &MVP[0][0]);
	// 1rst attribute buffer : vertices
	glEnableVertexAttribArray(0);
	glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
	glVertexAttribPointer(
		0,                  // attribute. No particular reason for 0, but must match the layout in the shader.
		3,                  // size
		GL_FLOAT,           // type
		GL_FALSE,           // normalized?
		0,                  // stride
		(void*)0            // array buffer offset
		);

	// Draw the triangles
	glDrawArrays(GL_TRIANGLES, 0, 12*3); // 12*3 indices starting at 0 -> 12 triangles
	glDisableVertexAttribArray(0);
}

void SetupGL() //
{

	//Parameter handling
	glShadeModel (GL_SMOOTH);

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
		-1.0f,-1.0f,-1.0f,
		-1.0f,-1.0f, 1.0f,
		-1.0f, 1.0f, 1.0f,
		1.0f, 1.0f,-1.0f,
		-1.0f,-1.0f,-1.0f,
		-1.0f, 1.0f,-1.0f,
		1.0f,-1.0f, 1.0f,
		-1.0f,-1.0f,-1.0f,
		1.0f,-1.0f,-1.0f,
		1.0f, 1.0f,-1.0f,
		1.0f,-1.0f,-1.0f,
		-1.0f,-1.0f,-1.0f,
		-1.0f,-1.0f,-1.0f,
		-1.0f, 1.0f, 1.0f,
		-1.0f, 1.0f,-1.0f,
		1.0f,-1.0f, 1.0f,
		-1.0f,-1.0f, 1.0f,
		-1.0f,-1.0f,-1.0f,
		-1.0f, 1.0f, 1.0f,
		-1.0f,-1.0f, 1.0f,
		1.0f,-1.0f, 1.0f,
		1.0f, 1.0f, 1.0f,
		1.0f,-1.0f,-1.0f,
		1.0f, 1.0f,-1.0f,
		1.0f,-1.0f,-1.0f,
		1.0f, 1.0f, 1.0f,
		1.0f,-1.0f, 1.0f,
		1.0f, 1.0f, 1.0f,
		1.0f, 1.0f,-1.0f,
		-1.0f, 1.0f,-1.0f,
		1.0f, 1.0f, 1.0f,
		-1.0f, 1.0f,-1.0f,
		-1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, 1.0f,
		-1.0f, 1.0f, 1.0f,
		1.0f,-1.0f, 1.0f
	};

	// Generate 1 buffer, put the resulting identifier in vertexbuffer
	glGenBuffers(1, &vertexbuffer);
	// The following commands will talk about our 'vertexbuffer' buffer
	glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
	// Give our vertices to OpenGL.
	glBufferData(GL_ARRAY_BUFFER, sizeof(g_nonstop_buffer_data), g_nonstop_buffer_data, GL_STATIC_DRAW);
}
