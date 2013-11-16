#include <math.h>
#include <iostream>
#include <ctime>
#include <GL/glew.h>
#include <GL/freeglut.h>
#include <glm.hpp>
#include "Scene.hpp"

#pragma comment(lib, "glew32.lib")
#define GLEW_STATIC

int g_iWindowWidth = 800;
int g_iWindowHeight = 600;
int g_iGLUTWindowHandle = 0;

int main(int argc, char **argv) {

    glutInit(&argc, argv);

    //Setting up  The Display
    //   -RGB color model + Alpha Channel = GLUT_RGBA
    glutInitDisplayMode(GLUT_RGBA|GLUT_DOUBLE);

    // Configure Window Position
    int iScreenWidth = glutGet(GLUT_SCREEN_WIDTH);
    int iScreenHeight = glutGet(GLUT_SCREEN_HEIGHT);

    glutInitDisplayMode( GLUT_RGBA | GLUT_ALPHA | GLUT_DOUBLE | GLUT_DEPTH );

    glutInitWindowPosition(0, 0);
	glutInitWindowSize(g_iWindowWidth, g_iWindowHeight);

    g_iGLUTWindowHandle = glutCreateWindow( "OpenGL" );

    SetupGL();

    // Loop require by OpenGL
    glutMainLoop();
    return 0;
}
//-----------------------------------------------------------


