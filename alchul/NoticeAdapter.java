package com.example.hong.alchul;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NoticeAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<NoticeVO> noticeData;
    private LayoutInflater inflater;
    private String id;
    private String stat;

    public NoticeAdapter(Context applicationContext, int talklist, ArrayList<NoticeVO> list, String id, String stat) {
        this.context = applicationContext;
        this.layout = talklist;
        this.noticeData = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.id= id;
        this.stat = stat;
    }

    @Override
    public int getCount() { // 전체 데이터 개수
        return noticeData.size();
    }

    @Override
    public Object getItem(int position) { // position번째 아이템
        return noticeData.get(position);
    }

    @Override
    public long getItemId(int position) { // position번째 항목의 id인데 보통 position
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //항목의 index, 전에 inflate 되어있는 view, listView

//첫항목을 그릴때만 inflate 함 다음거부터는 매개변수로 넘겨줌 (느리기때문) : recycle이라고 함
        ViewHolder holder;

        if(convertView == null){
//어떤 레이아웃을 만들어 줄 것인지, 속할 컨테이너, 자식뷰가 될 것인지
            convertView = inflater.inflate(layout, parent, false); //아이디를 가지고 view를 만든다
            holder = new ViewHolder();
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_id);
            holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);


            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

//누군지 판별
        String writer = noticeData.get(position).getUserState();
        holder.tv_time.setVisibility(View.VISIBLE);
        holder.tv_name.setVisibility(View.VISIBLE);
        holder.tv_title.setVisibility(View.VISIBLE);

        holder.tv_time.setText(noticeData.get(position).getTime());
        holder.tv_name.setText(noticeData.get(position).getId()+ " [" + writer + "]");
        holder.tv_title.setText(noticeData.get(position).getTitle());

        return convertView;
    }

    //뷰홀더패턴
    public class ViewHolder{
        TextView tv_time;
        TextView tv_name;
        TextView tv_title;
    }

}