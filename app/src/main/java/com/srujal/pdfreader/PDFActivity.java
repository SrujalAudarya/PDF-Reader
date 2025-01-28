package com.srujal.pdfreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.srujal.pdfreader.databinding.ActivityPdfactivityBinding;

import java.io.File;

public class PDFActivity extends AppCompatActivity {
    String filePath, fileName;
    private boolean isStarred = false;
    private SharedPreferences preferences;
    private ActivityPdfactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        preferences = getSharedPreferences("StarredPrefs", MODE_PRIVATE);

        filePath = getIntent().getStringExtra("path");
        fileName = getIntent().getStringExtra("name");

        binding.toolbar.setTitle(fileName);

        File file = new File(filePath);
        Uri path = Uri.fromFile(file);
        binding.pdfView.fromUri(path).load();

        // Check if this PDF is already starred
        isStarred = preferences.getBoolean(filePath, false);
        updateStarIcon(binding.btnStar);

        binding.btnStar.setOnClickListener(view -> {
            isStarred = !isStarred;
            preferences.edit().putBoolean(filePath, isStarred).apply();
            updateStarIcon(binding.btnStar);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("refresh", true); // Notify MainActivity to refresh
        setResult(RESULT_OK, resultIntent);
    }


    private void updateStarIcon(ImageView btnStar) {
        if (isStarred) {
            btnStar.setImageResource(R.drawable.clickstaricon); // Filled star
        } else {
            btnStar.setImageResource(R.drawable.staricon); // Unfilled star
        }
    }
}