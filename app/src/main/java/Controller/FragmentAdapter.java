package Controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;
import androidx.fragment.app.Fragment;

public class FragmentAdapter extends FragmentStateAdapter {

    List<Fragment> list;

    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.list = list;
    }

    public FragmentAdapter(@NonNull androidx.fragment.app.Fragment fragment, List<Fragment> list) {
        super(fragment);
        this.list = list;
    }

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> list) {
        super(fragmentManager, lifecycle);
        this.list = list;
    }


    @NonNull
    @Override
    public androidx.fragment.app.Fragment createFragment(int position) {
         list.get(position).onResume();
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
