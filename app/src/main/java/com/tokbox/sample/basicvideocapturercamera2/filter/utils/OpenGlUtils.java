package com.tokbox.sample.basicvideocapturercamera2.filter.utils;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 功能：
 * </p>
 * <p>Copyright corp.xxx.com 2018 All right reserved </p>
 *
 * @author tuke 时间 2019/7/7
 * @email tuke@xxx.com
 * <p>
 * 最后修改人：无
 * <p>
 */
public class OpenGlUtils {

    private static final String TAG = "ShaderUtils";

    public static final int NO_TEXTURE = -1;
    public static final int NOT_INIT = -1;
    public static final int ON_DRAWN = 1;

    /**
     * 根据着色器代码 文件路径 创建主程序
     * @param res 资源对象
     * @param vertexResPath   顶点着色器 文件路径
     * @param fragmentResPath 片元着色器 文件路径
     * @return
     */
    public static int createProgram(Resources res, String vertexResPath,String fragmentResPath){
        return createProgram("loadShaderSrcFromAssetFile(res, vertexResPath)", "loadShaderSrcFromAssetFile(res, fragmentResPath)");
    }

    /**
     * 根据 着色器代码 创建 主程序
     * @param vertexSrcCode 顶点着色器 代码
     * @param fragSrcCode   片元着色器 代码
     * @return
     */
    public static int createProgram(String vertexSrcCode, String fragSrcCode){
        String vertexCode = "// 顶点坐标\n" +
                "attribute vec4 vPosition;\n" +
                "// 顶点对应的纹理坐标\n" +
                "attribute vec2 vCoord;\n" +
                "// 顶点变换矩阵\n" +
                "uniform mat4 vMatrix;\n" +
                "\n" +
                "// 传给片元着色器的 纹理坐标\n" +
                "varying vec2 aTextureCoordinate;\n" +
                "\n" +
                "void main(){\n" +
                "    gl_Position = vMatrix * vPosition;\n" +
                "    aTextureCoordinate = vCoord;\n" +
                "}";
        String fragmentCode = "precision mediump float;\n" +
                "varying vec2 aTextureCoordinate;\n" +
                "uniform sampler2D vTexture;\n" +
                "void main() {\n" +
                "    vec4 color = texture2D( vTexture, aTextureCoordinate);\n" +
                "    float rgb = color.g;\n" +
                "    vec4 c = vec4(rgb,rgb,rgb,color.a);\n" +
                "    gl_FragColor = c;\n" +
                "}";
        String BILATERAL_FRAGMENT_SHADER = "" +
                "   varying highp vec2 aTextureCoordinate;\n" +
                "\n" +
                "    uniform sampler2D vTexture;\n" +
                "\n" +
                "    uniform highp vec2 singleStepOffset;\n" +
                "    uniform highp vec4 params;\n" +
                "    uniform highp float brightness;\n" +
                "\n" +
                "    const highp vec3 W = vec3(0.299, 0.587, 0.114);\n" +
                "    const highp mat3 saturateMatrix = mat3(\n" +
                "        1.1102, -0.0598, -0.061,\n" +
                "        -0.0774, 1.0826, -0.1186,\n" +
                "        -0.0228, -0.0228, 1.1772);\n" +
                "    highp vec2 blurCoordinates[24];\n" +
                "\n" +
                "    highp float hardLight(highp float color) {\n" +
                "    if (color <= 0.5)\n" +
                "        color = color * color * 2.0;\n" +
                "    else\n" +
                "        color = 1.0 - ((1.0 - color)*(1.0 - color) * 2.0);\n" +
                "    return color;\n" +
                "}\n" +
                "\n" +
                "    void main(){\n" +
                "    highp vec3 centralColor = texture2D(vTexture, aTextureCoordinate).rgb;\n" +
                "    blurCoordinates[0] = aTextureCoordinate.xy + singleStepOffset * vec2(0.0, -10.0);\n" +
                "    blurCoordinates[1] = aTextureCoordinate.xy + singleStepOffset * vec2(0.0, 10.0);\n" +
                "    blurCoordinates[2] = aTextureCoordinate.xy + singleStepOffset * vec2(-10.0, 0.0);\n" +
                "    blurCoordinates[3] = aTextureCoordinate.xy + singleStepOffset * vec2(10.0, 0.0);\n" +
                "    blurCoordinates[4] = aTextureCoordinate.xy + singleStepOffset * vec2(5.0, -8.0);\n" +
                "    blurCoordinates[5] = aTextureCoordinate.xy + singleStepOffset * vec2(5.0, 8.0);\n" +
                "    blurCoordinates[6] = aTextureCoordinate.xy + singleStepOffset * vec2(-5.0, 8.0);\n" +
                "    blurCoordinates[7] = aTextureCoordinate.xy + singleStepOffset * vec2(-5.0, -8.0);\n" +
                "    blurCoordinates[8] = aTextureCoordinate.xy + singleStepOffset * vec2(8.0, -5.0);\n" +
                "    blurCoordinates[9] = aTextureCoordinate.xy + singleStepOffset * vec2(8.0, 5.0);\n" +
                "    blurCoordinates[10] = aTextureCoordinate.xy + singleStepOffset * vec2(-8.0, 5.0);\n" +
                "    blurCoordinates[11] = aTextureCoordinate.xy + singleStepOffset * vec2(-8.0, -5.0);\n" +
                "    blurCoordinates[12] = aTextureCoordinate.xy + singleStepOffset * vec2(0.0, -6.0);\n" +
                "    blurCoordinates[13] = aTextureCoordinate.xy + singleStepOffset * vec2(0.0, 6.0);\n" +
                "    blurCoordinates[14] = aTextureCoordinate.xy + singleStepOffset * vec2(6.0, 0.0);\n" +
                "    blurCoordinates[15] = aTextureCoordinate.xy + singleStepOffset * vec2(-6.0, 0.0);\n" +
                "    blurCoordinates[16] = aTextureCoordinate.xy + singleStepOffset * vec2(-4.0, -4.0);\n" +
                "    blurCoordinates[17] = aTextureCoordinate.xy + singleStepOffset * vec2(-4.0, 4.0);\n" +
                "    blurCoordinates[18] = aTextureCoordinate.xy + singleStepOffset * vec2(4.0, -4.0);\n" +
                "    blurCoordinates[19] = aTextureCoordinate.xy + singleStepOffset * vec2(4.0, 4.0);\n" +
                "    blurCoordinates[20] = aTextureCoordinate.xy + singleStepOffset * vec2(-2.0, -2.0);\n" +
                "    blurCoordinates[21] = aTextureCoordinate.xy + singleStepOffset * vec2(-2.0, 2.0);\n" +
                "    blurCoordinates[22] = aTextureCoordinate.xy + singleStepOffset * vec2(2.0, -2.0);\n" +
                "    blurCoordinates[23] = aTextureCoordinate.xy + singleStepOffset * vec2(2.0, 2.0);\n" +
                "\n" +
                "    highp float sampleColor = centralColor.g * 22.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[0]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[1]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[2]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[3]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[4]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[5]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[6]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[7]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[8]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[9]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[10]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[11]).g;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[12]).g * 2.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[13]).g * 2.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[14]).g * 2.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[15]).g * 2.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[16]).g * 2.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[17]).g * 2.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[18]).g * 2.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[19]).g * 2.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[20]).g * 3.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[21]).g * 3.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[22]).g * 3.0;\n" +
                "    sampleColor += texture2D(vTexture, blurCoordinates[23]).g * 3.0;\n" +
                "\n" +
                "    sampleColor = sampleColor / 62.0;\n" +
                "\n" +
                "    highp float highPass = centralColor.g - sampleColor + 0.5;\n" +
                "\n" +
                "    for (int i = 0; i < 5; i++) {\n" +
                "        highPass = hardLight(highPass);\n" +
                "    }\n" +
                "    highp float lumance = dot(centralColor, W);\n" +
                "\n" +
                "    highp float alpha = pow(lumance, params.r);\n" +
                "\n" +
                "    highp vec3 smoothColor = centralColor + (centralColor-vec3(highPass))*alpha*0.1;\n" +
                "\n" +
                "    smoothColor.r = clamp(pow(smoothColor.r, params.g), 0.0, 1.0);\n" +
                "    smoothColor.g = clamp(pow(smoothColor.g, params.g), 0.0, 1.0);\n" +
                "    smoothColor.b = clamp(pow(smoothColor.b, params.g), 0.0, 1.0);\n" +
                "\n" +
                "    highp vec3 lvse = vec3(1.0)-(vec3(1.0)-smoothColor)*(vec3(1.0)-centralColor);\n" +
                "    highp vec3 bianliang = max(smoothColor, centralColor);\n" +
                "    highp vec3 rouguang = 2.0*centralColor*smoothColor + centralColor*centralColor - 2.0*centralColor*centralColor*smoothColor;\n" +
                "\n" +
                "    gl_FragColor = vec4(mix(centralColor, lvse, alpha), 1.0);\n" +
                "    gl_FragColor.rgb = mix(gl_FragColor.rgb, bianliang, alpha);\n" +
                "    gl_FragColor.rgb = mix(gl_FragColor.rgb, rouguang, params.b);\n" +
                "\n" +
                "    highp vec3 satcolor = gl_FragColor.rgb * saturateMatrix;\n" +
                "    gl_FragColor.rgb = mix(gl_FragColor.rgb, satcolor, params.a);\n" +
                "    gl_FragColor.rgb = vec3(gl_FragColor.rgb + vec3(brightness));\n" +
                "}";

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexCode);
        int fragShader = loadShader(GLES20.GL_FRAGMENT_SHADER, BILATERAL_FRAGMENT_SHADER);
        if (vertexShader == 0 || fragShader == 0) {
            return 0;
        }
        return createProgram(vertexShader, fragShader);

    }


    /**
     * 绑定着色器，链接主程序
     * @param vertexShader
     * @param fragShader
     * @return
     */
    public static int createProgram(int vertexShader, int fragShader) {
        if ( vertexShader == 0 || fragShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        // 绑定顶点着色器
        GLES20.glAttachShader(program, vertexShader);
        // 绑定片元着色器
        GLES20.glAttachShader(program, fragShader);
        // 链接主程序
        GLES20.glLinkProgram(program);

        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Log.e(TAG,"Could not compile program:" + program);
            Log.e(TAG,"GLES20 Error:"+ GLES20.glGetProgramInfoLog(program));

            GLES20.glDeleteProgram(program);
            program = 0;
        }
        return program;
    }

    /**
     * 加载源代码，编译shader
     * @param type    {@link GLES20#GL_VERTEX_SHADER,GLES20#GL_FRAGMENT_SHADER}
     * @param srcCode
     * @return
     */
    public static int loadShader(int type, String srcCode) {
        // 创建shader
        int shader = GLES20.glCreateShader(type);
        // 加载源代码
        GLES20.glShaderSource(shader, srcCode);
        // 编译shader
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        // 查看编译状态
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {

            Log.e(TAG,"Could not compile shader:" + shader
                    + " type = " + (type == GLES20.GL_VERTEX_SHADER ? "GL_VERTEX_SHADER" : "GL_FRAGMENT_SHADER") );
            Log.e(TAG,"GLES20 Error:"+ GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
        return shader;
    }

    /**
     * 从文件中 shader 源码
     * @param resources
     * @param shaderNamePath
     * @return
     */
    public static String loadShaderSrcFromAssetFile(Resources resources, String shaderNamePath) {
        StringBuilder result=new StringBuilder();
        try{
            InputStream is=resources.getAssets().open(shaderNamePath);
            int ch;
            byte[] buffer=new byte[1024];
            while (-1!=(ch=is.read(buffer))){
                result.append(new String(buffer,0,ch));
            }
        }catch (Exception e){
            return null;
        }
//        return result.toString().replaceAll("\\r\\n","\n");
        return result.toString().replaceAll("\\r\\n","");

    }


    /**
     * 从资源文件 读 shader 源码
     * @param resources
     * @param resourceId
     * @return
     */
    public static String readShaderFromRawResource(Resources resources, final int resourceId){
        final InputStream inputStream =resources.openRawResource(resourceId);
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String nextLine;
        final StringBuilder body = new StringBuilder();
        try{
            while ((nextLine = bufferedReader.readLine()) != null){
                body.append(nextLine);
                body.append('\n');
            }
        }
        catch (IOException e){
            return null;
        }
        return body.toString();
    }



}
