package com.example.kodama.tflite;

import android.app.Activity;
import android.widget.Switch;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.logging.Logger;

public abstract class Classifier {
    private static final Logger LOGGER = new Logger();

    public enum Device {
        CPU,
        GPU
    }

    //number of the results to show in the UI
    private static final int MAX_RESULTS = 3; //as zice sa punem 1

    //the loaded TensorFlow Lite model
    private MappedByteBuffer tfliteModel;

    // Image size along the x axis
    private final int imageSizeX;

    //Image size along the y axis
    private final int getImageSizeY;

    protected Interpreter tflite;



    //options for configuring the Interpreter
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    //labels corresponding to the output of the vision model
    private List<String> labels;
    //input image TensorBuffer
    private TensorImage inputImageBuffer;
    //output probability TensorBuffer
    private final TensorBuffer outputProbabilityBuffer;
    //processer to apply post processing of the output probability
    private final TensorProcessor probabilityProcessor;

    public static Classifier create (Activity activity, Device device, int numThreads) throws IOException {
        return new ClassifierFloatMobileNet(activity, device, numThreads);
    }


    //chestii

    protected Classifier(Activity activity, Device device, int numThreads) throws IOException {
        switch (device) {
            case GPU:
                //create a GPU delegate instance and add it to the interpreter op..
                break;
            case CPU:
                break;
        }
        tfliteOptions.setNumThreads(numThreads);
    }

    tflite = new Interpreter(tfliteModel, tfliteOptions);

    labels = FileUtil.loadLabels(activity, getLabelPath());


}
