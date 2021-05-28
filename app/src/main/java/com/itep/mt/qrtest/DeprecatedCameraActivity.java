package com.itep.mt.qrtest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import cn.bertsir.zbar.BuildConfig;
import cn.bertsir.zbar.Qr.ScanResult;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;
import cn.bertsir.zbar.utils.QRUtils;
import cn.bertsir.zbar.view.ScanView;

/**
 * Created by cy on 2020/06/18.
 */
public class DeprecatedCameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener{
    private static final String         TAG             = "CameraV1Activity";
    private              ScanView       sv;
    private              ImageView      mo_scanner_back;
    private              ImageView      iv_flash;
    private              ImageView      iv_album;
    private              TextView       tv_title;
    private              FrameLayout    fl_title;
    private              TextView       tv_des;
    private              QrConfig       options;
    public static final  int            RESULT_CANCELED = 401;
    private              int            time_out_time;
    private              CountDownTimer timer;

    private AlertDialog mDialog;

    private boolean isRequest;
    private boolean isPreview;

    private boolean isFlagQrCode = true;
    private boolean stopCounDown = false;
    private boolean hasPreview   = false;

    long createTime = 0;

    private        Camera        mCamera  = null;
    private        SurfaceView   mSurfaceView;
    private        SurfaceHolder mSurfaceHolder;
    private static Context       mContext = null;
    private        boolean       isPass   = false;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i("zBarLibary", "version: " + BuildConfig.VERSION_NAME);
        options = (QrConfig) getIntent().getExtras().get(QrConfig.EXTRA_THIS_CONFIG);
        initParam();
        setContentView(R.layout.activity_deprecated_camera);
        initView();


