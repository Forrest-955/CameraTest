package com.itep.mt.qrtest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lgh.uvccamera.UVCCameraProxy;
import com.lgh.uvccamera.bean.PicturePath;
import com.lgh.uvccamera.callback.ConnectCallback;
import com.lgh.uvccamera.callback.PhotographCallback;
import com.lgh.uvccamera.callback.PictureCallback;
import com.lgh.uvccamera.callback.PreviewCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import cn.bertsir.zbar.BuildConfig;
import cn.bertsir.zbar.Qr.ScanResult;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;
import cn.bertsir.zbar.utils.QRUtils;
import cn.bertsir.zbar.view.ScanView;

public class CameraActivity extends Activity implements View.OnClickListener {
    private static final String         TAG             = "UVCCameraActivity";
    private Context context = this;
    private UVCCameraProxy mUVCCamera;
    private TextureView mTextureView;
    private String temPath;
    private              ScanView       sv;
    private              ImageView      mo_scanner_back;
    private              ImageView      iv_flash;
    private              ImageView      iv_album;
    private TextView tv_title;
    private FrameLayout fl_title;
    private              TextView       tv_des;
    private              QrConfig       options;
    public static final  int            RESULT_CANCELED = 401;
    private              int            time_out_time;
    private CountDownTimer timer;
    long createTime = 0;
    private boolean isRequest;
    private boolean isPreview;

    private boolean isFlagQrCode = true;
    private boolean stopCounDown = false;
    private boolean isOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvc_camera);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Log.i("zBarLibary", "version: " + BuildConfig.VERSION_NAME);
        options = (QrConfig) getIntent().getExtras().get(QrConfig.EXTRA_THIS_CONFIG);
        initView();
        initUVCCamera();
        initParam();
        mUVCCamera.setPreviewSize(640, 480);
        mUVCCamera.startPreview();
    }

    private void initView() {
        mTextureView = findViewById(R.id.textureView);
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
    /**
     * 初始化参数
     */
    private void initParam (){
        createTime = System.currentTimeMillis();

        Symbol.scanType = QrConfig.TYPE_QRCODE;
        Symbol.scanFormat = -1;
        Symbol.is_only_scan_center = false;
        Symbol.is_auto_zoom = false;
        Symbol.doubleEngine = false;
        Symbol.looperScan = false;
        Symbol.looperWaitTime = 0;
        //try{
        //    startCountDown(10);
        //}catch (Throwable throwable){
        //    ScanResult scanResult = new ScanResult();
        //    scanResult.setContent("press cancel");
        //    scanResult.setType(ScanResult.CODE_QR);
        //    QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
        //    stopCountDown();
        //    Log.e(TAG,"timer 挂了");
        //    Log.e(TAG, "press cancel");
        //    throwable.printStackTrace();
        //    finish();
        //}
        //无效
//        handler.sendEmptyMessage(0);
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
                case 1:
                    ScanResult scanResult = new ScanResult();
                    if (!isOpen){
                        scanResult.setContent("camera open failed");
                    } else if (hasPreview) {
                        scanResult.setContent("get preview failed");
                    }
                    scanResult.setType(ScanResult.CODE_QR);
                    QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
                    finish();
                    handler.sendEmptyMessageDelayed(2, 1000);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化摄像头
     */
    private void initUVCCamera() {
        mUVCCamera = new UVCCameraProxy(context);
        mUVCCamera.getConfig()
                .isDebug(true)
                .setPicturePath(PicturePath.APPCACHE)
                .setDirName("uvccamera")
                .setProductId(0)
                .setVendorId(0);
        mUVCCamera.setPreviewTexture(mTextureView);
        mUVCCamera.setConnectCallback(new ConnectCallback() {
            @Override
            public void onAttached(UsbDevice usbDevice) {
                mUVCCamera.requestPermission(usbDevice);
            }

            @Override
            public void onGranted(UsbDevice usbDevice, boolean granted) {
                if (granted) {
                    mUVCCamera.connectDevice(usbDevice);
                }
            }

            @Override
            public void onConnected(UsbDevice usbDevice) {
                mUVCCamera.openCamera();
            }

            @Override
            public void onCameraOpened() {
                isOpen = true;
                Log.e(TAG, "摄像头打开成功");
                mUVCCamera.setPreviewSize(640, 480);
                mUVCCamera.startPreview();
            }

            @Override
            public void onDetached(UsbDevice usbDevice) {
                mUVCCamera.closeCamera();
            }
        });

        mUVCCamera.setPhotographCallback(new PhotographCallback() {
            @Override
            public void onPhotographClick() {
                mUVCCamera.takePicture();
//                mUVCCamera.takePicture("test.jpg");
            }
        });

        mUVCCamera.setPreviewCallback(new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] nv21Yuv) {
                if (isFlagQrCode){
                    isFlagQrCode = false;
                    new Thread(new Runnable(){
                        @Override
                        public void run (){
                            YuvImage yuvImage = new YuvImage(nv21Yuv, ImageFormat.NV21, 640, 480, null);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream(nv21Yuv.length);
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
                                //todo 完成
                                Log.e(TAG, "qrResult:" + qrcontent);
                                isFlagQrCode = false;
                                ScanResult scanResult = new ScanResult();
                                scanResult.setContent(qrcontent);
                                scanResult.setType(ScanResult.CODE_QR);
                                QrManager.getInstance()
                                        .getResultCallback()
                                        .onScanSuccess(scanResult);
                                finish();
                                return;
                            }else{
                                bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                                qrcontent = QRUtils.getInstance().decodeQRcodeByZxing(bmp);
                                if (!TextUtils.isEmpty(qrcontent)){
                                    //todo 完成
                                    Log.e(TAG, "qrResult:" + qrcontent);
                                    isFlagQrCode = false;
                                    ScanResult scanResult = new ScanResult();
                                    scanResult.setContent(qrcontent);
                                    scanResult.setType(ScanResult.CODE_QR);
                                    QrManager.getInstance()
                                            .getResultCallback()
                                            .onScanSuccess(scanResult);
                                    finish();
                                    return;
                                }
                            }
                            isFlagQrCode = true;
                        }
                    }).start();
                }
            }
        });

        mUVCCamera.setPictureTakenCallback(new PictureCallback() {
            @Override
            public void onPictureTaken(String path) throws FileNotFoundException {

            }
        });
    }

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
                finish();
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

    private boolean hasPreview = false;

    private void checkPreviewTimeOut (){
        hasPreview = true;
        if (stopCounDown){
            return;
        }
        long passTime = System.currentTimeMillis() - createTime;
        Log.d(TAG, "passTime:" + passTime);
        if (passTime > (15 * 1000)){
            stopCountDown();
            ScanResult scanResult = new ScanResult();
            scanResult.setContent("preview time out");
            scanResult.setType(ScanResult.CODE_QR);
            Log.e(TAG, "preview time out cancel");
            QrManager.getInstance().getResultCallback().onScanSuccess(scanResult);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
