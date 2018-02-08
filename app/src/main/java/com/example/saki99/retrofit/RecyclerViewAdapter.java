package com.example.saki99.retrofit;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Saki99 on 8.2.2018..
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Podatak> podaci;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView body;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            body = view.findViewById(R.id.body);
        }
    }

    public RecyclerViewAdapter(List<Podatak> podaci) {
        this.podaci = podaci;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(podaci.get(position).getTitle());
        holder.body.setText(podaci.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return this.podaci.size();
    }
}
