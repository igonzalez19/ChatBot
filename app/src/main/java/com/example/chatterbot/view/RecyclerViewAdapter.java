package com.example.chatterbot.view;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatterbot.R;
import com.example.chatterbot.data.Message;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private List<Message> messages = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position)
    {
        Message message = messages.get(position);
        if(message.isOutcoming())
        {
            holder.contentLinearLayout.setGravity(Gravity.END);
            holder.messageLinearLayout.setBackgroundResource(R.drawable.bubble_outcoming);
        }
        else
        {
            holder.contentLinearLayout.setGravity(Gravity.START);
            holder.messageLinearLayout.setBackgroundResource(R.drawable.bubble_incoming);
        }
        holder.tvMessage.setText(message.getMessage());
        holder.tvTime.setText(message.getTime());
    }

    @Override
    public int getItemCount()
    {
        return messages == null ? 0 : messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout contentLinearLayout, messageLinearLayout;
        private TextView tvMessage, tvTime;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            contentLinearLayout = itemView.findViewById(R.id.contentLinearLayout);
            messageLinearLayout = itemView.findViewById(R.id.messageLinearLayout);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    public void addMessage(Message message)
    {
        messages.add(message);
        notifyItemInserted(messages.size());
    }
}
