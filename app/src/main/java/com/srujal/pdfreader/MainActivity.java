package com.srujal.pdfreader;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
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

import androidx.appcompat.widget.SearchView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onPDFSelectorListener{

    private ActivityMainBinding binding;
    private PDFAdapter adapter;

    private boolean doubleTab = false;
    private List<File> pdfList = new ArrayList<>();
    private List<File> fullPdfList = new ArrayList<>(); // To store the original list

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

        setupSearchView();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            boolean shouldRefresh = data.getBooleanExtra("refresh", false);
            if (shouldRefresh) {
                displayPDF(); // Refresh the list to reorder starred PDFs
            }
        }
    }


    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPDFList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPDFList(newText);
                return true;
            }
        });
    }

    private void filterPDFList(String query) {
        List<File> filteredList = new ArrayList<>();
        for (File file : fullPdfList) {
            if (file.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(file);
            }
        }
        adapter.updateList(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No PDFs found matching your search.", Toast.LENGTH_SHORT).show();
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

        File storageDir = Environment.getExternalStorageDirectory(); // Deprecated but works
        if (storageDir != null) {
            fullPdfList = findPdf(storageDir); // Store the original list
            pdfList.addAll(fullPdfList); // Copy to display list
        }

        adapter = new PDFAdapter(this, pdfList, this);
        binding.pdfRecycleView.setAdapter(adapter);

        if (pdfList.isEmpty()) {
            Toast.makeText(this, "No PDF files found.", Toast.LENGTH_SHORT).show();
        }

        // Get starred PDFs from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("StarredPrefs", MODE_PRIVATE);
        List<File> starredList = new ArrayList<>();
        List<File> unstarredList = new ArrayList<>();

        for (File file : fullPdfList) {
            if (preferences.getBoolean(file.getAbsolutePath(), false)) {
                starredList.add(file);
            } else {
                unstarredList.add(file);
            }
        }

        // Combine starred and unstarred lists
        pdfList.clear();
        pdfList.addAll(starredList);
        pdfList.addAll(unstarredList);

        adapter = new PDFAdapter(this, pdfList, this);
        binding.pdfRecycleView.setAdapter(adapter);

        if (pdfList.isEmpty()) {
            Toast.makeText(this, "No PDF files found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPDFSelected(File file) {
        Intent intent = new Intent(MainActivity.this, PDFActivity.class);
        intent.putExtra("path", file.getAbsolutePath());
        intent.putExtra("name",file.getName());
        startActivityForResult(intent, 100); // Request code 100
    }

    @Override
    public void onBackPressed(){
        if (doubleTab) {
            finishAffinity();
            super.onBackPressed();
        }
        else {
            doubleTab = true;
            Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTab = false;
                }
            },2000);
        }
    }
}