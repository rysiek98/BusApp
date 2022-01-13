package pl.elka.busapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MenuActivity extends AppCompatActivity {

    private final int CHOOSE_PDF_FROM_DEVICE = 1001;
    private static final int PERMISSION_REQUEST_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (ContextCompat.checkSelfPermission(
                MenuActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        MenuActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            createDirectory();
        } else {
            askPermission();
        }

        Button showBtn = findViewById(R.id.showBtn);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callChooseFileFormDevice();
            }
        });

        Button exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

    }

    private void askPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                createDirectory();
            } else {
                Toast.makeText(MenuActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createDirectory() {

        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "BUS/");
        if (directory.exists()) {
            Toast.makeText(MenuActivity.this, "Directory is already exits!", Toast.LENGTH_SHORT).show();
        } else {
            directory.mkdir();
            Toast.makeText(MenuActivity.this, "Directory created!", Toast.LENGTH_SHORT).show();
        }
    }

    private void callChooseFileFormDevice() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse("Download");
        intent.setDataAndType(uri, "*/*");
        startActivityForResult(intent, CHOOSE_PDF_FROM_DEVICE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PDF_FROM_DEVICE && resultCode == RESULT_OK) {
            if (data != null) {
                String path = data.getData().getPath();
                File file = new File(path);
                File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "BUS/" + file.getName() + ".pdf");
                if (file2.exists()) {
                    Toast.makeText(this, "File already exist!", Toast.LENGTH_SHORT).show();
                } else {
                    FileInputStream inStream = null;
                    try {
                        inStream = new FileInputStream(path);
                        FileOutputStream outStream = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "BUS/" + file.getName() + ".pdf");
                        FileChannel inChannel = inStream.getChannel();
                        FileChannel outChannel = outStream.getChannel();
                        inChannel.transferTo(0, inChannel.size(), outChannel);
                        inStream.close();
                        outStream.close();
                        Toast.makeText(this, file.getName(), Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}