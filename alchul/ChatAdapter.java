package com.example.hong.alchul;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String id;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView profile;
        TextView username, realtime, usermessage, mymessage, mytime;

        public MyViewHolder(@NonNull View view) {
            super(view);
            profile = view.findViewById(R.id.photoImageView);
            username = view.findViewById(R.id.nameTextView);
            realtime = view.findViewById(R.id.realtime);
            usermessage = view.findViewById(R.id.messageTextView);
            mymessage = view.findViewById(R.id.mymsg);
            mytime = view.findViewById(R.id.mytime);

        }
    }

    private ArrayList<ChatVO> chatInfoList;
    ChatAdapter(ArrayList<ChatVO> chatInfoList, String id){
        this.chatInfoList = chatInfoList;
        this.id = id;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        if(chatInfoList.get(position).getId().equals(id)){

            myViewHolder.profile.setVisibility(View.GONE);
            myViewHolder.username.setVisibility(View.GONE);
            myViewHolder.usermessage.setVisibility(View.GONE);
            myViewHolder.realtime.setVisibility(View.GONE);

            myViewHolder.mymessage.setText(chatInfoList.get(position).content);
            myViewHolder.mytime.setText(chatInfoList.get(position).time);
        }else {
            myViewHolder.profile.setImageResource(chatInfoList.get(position).imageID);
            myViewHolder.username.setText(chatInfoList.get(position).name);
            myViewHolder.usermessage.setText(chatInfoList.get(position).content);
            myViewHolder.realtime.setText(chatInfoList.get(position).time);

            myViewHolder.mytime.setVisibility(View.GONE);
            myViewHolder.mymessage.setVisibility(View.GONE);
        }


    }

    @Override
    public long getItemId(int position) { // position번째 항목의 id인데 보통 position
        return position;
    }

    @Override
    public int getItemCount() {
        return chatInfoList.size();
    }
}
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //항목의 index, 전에 inflate 되어있는 view, listView

//첫항목을 그릴때만 inflate 함 다음거부터는 매개변수로 넘겨줌 (느리기때문) : recycle이라고 함
        ViewHolder holder;

        if(convertView == null){
//어떤 레이아웃을 만들어 줄 것인지, 속할 컨테이너, 자식뷰가 될 것인지
            convertView = inflater.inflate(layout, parent, false); //아이디를 가지고 view를 만든다
            holder = new ViewHolder();
            holder.img= (ImageView)convertView.findViewById(R.id.iv_profile);
            holder.tv_msg = (TextView)convertView.findViewById(R.id.tv_content);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_id);
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            holder.my_msg = (TextView)convertView.findViewById(R.id.my_msg);
            holder.my_time = (TextView)convertView.findViewById(R.id.my_time);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

//누군지 판별
        if(chatData.get(position).getId().equals(id)){
            holder.tv_time.setVisibility(View.GONE);
            holder.tv_name.setVisibility(View.GONE);
            holder.tv_msg.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);

            holder.my_msg.setVisibility(View.VISIBLE);
            holder.my_time.setVisibility(View.VISIBLE);

            holder.my_time.setText(chatData.get(position).getTime());
            holder.my_msg.setText(chatData.get(position).getContent());
        }else{
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_name.setVisibility(View.VISIBLE);
            holder.tv_msg.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.VISIBLE);

            holder.my_msg.setVisibility(View.GONE);
            holder.my_time.setVisibility(View.GONE);

            holder.img.setImageResource(chatData.get(position).getImageID()); // 해당 사람의 프사 가져옴
            holder.tv_msg.setText(chatData.get(position).getContent());
            holder.tv_time.setText(chatData.get(position).getTime());
            holder.tv_name.setText(chatData.get(position).getId());
        }

        return convertView;
    }

    //뷰홀더패턴
    public class ViewHolder{
        ImageView img;
        TextView tv_msg;
        TextView tv_time;
        TextView tv_name;
        TextView my_time;
        TextView my_msg;
    }

}
*/