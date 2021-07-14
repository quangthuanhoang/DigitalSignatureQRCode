package vn.edu.hust.soict.thuanhq.digitalsignatureqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.xml.bind.DatatypeConverter;

public class ScanFormGalleryActivity extends AppCompatActivity {
    TextView textQR;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_form_gallery);
        textQR = findViewById(R.id.textQR);
        dialog = new Dialog(this);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        textQR.setText(message);
    }

    public void verify(View view) {
        String a = textQR.getText().toString();
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