package com.example.blooddonationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.Model.UserModel;
import com.example.blooddonationapp.R;

import java.util.ArrayList;
import java.util.List;

public class UserModelAdapter extends RecyclerView.Adapter<UserModelAdapter.UserModelViewHolder> {

    Context context;
    List<UserModel> userModelList;

    public UserModelAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public UserModelAdapter.UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_profile_layout, parent, false);
        return new UserModelAdapter.UserModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserModelAdapter.UserModelViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);

        /*if (user.getType().equals("recipient")){
            holder.requestButton.setVisibility(View.GONE);
        }*/

        holder.userNameTextView.setText(userModel.getName());
        holder.userNumberTextView.setText(userModel.getEmail());
        holder.userBloodGroupTextView.setText(userModel.getBlood_group());
        //holder.userTypeTextView.setText("Type : "+user.getType());
        holder.userAddressTextView.setText(userModel.getAddress());
        /*holder.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Send Request", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public void filterList(ArrayList<UserModel> filteredList) {
        userModelList = filteredList;
        notifyDataSetChanged();
    }

    public class UserModelViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, userNumberTextView, userBloodGroupTextView,userTypeTextView, userAddressTextView;
        AppCompatButton requestButton;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userNumberTextView = itemView.findViewById(R.id.userNumberTextView);
            userBloodGroupTextView = itemView.findViewById(R.id.userBloodGroupTextView);
            userBloodGroupTextView = itemView.findViewById(R.id.userBloodGroupTextView);
            //userTypeTextView = itemView.findViewById(R.id.userTypeTextView);
            userAddressTextView = itemView.findViewById(R.id.userAddressTextView);

            //requestButton = itemView.findViewById(R.id.requestButton);

        }
    }
}
