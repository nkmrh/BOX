uniform sampler2D src_tex_unit0;
uniform vec2 src_tex_offset0;
uniform vec2 dest_tex_size;
uniform float par_flt1;

void main(void)
{
    float n = par_flt1;
    float d = 1.0 / n;
    vec2 tex_coords = gl_TexCoord[0].st;

	int fx = int(tex_coords.s * dest_tex_size.x / n);
	int fy = int(tex_coords.t * dest_tex_size.y / n);  
	
    float s = n * (float(fx) + d) / dest_tex_size.x;
    float t = n * (float(fy) + d) / dest_tex_size.y;
    
    gl_FragColor = texture2D(src_tex_unit0, vec2(s, t)).rgba;
}
