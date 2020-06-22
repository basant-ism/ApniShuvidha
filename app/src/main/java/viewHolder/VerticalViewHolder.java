package viewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apnishuvidha.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import decoration.SpacesItemDecoration;
import model.Category;

public class VerticalViewHolder extends RecyclerView.ViewHolder
{
    public TextView tvCategoryName;
    public RecyclerView horizontalRecyclerView;
    public ImageView imgSeeMore;


    public VerticalViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCategoryName=itemView.findViewById(R.id.tv_category_name);
        imgSeeMore=itemView.findViewById(R.id.img_see_more);
        horizontalRecyclerView=itemView.findViewById(R.id.horizontal_recycler_view);
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),RecyclerView.HORIZONTAL,false));
        horizontalRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
    }



}
