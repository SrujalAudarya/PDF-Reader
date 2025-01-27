package com.srujal.pdfreader;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.srujal.pdfreader.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PDFAdapter adapter;
    private List<File> pdfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Request runtime permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                displayPDF();
            }
        } else {
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            displayPDF();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(MainActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest request, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }

//    private void runTimePermission() {
//        Dexter.withContext(this)
//                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        displayPDF();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        Toast.makeText(MainActivity.this, "Permission Denied. Unable to access PDFs.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest request, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                displayPDF();
            } else {
                Toast.makeText(this, "Permission is required to access PDFs.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private ArrayList<File> findPdf(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        if (files != null) {
            for (File fileItem : files) {
                if (fileItem.isDirectory() && !fileItem.isHidden()) {
                    arrayList.addAll(findPdf(fileItem));
                } else {
                    if (fileItem.getName().endsWith(".pdf")) {
                        arrayList.add(fileItem);
                        //Log.d("PDFReader", "Found PDF: " + fileItem.getAbsolutePath());
                    }
                }
            }
        }
        return arrayList;
    }


    private void displayPDF() {
        binding.pdfRecycleView.setHasFixedSize(true);
        binding.pdfRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        pdfList = new ArrayList<>();

        // Use public storage for accessing PDFs
        File storageDir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            storageDir = Environment.getExternalStorageDirectory(); // Deprecated but works
        } else {
            storageDir = Environment.getExternalStorageDirectory();
        }

        if (storageDir != null) {
            pdfList.addAll(findPdf(storageDir));
        }

        adapter = new PDFAdapter(this, pdfList);
        binding.pdfRecycleView.setAdapter(adapter);

        if (pdfList.isEmpty()) {
            Toast.makeText(this, "No PDF files found.", Toast.LENGTH_SHORT).show();
        }
    }
}
