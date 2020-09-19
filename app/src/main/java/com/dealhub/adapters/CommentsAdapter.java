package com.dealhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.models.Comments;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    private ArrayList cmnts;
    private Context context;

    public CommentsAdapter(Context context) {
        cmnts = new ArrayList();
        this.context = context;
    }

    public void loadComments(ArrayList output) {
        this.cmnts = output;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        CommentsAdapter.ViewHolder vh = new CommentsAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comments cmt= (Comments) cmnts.get(position);
        Picasso.get().load(cmt.getProfileurl()).into(holder.proimage);
        holder.uname.setText(cmt.getPublishername());
        holder.cmnt.setText(cmt.getComment());
    }

    @Override
    public int getItemCount() {
        return cmnts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView proimage;
        TextView uname,cmnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            proimage=itemView.findViewById(R.id.image_profile_comment);
            uname=itemView.findViewById(R.id.username__comment);
            cmnt=itemView.findViewById(R.id.comment_comment);

        }
    }
}
