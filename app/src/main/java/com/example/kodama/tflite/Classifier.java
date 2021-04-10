package com.example.kodama.tflite;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.SystemClock;
import android.os.Trace;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import com.example.kodama.tflite.Classifier.Device;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class Classifier {
   // private static final Logger LOGGER = new Logger();

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
    private final int imageSizeY;

    private GpuDelegate gpuDelegate = null;

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

    public static ClassifierFloatMobileNet create (Activity activity, Device device, int numThreads) throws IOException {
        return new ClassifierFloatMobileNet(activity, device, numThreads);
    }

    public static class Recognition {
        //a unique identifier for what has been recognized

        private final String id;
        private final String title;
        private final Float confidence;

        private RectF location;

        public Recognition(final String id, final String title, final Float confidence, final RectF location) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        public RectF getLocation() {
            return new RectF(location);
        }

        public void setLocation(RectF location) {
            this.location = location;
        }

        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "]";
            }
            if (title != null) {
                resultString += title + " ";
            }
            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }
            if (location != null) {
                resultString += location + " ";
            }
            return resultString.trim();
        }
    }

    protected Classifier(Activity activity, Device device, int numThreads) throws IOException {
        tfliteModel = FileUtil.loadMappedFile(activity, getModelPath());
        switch (device) {
            case GPU:
                //create a GPU delegate instance and add it to the interpreter op..
                GpuDelegate gpuDelegate = new GpuDelegate(); //aici am facut eu local variable
                tfliteOptions.addDelegate(gpuDelegate);
                break;
            case CPU:
                break;
        }
        tfliteOptions.setNumThreads(numThreads);

        tflite = new Interpreter(tfliteModel, tfliteOptions);

        labels = FileUtil.loadLabels(activity, getLabelPath());

        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape();
        imageSizeX = imageShape[1];
        imageSizeY = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
        int probabilityTensorIndex = 0;
        int[] probabilityShape =
                tflite.getOutputTensor(probabilityTensorIndex).shape();
        DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType(); //am pus eu getType ca aia credeam ca e

        inputImageBuffer = new TensorImage(imageDataType);
        outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
        probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();
        //LOGGER.d("Created a Tensorflow Lite Image Classifier");
    }

    public List<Recognition> recognizeImage(final Bitmap bitmap, int sensorOrientation) {
        Trace.beginSection("recognizeImage");
        Trace.beginSection("loadImage");
        long startTimeForLoadImage = SystemClock.uptimeMillis();
        inputImageBuffer = loadImage(bitmap, sensorOrientation);
        long endTimeForLoadImage = SystemClock.uptimeMillis();
        Trace.endSection();

        //LOGGER.v("Timeout to load the image:" + (endTimeForLoadImage - startTimeForLoadImage)) //poate mai ceva

        Trace.beginSection("runInterference");
        long startTimeForReference = SystemClock.uptimeMillis();

        //TODO: run tflite interference
        tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
        long endTimeForReference = SystemClock.uptimeMillis();
        Trace.endSection();
        //LOGGER.v("Timecost to run model interference: " + (endTimeForReference - startTimeForReference));

        //TODO: Use tensorLabel from TFLite Library to associate the ...with category labels
        Map<String, Float> labeledProbability = new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                .getMapWithFloatValue();
        Trace.endSection();

        return getTopKProbability(labeledProbability);
    }

    public void close() {
        if(tflite != null) {
            tflite.close();
            tflite = null;
        }
        //TODO: close the GPU delegate
        if (gpuDelegate != null) {
            gpuDelegate.close();
            gpuDelegate = null;
        }
        tfliteModel = null;
    }

    /** Get the image size along the x axis. */
    public int getImageSizeX() {
        return imageSizeX;
    }

    /** Get the image size along the y axis. */
    public int getImageSizeY() {
        return imageSizeY;
    }

    private TensorImage loadImage(final Bitmap bitmap, int sensorOrientation) {
        inputImageBuffer.load(bitmap);

        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int numRotation = sensorOrientation / 90;
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(new Rot90Op(numRotation))
                .add(getPreprocessNormalizeOp())
                .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private static List<Recognition> getTopKProbability(Map<String, Float> labelProb) {
        PriorityQueue<Recognition> pq =
                new PriorityQueue<>(
                        MAX_RESULTS,
                        new Comparator<Recognition>() {
                            @Override
                            public int compare(Recognition lhs, Recognition rhs) {
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                            }
                        });
        for(Map.Entry<String, Float> entry : labelProb.entrySet()) {
           pq.add(new Recognition("" + entry.getKey(), entry.getKey(), entry.getValue(), null));
        }

        final ArrayList<Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for(int i=0; i<recognitionsSize;++i) {
            recognitions.add(pq.poll());
        }
        return  recognitions;
    }
    /** Gets the name of the model file stored in Assets. */
    protected abstract String getModelPath();

    /** Gets the name of the label file stored in Assets. */
    protected abstract String getLabelPath();

    /** Gets the TensorOperator to nomalize the input image in preprocessing. */
    protected abstract TensorOperator getPreprocessNormalizeOp();

    protected abstract TensorOperator getPostprocessNormalizeOp();

}
