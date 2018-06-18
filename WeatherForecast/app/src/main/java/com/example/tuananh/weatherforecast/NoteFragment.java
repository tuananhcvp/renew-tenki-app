package com.example.tuananh.weatherforecast;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuananh.weatherforecast.databinding.FragmentNoteBinding;
import com.example.tuananh.weatherforecast.model.AlarmNote;
import com.example.tuananh.weatherforecast.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment implements ListNoteFragment.OnListNoteFragmentListener,
        ListAlarmNoteFragment.OnListAlarmNoteListener {

    private ViewPagerAdapter pagerAdapter;

    private FragmentNoteBinding binding;

    /**
     * NoteFragment initialize
     *
     * @return NoteFragment
     */
    public static NoteFragment newInstance() {
        Bundle args = new Bundle();
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_tenki_note));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_note, container, false);

        binding = FragmentNoteBinding.bind(layout);

        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(CreateNoteFragment.newInstance(), getResources().getString(R.string.create_note));
        pagerAdapter.addFragment(ListNoteFragment.newInstance(), getResources().getString(R.string.list_note));
        pagerAdapter.addFragment(ListAlarmNoteFragment.newInstance(), getResources().getString(R.string.alarm_note));
        binding.pagerNote.setAdapter(pagerAdapter);

        ((ListNoteFragment) pagerAdapter.getItem(1)).setNoteCallbackListener(this);
        ((ListAlarmNoteFragment) pagerAdapter.getItem(2)).setAlarmNoteCallbackListener(this);

        binding.tabsNote.setupWithViewPager(binding.pagerNote);

        return binding.getRoot();
    }

    @Override
    public void sendAlarmNoteInfo(AlarmNote note) {
        binding.pagerNote.setCurrentItem(0);
        ((CreateNoteFragment) pagerAdapter.getItem(0)).showNoteAlarm(note);
    }

    @Override
    public void sendNoteInfo(Note note) {
        binding.pagerNote.setCurrentItem(0);
        ((CreateNoteFragment) pagerAdapter.getItem(0)).showNoteEdit(note);
    }

    /**
     * Adapter for the viewpager using FragmentPagerAdapter
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
