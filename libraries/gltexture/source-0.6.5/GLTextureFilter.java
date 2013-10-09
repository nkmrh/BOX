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
import processing.xml.*;

import javax.media.opengl.*;
import java.nio.*;

// Changes:
// Renamed to GLTextureFilter.
// Added checkDestTex method to initialize destination textures.
// Added comments. 
// ToDo: 
// Test multiple source and destination textures (glDrawBuffers).
// Check setGLConf method, what is really needed and what it not.

/**
 * This class defines a 2D filter to apply on GLTexture objects. A filter is basically
 * a glsl shader program with a set of predefined uniform attributes and a 2D grid 
 * where the input textures are mapped on. The points of the 2D grid can be altered in
 * the vertex stage of the filter, allowing for arbitrary distorsions in the shape of
 * the mesh.
 * The filter is specified in a xml file where the files names of the vertex and fragment 
 * shaders stored, as well as the definition of the grid (resolution and spacing).
 */
public class GLTextureFilter
{
    /**
     * Creates an instance of GLTextureFilter, loading the filter from filename. 
	 * The number of input textures is set to 1.
     * @param parent PApplet
     * @param filename String
     */
    public GLTextureFilter(PApplet parent, String filename)
    {
        this(parent, filename, 1);
    }

    /**	
     * Creates an instance of GLTextureFilter, loading the filter from filename.
	 * The filter will be able to accept up to nInTex input or source textures.
     * @param parent PApplet
     * @param filename String 
	 * @param nInTex int
     */	 
    public GLTextureFilter(PApplet parent, String filename, int nInTex)
    {
        this.parent = parent;
       
        pgl = (PGraphicsOpenGL)parent.g;
        gl = pgl.gl;
        glstate = new GLState(gl);        
        initFBO();

        defFilterParams = new GLTextureFilterParams();
        
        xmlFilterCfg = new XMLElement(parent, filename);
        initFilter(nInTex);
    }
  
    /**
     * Returns the description of the filter.
     * @return String
     */   
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the number of input or source textures supported by this filter.
     * @return int
     */	
    public int getNumInputTextures()
    {
        return numInputTex;  
    }

    /**
     * Applies the shader program on textures srcTex, writing the output to the textures destTex.
	 * The default parameter with all the fields set to zero is used. 
     * @param srcTex GLTexture[] 
     * @param destTex GLTexture[]
     */	
    public void apply(GLTexture[] srcTex, GLTexture[] destTex)
    {
        apply(srcTex, destTex, defFilterParams);
    }
    
    /**
     * Applies the shader program on textures srcTex, writing the output to the textures destTex.
	 * Uses the provide parameters params. 
     * @param srcTex GLTexture[] 
     * @param destTex GLTexture[]
	 * @param params GLTextureFilterParams
     */	
    public void apply(GLTexture[] srcTex, GLTexture[] destTex, GLTextureFilterParams params)
    {
        pgl.beginGL();
      
	    checkDestTex(destTex, srcTex[0].width, srcTex[0].height);
	  
        int w = destTex[0].width;
        int h = destTex[0].height;
        setGLConf(w, h);
      
        bindDestFBO(); 
        bindDestTexToFBO(destTex);
          
        shader.start();
        setupShader(srcTex, w, h, params);
      
        bindSrcTex(srcTex);
        grid.render(w, h, srcTex.length);
        unbindSrcTex(srcTex.length);
    
        shader.stop();

        unbindDestFBO();
        
        
        restoreGLConf();

        pgl.endGL();
    }
  
    /**
     * @invisible
     */  
    protected void setGLConf(int w, int h)
    {
        blendOn = gl.glIsEnabled(GL.GL_BLEND);
        //gl.glGetIntegerv(GL.GL_POLYGON_MODE, polyMode);
      
        gl.glDisable(GL.GL_BLEND);
        gl.glPolygonMode(GL.GL_FRONT, GL.GL_FILL);

        glstate.saveView();
        glstate.setOrthographicView(w, h);
    }

