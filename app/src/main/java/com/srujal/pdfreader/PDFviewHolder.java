package com.srujal.pdfreader;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PDFviewHolder extends RecyclerView.ViewHolder {

    private TextView tvName;
    private CardView cardView;


    public PDFviewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvPdfName);
        cardView = itemView.findViewById(R.id.pdfCardView);
    }
}
