package com.cloudream.ishow.gpuimage.filter;

import com.cloudream.ishow.gpuimage.GPUImageFilterType;
import com.cloudream.ishow.gpuimage.GPUImageTwoInputFilter;

public class GPUImageColorBurnBlendFilter extends GPUImageTwoInputFilter {
    private static final String COLOR_BURN_BLEND_FRAGMENT_SHADER =
            "varying highp vec2 textureCoordinate;\n" +
                    "varying highp vec2 textureCoordinate2;\n" +
                    "\n" +
                    "uniform sampler2D inputImageTexture;\n" +
                    "uniform sampler2D inputImageTexture2;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "	mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                    "	mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" +
                    "	mediump vec4 whiteColor = vec4(1.0);\n" +
                    "	gl_FragColor = whiteColor - (whiteColor - textureColor) / textureColor2;\n" +
                    "}";

    public GPUImageColorBurnBlendFilter() {
        super(GPUImageFilterType.BLEND_COLOR_BURN, COLOR_BURN_BLEND_FRAGMENT_SHADER);
    }
}
