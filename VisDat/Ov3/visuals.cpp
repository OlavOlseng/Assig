#include <math.h>
#include <iostream>
#include <ctime>
#include <GL/glew.h>
#include <GL/freeglut.h>
#include <glm.hpp>
#include <gtc/matrix_transform.hpp>

#include "visuals.h"
#include <time.h>
#include "shader.hpp"

#define PI 3.14159265359

// This will be used with shader
//GLuint VertexArrayID;
GLuint vertexbuffer;
GLuint programID_1, programID_2;

int last_time, current_time;

GLuint MatrixID; // Handler Matrix for moving the cam
glm::mat4 MVP; // FInal Homogeneous Matrix
glm::mat4 Projection,View,Model;
glm::mat4 Rot, Trans, Scale;
glm::mat4 Models[100];

void Idle()
{
	
    //init model transformations
	Model = glm::mat4(1.0f);
	Rot = glm::mat4(1.0f);
	Trans = glm::mat4(1.0f);
	Scale = glm::mat4(1.0f);

	current_time = clock(); // update current timer

    if(rot_angle ==-1) // only happens the first time Idel is called . This avoid timer problems.
    {
        rot_angle =0;
    }
    else
    {
        int dt = current_time - last_time;

        /* Compute animation here*/
		if(g_eCurrentScene == 1)
		{
			Rot = glm::rotate(Rot, current_time/5.0f, glm::vec3(1.0f, 0.0f, 0.0f));
		}
		else if (g_eCurrentScene == 2)
        {
			Rot = glm::rotate(Rot, current_time/5.0f, glm::vec3(1.0f, 0.0f, 0.0f));
			Rot = glm::rotate(Rot, current_time/7.0f, glm::vec3(0.0f, 1.0f, 0.0f));
		}
		else if (g_eCurrentScene == 3)
        {
			Rot = glm::rotate(Rot, current_time/5.0f, glm::vec3(1.0f, 0.0f, 0.0f));
			Rot = glm::rotate(Rot, current_time/7.0f, glm::vec3(0.0f, 1.0f, 0.0f));
			Rot = glm::rotate(Rot, current_time/9.0f, glm::vec3(1.0f, 0.0f, 1.0f));
		}
		else if (g_eCurrentScene == 4)
        {
			Trans = glm::translate(Trans, glm::vec3(2.0f*sin(current_time / 500.0f), 0.0f, 0.0f));
        }
		else if (g_eCurrentScene == 5)
        {
			Trans = glm::translate(Trans, glm::vec3(0.0f, 0.0f, 2.0f*sin(current_time / 500.0f)));
			Rot = glm::rotate(Rot, current_time/5.0f, glm::vec3(1.0f, 0.0f, 0.0f));
			Rot = glm::rotate(Rot, current_time/7.0f, glm::vec3(0.0f, 1.0f, 0.0f));
			Rot = glm::rotate(Rot, current_time/9.0f, glm::vec3(1.0f, 0.0f, 1.0f));
		}
		else if (g_eCurrentScene == 6)
        {
			Trans = glm::translate(Trans, glm::vec3(6.0f*cos(current_time / 200.0f), 10.0f*sin(current_time / 1000.0f), 6.0f*(-sin(current_time / 200.0f))));
			Rot = glm::rotate(Rot, current_time/5.0f, glm::vec3(1.0f, 0.0f, 0.0f));
			Rot = glm::rotate(Rot, current_time/7.0f, glm::vec3(0.0f, 1.0f, 0.0f));
			Rot = glm::rotate(Rot, current_time/9.0f, glm::vec3(1.0f, 0.0f, 1.0f));
		}
		else if (g_eCurrentScene == 7)
		{
			float fraction = 2*PI/100.0f;
			float radius = 40.0f;
			for(int i = 0; i < 100; i++) 
			{
				if (i % 2 == 0) radius = 30.0f;
				else radius = 40.0f;
				float position = i*fraction + current_time/1000.0f;
				Models[i] = glm::translate(glm::mat4(1.0f), glm::vec3(-radius*cos(position), 0.0f, radius*sin(position)));
				Models[i] = glm::scale(Models[i], glm::vec3(0.5f, 0.5f, 0.5f));
				Models[i] = glm::rotate(Models[i], position*1000, glm::vec3(0.0f, 0.0f, 1.0f));
			}
		}
    }
    glutPostRedisplay();

    last_time = clock();// update when the last timer;


}

