package com.orange_infinity.gratitude.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orange_infinity.gratitude.R

class LevelNoticingFragment : Fragment() {

    companion object {
        fun newInstance(): LevelNoticingFragment {
            return LevelNoticingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.level_noticing_fragment, container, false)

        return v
    }
}