package com.example.kodama.tflite;

import android.app.Activity;

import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.model.Model;

import java.io.IOException;

public class ClassifierFloatMobileNet  extends Classifier{
    private static final float IMAGE_MEAN = 127.5f;

    private static final float IMAGE_STD = 127.5f;

    private static final float PROBABILITY_MEAN = 0.0f;

    private static final float PROBABILITY_STD = 1.0f;

    public ClassifierFloatMobileNet (Activity activity, Model.Device device, int numeThreads) throws IOException {
        super(activity, device, numeThreads);
    }

    protected String getModelPath() {
        return "model.tflite";
    }

    protected String getLablePath() {
        return "labels.txt";
    }

    protected TensorOperator getPreprocessNormalizedOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
}
