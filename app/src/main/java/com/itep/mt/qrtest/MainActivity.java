package com.itep.mt.qrtest;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.bertsir.zbar.Qr.ScanResult;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private String QR_test = "QR_test";
    private String QR_count = "QR_count";
    private String QR_total = "QR_total";
    private String QR_pass = "QR_pass";
    private String QR_fail_scan = "QR_fail_common";
    private String QR_fail_camera = "QR_fail_camera";
    private String QR_fail_preview = "QR_fail_preview";

    private boolean testRunning = false;
    private final int START_SCAN = 0;
    private final int SCANNING = 1;
    private final int COMPLETE_SCAN = 2;
    private final int REBOOT = 3;

    private final int CAMERA_TYPE_PHONE = 1;
    private final int CAMERA_TYPE_UVC = 2;
    private final int CAMERA_TYPE_OLD = 3;
    private int cameraType = 1;

    private int testCount;
    private int testPass;
    private int testFailScan;
    private int testFailCamera;
    private int testFailPreview;
    private EditText etTime;
    private TextView tvLog;
    private Button btnTest;
    private TextView tvTime;
    private SPUtil sp;
    private QrConfig qrConfig;
    private String path = Environment.getExternalStoragePublicDirectory("Download") + "/qr.txt";
    private boolean reboot = false;
    private Button btnGetResult;
    private Button btnClearResult;
    private TextView tvResult;
    private boolean isKeep = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        testCount = 0;
        testPass = 0;
        testFailScan = 0;
        testFailCamera = 0;
        testFailPreview = 0;
        handler.sendEmptyMessage(START_SCAN);
        if (checkQRRun()) {
            etTime.setText(sp.getInt(QR_total) + "");
            testPass = sp.getInt(QR_pass);
            testFailScan = sp.getInt(QR_fail_scan);
            testFailCamera = sp.getInt(QR_fail_camera);
            testFailPreview = sp.getInt(QR_fail_preview);
            testCount = testPass + testFailScan + testFailCamera + testFailPreview;
            String log = "???????????????" + testCount + "\n??????" + testPass + "???\n??????" + testFailScan + "???\n??????????????????" + testFailCamera + "???\n??????????????????" + testFailPreview + "???";
            tvLog.setText(log);
            switch (cameraType) {
                case CAMERA_TYPE_PHONE:
                    startQRScan();
                    break;
                case CAMERA_TYPE_UVC:
                    startUSBScan();
                    break;
                case CAMERA_TYPE_OLD:
                    startOldScan();
                    break;
            }
////            startUSBScan();
        }
    }


    private void init() {
        btnGetResult = findViewById(R.id.btn_getResult);
        btnGetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                if (isKeep) {
                    try {
                        result = readTXT(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tvResult.setText(result);
                } else {
                    try {
                        int success = 0;
                        int fail = 0;
                        String s = "1";
                        result = readTXT(path);
                        char[] cResult = result.toCharArray();
                        for (int i = 0; i < cResult.length; i++) {
                            if (cResult[i] == s.toCharArray()[0]) {
                                success++;
                            } else {
                                fail++;
                            }
                        }
                        tvResult.setText("????????????: " + success + "\n????????????: " + fail + "\n????????????" + cResult.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnClearResult = findViewById(R.id.btn_clearResult);
        btnClearResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGetResult.setEnabled(false);
                writeToFile(path, "");
                tvResult.setText("????????????: 0" + "\n????????????: 0" + "\n????????????0");
            }
        });
        tvResult = findViewById(R.id.tv_result);
        etTime = findViewById(R.id.et_test_time);
        tvLog = findViewById(R.id.tv_test_log);
        btnTest = findViewById(R.id.btn_test);
        tvTime = findViewById(R.id.tv_test_time);
        sp = SPUtil.getInstance(MainActivity.this);
        testRunning = sp.getBoolean(QR_test);
        btnTest.setText(testRunning ? "??????" : "??????");
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRunning = !testRunning;
                pressBtn(testRunning);
            }
        });

        qrConfig = new QrConfig.Builder().setShowLight(false)//?????????????????????
                .setShowTitle(true)//??????Title
                .setShowAlbum(false)//???????????????????????????
                .setCornerColor(Color.WHITE)//?????????????????????
                .setLineColor(Color.WHITE)//?????????????????????
                .setLineSpeed(QrConfig.LINE_MEDIUM)//?????????????????????
                .setPlaySound(false)//?????????????????????bi~?????????
                .setTitleText("???????????????")//??????Tilte??????
                .setTitleBackgroudColor(Color.BLACK)//?????????????????????
                .setShowZoom(false)//????????????????????????
                .setAutoZoom(false)//????????????????????????
                .setFingerZoom(false)//????????????????????????
                .setScreenOrientation(QrConfig.SCREEN_LANDSCAPE)//??????????????????
                .setDoubleEngine(false)//???????????????????????????(???????????????????????????????????????????????????????????????????????????)
                .setOpenAlbumText("????????????????????????")//?????????????????????
                //.setScanLineStyle(ScanLineView.style_radar)//??????????????????
                .setTimeOutTime(10)//????????????
                .setAutoLight(false)//????????????
                .setShowVibrator(false)//??????????????????
                .create();
        initPermission();
    }


    void pressBtn(boolean testRunning) {
        sp.putBoolean(QR_test, testRunning);
        if (testRunning) {
//            writeToFile( path, "");
            //SysCommand.runCmd("echo 1 > /mnt/internal_sd/mt/system/qr.txt");
        } else {
            isKeep = true;
            btnGetResult.setEnabled(true);
            writeToFile(path, "");
            //SysCommand.runCmd("echo 0 > /mnt/internal_sd/mt/system/qr.txt");
        }

        if (testRunning) {
            reboot = false;
            btnTest.setText("??????");
            testCount = 0;
            testPass = 0;
            testFailScan = 0;
            testFailCamera = 0;
            testFailPreview = 0;
            handler.sendEmptyMessage(START_SCAN);
//            chooseRebootAlert();
        } else {
            btnTest.setText("??????");
            handler.removeMessages(START_SCAN);
        }


    }

    private String readTXT(String path) throws IOException {
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        return line;
    }

    private void addToFile(String path, String value) {
        RandomAccessFile raf = null;
        File file = new File(path);
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(value.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeToFile(String path, Object value) {
        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(value.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initPermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.VIBRATE};
        Log.e(TAG, "requestPermissions");
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Log.e(TAG, "granted");
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Log.e(TAG, "denied");
                    }
                })
                .start();
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????launcher
     *
     * @return
     */
    private boolean checkQRRun() {
        int qr = 0;
        return qr == 1;
    }

    /**
     * ????????????????????????
     */
    private void startQRScan() {
        QrManager.getInstance()
                .init(qrConfig)
                .startScan(MainActivity.this, new QrManager.OnScanResultCallback() {
                    @Override
                    public void onScanSuccess(ScanResult result) {
                        Log.e(TAG, "OnScanResultCallback:" + result.getContent());
                        Message msg = handler.obtainMessage();
                        msg.what = COMPLETE_SCAN;
                        msg.obj = result.getContent();
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * ??????uvc camera??????
     */
    private void startUSBScan() {
        QrManager.getInstance()
                .init(qrConfig)
                .startScan(MainActivity.this, CameraActivity.class, new QrManager.OnScanResultCallback() {
                    @Override
                    public void onScanSuccess(ScanResult result) {
                        Log.e(TAG, "OnScanResultCallback:" + result.getContent());
                        Message msg = handler.obtainMessage();
                        msg.what = COMPLETE_SCAN;
                        msg.obj = result.getContent();
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * ??????camera v1??????
     */
    private void startOldScan() {
        QrManager.getInstance()
                .init(qrConfig)
                .startScan(MainActivity.this, DeprecatedCameraActivity.class, new QrManager.OnScanResultCallback() {
                    @Override
                    public void onScanSuccess(ScanResult result) {
                        Log.e(TAG, "OnScanResultCallback:" + result.getContent());
                        Message msg = handler.obtainMessage();
                        msg.what = COMPLETE_SCAN;
                        msg.obj = result.getContent();
                        handler.sendMessage(msg);
                    }
                });
    }


    private void reboot() {
        //Intent intent2 = new Intent(Intent.ACTION_REBOOT);
        //intent2.putExtra("nowait", 1);
        //intent2.putExtra("interval", 1);
        //intent2.putExtra("window", 0);
        //sendBroadcast(intent2);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START_SCAN:
                    if (!testRunning) {
                        break;
                    }
                    int testTime = Integer.parseInt(etTime.getText().toString().trim());
                    sp.putInt(QR_total, testTime);
                    if (testCount < testTime) {
                        //tvTime.setText("???????????????" + testCount);
                        if (isKeep) {
                            String result = "????????????: " + testPass + "????????????: " + testCount;
                            writeToFile(path, result);
                        }
                        handler.sendEmptyMessageDelayed(SCANNING, 1500);
                    } else {
                        if (isKeep) {
                            String result = "????????????: " + testPass + "????????????: " + testCount;
                            writeToFile(path, result);
                        }
                    }
//                    handler.sendEmptyMessageDelayed(SCANNING, 1500);
                    break;
                case SCANNING:
                    switch (cameraType) {
                        case CAMERA_TYPE_PHONE:
                            startQRScan();
                            break;
                        case CAMERA_TYPE_UVC:
                            startUSBScan();
                            break;
                        case CAMERA_TYPE_OLD:
                            startOldScan();
                            break;
                    }
                    break;
                case COMPLETE_SCAN:
                    String result = (String) msg.obj;
                    if (result.equals("test")) {
                        addToFile(path, "1");
                    } else {
                        addToFile(path, "0");
                    }
//                    btnGetResult.setEnabled(true);
                    switch (result) {
                        case "test":
                            testPass++;
                            sp.putInt(QR_pass, testPass);
                            Log.e(TAG, QR_test + "pass");
                            break;
                        case "press cancel":
                            testRunning = false;
                            pressBtn(testRunning);
                            break;
                        case "time out cancel":
                            testFailScan++;
                            sp.putInt(QR_fail_scan, testFailScan);
                            Log.e(TAG, QR_test + QR_fail_scan);
                            break;
                        case "camera open failed":
                            testFailCamera++;
                            sp.putInt(QR_fail_camera, testFailCamera);
                            Log.e(TAG, QR_test + QR_fail_camera);
                            break;
                        case "get preview failed":
                            testFailPreview++;
                            sp.putInt(QR_fail_preview, testFailPreview);
                            Log.e(TAG, QR_test + QR_fail_preview);
                            break;
                        default:
                            testFailCamera++;
                            sp.putInt(QR_fail_camera, testFailCamera);
                            Log.e(TAG, QR_test + QR_fail_camera);
                            break;
                    }
                    testCount = testPass + testFailScan + testFailCamera + testFailPreview;
                    String log = "???????????????" + testCount + "\n??????" + testPass + "???\n??????" + testFailScan + "???\n??????????????????" + testFailCamera + "???\n??????????????????" + testFailPreview + "???";
                    tvLog.setText(log);
                    if (testRunning) {
                        handler.sendEmptyMessageDelayed(REBOOT, 1500);

                    }
                    break;
                case REBOOT:
                    if (testRunning) {
                        if (reboot) {
                            reboot();
                        } else {
                            handler.sendEmptyMessage(START_SCAN);
                        }
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private void chooseRebootAlert() {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("??????????????????").setPositiveButton("???", (dialog1, which) -> {
            reboot = true;
            btnTest.setText("??????");
            testCount = 0;
            testPass = 0;
            testFailScan = 0;
            testFailCamera = 0;
            testFailPreview = 0;
            handler.sendEmptyMessage(START_SCAN);
            dialog1.dismiss();
        }).setNegativeButton("???", (dialog2, which) -> {
            reboot = false;
            btnTest.setText("??????");
            testCount = 0;
            testPass = 0;
            testFailScan = 0;
            testFailCamera = 0;
            testFailPreview = 0;
            handler.sendEmptyMessage(START_SCAN);
            dialog2.dismiss();
        }).create();
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
