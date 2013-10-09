// Basic example of GLTexture and GLTextureFilter.
// By Andres Colubri

import processing.opengl.*;
import codeanticode.gltexture.*;

GLTextureFilter pulseEmboss;
GLTexture tex0, tex1;
PImage img;
PFont font;

void setup()
{
    size(640, 480, OPENGL);

    tex0 = new GLTexture(this, "milan_rubbish.jpg");
    tex1 = new GLTexture(this, tex0.width, tex0.height);
    
    // A filter is defined in an xml file where the glsl shaders and grid are specified.
    pulseEmboss = new GLTextureFilter(this, "pulsatingEmboss.xml");
    
    img = new PImage();
    font = loadFont("EstrangeloEdessa-24.vlw");
    textFont(font, 24); 
    
    // A GLTexture object can be created in different ways:
    /*
    //tex0 = new GLTexture(this);
    //tex0 = new GLTexture(this, 200, 200);
    //tex0.loadImage("milan_rubbish.jpg");
    */
    
    /*     
    // GLTexture is a descendant of PImage, so it has pixels that can be 
    // modified.
    tex0.init(100, 100);
    tex0.loadPixels();
    int k = 0;
    for (int j = 0; j < tex0.height; j++)
        for (int i = 0; i < tex0.width; i++)    
        {
           if (j < 50) tex0.pixels[k] = 0xffffffff;
           else tex0.pixels[k] = 0xffffff00;        
           k++;
        }
    // loadTexture function copies pixels to texture.
    tex0.loadTexture();
    */

    /*
    // Images can pe passed to a GLTexture object using a PImage as an intermediate container:
    img = loadImage("milan_rubbish.jpg"); 
    tex0.putImage(img);
    */
}

void draw()
{
   background(0); 

   image(tex0, 0, 0);
   text("source texture", 0, 220);
  
   // A filer is applied on a texture by passing it as a parameter,
   // together with the destination texture. Right after applying the
   // filter, only the texture data in tex1 contains the filtered image,
   // not the pixel nor the image.
   tex0.filter(pulseEmboss, tex1);
      
   // For fastest drawing, the texture can be rendered using  
   // the renderTexture function.
   tex1.renderTexture(mouseX, mouseY);
   fill(0);
   rect(mouseX, mouseY + 200, 200, 40);
   fill(255);
   text("filtered texture", mouseX, mouseY + 220);

   /*   
   // A PImage can be obtained from a GLTexture object.
   tex1.getImage(img);
   image(img, mouseX, mouseY);
   */

   /*
   // updateTexture() copies the texture to the pixels, and then
   // the pixels are updated, so the texture can be drawn as a regular
   // PImage object.
   tex1.updateTexture();
   tex1.updatePixels();
   image(tex1, mouseX, mouseY);
   */
}
