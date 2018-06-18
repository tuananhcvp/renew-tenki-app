package com.example.tuananh.weatherforecast.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.model.Note;

import java.util.List;

public class ListNoteAdapter extends BaseAdapter {
    private Activity context;
    private List<Note> list;

    public ListNoteAdapter(Activity context, List<Note> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_note_item, null);
            holder = new ViewHolder();
            holder.txtNoteContent = (TextView) convertView.findViewById(R.id.txtNoteContent);
            holder.txtDateInfo = (TextView) convertView.findViewById(R.id.txtDateInfo);

            convertView.setTag(holder);
            convertView.setTag(R.id.txtNoteContent, holder.txtNoteContent);
            convertView.setTag(R.id.txtDateInfo, holder.txtDateInfo);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = list.get(position);
        holder.txtNoteContent.setText(note.getContent());

        if (note.getModifyTime().equalsIgnoreCase("")) {
            holder.txtDateInfo.setText(context.getResources().getString(R.string.create_at) + ": " + note.getCreateTime());
        } else {
            holder.txtDateInfo.setText(context.getResources().getString(R.string.modify_at) + ": " + note.getModifyTime());
        }

        return convertView;
    }

    static class ViewHolder {
        protected TextView txtNoteContent;
        protected TextView txtDateInfo;
    }
}
