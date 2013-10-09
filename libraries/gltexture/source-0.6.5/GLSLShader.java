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
// Renamed to GLSLShader.
// Added comments.
// ToDo: 
// Add support for geometry shader.

/**
 * This class encapsulates a glsl shader. Based in the code by JohnG (http://www.hardcorepawn.com/)
 */
public class GLSLShader
{
    /**
     * Creates an instance of GLSLShader.
     * @param parent PApplet
     */
    public GLSLShader(PApplet parent)
    {
        this.parent = parent;
       
        pgl = (PGraphicsOpenGL)parent.g;
        gl = pgl.gl;
		
        programObject = gl.glCreateProgramObjectARB();
        vertexShader = -1;
        fragmentShader = -1;
    }

    /**
     * Loads and compiles the vertex shader contained in file.
     * @param file String
     */	
    public void loadVertexShader(String file)
    {
        String shaderSource = parent.join(parent.loadStrings(file), "\n");
        vertexShader = gl.glCreateShaderObjectARB(GL.GL_VERTEX_SHADER_ARB);
        gl.glShaderSourceARB(vertexShader, 1, new String[]{shaderSource}, (int[]) null, 0);
        gl.glCompileShaderARB(vertexShader);
        checkLogInfo("Vertex shader compilation: ", vertexShader);
        gl.glAttachObjectARB(programObject, vertexShader);
    }

    /**
     * Loads and compiles the fragment shader contained in file.
     * @param file String
     */	
    public void loadFragmentShader(String file)
    {
        String shaderSource = parent.join(parent.loadStrings(file), "\n");
        fragmentShader = gl.glCreateShaderObjectARB(GL.GL_FRAGMENT_SHADER_ARB);
        gl.glShaderSourceARB(fragmentShader, 1, new String[]{shaderSource},(int[]) null, 0);
        gl.glCompileShaderARB(fragmentShader);
        checkLogInfo("Fragment shader compilation: ", fragmentShader);
        gl.glAttachObjectARB(programObject, fragmentShader);
    }

    /**
     * Returns the ID location of the attribute parameter given its name.
     * @param name String
	 * @return int
     */
    public int getAttribLocation(String name)
    {
        return(gl.glGetAttribLocationARB(programObject, name));
    }

    /**
     * Returns the ID location of the uniform parameter given its name.
     * @param name String
	 * @return int
     */
    public int getUniformLocation(String name)
    {
        return(gl.glGetUniformLocationARB(programObject, name));
    }

    /**
     * Links the shader program and validates it.
     */	
    public void linkProgram()
    {
        gl.glLinkProgramARB(programObject);
        gl.glValidateProgramARB(programObject);
        checkLogInfo("GLSL program validation: ", programObject);
    }

    /**
     * Starts the execution of the shader program.
     */
    public void start()
    {
        gl.glUseProgramObjectARB(programObject);
    }

    /**
     * Stops the execution of the shader program.
     */	
    public void stop()
    {
        gl.glUseProgramObjectARB(0);
    }

    /**
     * @invisible
	 * Check the log error for the opengl object obj. Prints error message if needed.
     */		
    protected void checkLogInfo(String title, int obj)
    {
        IntBuffer iVal = BufferUtil.newIntBuffer(1);
        gl.glGetObjectParameterivARB(obj, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

        int length = iVal.get();
        
        if (length <= 1) 
        {
            return;
        }    
        
        // Some error ocurred...
        ByteBuffer infoLog = BufferUtil.newByteBuffer(length);
        iVal.flip();
        gl.glGetInfoLogARB(obj, length, iVal, infoLog);
        byte[] infoBytes = new byte[length];
        infoLog.get(infoBytes);
        System.err.println(title);
        System.err.println(new String(infoBytes));
    }
    
    /**
     * @invisible
     */		
    protected PApplet parent;
	
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
    protected int programObject;
	
    /**
     * @invisible
     */		
    protected int vertexShader;
	
    /**
     * @invisible
     */		
    protected int fragmentShader;    
}
