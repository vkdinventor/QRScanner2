package com.example.vikash.qrscanner2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_CAMERA = 135;
    TextView barcodeInfo = null;
    TextView prduct = null;
    TextView finishtype = null;
    TextView product_size = null;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private BarcodeDetector barcodeDetector;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.v("TAG result", "Request Premission gramted");
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //startCamera();
                } else {
                    Log.v("TAG result", "Request Premission denied");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("TAG ", "on resume called");
        startCamera();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        barcodeInfo = (TextView) findViewById(R.id.code_info);
        prduct = (TextView) findViewById(R.id.prodct_code);
        finishtype = (TextView) findViewById(R.id.finish_type);
        product_size = (TextView) findViewById(R.id.size);

        //final Activity mainActivity;
        barcodeDetector = new BarcodeDetector.Builder(this).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).build();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            startCamera();
        }

        Log.v("TAG onCreat", "onCreatecalled");
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()

                                     {
                                         @Override
                                         public void release() {
                                         }

                                         @Override
                                         public void receiveDetections(Detector.Detections<Barcode> detections) {
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

    private void startCamera() {
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

                                               @Override
                                               public void surfaceCreated(SurfaceHolder holder) {
                                                   Log.v("TAG Camera", "surfaceCreated");
                                                   try {
                                                       if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                                                               == PackageManager.PERMISSION_GRANTED) {
                                                           cameraSource.start(holder);
                                                       } else
                                                           Toast.makeText(MainActivity.this, "please allow camera access", Toast.LENGTH_LONG).show();
                                                   } catch (IOException ie) {
                                                       Log.e("TAG CAMERA SOURCE", ie.getMessage());
                                                   }
                                               }

                                               @Override
                                               public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                                   Log.v("TAG Camera", "surfaceChanged");
                                               }

                                               @Override
                                               public void surfaceDestroyed(SurfaceHolder holder) {
                                                   Log.v("TAG Camera", "surfaceDestroyed");
                                                   cameraSource.stop();
                                               }
                                           }

        );
    }


}
