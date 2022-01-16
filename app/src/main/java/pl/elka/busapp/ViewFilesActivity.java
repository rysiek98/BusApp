package pl.elka.busapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;

public class ViewFilesActivity extends AppCompatActivity {

    private ArrayList<String> addedFiles;
    private ArrayList<MaterialButton> files = new ArrayList<>();
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files);
        getSupportActionBar().hide();

        container = findViewById(R.id.filesContainer);
        listAddedFiles();
        paintListOfFiles();
        activeButtons();
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        container.removeAllViews();
        addedFiles.clear();
        files.clear();
        listAddedFiles();
        paintListOfFiles();
        activeButtons();
    }


    private void listAddedFiles() {
        File busDir = new File(Environment.getExternalStorageDirectory() + File.separator + "BUS");
        addedFiles = new ArrayList<>();
        for (File f : busDir.listFiles()) {
            if (f.isFile()) {
                addedFiles.add(f.getName());
            }
        }
    }

    private void paintListOfFiles() {
        for (String name : addedFiles) {
            MaterialButton newElement = new MaterialButton(this);
            newElement.setText(name);
            newElement.setId(container.getChildCount() + 1);
            newElement.setBackgroundColor(Color.BLACK);
            newElement.setTextColor(Color.WHITE);
            newElement.setCornerRadius(20);
            container.addView(newElement);
            files.add(newElement);
        }
    }

    private void activeButtons() {
        for (MaterialButton bt : files) {
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ViewFilesActivity.this, ViewFileActivity.class);
                    intent.putExtra("fileName", bt.getText());
                    startActivity(intent);
                }
            });
        }
    }
}