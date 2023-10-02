package com.example.movieapp.adapter;

import static com.example.movieapp.utils.CONSTANT.TAG_EDIT_PROFILE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.databinding.LayoutItemUserBinding;
import com.example.movieapp.databinding.LayoutReminderItemBinding;
import com.example.movieapp.entity.User;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.UsersDiffUtilCallBack;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<User> userArrayList = new ArrayList<>();
    private final UserItemClickListener userItemClickListener;

    public UserAdapter(UserItemClickListener userItemClickListener) {
        this.userItemClickListener = userItemClickListener;
    }

    public void setData(ArrayList<User> newUserList) {
        Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "setData#start");
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UsersDiffUtilCallBack(newUserList, userArrayList));
        diffResult.dispatchUpdatesTo(this);
        userArrayList.clear();
        this.userArrayList.addAll(newUserList);
        Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "setData#end");
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final LayoutItemUserBinding binding;

        public UserViewHolder(LayoutItemUserBinding layoutItemUserBinding) {
            super(layoutItemUserBinding.getRoot());
            this.binding = layoutItemUserBinding;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        UserViewHolder userViewHolder = (UserViewHolder) holder;

        userViewHolder.binding.tvNameOfUser.setText(user.name);

        String imgAvatar = user.avatar;

        if (!imgAvatar.equals("")){
            try {
                byte[] b = Base64.decode(imgAvatar, Base64.DEFAULT);
                Log.d(TAG_EDIT_PROFILE, "getAvatar#b.length" + b.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                userViewHolder.binding.imgAvatarUser.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // java.lang.IllegalArgumentException: bad base-64
                android.util.Log.i(TAG_EDIT_PROFILE, "", e);
            }
        }

        userViewHolder.itemView.setOnClickListener(v -> userItemClickListener.onItemClick(user));
        userViewHolder.itemView.setOnLongClickListener(v -> {
            userItemClickListener.deleteUserClick(user);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public interface UserItemClickListener {
        void onItemClick(User user);
        void deleteUserClick(User user);
    }
}