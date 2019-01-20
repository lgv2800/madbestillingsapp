package com.example.gruppeb.madbestillingsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gruppeb.madbestillingsapp.Model.MyOrdersScreenModel;
import com.example.gruppeb.madbestillingsapp.R;

import java.util.List;

public class MyOrdersScreenAdapter extends RecyclerView.Adapter<MyOrdersScreenAdapter.MyOrdersScreenViewHolder> {

    private Context mContext;
    private List<MyOrdersScreenModel> dishesFromOrdersList;

    public MyOrdersScreenAdapter(Context mContext, List<MyOrdersScreenModel> dishesFromOrdersList) {
        this.mContext = mContext;
        this.dishesFromOrdersList = dishesFromOrdersList;
    }

    @NonNull
    @Override
    public MyOrdersScreenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        View mView = mLayoutInflater.inflate(R.layout.my_orders_screen_layout, null);
        mView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MyOrdersScreenViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersScreenViewHolder myOrdersScreenViewHolder, int position) {
        MyOrdersScreenModel mMyOrdersScreenModel = dishesFromOrdersList.get(position);

        myOrdersScreenViewHolder.textViewDishOrderID.setText(Integer.toString(mMyOrdersScreenModel.getOrderDishID()));
        myOrdersScreenViewHolder.textViewDishName.setText(mMyOrdersScreenModel.getOrderDishName());
        myOrdersScreenViewHolder.textViewDishBreadType.setText(mMyOrdersScreenModel.getOrderDishBreadType());
        myOrdersScreenViewHolder.textViewDishOrderDate.setText(mMyOrdersScreenModel.getOrderDishDate());
        //myOrdersScreenViewHolder.mImageView.setImageDrawable(mContext.getResources().getDrawable(mMyOrdersScreenModel.getOrderImageView(), null));
    }

    @Override
    public int getItemCount() {
        return dishesFromOrdersList.size();
    }

    class MyOrdersScreenViewHolder extends RecyclerView.ViewHolder {

        //ImageView mImageView;
        TextView textViewDishName, textViewDishBreadType, textViewDishOrderID, textViewDishOrderDate;

        public MyOrdersScreenViewHolder(@NonNull View itemView) {
            super(itemView);

            //mImageView = itemView.findViewById(R.id.imageView_DishImage);
            textViewDishName = itemView.findViewById(R.id.textView_DishName);
            textViewDishBreadType = itemView.findViewById(R.id.textView_DishBreadType);
            textViewDishOrderDate = itemView.findViewById(R.id.textView_DishOrderDate);
            textViewDishOrderID = itemView.findViewById(R.id.textView_DishOrderID);
        }
    }


}
