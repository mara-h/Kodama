package com.example.kodama.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.kodama.R;
import com.example.kodama.controllers.RecognitionController;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.net.Uri.fromFile;


public class RetakePhotoActivity extends Activity {

    private static final String IMAGE_FILE_LOCATION = "image_file_location";
    private RecognitionController recognitionController;
    protected Interpreter tflite;
    private TensorImage inputImageBuffer;
    private TensorBuffer outputProbabilityBuffer;
    private TensorProcessor probabilityProcessor;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private Bitmap bitmap;
    private List<String> labels;
    private Uri imageuri;
    private TextView classitext;
    private ImageView imageView;
    private  int imageSizeX;
    private  int imageSizeY;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retake_photo);
        ImageButton retakeButton = (ImageButton) findViewById(R.id.btn_retakepicture);
        ImageButton useButton = (ImageButton) findViewById(R.id.btn_usepicture);
        ImageButton rechooseButton = (ImageButton) findViewById(R.id.btn_rechoose);
        TextView classitext = (TextView) findViewById(R.id.classifytext) ;

        ImageView imageView = findViewById(R.id.pictureViewRetake); //!!!!!!!!!!!!!!!!!!!!!!
        File imageFile = new File(getIntent().getStringExtra(IMAGE_FILE_LOCATION));

        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetakePhotoActivity.this, CameraActivity.class));
            }
        });

        rechooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 12);
            }
        });
       // imageView.setImageURI(null);
        //Uri uri = Uri.fromFile(imageFile);

      //  imageView.setImageURI(uri);
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

       imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));


        try{
            tflite=new Interpreter(loadmodelfile(this));
        }catch (Exception e) {
            e.printStackTrace();
        }

        useButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the shape of the model
                int imageTensorIndex = 0;
                int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
                imageSizeY = imageShape[1];
                imageSizeX = imageShape[2];
                DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

                int probabilityTensorIndex = 0;
                int[] probabilityShape =
                        tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
                DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

                inputImageBuffer = new TensorImage(imageDataType);
                outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
                probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

                inputImageBuffer = loadImage(bitmap);

                tflite.run(inputImageBuffer.getBuffer(),outputProbabilityBuffer.getBuffer().rewind());
                showresult();
            }
        });



//        try {
//            tflite = new Interpreter(recognitionController.loadModelFile(this));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        useButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //get the shape of the model
//                int imageTensorIndex = 0;
//                int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
//                recognitionController.setImageSizeY(imageShape[1]);
//                recognitionController.setImageSizeX(imageShape[2]);
//                DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
//
//                int probabilityTensorIndex = 0;
//                int[] probabilityShape = tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
//                DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();
//
//                inputImageBuffer = new TensorImage(imageDataType);
//                recognitionController.setInputImageBuffer(inputImageBuffer);
//                outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
//                probabilityProcessor = new TensorProcessor.Builder().add(recognitionController.getPostprocessNormalizeOp()).build();
//                inputImageBuffer = recognitionController.loadImage(bitmap);
//                tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
//                showResult();
//            }
//        });
    }
//
//   private void showResult() {
//
//       try {
//           labels = FileUtil.loadLabels(this, "dict.txt");
//       } catch (Exception e) {
//           e.printStackTrace();
//       }
//       Map<String, Float> labeledProbability =
//               new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
//                       .getMapWithFloatValue();
//       float maxValueInMap = (Collections.max(labeledProbability.values()));
//
//       for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
//           if (entry.getValue() == maxValueInMap) {
//               classitext.setText(entry.getKey());
//           }
//       }
//       tflite.close();
//   }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
//            imageuri = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
//                imageView.setImageBitmap(bitmap); //
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        //process image and resize it with required size
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }


    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("model.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    private TensorOperator getPostprocessNormalizeOp(){
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

    private void showresult(){

        try{
            labels = FileUtil.loadLabels(this,"dict.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        float maxValueInMap =(Collections.max(labeledProbability.values()));

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                classitext.setText(entry.getKey());
            }
        }
        Toast.makeText(getApplicationContext(),"cacat pe bat", Toast.LENGTH_LONG).show();

        if(classitext == null) {
            Toast.makeText(getApplicationContext(),"app won't run with null classitext", Toast.LENGTH_LONG).show();
        }
        tflite.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}