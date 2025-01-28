package com.srujal.pdfreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFviewHolder> {

    private Context context;
    private List<File> list;
    private onPDFSelectorListener listener;

    public PDFAdapter(Context context, List<File> list, onPDFSelectorListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PDFviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PDFviewHolder(LayoutInflater.from(context).inflate(R.layout.sample_items, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PDFviewHolder holder, int position) {
        File currentFile = list.get(position);

        holder.tvName.setText(list.get(position).getName());
        holder.tvName.setSelected(true); // This is necessary for the marquee effect.

        // Check if the PDF is starred
        SharedPreferences sharedPreferences = context.getSharedPreferences("StarredPrefs", Context.MODE_PRIVATE);
        boolean isStarred = sharedPreferences.getBoolean(currentFile.getAbsolutePath(), false);

        // Show or hide the star icon based on the starred status
        if (isStarred) {
            holder.ivStar.setVisibility(View.VISIBLE);
        } else {
            holder.ivStar.setVisibility(View.GONE);
        }

        // Ensure the TextView is focusable and has the proper marquee behavior
        holder.tvName.setFocusable(true);
        holder.tvName.setFocusableInTouchMode(true);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPDFSelected(list.get(position));
            }
        });

        // Optionally, apply some delay before starting the marquee, to ensure it works in a smooth way
        holder.tvName.postDelayed(() -> {
            holder.tvName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.tvName.setMarqueeRepeatLimit(-1);
        }, 100);  // Delay to make sure the view is fully rendered
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<File> updatedList) {
        this.list = updatedList;
        notifyDataSetChanged();
    }
}