void ReshapeGL( int w, int h )
{
    std::cout << "ReshapGL( " << w << ", " << h << " );" << std::endl;

    if ( h == 0)                                        // Prevent a divide-by-zero error
    {
        h = 1;                                      // Making Height Equal One
    }

    g_iWindowWidth = w;
    g_iWindowHeight = h;

    glViewport( 0, 0, 800, 600 );

}


void KeyboardGL( unsigned char c, int x, int y )
{
    // Store the current scene so we can test if it has changed later.
    unsigned char currentScene = g_eCurrentScene;
	
	switch ( c )
    {
    case '1':
    {
        glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );                         // White background
        g_eCurrentScene = 1;
    }
        break;
    case '2':
    {
        glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );                         // Black background
        g_eCurrentScene = 2;
    }
        break;
    case '3':
    {
        glClearColor( 0.27f, 0.27f, 0.27f, 1.0f );                      // Dark-Gray background
        g_eCurrentScene = 3;
    }
        break;
    case '4':
    {
        glClearColor( 0.4f, 0.4f, 0.4f, 1.0f );                      // Light-Gray background
        g_eCurrentScene = 4;
    }
        break;
    case '5':
    {
        glClearColor( 0.7f, 0.7f, 0.7f, 1.0f );                      // Light-Gray background
        g_eCurrentScene = 5;
    }
		break;
	  case '6':
    {
        glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );                      // Light-Gray background
        g_eCurrentScene = 6;
    }
        break;

	  case '7':
	{
			 glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			 g_eCurrentScene = 7;
	}
		break;
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


    if ( currentScene != g_eCurrentScene )
    {
        std::cout << "Changed Render Scene: " << int( g_eCurrentScene ) << std::endl;
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

    switch ( g_eCurrentScene )
    {
    case 1:
    {
        RenderScene1();
    }
        break;
    case 2:
    {
        RenderScene2();
    }
        break;
    case 3:
    {
        RenderScene3();
    }
        break;
    case 4:
    {
        RenderScene4();
    }
        break;
	case 5:
	case 6:
    {
        RenderScene5();
    }
        break;
	case 7:
	{
		RenderScene7();
	}
	break;
    }


    glutSwapBuffers();
    // All drawing commands applied to the
    // hidden buffer, so now, bring forward
    // the hidden buffer and hide the visible one

}

void RenderScene1()
{
	Projection = glm::perspective(45.0f, 4.0f/3.0f, 0.1f, 100.0f);

	View       = glm::lookAt(
                glm::vec3(4,3,3), // Camera is at (4,3,3), in World Space
                glm::vec3(0,0,0), // and looks at the origin
                glm::vec3(0,1,0)  // Head is up (set to 0,-1,0 to look upside-down)
                );
    // Model matrix : an identity matrix (model will be at the origin)
    // Our ModelViewProjection : multiplication of our 3 matrices
	    

	//init model transformations
	Model = Trans*Rot*Scale;
	MVP        = Projection * View * Model; // Remember, matrix multiplication is the other way around

	glUseProgram(programID_2);

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

    // Draw the triangle !
    glDrawArrays(GL_TRIANGLES, 0, 36); // From index 0 to 3 -> 1 triangle

    glDisableVertexAttribArray(0);

}

