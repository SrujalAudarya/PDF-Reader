package com.srujal.pdfreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFviewHolder> {

    private Context context;
    private List<File> list;

    public PDFAdapter(Context context, List<File> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PDFviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PDFviewHolder(LayoutInflater.from(context).inflate(R.layout.sample_items, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PDFviewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.tvName.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
