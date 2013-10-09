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

// Changes:
// Renamed to GLTextureFilterParams.
// Added comments.

/**
 * This class stores the parameters for a filter. There are 6 predefined parameters:
 * three float numbers and three matrices (2x2, 3x3 and 4x4). This parameters
 * should correspond to uniform parameters defined in the shader code.
 */
public class GLTextureFilterParams
{
    /**
     * Creates an instance of GLTextureFilterParams, setting all the parameters to zero.
     */
    public GLTextureFilterParams()
    {
        parFlt1 = parFlt2 = parFlt3 = 0.0f;
        parMat2 = new float[2 * 2];
        parMat3 = new float[3 * 3];
        parMat4 = new float[4 * 4];
    }
    
    /**
     * Sets the (i, j) element of the 2x2 matrix parameter to v.
	 * @param i int
	 * @param j int	 
	 * @param v float	 
     */
    public void setMat2(int i, int j, float v)
    {
        parMat2[2 * i + j] = v; 
    }
	
    /**
     * Returns the (i, j) element of the 2x2 matrix parameter.
	 * @param i int
	 * @param j int
     */	
    public float getMat2(int i, int j)
    {
        return parMat2[2 * i + j];  
    }

    /**
     * Sets the (i, j) element of the 3x3 matrix parameter to v.
	 * @param i int
	 * @param j int	 
	 * @param v float	 
     */	
    public void setMat3(int i, int j, float v)
    {
        parMat3[3 * i + j] = v; 
    }

    /**
     * Returns the (i, j) element of the 3x3 matrix parameter.
	 * @param i int
	 * @param j int
     */		
    public float getMat3(int i, int j)
    {
        return parMat3[3 * i + j];  
    }

    /**
     * Sets the (i, j) element of the 4x4 matrix parameter to v.
	 * @param i int
	 * @param j int	 
	 * @param v float	 
     */	
    public void setMat4(int i, int j, float v)
    {
        parMat4[4 * i + j] = v; 
    }
	
    /**
     * Returns the (i, j) element of the 4x4 matrix parameter.
	 * @param i int
	 * @param j int
     */		
    public float getMat4(int i, int j)
    {
        return parMat4[4 * i + j];  
    }
    
    /**
     * Float parameter 1.
     */
    public float parFlt1;
	
    /**
     * Float parameter 2.
     */
    public float parFlt2;
	
    /**
     * Float parameter 3.
     */
    public float parFlt3;
	
    /**
     * 2x2 matrix parameter.
     */
    public float[] parMat2;
	
    /**
     * 3x3 matrix parameter.
     */
    public float[] parMat3;
	
    /**
     * 4x matrix parameter.
     */
    public float[] parMat4;
}
