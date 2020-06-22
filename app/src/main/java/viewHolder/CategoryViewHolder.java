package viewHolder;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apnishuvidha.R;
import com.squareup.picasso.Picasso;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView tvCategoryName;
    public ImageView imgCategory;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCategoryName=itemView.findViewById(R.id.tv_category_name);
        imgCategory=itemView.findViewById(R.id.img_category);
    }
    public void setCategoryName(String name)
    {
        tvCategoryName.setText(name);
    }
    public void setCategoryImage(String image)
    {
        Picasso.get().load(image).into(imgCategory);
    }
}
