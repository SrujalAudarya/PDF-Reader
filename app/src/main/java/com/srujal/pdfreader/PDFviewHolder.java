package com.srujal.pdfreader;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PDFviewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public CardView cardView;
    public ImageView ivStar; // Add the star icon reference

    public PDFviewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvPdfName);
        cardView = itemView.findViewById(R.id.pdfCardView);
        ivStar = itemView.findViewById(R.id.ivStar);
    }
}
