/*
package com.example.blooddonationapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.MainViewModel;
import com.example.blooddonationapp.Model.UserModel;
import com.example.blooddonationapp.R;

import java.util.ArrayList;
public class UserModelAdapter extends RecyclerView.Adapter<UserModelAdapter.UserModelViewHolder> {

    Activity activity;
    //Context context;
    ArrayList<UserModel> userModelList;

    MainViewModel mainViewModel;
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<UserModel> selectList = new ArrayList<>();

    public UserModelAdapter(Activity activity, ArrayList<UserModel> userModelList) {
        this.activity = activity;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_layout, parent, false);
        mainViewModel = ViewModelProviders.of((FragmentActivity) activity)
                .get(MainViewModel.class);

        return new UserModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserModelViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);

        holder.userNameTextView.setText(userModel.getName());
        holder.userNumberTextView.setText(userModel.getEmail());
        holder.userBloodGroupTextView.setText(userModel.getBlood_group());
        //holder.userTypeTextView.setText("Type : "+user.getType());
        holder.userAddressTextView.setText(userModel.getAddress());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (!isEnable){

                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            menuInflater.inflate(R.menu.select_menu, menu);

                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            isEnable = true;
                            ClickItem(holder);

                            mainViewModel.getText().observe((LifecycleOwner) activity
                                    , new Observer<UserModel>() {
                                        @Override
                                        public void onChanged(UserModel s) {
                                            actionMode.setTitle(String.format("%s Selected", s));
                                        }
                                    });

                            return true;
                        }

                        @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                            int id = menuItem.getItemId();
                            switch (id) {
                                case R.id.menu_delete:
                                    for (UserModel s : selectList) {
                                        userModelList.remove(s);

                                    }
                                    if (userModelList.size() == 0) {
                                        //tvEmpty.setVisibility(View.VISIBLE);
                                    }
                                    actionMode.finish();
                                    break;

                                case R.id.menu_select_all:
                                    if (selectList.size() == userModelList.size()) {
                                        //when all item is selected
                                        isSelectAll = false;
                                        selectList.clear();
                                    } else {
                                        //when all item is unselected

                                        isSelectAll = true;
                                        selectList.clear();
                                        selectList.addAll(userModelList);
                                    }
                                    mainViewModel.setText(userModelList.get(selectList.size()));
                                    notifyDataSetChanged();
                                    break;
                            }

                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                            isEnable = false;
                            isSelectAll = false;
                            selectList.clear();
                            notifyDataSetChanged();
                        }
                    };

                    ((AppCompatActivity) view.getContext()).startActionMode(callback);

                }else {
                    ClickItem(holder);
                }

                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnable){
                    ClickItem(holder);
                }else {
                    Toast.makeText(activity, "You Clicked " + userModelList.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (isSelectAll){
            holder.ivCheckBox.setVisibility(View.VISIBLE);

            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    private void ClickItem(UserModelViewHolder holder) {
        UserModel s = userModelList.get(holder.getAdapterPosition());
        if (holder.ivCheckBox.getVisibility() == View.GONE){
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectList.add(s);
        }else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            selectList.remove(s);
        }

        mainViewModel.setText(userModelList.get(selectList.size()));
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<UserModel> filteredList) {
        userModelList = filteredList;
        notifyDataSetChanged();
    }

    public static class UserModelViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, userNumberTextView, userBloodGroupTextView,userTypeTextView, userAddressTextView;
        AppCompatButton requestButton;
        ImageView ivCheckBox;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userNumberTextView = itemView.findViewById(R.id.userNumberTextView);
            userBloodGroupTextView = itemView.findViewById(R.id.userBloodGroupTextView);
            userBloodGroupTextView = itemView.findViewById(R.id.userBloodGroupTextView);
            //userTypeTextView = itemView.findViewById(R.id.userTypeTextView);
            userAddressTextView = itemView.findViewById(R.id.userAddressTextView);

            //requestButton = itemView.findViewById(R.id.requestButton);
            ivCheckBox = itemView.findViewById(R.id.iv_check_box);

        }
    }
}
*/