void RenderScene2()
{
    Projection = glm::perspective(45.0f, 4.0f/3.0f, 0.1f, 100.0f);

	View       = glm::lookAt(
                glm::vec3(4,3,3), // Camera is at (4,3,3), in World Space
                glm::vec3(0,0,0), // and looks at the origin
                glm::vec3(0,1,0)  // Head is up (set to 0,-1,0 to look upside-down)
                );
    // Model matrix : an identity matrix (model will be at the origin)
    // Our ModelViewProjection : multiplication of our 3 matrices
	    

	//init model transformations
	Model = Model*Rot*Scale*Trans;
	MVP        = Projection * View * Model; // Remember, matrix multiplication is the other way around

	glUseProgram(programID_2);

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

    // Draw the triangle !
    glDrawArrays(GL_TRIANGLES, 0, 36); // From index 0 to 3 -> 1 triangle

    glDisableVertexAttribArray(0);

}

void RenderScene3()
{
Projection = glm::perspective(45.0f, 4.0f/3.0f, 0.1f, 100.0f);

	View       = glm::lookAt(
                glm::vec3(4,3,3), // Camera is at (4,3,3), in World Space
                glm::vec3(0,0,0), // and looks at the origin
                glm::vec3(0,1,0)  // Head is up (set to 0,-1,0 to look upside-down)
                );
    // Model matrix : an identity matrix (model will be at the origin)
    // Our ModelViewProjection : multiplication of our 3 matrices
	    

	//init model transformations
	Model = Trans*Rot*Scale;
	MVP        = Projection * View * Model; // Remember, matrix multiplication is the other way around

	glUseProgram(programID_2);

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

    // Draw the triangle !
    glDrawArrays(GL_TRIANGLES, 0, 36); // From index 0 to 3 -> 1 triangle

    glDisableVertexAttribArray(0);
}
void RenderScene4()
{
Projection = glm::perspective(45.0f, 4.0f/3.0f, 0.1f, 100.0f);

	View       = glm::lookAt(
                glm::vec3(4,3,3), // Camera is at (4,3,3), in World Space
                glm::vec3(0,0,0), // and looks at the origin
                glm::vec3(0,1,0)  // Head is up (set to 0,-1,0 to look upside-down)
                );
    // Model matrix : an identity matrix (model will be at the origin)
    // Our ModelViewProjection : multiplication of our 3 matrices
	    

	//init model transformations
	Model = Trans*Rot*Scale;
	MVP        = Projection * View * Model; // Remember, matrix multiplication is the other way around

	glUseProgram(programID_2);

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

    // Draw the triangle !
    glDrawArrays(GL_TRIANGLES, 0, 36); // From index 0 to 3 -> 1 triangle
    glDisableVertexAttribArray(0);
}

void RenderScene5()
{
   Projection = glm::perspective(45.0f, 4.0f/3.0f, 0.1f, 100.0f);

	View       = glm::lookAt(
                glm::vec3(0,20,1), // Camera is at (4,3,3), in World Space
                glm::vec3(0,0,0), // and looks at the origin
                glm::vec3(0,1,0)  // Head is up (set to 0,-1,0 to look upside-down)
                );
    // Model matrix : an identity matrix (model will be at the origin)
    // Our ModelViewProjection : multiplication of our 3 matrices
	    

	//init model transformations
	Model = Trans*Rot*Scale;
	MVP        = Projection * View * Model; // Remember, matrix multiplication is the other way around

	glUseProgram(programID_2);

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

    // Draw the triangle !
    glDrawArrays(GL_TRIANGLES, 0, 36); // From index 0 to 3 -> 1 triangle

    glDisableVertexAttribArray(0);
}

void RenderScene7() {
	 Projection = glm::perspective(45.0f, 4.0f/3.0f, 0.1f, 500.0f);

	View       = glm::lookAt(
                glm::vec3(0,100,1), // Camera is at (4,3,3), in World Space
                glm::vec3(0,0,0), // and looks at the origin
                glm::vec3(0,1,0)  // Head is up (set to 0,-1,0 to look upside-down)
                );
    // Model matrix : an identity matrix (model will be at the origin)
    // Our ModelViewProjection : multiplication of our 3 matrices
	    

	//init model transformations
	for(int i = 0; i < 100; i++)
	{
	MVP        = Projection * View * Models[i]; // Remember, matrix multiplication is the other way around

	glUseProgram(programID_2);

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

    // Draw the triangle !
    glDrawArrays(GL_TRIANGLES, 0, 36); // From index 0 to 3 -> 1 triangle

    glDisableVertexAttribArray(0);
	}
}

