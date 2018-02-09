package com.example.saki99.retrofit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

/**
 * Created by Saki99 on 9.2.2018..
 */

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public static final int RESULT_CODE = 1;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkPermission()) {

                Toast.makeText(ScannerActivity.this, "Dozvoljeno!", Toast.LENGTH_LONG).show();

            } else {

                requestPermission();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (scannerView == null) {

                scannerView = new ZXingScannerView(this);
                setContentView(scannerView);
            }

            scannerView.setResultHandler(this);
            scannerView.startCamera();

        } else {

            requestPermission();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        String scanResult = result.getText();

        int id = Integer.parseInt(scanResult.substring(scanResult.lastIndexOf("/") + 1));

        AlertDialog.Builder bil = new AlertDialog.Builder(this);
        bil.setMessage(String.valueOf(id));
        bil.setTitle("Title");
        AlertDialog dialog = bil.create();
        dialog.show();

        Intent i = new Intent();
        i.putExtra("id", id);
        setResult(RESULT_CODE, i);
        finish();
    }

    private boolean checkPermission() {

        return (ContextCompat.checkSelfPermission(ScannerActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ScannerActivity.this, new String[] { CAMERA }, REQUEST_CAMERA);
    }

    public void onRequestPermissionResult(int requestCode, String permission[], int grantResult[]) {

        switch (requestCode) {

            case REQUEST_CAMERA:

                if (grantResult.length > 0) {

                    boolean cameraAcepted = grantResult[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAcepted) {

                        Toast.makeText(ScannerActivity.this, "Dozvoljeno!", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(ScannerActivity.this, "Odbijen pristup kameri!", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {

                                displayAlertMessage("Morate omoguciti dozvolu za kameru",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[] { CAMERA }, REQUEST_CAMERA);
                                                }

                                            }
                                        });
                            }
                        }
                    }

                    break;
                }
        }
    }

    private void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(ScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
