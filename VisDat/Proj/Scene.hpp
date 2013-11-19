#include <glm.hpp>

//-------- Variables -------------------------------
extern int g_iWindowHeight, g_iWindowWidth, g_iGLUTWindowHandle;

//-------- Functions --------------------------------

// Cleanup : This is the exist function.
void Cleanup( int exitCode, bool bExit = true );



// SetupGL :  Set up the OpenGL state machine and create a light source
void SetupGL();


// DisplayGL :  The function responsible for drawing everything in the
// OpenGL context associated to a window.
void DisplayGL();

// KeyboardGL :  This functions is called whenever the keyboard is used
// inside the opengl window
void KeyboardGL( unsigned char c, int x, int y );
void Specialkey(int c, int x, int y );

// MouseGL :  This functions is called whenever the mouse is used
// inside the opengl window
void MouseGL( int button, int state, int x, int y );
void Mouse_active( int x, int y);
void Mouse_inactive (int x, int y);


// Handle the window size changes and define the world coordinate
// system and projection type
void ReshapeGL( int w, int h );

//Function called when screen is idle.
void Idle();
// Functions called to draw different scenes
void RenderImage();
void RenderNumber(int number, glm::vec3 position, float rotation);

//function for generating the scene
void genScene(const char* path);