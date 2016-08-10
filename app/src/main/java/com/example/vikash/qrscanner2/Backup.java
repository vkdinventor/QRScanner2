package com.example.vikash.qrscanner2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class Backup extends Activity  {
    final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    private CameraSource cameraSource;
    private SurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        final TextView barcodeInfo = (TextView) findViewById(R.id.code_info);
        final TextView prduct = (TextView) findViewById(R.id.prodct_code);
        final TextView finishtype = (TextView) findViewById(R.id.finish_type);
        final TextView product_size = (TextView) findViewById(R.id.size);

        //final Activity mainActivity;
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).build();

        if (ContextCompat.checkSelfPermission(Backup.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Backup.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                                               @Override
                                               public void surfaceCreated(SurfaceHolder holder) {

                                                   try {
                                                       if (ContextCompat.checkSelfPermission(Backup.this, Manifest.permission.CAMERA)
                                                               == PackageManager.PERMISSION_GRANTED) {
                                                           cameraSource.start(cameraView.getHolder());
                                                       } else
                                                           Toast.makeText(Backup.this, "please allow camera access", Toast.LENGTH_LONG).show();
                                                   } catch (IOException ie) {
                                                       Log.e("CAMERA SOURCE", ie.getMessage());
                                                   }
                                               }

                                               @Override
                                               public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                               }

                                               @Override
                                               public void surfaceDestroyed(SurfaceHolder holder) {
                                                   cameraSource.stop();
                                               }
                                           }

        );

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()

                                     {
                                         @Override
                                         public void release () {
                                         }

                                         @Override
                                         public void receiveDetections (Detector.Detections < Barcode > detections) {
                                             //setting the result
                                             final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                                             if (barcodes.size() != 0) {

                                                 barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                                                     public void run() {
                                                         barcodeInfo.setText(barcodes.valueAt(0).displayValue);

//                            //Do not start web brower
//                            Intent webIntent = new Intent(getBaseContext(), WebActivity.class);
//                            webIntent.putExtra("url", barcodes.valueAt(0).displayValue.toString());
//                            startActivity(webIntent);
                                                     }
                                                 });
                                                 String str = barcodes.valueAt(0).displayValue;

                                                 final String[] str2 = str.split("/");
                                                 final int strLength = str2.length;
                                                 // Log.v("TAG ", str2[0] + " " + str2[1] + " " + str2[1]);
                                                 prduct.post(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         prduct.setText(str2[strLength - 3]);
                                                     }
                                                 });
                                                 finishtype.post(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         finishtype.setText(str2[strLength - 2]);
                                                     }
                                                 });
                                                 product_size.post(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         product_size.setText(str2[strLength - 1]);
                                                     }
                                                 });

                                             }
                                         }
                                     }

        );
    }

}

