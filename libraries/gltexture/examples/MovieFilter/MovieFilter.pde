// Applying a filter to a movie (it also uses the gsvideo library).
// By Andres Colubri
import processing.opengl.*;

import codeanticode.gsvideo.*;
import codeanticode.gltexture.*;

GSMovie movie;
GLTexture tex0, tex1, tex2;

GLTextureFilter blur, pixelate;
GLTextureFilterParams params;

void setup()
{
    size(640, 480, OPENGL);
   
    movie = new GSMovie(this, "station.mov");
    movie.loop();
   
    tex0 = new GLTexture(this);
    tex1 = new GLTexture(this);
    tex2 = new GLTexture(this);
   
    blur = new GLTextureFilter(this, "gaussBlur.xml");
    pixelate = new GLTextureFilter(this, "pixelate.xml");
 
    params = new GLTextureFilterParams();    
}

void draw()
{
    if (movie.available())
    {
        movie.read();

        tex0.putPixelsIntoTexture(movie);

        params.parFlt1 = map(mouseX, 20, 640, 1, 30);
        tex0.filter(blur, tex1);
        tex1.filter(pixelate, tex2, params);
        
        tex2.renderTexture(0, 0, width, height);
    }
}
