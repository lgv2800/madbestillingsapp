package com.example.gruppeb.madbestillingsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gruppeb.madbestillingsapp.Model.EveningMenuScreenModel;
import com.example.gruppeb.madbestillingsapp.R;

import java.util.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gruppeb.madbestillingsapp.R;

public class EveningMenuScreenAdapter extends RecyclerView.Adapter<EveningMenuScreenAdapter.EveningMenuScreenViewHolder> {

    private Context mContext;
    private List<EveningMenuScreenModel> eveningMenuList;

    public EveningMenuScreenAdapter(Context mContext, List<EveningMenuScreenModel> eveningMenuList) {
        this.mContext = mContext;
        this.eveningMenuList = eveningMenuList;
    }

    @NonNull
    @Override
    public EveningMenuScreenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        View mView = mLayoutInflater.inflate(R.layout.evening_menu_screen_layout, null);
        mView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new EveningMenuScreenViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull EveningMenuScreenViewHolder eveningMenuScreenViewHolder, int position) {
        EveningMenuScreenModel mEveningMenuScreenModel = eveningMenuList.get(position);

        eveningMenuScreenViewHolder.textViewEveningDishName.setText(mEveningMenuScreenModel.getEveningName());
        eveningMenuScreenViewHolder.textViewEveningDishDescription.setText(mEveningMenuScreenModel.getEveningDescription());
        eveningMenuScreenViewHolder.textViewEveningDishComment.setText(mEveningMenuScreenModel.getEveningComment());
        //eveningMenuScreenViewHolder.mImageView.setImageDrawable(mContext.getResources().getDrawable(mEveningMenuScreenModel.getEveningImageView(), null));
    }

    @Override
    public int getItemCount() {
        return eveningMenuList.size();
    }

    class EveningMenuScreenViewHolder extends RecyclerView.ViewHolder {

        //ImageView mImageView;
        TextView textViewEveningDishName, textViewEveningDishDescription, textViewEveningDishComment;

        public EveningMenuScreenViewHolder(@NonNull View itemView) {
            super(itemView);

            //mImageView = itemView.findViewById(R.id.imageView_EveningDishImage);
            textViewEveningDishName = itemView.findViewById(R.id.textView_EveningDishName);
            textViewEveningDishDescription = itemView.findViewById(R.id.textView_EveningDishDescription);
            textViewEveningDishComment = itemView.findViewById(R.id.textView_EveningDishComment);
        }
    }

}