    /**
     * @invisible
     */	
    protected void restoreGLConf()
    {
        glstate.restoreView();

        if (blendOn) gl.glEnable(GL.GL_BLEND);
		//glPolygonMode(GL_FRONT, m_poly_mode);
    }
    
    /**
     * @invisible
     */	
    protected void bindSrcTex(GLTexture[] srcTex)
    {
        gl.glEnable(GL.GL_TEXTURE_2D);
        
        for (int i = 0; i < srcTex.length; i++)
        {
            gl.glActiveTexture(GL.GL_TEXTURE0 + i);
            gl.glBindTexture(GL.GL_TEXTURE_2D, srcTex[i].getTextureID());
        }      
    }

    /**
     * @invisible
     */	
    protected void unbindSrcTex(int ntex)
    {
        for (int i = ntex; 0 < i; i--)
        {
            gl.glActiveTexture(GL.GL_TEXTURE0 + i - 1);
            gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        }
    }

    /**
     * @invisible
     */	
    protected void bindDestFBO()
    {
        gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, destFBO[0]);  
    }
  
     /**
     * @invisible
     */ 
    protected void unbindDestFBO()
    {
        gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);  
    }  
  
    /**
     * @invisible
     */  
    protected void bindDestTexToFBO(GLTexture[] destTex)
    {
        for (int i = 0; i < destTex.length; i++)
        {
            gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT + i, GL.GL_TEXTURE_2D, destTex[i].getTextureID(), 0);
        }
        checkFBO();
        
		// Check this:
        // gl.glDrawBuffers(2, IntBuffer.wrap(colorDrawBuffers));
    }    
    
    /**
     * @invisible
     */	
    protected void checkFBO()
    {
        int stat = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
        if (stat != GL.GL_FRAMEBUFFER_COMPLETE_EXT) System.err.println("Framebuffer Object error!");
    }
    
    /**
     * @invisible
     */	
    protected void initFBO()
    {
        gl.glGenFramebuffersEXT(1, destFBO, 0);
    }
    
    /**
     * @invisible
     */	
    protected void initFilter(int nInTex)
    {
        grid = null;
        String vertexFN, geometryFN, fragmentFN;
        
        // Parsing xml configuration.
        int n = xmlFilterCfg.getChildCount();
        String name;
        XMLElement child;
        vertexFN = geometryFN = fragmentFN = "";
        for (int i = 0; i < n; i++) 
        {
            child = xmlFilterCfg.getChild(i);
            name = child.getName();
            if (name.equals("description"))
            {
                description = child.getContent();
            }            
            else if (name.equals("vertex"))
            {
                vertexFN = child.getContent();
            }
            else if (name.equals("geometry"))
            {
                geometryFN = child.getContent(); 
            }
            else if (name.equals("fragment"))
            {
                fragmentFN = child.getContent();
            }
            else if (name.equals("grid"))
            {
                grid = new GLTextureGrid(gl, child);
            }
            else
            {
                System.err.println("Unrecognized element in filter config file!");
            }
        }

        if (grid == null)
        {
            // Creates a 1x1 grid.
            grid = new GLTextureGrid(gl);
        }

        // Initializing shader.
        shader = new GLSLShader(parent);
        if (!vertexFN.equals(""))
        {
            shader.loadVertexShader(vertexFN);
        }
        if (!geometryFN.equals(""))
        {
            //shader.loadGeometryShader(vertexFN);
        }            
        if (!fragmentFN.equals(""))
        {
            shader.loadFragmentShader(fragmentFN);
        }            

        shader.linkProgram();
           
        // Initializing list of input textures.
        numInputTex = nInTex;
        srcTexUnitUniform = new int[numInputTex];
        srcTexOffsetUniform = new int[numInputTex];
            
        // Gettting uniform parameters.
        for (int i = 0; i < numInputTex; i++)
        {
	        srcTexUnitUniform[i] = shader.getUniformLocation("src_tex_unit" + i);
	        srcTexOffsetUniform[i] = shader.getUniformLocation("src_tex_offset" + i);
        }

        timingDataUniform = shader.getUniformLocation("timing_data");
        destTexSizeUniform = shader.getUniformLocation("dest_tex_size");
        parFlt1Uniform = shader.getUniformLocation("par_flt1");
        parFlt2Uniform = shader.getUniformLocation("par_flt2");
        parFlt3Uniform = shader.getUniformLocation("par_flt3");
        parMat2Uniform = shader.getUniformLocation("par_mat2");
        parMat3Uniform = shader.getUniformLocation("par_mat3");        
		parMat4Uniform = shader.getUniformLocation("par_mat4");
    }

    /**
     * @invisible
     */	
    void setupShader(GLTexture[] srcTex, int w, int h, GLTextureFilterParams params)
    {
        int n = parent.min(numInputTex, srcTex.length); 
        for (int i = 0; i < n; i++)
        {
           if (-1 < srcTexUnitUniform[i]) gl.glUniform1iARB(srcTexUnitUniform[i], i);
           if (-1 < srcTexOffsetUniform[i]) gl.glUniform2fARB(srcTexOffsetUniform[i], 1.0f / srcTex[0].width, 1.0f / srcTex[0].height);
        }
        
        if (-1 < timingDataUniform)
        {
            int fcount = parent.frameCount;
            int msecs = parent.millis();          
            gl.glUniform2fARB(timingDataUniform, fcount, msecs);
        }
        
        if (-1 < destTexSizeUniform) gl.glUniform2fARB(destTexSizeUniform, w, h);
        
        if (-1 < parFlt1Uniform) gl.glUniform1fARB(parFlt1Uniform, params.parFlt1);
        if (-1 < parFlt2Uniform) gl.glUniform1fARB(parFlt2Uniform, params.parFlt2);
        if (-1 < parFlt3Uniform) gl.glUniform1fARB(parFlt3Uniform, params.parFlt3);        
        
        if (-1 < parMat2Uniform) gl.glUniformMatrix2fvARB(parMat2Uniform, 1, false, FloatBuffer.wrap(params.parMat2));
        if (-1 < parMat3Uniform) gl.glUniformMatrix3fvARB(parMat3Uniform, 1, false, FloatBuffer.wrap(params.parMat3));
        if (-1 < parMat4Uniform) gl.glUniformMatrix4fvARB(parMat4Uniform, 1, false, FloatBuffer.wrap(params.parMat4));
    }
	
    /**
     * @invisible
     */	
	protected void checkDestTex(GLTexture[] destTex, int w, int h)
	{
	    for (int i = 0; i < destTex.length; i++)
           if (!destTex[i].available())
		   {
		       destTex[i].init(w, h);
		   }
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
    protected GLState glstate;    
	
    /**
     * @invisible
     */	
    protected int[] destFBO = { 0 };
	
    /**
     * @invisible
     */	
    protected GLTextureGrid grid;
	
    /**
     * @invisible
     */	
	protected GLTextureFilterParams defFilterParams;
    
    /**
     * @invisible
     */	
	protected XMLElement xmlFilterCfg;
	
    /**
     * @invisible
     */	
    protected String description;
    
    /**
     * @invisible
     */	
	protected boolean blendOn;
	
    /**
     * @invisible
     */	
	protected GLSLShader shader;
	
    /**
     * @invisible
     */	
    protected int numInputTex;
	
    /**
     * @invisible
     */	
    protected int[] srcTexUnitUniform;    
	
    /**
     * @invisible
     */	
    protected int[] srcTexOffsetUniform;
	
    /**
     * @invisible
     */	
    protected int timingDataUniform;
	
    /**
     * @invisible
     */	
    protected int destTexSizeUniform;
	
    /**
     * @invisible
     */	
    protected int parFlt1Uniform;
	
    /**
     * @invisible
     */	
    protected int parFlt2Uniform;
	
    /**
     * @invisible
     */	
    protected int parFlt3Uniform;
	
    /**
     * @invisible
     */	
    protected int parMat2Uniform;
	
    /**
     * @invisible
     */	
    protected int parMat3Uniform;
	
    /**
     * @invisible
     */	
    protected int parMat4Uniform;
}
