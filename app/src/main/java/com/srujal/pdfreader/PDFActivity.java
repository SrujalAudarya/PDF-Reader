package com.srujal.pdfreader;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.srujal.pdfreader.databinding.ActivityPdfactivityBinding;

public class PDFActivity extends AppCompatActivity {

    String filePath;
    private ActivityPdfactivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}