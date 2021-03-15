package com.whiteside.insta.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter : FragmentStateAdapter {
    var list: List<Fragment>

    constructor(fragmentActivity: FragmentActivity, list: List<Fragment>) : super(fragmentActivity) {
        this.list = list
    }

    constructor(fragment: Fragment, list: List<Fragment>) : super(fragment) {
        this.list = list
    }

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle, list: List<Fragment>) : super(fragmentManager, lifecycle) {
        this.list = list
    }

    override fun createFragment(position: Int): Fragment {
        list[position].onResume()
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}