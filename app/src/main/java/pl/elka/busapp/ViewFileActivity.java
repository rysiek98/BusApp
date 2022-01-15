package pl.elka.busapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class ViewFileActivity extends AppCompatActivity {

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
        PDFView pdfView = findViewById(R.id.pdfView);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "BUS/" + fileName);
        pdfView.fromFile(file).load();

        MaterialButton backBt = findViewById(R.id.backBtn);
        MaterialButton removeBt = findViewById(R.id.deleteBtn);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        removeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.delete();
                finish();
            }
        });
    }
}