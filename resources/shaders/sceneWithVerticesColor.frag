#version 330
//uniform vec4 uni_color;         //1 titik punya warna sama / uniform
out vec4 frag_color;
in vec4 out_color;
void main()
{
    //warna bidang
    //frag_color = vec4(1.0, 0.0, 0.0, 1.0);            //hardcode
    frag_color = out_color;
}