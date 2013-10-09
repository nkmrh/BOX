/**
 * This package provides classes to facilitate the handling of opengl textures and glsl shaders in Processing.
 * @author Andres Colubri
 * @version 0.6.5
 *
 * Copyright (c) 2008 Andres Colubri
 *
 * This source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is available on the World
 * Wide Web at <http://www.gnu.org/copyleft/gpl.html>. You can also
 * obtain it by writing to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
 
package codeanticode.gltexture;

import processing.core.*;
import processing.opengl.*;

import javax.media.opengl.*;
import com.sun.opengl.util.*;
import java.nio.*;

// Changes:
// Renamed to GLTexture.
// Fixed bug in putImage where height was compared to img.width.
// Added putPixelsIntoTexture and putPixels methods.
// Added comments.// 
// ToDo:  
// Color texture using tint color in renderTexture method.
// Add support for rectangular textures, mipmapping, etc.
// Test compatiliby of texture usage with normal processing operation.

/**
 * This class adds an opengl texture to a PImage object. The texture is handled in a similar way to the
 * pixels property: image data can be copied to and from the texture using loadTexture and updateTexture methods.
 * However, bringing the texture down to image or pixels data can slow down the application considerably (since
 * involves copying texture data from GPU to CPU), especially when handling large textures. So it is recommended 
 * to do all the texture handling without calling updateTexture, and doing so only at the end if the texture
 * is needed as a regular image.
 */
public class GLTexture extends PImage implements PConstants 
{
    /**
     * Creates an instance of GLTexture with size 1x1. The texture is not initialized.
     * @param parent PApplet
     */
    public GLTexture(PApplet parent)
    {
        super(1, 1, ARGB);  
        this.parent = parent;
       
        pgl = (PGraphicsOpenGL)parent.g;
        gl = pgl.gl;       
        glstate = new GLState(gl);
    }  

    /**
     * Creates an instance of GLTexture with size width x height. The texture is initialized (empty) to that size.
     * @param parent PApplet
     * @param width int 
     * @param height int 
     */	 
    public GLTexture(PApplet parent, int width, int height)
    {
        super(width, height, ARGB);  
        this.parent = parent;
       
        pgl = (PGraphicsOpenGL)parent.g;
        gl = pgl.gl;
        glstate = new GLState(gl);
       
        initTexture(width, height);
    }
    
    /**
     * Creates an instance of GLTexture using image file filename as source.
	 * @param filename String
     */	
    public GLTexture(PApplet parent, String filename)
    {
        super(1, 1, ARGB);  
        this.parent = parent;
       
        pgl = (PGraphicsOpenGL)parent.g;
        gl = pgl.gl;
        glstate = new GLState(gl);      
    
        loadTexture(filename);
    }

    /**
     * Sets the size of the image and texture to width x height. If the texture is already initialized,
	 * it first destroys the current opengl texture object and then creates a new one with the specified
	 * size.
     * @param width int
     * @param height int
     */
    public void init(int width, int height)
    {
        init(width, height, ARGB);
        initTexture(width, height);
    }

    /**
     * Returns true if the texture has been initialized.
     * @return boolean
     */  
    public boolean available()
    {
        return 0 < tex[0];
    }
    
    /**
     * Provides the ID of the opegl texture object.
     * @return int
     */	
    public int getTextureID()
    {
        return tex[0];
    }

    /**
     * Returns the texture target.
     * @return int
     */	
    public int getTextureTarget()
    {
        // Only 2D textures for now...
        return GL.GL_TEXTURE_2D;
    }    

    /**
     * Puts img into texture, pixels and image.
     * @param img PImage
     */
    public void putImage(PImage img)
    {
        img.loadPixels();
        
        if ((img.width != width) || (img.height != height))
        {
            init(img.width, img.height);
        }

        // Putting img into pixels...
        parent.arraycopy(img.pixels, pixels);
   
        // ...into texture...
        loadTexture();
        
        // ...and into image.
        updatePixels();
    }

    /**
     * Puts pixels of img into texture only.
     * @param img PImage
     */
    public void putPixelsIntoTexture(PImage img)
    {
	
        if ((img.width != width) || (img.height != height))
        {
            init(img.width, img.height);
        }
   
        // Putting into texture.
		putPixels(img.pixels);
    }
	
    /**
     * Copies texture to img.
     * @param img PImage
     */	
    public void getImage(PImage img)
    {
        int w = width;
        int h = height;
        
        if ((img.width != w) || (img.height != h))
        {
            img.init(w, h, ARGB);
        }
       
        IntBuffer buffer = BufferUtil.newIntBuffer(w * h * 4);
        gl.glBindTexture(GL.GL_TEXTURE_2D, tex[0]);
        gl.glGetTexImage(GL.GL_TEXTURE_2D, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, buffer);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);       
       