void SetupGL() //
{

    //Parameter handling
    glShadeModel (GL_SMOOTH);
    glEnable(GL_DEPTH_TEST);

    GLfloat light_position[] = { 0.0, 5.0,5.0,0.0 };
    GLfloat mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
	GLfloat mat_shininess[] = { 50.0 };

    // polygon rendering mode
    glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);
	glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
	glEnable(GL_COLOR_MATERIAL);
    glColorMaterial( GL_FRONT, GL_AMBIENT_AND_DIFFUSE );

	//Set up light source
    glLightfv(GL_LIGHT0, GL_POSITION, light_position);
    glEnable(GL_LIGHTING);
    glEnable(GL_LIGHT0);

    // Black background
    glClearColor(0.0f,0.0f,0.0f,1.0f);

    // Register GLUT callbacks
    glutDisplayFunc(DisplayGL);
    glutKeyboardFunc(KeyboardGL);
    glutReshapeFunc(ReshapeGL);

    // Setup initial GL State
    glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
    glClearDepth( 1.0f );

    //
    // Init GLEW
    if ( glewInit() != GLEW_OK )
    {
        std::cerr << "Failed to initialize GLEW." << std::endl;
        exit(-1);
    }

    // Setup initial GL State
    glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
    glClearDepth( 1.0f );

    std::cout << "Initialise OpenGL: Success!" << std::endl;

    //VAO
    //        glGenVertexArrays(1, &VertexArrayID);
    //        glBindVertexArray(VertexArrayID);

    // Create and compile our GLSL program from the shaders
    programID_1 = LoadShaders( "SimpleVertexShader.vertexshader", "SimpleFragmentShader.fragmentshader" );
    programID_2 = LoadShaders( "SimpleTransform.vertexshader", "SimpleFragmentShader.fragmentshader" );

	MatrixID = glGetUniformLocation(programID_2, "MVP");

    //VBO
    static const GLfloat g_vertex_buffer_data[] = {
        
		//Front
		-1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        -1.0f,  1.0f, 1.0f,

		1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f,  1.0f, 1.0f,

		//Rear
		-1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f,  1.0f, -1.0f,

		1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        -1.0f,  1.0f, -1.0f,

		//Top
		-1.0f,  1.0f, 1.0f,
		1.0f,  1.0f, 1.0f,
		1.0f,  1.0f, -1.0f,

		-1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, -1.0f,
		-1.0f, 1.0f, -1.0f,

		//Bottom
		-1.0f,  -1.0f, 1.0f,
		1.0f,  -1.0f, 1.0f,
		1.0f,  -1.0f, -1.0f,

		-1.0f, -1.0f, 1.0f,
		1.0f, -1.0f, -1.0f,
		-1.0f, -1.0f, -1.0f,

		//Left
		-1.0f,  -1.0f, -1.0f,
		-1.0f,  -1.0f, 1.0f,
		-1.0f,  1.0f, 1.0f,

		-1.0f, -1.0f, -1.0f,
		-1.0f, 1.0f, -1.0f,
		-1.0f, 1.0f, 1.0f,

		//Right
		1.0f,  -1.0f, -1.0f,
		1.0f,  -1.0f, 1.0f,
		1.0f,  1.0f, 1.0f,

		1.0f, -1.0f, -1.0f,
		1.0f, 1.0f, -1.0f,
		1.0f, 1.0f, 1.0f,
	};

    // Generate 1 buffer, put the resulting identifier in vertexbuffer
    glGenBuffers(1, &vertexbuffer);
    // The following commands will talk about our 'vertexbuffer' buffer
    glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
    // Give our vertices to OpenGL.
    glBufferData(GL_ARRAY_BUFFER, sizeof(g_vertex_buffer_data), g_vertex_buffer_data, GL_STATIC_DRAW);
}