        mContext = this;

    }

    private void initView (){
        /* SurfaceHolder set */
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(DeprecatedCameraActivity.this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        sv = (ScanView) findViewById(R.id.sv);
        sv.setType(options.getScan_view_type());

        mo_scanner_back = (ImageView) findViewById(R.id.scanner_back);
        mo_scanner_back.setOnClickListener(this);
        mo_scanner_back.setImageResource(options.getBackImgRes());

        sv.setCornerColor(options.getCORNER_COLOR());
        sv.setLineSpeed(options.getLine_speed());
        sv.setLineColor(options.getLINE_COLOR());
        sv.setScanLineStyle(options.getLine_style());

    }

    private void initParam (){
        createTime = System.currentTimeMillis();

        Symbol.scanType = QrConfig.TYPE_QRCODE;
        Symbol.scanFormat = -1;
        Symbol.is_only_scan_center = false;
        Symbol.is_auto_zoom = false;
        Symbol.doubleEngine = false;
        Symbol.looperScan = false;
        Symbol.looperWaitTime = 0;

        handler.sendEmptyMessage(0);
        Symbol.screenWidth = QRUtils.getInstance().getScreenWidth(this);
        Symbol.screenHeight = QRUtils.getInstance().getScreenHeight(this);
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage (@NonNull Message msg){
            Log.e(TAG, "handleMessage:" + msg.what + stopCounDown);
            if (stopCounDown){
                return;
            }
            switch (msg.what){
                case 0:
                    handler.sendEmptyMessageDelayed(1, 15 * 1000);
                    break;
                case 1:{
                    ScanResult scanResult = new ScanResult();
                    if (!hasPreview){
                        scanResult.setContent("get preview failed");
                    }else{
                        scanResult.setContent("time out cancel");
                    }
                    scanResult.setType(ScanResult.CODE_QR);
                    Log.e(TAG, "time out cancel");
                    try{
                        QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
                    }catch (Exception e){
                    }
                    DeprecatedCameraActivity.this.finish();
                    //handler.sendEmptyMessageDelayed(2, 1000);
                }
                break;
                //do QR scan
                case 2:

                    break;
                default:
                    break;
            }
        }
    };

    private void startCountDown (int time) throws Throwable{
        timer = new CountDownTimer(time * 1000, 1000){
            @Override
            public void onTick (long millisUntilFinished){
                Log.e(TAG, "count down" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish (){
                //todo 失败
                ScanResult scanResult = new ScanResult();
                scanResult.setContent("time out cancel");
                scanResult.setType(ScanResult.CODE_QR);
                Log.e(TAG, "time out cancel");
                QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
                DeprecatedCameraActivity.this.finish();
            }
        };
        timer.start();
    }

    private void stopCountDown (){
        stopCounDown = true;
        if (timer != null){
            timer.cancel();
        }
    }

    private void checkPreviewTimeOut (){
        hasPreview = true;
        if (stopCounDown){
            return;
        }
        long passTime = System.currentTimeMillis() - createTime;
        Log.d(TAG, "passTime:" + passTime);
        if (passTime > (15 * 1000)){
            stopCountDown();
            stopCamera();
            ScanResult scanResult = new ScanResult();
            scanResult.setContent("preview time out");
            scanResult.setType(ScanResult.CODE_QR);
            Log.e(TAG, "preview time out cancel");
            QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
            DeprecatedCameraActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy (){
        super.onDestroy();
        stopCamera();
        stopCountDown();
    }


    @Override
    public void surfaceCreated (SurfaceHolder surfaceholder){
        try{
            mCamera = Camera.open(findFrontFacingCamera());
        }catch (Exception exception){
            Log.e(TAG,"android.hardware.camera打开失败");
            exception.printStackTrace();
            mCamera = null;
        }

        if (mCamera == null){
            stopCountDown();
            stopCamera();
            ScanResult scanResult = new ScanResult();
            scanResult.setContent("camera open failed");
            scanResult.setType(ScanResult.CODE_QR);
            Log.e(TAG, "camera open failed");
            QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
            DeprecatedCameraActivity.this.finish();

        }else{
            try{
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.setPreviewCallback(new Camera.PreviewCallback(){
                    @Override
                    public void onPreviewFrame (byte[] data, Camera camera){
                        Log.d(TAG, "onPreviewFrame: " + data.length);
                        checkPreviewTimeOut();
                        new Thread(new Runnable(){
                            @Override
                            public void run (){
                                if (isFlagQrCode){
                                    isFlagQrCode = false;
                                    Camera.Parameters parameters = camera.getParameters();
                                    Camera.Size size = parameters.getPreviewSize();

                                    //Image barcode = new Image(size.width, size.height, "Y800");
                                    //barcode.setData(data);

                                    YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
                                    boolean result = yuvImage.compressToJpeg(new Rect(0, 0, 640, 480), 100, bos);
                                    if (!result){
                                        return;
                                    }
                                    byte[] buffer = bos.toByteArray();
                                    Bitmap bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                                    String qrcontent = null;
                                    try{
                                        qrcontent = QRUtils.getInstance().decodeQRcode(bmp);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    if (!TextUtils.isEmpty(qrcontent)){
                                        Log.e(TAG, "qrResult:" + qrcontent);
                                        isFlagQrCode = false;
                                        ScanResult scanResult = new ScanResult();
                                        scanResult.setContent(qrcontent);
                                        scanResult.setType(ScanResult.CODE_QR);
                                        QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
                                        DeprecatedCameraActivity.this.finish();
                                        return;
                                    }else{
                                        bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                                        qrcontent = QRUtils.getInstance().decodeQRcodeByZxing(bmp);
                                        if (!TextUtils.isEmpty(qrcontent)){
                                            Log.e(TAG, "qrResult:" + qrcontent);
                                            isFlagQrCode = false;
                                            ScanResult scanResult = new ScanResult();
                                            scanResult.setContent(qrcontent);
                                            scanResult.setType(ScanResult.CODE_QR);
                                            QrManager.getInstance()
                                                     .getResultCallback()
                                                     .onScanSuccess(scanResult);
                                            DeprecatedCameraActivity.this.finish();
                                            return;
                                        }
                                    }
                                    isFlagQrCode = true;
                                }
                            }
                        }).start();


                    }
                });
                //设置方向
                mCamera.setDisplayOrientation(90);
            }catch (IOException exception){
                mCamera.release();
                mCamera = null;
                exception.printStackTrace();
                DeprecatedCameraActivity.this.finish();
            }
        }
    }

    private int findFrontFacingCamera() {
        int cameraId = 0;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
//                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    @Override
    public void surfaceChanged (SurfaceHolder surfaceholder, int format, int w, int h){
        startCamera();
    }

    @Override
    public void surfaceDestroyed (SurfaceHolder surfaceholder){
        stopCamera();
    }


    @Override
    public void onClick (View v){
        if (v.getId() == R.id.scanner_back){
            setResult(RESULT_CANCELED);//兼容混合开发
            stopCamera();
            ScanResult scanResult = new ScanResult();
            scanResult.setContent("press cancel");
            scanResult.setType(ScanResult.CODE_QR);
            QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
            stopCountDown();
            Log.e(TAG, "press cancel");
            DeprecatedCameraActivity.this.finish();
        }
    }


    private void startCamera (){
        if (mCamera != null){
            try{
                Camera.Parameters parameters = mCamera.getParameters();
                //parameters.setPreviewSize(1280, 960);          // 设置 black screen
                parameters.setPictureFormat(ImageFormat.JPEG);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                parameters.setRotation(Camera.CameraInfo.CAMERA_FACING_BACK);
                parameters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void stopCamera (){

        if (mCamera != null){
            try{
                mCamera.stopPreview();
                mCamera.release();
            }catch (Exception e){
                e.printStackTrace();
            }
            mCamera = null;
        }
    }


}
