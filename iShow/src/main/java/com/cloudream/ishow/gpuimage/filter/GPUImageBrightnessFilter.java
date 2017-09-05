package com.cloudream.ishow.gpuimage.filter;

import android.opengl.GLES20;

import com.cloudream.ishow.gpuimage.GPUImageFilter;
import com.cloudream.ishow.gpuimage.GPUImageFilterType;

/**
 * brightness value ranges from -0.5 to 0.5, with 0.0 as the normal level.
 */
public class GPUImageBrightnessFilter extends GPUImageFilter {
    private static final String FRAGMENT_SHADER =
            "varying highp vec2 textureCoordinate;\n" +
                    "\n" +
                    "uniform sampler2D inputImageTexture;\n" +
                    "uniform lowp float brightness;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "	lowp vec4 color = texture2D(inputImageTexture, textureCoordinate);\n" +
                    "	\n" +
                    "	lowp vec3 value;\n" +
                    "	if(brightness < 0.0)\n" +
                    "		value = color.rgb * (1.0 + brightness);\n" +
                    "	else\n" +
                    "		value = color.rgb + (vec3(1.0) - color.rgb) * brightness;\n" +
                    "	\n" +
                    "	gl_FragColor = vec4(value, color.w);\n" +
                    "}";

    private int mBrightnessLocation;
    private float mBrightness;

    public GPUImageBrightnessFilter() {
        this(0.0F);
    }

    public GPUImageBrightnessFilter(final float brightness) {
        super(GPUImageFilterType.BRIGHTNESS, NO_FILTER_VERTEX_SHADER, FRAGMENT_SHADER);
        mBrightness = brightness;
    }

    @Override
    public void onInit() {
        super.onInit();
        mBrightnessLocation = GLES20.glGetUniformLocation(getProgram(), "brightness");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setBrightness(mBrightness);
    }

    public void setBrightness(final float brightness) {
        mBrightness = brightness;
        setFloat(mBrightnessLocation, mBrightness);
    }
}
