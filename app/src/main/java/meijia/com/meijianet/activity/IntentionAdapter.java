package meijia.com.meijianet.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/22
 */
public class IntentionAdapter extends FragmentPagerAdapter {

    private String title[];
    private List<Fragment> datas;

    public IntentionAdapter(FragmentManager fm, String[] title, List<Fragment> datas) {
        super(fm);
        this.title = title;
        this.datas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
