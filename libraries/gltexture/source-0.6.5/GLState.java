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
 
import javax.media.opengl.*;
import com.sun.opengl.*;
import com.sun.opengl.util.*;

// Changes:
// Renamed to GLState

/**
 * @invisible
 * This class that offers some utility methods to set and restore opengl state.
 */
public class GLState
{
    public GLState(GL gl)
    {
        this.gl = gl;  
    }
  
    public void setOrthographicView(int w, int h)
    {
        gl.glViewport(0, 0, w, h);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(0.0, w, 0.0, h, -100.0, +100.0);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
    public void saveView()
    {
        gl.glPushAttrib(GL.GL_VIEWPORT_BIT);
        saveGLMatrices();
    }

    public void restoreView()
    {
        restoreGLMatrices();
        gl.glPopAttrib();
    }
    
    public void saveGLState()
    {
        gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
        saveGLMatrices();
    }      
   
    public void restoreGLState()
    {
        restoreGLMatrices();
        gl.glPopAttrib();
    }
    
    public void saveGLMatrices()
    {
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();
        //gl.glMatrixMode(GL.GL_TEXTURE);
        //gl.glPushMatrix();
    }

    public void restoreGLMatrices()
    {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPopMatrix();
        //gl.glMatrixMode(GL.GL_TEXTURE);
        //gl.glPopMatrix();
    }

    protected GL gl;    
}
