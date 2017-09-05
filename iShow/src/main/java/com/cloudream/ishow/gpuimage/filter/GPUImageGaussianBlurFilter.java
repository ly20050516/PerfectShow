package com.cloudream.ishow.gpuimage.filter;

import com.cloudream.ishow.gpuimage.GPUImageFilterType;
import com.cloudream.ishow.gpuimage.GPUImageTwoPassTextureSamplingFilter;

/**
 * A more generalized 9x9 Gaussian blur filter
 * blurSize value ranging from 0.0 on up, with a default of 1.0
 */
public class GPUImageGaussianBlurFilter extends GPUImageTwoPassTextureSamplingFilter {
    private static final String VERTEX_SHADER =
            "attribute vec4 position;\n" +
                    "attribute vec4 inputTextureCoordinate;\n" +
                    "\n" +
                    "const int GAUSSIAN_SAMPLES = 9;\n" +
                    "\n" +
                    "uniform float texelWidthOffset;\n" +
                    "uniform float texelHeightOffset;\n" +
                    "\n" +
                    "varying vec2 textureCoordinate;\n" +
                    "varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "	gl_Position = position;\n" +
                    "	textureCoordinate = inputTextureCoordinate.xy;\n" +
                    "	\n" +
                    "	// Calculate the positions for the blur\n" +
                    "	int multiplier = 0;\n" +
                    "	vec2 blurStep;\n" +
                    "	vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset);\n" +
                    "	\n" +
                    "	for (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n" +
                    "	{\n" +
                    "		multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n" +
                    "		// Blur in x (horizontal)\n" +
                    "		blurStep = float(multiplier) * singleStepOffset;\n" +
                    "		blurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n" +
                    "	}\n" +
                    "}\n";

    private static final String FRAGMENT_SHADER =
            "uniform sampler2D inputImageTexture;\n" +
                    "\n" +
                    "const lowp int GAUSSIAN_SAMPLES = 9;\n" +
                    "\n" +
                    "varying highp vec2 textureCoordinate;\n" +
                    "varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "	lowp vec3 sum = vec3(0.0);\n" +
                    "	lowp vec4 fragColor=texture2D(inputImageTexture,textureCoordinate);\n" +
                    "	\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[0]).rgb * 0.05;\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[1]).rgb * 0.09;\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[2]).rgb * 0.12;\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[3]).rgb * 0.15;\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[4]).rgb * 0.18;\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[5]).rgb * 0.15;\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[6]).rgb * 0.12;\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[7]).rgb * 0.09;\n" +
                    "	sum += texture2D(inputImageTexture, blurCoordinates[8]).rgb * 0.05;\n" +
                    "\n" +
                    "	gl_FragColor = vec4(sum,fragColor.a);\n" +
                    "}";

    protected float mBlurSize = 1.0F;

    public GPUImageGaussianBlurFilter() {
        this(1.0F);
    }

    public GPUImageGaussianBlurFilter(float blurSize) {
        super(VERTEX_SHADER, FRAGMENT_SHADER, VERTEX_SHADER, FRAGMENT_SHADER);
        setFilterType(GPUImageFilterType.GAUSSIAN_BLUR);
        mBlurSize = blurSize;
    }

    @Override
    public float getVerticalTexelOffsetRatio() {
        return mBlurSize;
    }

    @Override
    public float getHorizontalTexelOffsetRatio() {
        return mBlurSize;
    }

    /**
     * A multiplier for the blur size, ranging from 0.0 on up, with a default of 1.0
     *
     * @param blurSize from 0.0 on up, default 1.0
     */
    public void setBlurSize(float blurSize) {
        mBlurSize = blurSize;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                initTexelOffsets();
            }
        });
    }
}
