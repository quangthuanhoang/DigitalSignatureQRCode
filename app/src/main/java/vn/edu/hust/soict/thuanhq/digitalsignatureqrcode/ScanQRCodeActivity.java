package vn.edu.hust.soict.thuanhq.digitalsignatureqrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

public class ScanQRCodeActivity extends AppCompatActivity {
     private SurfaceView surfaceView;
     private CameraSource cameraSource;
     private TextView textView;
     private ScrollView scrollView;
     private Dialog dialog;
     private BarcodeDetector barcodeDetector;
     private String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_q_r_code);
        surfaceView = findViewById(R.id.camerapreview);
        textView = findViewById(R.id.textScan);
        scrollView = findViewById(R.id.scrollText);
        dialog = new Dialog(this);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
               }catch (Exception e) {
                   e.printStackTrace();
               }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            surfaceView.setVisibility(View.INVISIBLE);
                            scrollView.setVisibility(View.VISIBLE);
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            a = textView.getText().toString();
                        }
                    });
                }
            }
        });
    }

    public void verify(View view) {
        String tmp = "";
        String tmpText = "";
        int index = 0;
        for(int i = 0; i < a.length(); i++) {
            if(a.charAt(i) != '|') {
                tmp += a.charAt(i);
                index = i;
            }
            else break;
        }
        for(int i = index + 2; i < a.length(); i++) {
            tmpText += a.charAt(i);
        }
        byte[] encryp = DatatypeConverter.parseHexBinary(tmp);
        SignVerify signVerify = new SignVerify();
        if(signVerify.verify(encryp, tmpText)) {
            dialog.setContentView(R.layout.legal_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buttonOk = dialog.findViewById(R.id.btnOK);
            TextView txtInfo = dialog.findViewById(R.id.txtInfo);
            txtInfo.setText(tmpText);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }
        else {
            dialog.setContentView(R.layout.illegal_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buttonOk = dialog.findViewById(R.id.btnOK);
            TextView txtInfo = dialog.findViewById(R.id.txtInfo);
            txtInfo.setText(tmpText);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}