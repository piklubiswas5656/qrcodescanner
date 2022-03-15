package qr.code.scanner.android.digital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class QrcodeActivity extends AppCompatActivity {
    private CodeScanner codeScanner;
    private String scantext;
    private Button copy;
    private CodeScannerView scannerView;
    private boolean parmition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        scannerView = findViewById(R.id.scanner_view);
        checkPermition();

    }


    public void checkPermition() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        try {
                            runCodeScanner();
                            parmition = true;
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public void runCodeScanner() {
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String data = result.getText();

                            if (!result.getText().isEmpty()) {
                                Intent intent = new Intent(getApplicationContext(), Qrcoderesult.class);
                                intent.putExtra(Constant.qrresult, data);
                                startActivity(intent);

//                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                                ClipData clip = ClipData.newPlainText("textcopy", result.getText().toString());
//                                clipboard.setPrimaryClip(clip);
//                                Toast.makeText(getApplicationContext(), "Copy", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                        }

                    }
                });


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (parmition) {
            codeScanner.startPreview();
        }

    }

    @Override
    protected void onPause() {
        if (parmition) {
            codeScanner.releaseResources();
        }

        super.onPause();
    }
}