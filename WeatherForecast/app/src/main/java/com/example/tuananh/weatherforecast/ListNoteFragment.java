package com.example.tuananh.weatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.tuananh.weatherforecast.adapter.ListNoteAdapter;
import com.example.tuananh.weatherforecast.databinding.FragmentListNoteBinding;
import com.example.tuananh.weatherforecast.model.Note;
import com.example.tuananh.weatherforecast.utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ListNoteFragment extends Fragment {
    private List<Note> noteList = new ArrayList<Note>();
    private ListNoteAdapter adapter;
    private boolean isInitialize = false;
    OnListNoteFragmentListener mCallback;
    private FragmentListNoteBinding binding;

    private static final int MENU_ITEM_DELETE = 111;
    private static final int MENU_ITEM_SETALARM = 222;

    /**
     * ListNoteFragment initialize
     *
     * @return ListNoteFragment
     */
    public static ListNoteFragment newInstance() {
        Bundle args = new Bundle();
        ListNoteFragment fragment = new ListNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (isInitialize) {
                updateListNote();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isInitialize = true;

        View view = inflater.inflate(R.layout.fragment_list_note, container, false);
        binding = FragmentListNoteBinding.bind(view);

        init();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitialize = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isInitialize = false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle(getResources().getString(R.string.select_action));
        menu.add(0, MENU_ITEM_SETALARM, 0, getResources().getString(R.string.set_alarm));
        menu.add(0, MENU_ITEM_DELETE, 1, getResources().getString(R.string.delete_note));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Note selectedNote = (Note) binding.listNote.getItemAtPosition(info.position);

        if (item.getItemId() == MENU_ITEM_SETALARM) {
            Intent intent = new Intent(getActivity(), AlarmActivity.class);
            intent.putExtra("SetAlarmNote", selectedNote);
            startActivity(intent);

        } else if (item.getItemId() == MENU_ITEM_DELETE) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(getResources().getString(R.string.check_delete))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.txt_yes), (dialogInterface, i) -> deleteNote(selectedNote))
                    .setNegativeButton(getResources().getString(R.string.txt_no), null)
                    .show();
        }

        return true;
    }

    private void init() {
        noteList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        noteList.addAll(db.getAllNotes());

        adapter = new ListNoteAdapter(getActivity(), noteList);
        binding.listNote.setAdapter(adapter);
        registerForContextMenu(binding.listNote);

        binding.listNote.setOnItemClickListener((parent, view, position, id) -> mCallback.sendNoteInfo(noteList.get(position)));

    }

    private void deleteNote(Note note) {
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        db.deleteNote(note);
        noteList.remove(note);
        adapter.notifyDataSetChanged();
    }

    private void updateListNote() {
        noteList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        noteList.addAll(db.getAllNotes());
        adapter.notifyDataSetChanged();
    }

    public void setNoteCallbackListener(OnListNoteFragmentListener callback) {
        this.mCallback = callback;
    }

    public interface OnListNoteFragmentListener {
        void sendNoteInfo(Note note);
    }
}
