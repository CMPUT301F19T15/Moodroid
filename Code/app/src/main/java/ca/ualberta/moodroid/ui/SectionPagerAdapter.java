package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Section pager adapter.
 */
public class SectionPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    /**
     * Instantiates a new Section pager adapter.
     *
     * @param fm the fm
     */
    public SectionPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * Add fragment.
     *
     * @param fragment the fragment
     */
    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }

}
