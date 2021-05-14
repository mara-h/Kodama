//package com.example.kodama;
//
//import android.graphics.SurfaceTexture;
//import android.hardware.camera2.CameraAccessException;
//import android.hardware.camera2.CameraCaptureSession;
//import android.hardware.camera2.CameraCharacteristics;
//import android.hardware.camera2.CameraDevice;
//import android.hardware.camera2.CameraManager;
//import android.hardware.camera2.CaptureRequest;
//import android.os.HandlerThread;
//import android.util.Log;
//import android.view.TextureView;
//
//import androidx.annotation.Size;
//
//import com.example.kodama.view.CameraActivity;
//import com.google.android.gms.common.util.concurrent.HandlerExecutor;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.InOrder;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Collections;
//import java.util.concurrent.Executor;
//import java.util.logging.Handler;
//import static android.content.ContentValues.TAG;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.fail;
//import static org.mockito.Mockito.atLeastOnce;
//import static org.mockito.Mockito.inOrder;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoMoreInteractions;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CameraTest {
//    private static HandlerThread callbackThread = new HandlerThread("Callback thread");
//
//    private CameraCaptureSession mPreviewCaptureSession;
//    private CameraDevice mCamera;
//    private TextureView mTextureView;
//    private CaptureRequest previewRequest;
//    private CameraManager mCameraManager;
//    private HandlerThread mHandlerThread;
//    private Handler mHandler;
//
//
//    @Mock
//    CameraActivity _camera;
//    @Mock
//    CameraDevice.StateCallback mockListener = Mockito.mock(CameraDevice.StateCallback.class);
//
//    @Before
//    public void setUp() {
//        callbackThread.start();
//        mHandlerThread = new HandlerThread(TAG);
//        mHandlerThread.start();
//       // mHandler = new Handler(mHandlerThread.getLooper());
//
//    }
//
//    @After
//    public void tearDown() {
//        callbackThread.quit();
//        callbackThread = null;
//
//        mHandlerThread.quitSafely();
//        mHandler = null;
//    }
//
//    // Test: that properties can be queried from each device, without exceptions.
//    @Test
//    public void testCameraManagerGetCameraCharacteristics() throws Exception {
//        String[] ids = new String[10];
//        for (int i = 0; i < ids.length; i++) {
//            CameraCharacteristics props = mCameraManager.getCameraCharacteristics(ids[i]);
//            assertNotNull(
//                    String.format("Can't get camera characteristics from: ID %s", ids[i]), props);
//        }
//    }
//
//    // Test: that an exception is thrown if an invalid device id is passed down.
//    @Test
//    public void testCameraManagerInvalidDevice() throws Exception {
//        String[] ids = new String[10];
//        // Create an invalid id by concatenating all the valid ids together.
//        StringBuilder invalidId = new StringBuilder();
//        invalidId.append("INVALID");
//        for (int i = 0; i < ids.length; i++) {
//            invalidId.append(ids[i]);
//        }
//        try {
//            mCameraManager.getCameraCharacteristics(
//                    invalidId.toString());
//            fail(String.format("Accepted invalid camera ID: %s", invalidId.toString()));
//        } catch (IllegalArgumentException e) {
//            // This is the exception that should be thrown in this case.
//        }
//    }
//
//
//    /**
//     * Test: that opening the same device multiple times and make sure the right
//     * error state is set.
//     */
//    @Test
//    public void testCameraManagerOpenCameraTwice() throws Exception {
//        testCameraManagerOpenCameraTwice(/*useExecutor*/ false);
//        testCameraManagerOpenCameraTwice(/*useExecutor*/ true);
//    }
//
//    private void testCameraManagerOpenCameraTwice(boolean useExecutor) throws Exception {
//        String[] ids = new String[10];
//        final Executor executor = useExecutor ? new HandlerExecutor(mHandler) : null;
//        // Test across every camera device.
//        for (int i = 0; i < ids.length; ++i) {
//            CameraDevice successCamera = null;
//            try {
//                CameraDevice.StateCallback mockSuccessListener = Mockito.mock(CameraDevice.StateCallback.class);
//                CameraDevice.StateCallback mockFailListener = Mockito.mock(CameraDevice.StateCallback.class);
//
//                if (useExecutor) {
//                    mCameraManager.openCamera(ids[i], executor, mockSuccessListener);
//                    mCameraManager.openCamera(ids[i], executor, mockFailListener);
//                }
//
//                ArgumentCaptor<CameraDevice> argument =
//                        ArgumentCaptor.forClass(CameraDevice.class);
//                verify(mockSuccessListener, atLeastOnce()).onOpened(argument.capture());
//                verify(mockSuccessListener, atLeastOnce()).onDisconnected(argument.capture());
//
//                verify(mockFailListener, atLeastOnce()).onOpened(argument.capture());
//                successCamera = verifyCameraStateOpened(
//                        ids[i], mockFailListener);
//                verifyNoMoreInteractions(mockFailListener);
//            } finally {
//                if (successCamera != null) {
//                    successCamera.close();
//                }
//            }
//        }
//    }
//
//
//    final CameraDevice.StateCallback mCameraDeviceStateCallback = Mockito.mock(CameraDevice.StateCallback.class);
//
//    private static CameraDevice verifyCameraStateOpened(String cameraId, CameraDevice.StateCallback listener) {
//        ArgumentCaptor<CameraDevice> argument =
//                ArgumentCaptor.forClass(CameraDevice.class);
//        InOrder inOrder = inOrder(listener);
//        inOrder.verify(listener)
//                .onOpened(argument.capture());
//        CameraDevice camera = argument.getValue();
//        assertNotNull(
//                String.format("Failed to open camera device ID: %s", cameraId),
//                camera);
//        return camera;
//    }
//
//
//    //
//    @Test
//    public void connectCameraTest(CameraManager cameraManager, String mCameraID, Handler handler, boolean hasAccess) {
//        try {
//            cameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, handler);
//        } catch (CameraAccessException e) {
//            if (hasAccess) {
//                fail("Unexpected exception" + e);
//            } else {
//                assertEquals(e.getReason(), (CameraAccessException.CAMERA_DISABLED));
//            }
//
//        }
//
//    }
//
//    SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
//    final CameraCaptureSession.StateCallback mCaptureSession =
//            new CameraCaptureSession.StateCallback() {
//                @Override
//                public void onConfigured(CameraCaptureSession session) {
//                    Log.i(TAG, "Finished configuring camera outputs");
//                    mPreviewCaptureSession = session;
//                    try {
//                        session.setRepeatingRequest(previewRequest, null, null);
//                    } catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
////chestiiiiiiiiiiii
//
//                }
//
//                @Override
//                public void onConfigureFailed(CameraCaptureSession session) {
//                    Log.e(TAG, "Configuration error on device '" + mCamera.getId());
//                }
//            };
//
////    private static Size chooseOptimalSizeTest(Size[] choices, int width, int height) {
////        List<Size> bigEnough = new ArrayList<Size>();
////        for (Size option : choices) {
////            if (option.getWidth() >= width && option.getHeight() >= height) {
////                bigEnough.add(option);
////            }
////        }
////        if (bigEnough.size() > 0) {
////            return Collections.min(bigEnough, new CompareSizesByArea());
////        } else {
////            Log.e(TAG, "Couldn't find any suitable preview size");
////            return choices[0];
////        }
////    }
//
//}
//
//
//
//