        buffer.get(img.pixels);
        img.updatePixels();       
    }

    /**
     * Load texture, pixels and image from file.
     * @param filename String
     */
    public void loadTexture(String filename)
    {
        PImage img = parent.loadImage(filename);
        putImage(img);
    }

    /**
     * Copy pixels to texture (loadPixels should have been called beforehand).
     */	
    public void loadTexture()
    {
	    putPixels(pixels);
    }
	
    /**
     * Copy texture to pixels (doesn't call updatePixels).
     */		
    public void updateTexture()
    {
        IntBuffer buffer = BufferUtil.newIntBuffer(width * height * 4);        
        gl.glBindTexture(GL.GL_TEXTURE_2D, tex[0]);
        gl.glGetTexImage(GL.GL_TEXTURE_2D, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, buffer);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        buffer.get(pixels);
    }

    /**
     * Applies filter texFilter using this texture as source and destTex as destination.
     */
    public void filter(GLTextureFilter texFilter, GLTexture destTex)
    {
        texFilter.apply(new GLTexture[] { this }, new GLTexture[] { destTex });
    }

    /**
     * Applies filter texFilter using this texture as source and destTex as multiple destinations.
     */
    public void filter(GLTextureFilter texFilter, GLTexture[] destTexArray)
    {
        texFilter.apply(new GLTexture[] { this }, destTexArray);
    }

    /**
     * Applies filter texFilter using this texture as source, destTex as destination and params as the
	 * parameters for the filter.
     */	
    public void filter(GLTextureFilter texFilter, GLTexture destTex, GLTextureFilterParams params)
    {
        texFilter.apply(new GLTexture[] { this }, new GLTexture[] { destTex }, params);
    }

	/**
     * Applies filter texFilter using this texture as source, destTex as multiple destinations and params as the
	 * parameters for the filter.
     */	
    public void filter(GLTextureFilter texFilter, GLTexture[] destTexArray, GLTextureFilterParams params)
    {
        texFilter.apply(new GLTexture[] { this }, destTexArray, params);
    }

    /**
     * Draws the texture using the opengl commands, inside a rectangle located at the origin with the original
	 * size of the texture.
     */		
    public void renderTexture()
    {
        renderTexture(0, 0, width, height);
    }

    /**
     * Draws the texture using the opengl commands, inside a rectangle located at (x,y) with the original
	 * size of the texture.
     * @param x float
     * @param y float
     */	
    public void renderTexture(float x, float y)
    {
        renderTexture(x, y, width, height);
    }
    
    /**
     * Draws the texture using the opengl commands, inside a rectangle of width w and height h
	 * located at (x,y).
     * @param x float
     * @param y float
     * @param w float
     * @param h float	 
     */	
    public void renderTexture(float x, float y, float w, float h)
    {
        pgl.beginGL();
        
        int pw = parent.width;
        int ph = parent.height;
        
        glstate.saveGLState();  

        glstate.setOrthographicView(pw, ph);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, tex[0]);
    
        // set tint color??
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    
        gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(x, ph - y);

            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(x + w, ph - y);

            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex2f(x + w, ph - (y + h));

            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex2f(x, ph - (y + h));
        gl.glEnd();  
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        
        glstate.restoreGLState();

        pgl.endGL();      
    }

    /**
     * @invisible
	 * Creates the opengl texture object.
     * @param w int
     * @param h int	 
     */
    protected void initTexture(int w, int h)
    {
        if (tex[0] != 0)
        {
            releaseTexture();
        }
        
        gl.glGenTextures(1, tex, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, tex[0]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, w, h, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, null);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);        
    }

    /**
     * @invisible
	 * Deletes the opengl texture object.
     */
    protected void releaseTexture()
    {
        gl.glDeleteTextures(1, tex, 0);  
        tex[0] = 0;
    }

    /**
     * @invisible
	 * Copies pix into the texture.
     * @param pix int[]
     */	
    public void putPixels(int[] pix)
    {
        if (tex[0] == 0)
        {
            initTexture(width, height);
        }      
        gl.glBindTexture(GL.GL_TEXTURE_2D, tex[0]);
        gl.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, width, height, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, IntBuffer.wrap(pix));
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    }
    
    /**
     * @invisible
     */	
    protected GL gl;
	
    /**
     * @invisible
     */		
    protected PGraphicsOpenGL pgl;
	
    /**
     * @invisible
     */		
    protected int[] tex = { 0 }; 
	
    /**
     * @invisible
     */		
    protected GLState glstate;
}
