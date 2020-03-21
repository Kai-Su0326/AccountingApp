package com.example.accountingapp;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    private LayoutInflater mInflater;
    public Context mContext;

    private LinkedList<CategoryResBean> cellList = GlobalUtil.getInstance().costRes;

    private String selected = "";

    public String getSelected() {
        return selected;
    }

    public interface OnCategoryClickListener{
        void onClick(String category);
    }

    private OnCategoryClickListener onCategoryClickListener;

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public CategoryRecyclerAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        selected = cellList.get(0).title; //因为默认选中第一项

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { //RecyclerView中必须有ViewHolder
        View view = mInflater.inflate(R.layout.cell_category,viewGroup,false);
        CategoryViewHolder myViewHolder = new CategoryViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) { //connect the view to the front end
        final CategoryResBean res = cellList.get(i);
        categoryViewHolder.imageView.setImageResource(res.resBlack);
        categoryViewHolder.textView.setText(res.title);
        categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = res.title;
                notifyDataSetChanged();
                if(onCategoryClickListener != null){
                    onCategoryClickListener.onClick(selected);
                }
            }
        });
        //选中后背景颜色变化
        if(categoryViewHolder.textView.getText().toString().equals(selected)){
            categoryViewHolder.background.setBackgroundResource(R.drawable.bg_edit_text);
        } else {
            categoryViewHolder.background.setBackgroundResource(R.color.colorPrimary);
        }
    }

    public void changeType(RecordBean.RecordType type){
        if(type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
            cellList = GlobalUtil.getInstance().costRes;
        } else {
            cellList = GlobalUtil.getInstance().incomeRes;
        }
        selected = cellList.get(0).title;
        notifyDataSetChanged(); //Called onBindViewHolder again!
    }
    @Override
    public int getItemCount() { //todo: THE BIGGEST CHALLENGE
        return cellList.size();
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder{

    RelativeLayout background;
    ImageView imageView;
    TextView textView;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        background = (RelativeLayout) itemView.findViewById(R.id.cell_background);
        imageView = (ImageView) itemView.findViewById(R.id.imageView_category_pic);
        textView = (TextView) itemView.findViewById(R.id.textView_category);
    }
}