package pl.elka.busapp;

import static pl.elka.busapp.Utils.decryptToFile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ViewFileActivity extends AppCompatActivity {

    String my_key = "1234567890123456";
    String my_spec_key = "0987654321876543";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);
        String fileName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                fileName = null;
            } else {
                fileName = extras.getString("fileName");
            }
        } else {
            fileName = (String) savedInstanceState.getSerializable("fileName");
        }
        File outFile = new File(Environment.getExternalStorageDirectory() + File.separator + "BUS/" + fileName + ".pdf");
        File inFile = new File(Environment.getExternalStorageDirectory() + File.separator + "BUS/" + fileName);
        try {
            decryptToFile(my_key, my_spec_key, new FileInputStream(inFile), new FileOutputStream(outFile));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromFile(outFile).load();

        MaterialButton backBt = findViewById(R.id.backBtn);
        MaterialButton removeBt = findViewById(R.id.deleteBtn);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outFile.delete();
                finish();
            }
        });

        removeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inFile.delete();
                outFile.delete();
                finish();
            }
        });
    }

}