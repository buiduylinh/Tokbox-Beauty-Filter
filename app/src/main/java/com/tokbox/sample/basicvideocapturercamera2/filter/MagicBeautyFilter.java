package com.tokbox.sample.basicvideocapturercamera2.filter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;


/**
 * Created by Administrator on 2016/5/22.
 */
public class MagicBeautyFilter extends AbsOesImageFilter {
    private int paramsLocation;
    private int brightnessLocation;
    private int singleStepOffsetLocation;

    public MagicBeautyFilter(Context context) {
        super(context);
    }

    private float toneLevel;
    private float beautyLevel;
    private float brightLevel;

    @Override
    public String getVertexResPath() {
        return null;
    }

    @Override
    public String getFragmentResPath() {
        return null;
    }

    @Override
    public void initOtherHandle() {
        paramsLocation = GLES20.glGetUniformLocation(mProgram, "params");
        brightnessLocation = GLES20.glGetUniformLocation(mProgram, "brightness");
        singleStepOffsetLocation = GLES20.glGetUniformLocation(mProgram, "singleStepOffset");

        toneLevel = -1f;
        beautyLevel = -1f;
        brightLevel = 0.34f;

        setParams(beautyLevel, toneLevel);
        setBrightLevel(0.34f);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        setTexelSize(width, height);
    }

    @Override
    public void setOtherHandle() {
        //setParams(beautyLevel, toneLevel);
        //setBrightLevel(0.34f);
    }

    public void setBeautyLevel(float beautyLevel) {
        this.beautyLevel = beautyLevel;
        setParams(beautyLevel, toneLevel);
    }

    public void setToneLevel(float toneLevel) {
        this.toneLevel = toneLevel;
        setParams(beautyLevel, toneLevel);
    }


    public void setParams(float beauty, float tone) {
        float[] vector = new float[4];
        vector[0] = 1.0f - 0.6f * beauty;
        vector[1] = 1.0f - 0.3f * beauty;
        vector[2] = 0.1f + 0.3f * tone;
        vector[3] = 0.1f + 0.3f * tone;

        setFloatVec4(paramsLocation, vector);
    }

    public void setBrightLevel(float brightLevel) {
        this.brightLevel = brightLevel;
        setFloat(brightnessLocation, 0.6f * (-0.5f + brightLevel));
    }

    private void setTexelSize(final float w, final float h) {
        setFloatVec2(singleStepOffsetLocation, new float[] {2.0f / w, 2.0f / h});
    }


    protected void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec2(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(location, floatValue);
            }
        });
    }
}
