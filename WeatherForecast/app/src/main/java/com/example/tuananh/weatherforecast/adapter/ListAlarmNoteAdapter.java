package com.example.tuananh.weatherforecast.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.model.AlarmNote;

import java.util.List;

public class ListAlarmNoteAdapter extends BaseAdapter {
    private Activity context;
    private List<AlarmNote> list;

    public ListAlarmNoteAdapter(Activity context, List<AlarmNote> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListAlarmNoteAdapter.ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_note_item, null);
            holder = new ListAlarmNoteAdapter.ViewHolder();
            holder.txtNoteContent = (TextView) convertView.findViewById(R.id.txtNoteContent);
            holder.txtDateInfo = (TextView) convertView.findViewById(R.id.txtDateInfo);

            convertView.setTag(holder);
            convertView.setTag(R.id.txtNoteContent, holder.txtNoteContent);
            convertView.setTag(R.id.txtDateInfo, holder.txtDateInfo);
        } else {
            holder = (ListAlarmNoteAdapter.ViewHolder) convertView.getTag();
        }

        AlarmNote note = list.get(position);
        holder.txtNoteContent.setText(note.getAlarmContent());
        holder.txtDateInfo.setText(context.getResources().getString(R.string.alarm_at) + ": " + note.getAlarmTime());

        return convertView;
    }

    static class ViewHolder {
        protected TextView txtNoteContent;
        protected TextView txtDateInfo;
    }
}
