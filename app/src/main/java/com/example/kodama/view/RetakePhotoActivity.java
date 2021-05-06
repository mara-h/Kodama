package com.example.kodama.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kodama.R;

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
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class RetakePhotoActivity extends Activity {

    private static int RESULT_LOAD_IMAGE = 1;
    private String picturePath;
    private static final String IMAGE_FILE_LOCATION = "image_file_location";
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
    private ImageView imageView;
    private  int imageSizeX;
    private  int imageSizeY;

    private AnimatedVectorDrawable animation;
    private TextView classitext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retake_photo);
        ImageButton retakeButton = (ImageButton) findViewById(R.id.btn_retakepicture);
        ImageButton useButton = (ImageButton) findViewById(R.id.btn_usepicture);
        ImageButton rechooseButton = (ImageButton) findViewById(R.id.btn_rechoose);
        ImageButton cancelButton = (ImageButton) findViewById(R.id.btn_x);
        ImageButton savePhoto = (ImageButton) findViewById(R.id.download_photo_button);
        ImageButton gotoButton = (ImageButton) findViewById(R.id.goto_button);
        classitext=(TextView)findViewById(R.id.classifytext);
       // ImageButton useButton = (ImageButton) findViewById(R.id.useAnimated);
     //   ImageView checkAnimation = (ImageView) findViewById(R.id.useAnimated) ;
     //   Animatable animatable = (Animatable) checkAnimation.getDrawable();
       // animatable.start();

        imageView = findViewById(R.id.pictureViewRetake);

        cancelButton.setVisibility(View.GONE);
        classitext.setVisibility(View.GONE);


        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetakePhotoActivity.this, CameraActivity.class));
            }
        });

        File imageFile = new File(getIntent().getStringExtra(IMAGE_FILE_LOCATION));

        imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        rechooseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

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
                //Drawable d = useButton.getDrawable();
                //if (d instanceof AnimatedVectorDrawable) {
                  //  animation = (AnimatedVectorDrawable) d;
                //    animation.start();
               // }
                useButton.setVisibility(View.GONE);
                cancelButton.setVisibility((View.VISIBLE));
                classitext.setVisibility(View.VISIBLE);
            }
        });






        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetakePhotoActivity.this, HomeActivity.class));
            }
        });

        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent mediaStoreUpdateIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                  mediaStoreUpdateIntent.setData(Uri.fromFile(imageFile));
                  sendBroadcast(mediaStoreUpdateIntent);

                Toast.makeText(getApplicationContext(),"Imagine salvata. trebuie schimbat sa arate altfel+sa se salveze o data", Toast.LENGTH_SHORT).show();
            }
        });


        gotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"nu stiu daca in history sau mai bine catre un google. sau transformam history in baza de date", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RetakePhotoActivity.this, HistoryActivity.class));

            }
        });

    }


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

        tflite.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent(RetakePhotoActivity.this, RetakePhotoActivity.class);
            intent.putExtra(IMAGE_FILE_LOCATION, picturePath);
            startActivity(intent);

            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath)); asa crapa
           // bitmap = BitmapFactory.decodeFile(picturePath);
        }
    }


}