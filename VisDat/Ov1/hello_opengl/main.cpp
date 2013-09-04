#include <GL/freeglut.h>
#include <stdio.h>
#include "../Visual_addon/visuals.h"

void draw(void) {
     
	// Black background
	glClearColor(0.0f,0.0f,0.0f,1.0f);
    glClear(GL_COLOR_BUFFER_BIT);
    //Draw i
    glFlush();
 
}
 
//Main program
 
int main(int argc, char **argv) {
 
    glutInit(&argc, argv);
     
	/*Setting up  The Display
	/    -RGB color model + Alpha Channel = GLUT_RGBA
	/    -RGB color model + Alpha Channel = GLUT_RGBA
	*/
    glutInitDisplayMode(GLUT_RGBA|GLUT_DOUBLE);
     
    //Configure Window Postion
    glutInitWindowPosition(50, 25);
     
    //Configure Window Size
    glutInitWindowSize(480,480);

    //Create Window
    glutCreateWindow("Hello OpenGL");
	Setup();
    //Call to the drawing function
    glutDisplayFunc(Render);
	glutReshapeFunc(Resize);
     
	// Loop require by OpenGL
    glutMainLoop();
    return 0;
}
