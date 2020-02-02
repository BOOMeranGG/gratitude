package com.orange_infinity.gratitude.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orange_infinity.gratitude.R
import kotlinx.android.synthetic.main.level_noticing_fragment.view.*

class LevelNoticingFragment : Fragment() {

    private lateinit var fragmentActivity: FragmentActivity
    private var countOfRecords: Int = 1
    private var fragmentTop: FillRecordFragment? = null
    private var fragmentBottom: FillRecordFragment? = null

    companion object {
        fun newInstance(activity: FragmentActivity, countOfRecords: Int): LevelNoticingFragment {
            val instance = LevelNoticingFragment()
            instance.fragmentActivity = activity
            instance.countOfRecords = countOfRecords

            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.level_noticing_fragment, container, false)

        v.tvNumberOf.text = "${countOfRecords % 3 + 1} of 3"
        val level = countOfRecords / 3 + 1
        var name = "Noticing"
        if (level == 2) {
            name = "Reflecting"
        }
        v.tvLevelNoticing.text = "Level ${countOfRecords / 3 + 1}: $name"
        createFillingTopFragment()
        createFillingBottomFragment()

        v.btnSave.setOnClickListener {
            fragmentTop?.saveRecord()
            fragmentBottom?.saveRecord()
            activity?.finish()
        }

        return v
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun createFillingTopFragment() {
        val fm = fragmentActivity.supportFragmentManager
        fragmentTop = fm.findFragmentById(R.id.fillRecordContainerTop) as FillRecordFragment?

        if (fragmentTop == null) {
            fragmentTop = FillRecordFragment.newInstance(fragmentActivity, countOfRecords, true)
            fm.beginTransaction()
                .add(R.id.fillRecordContainerTop, fragmentTop!!)
                .commit()
        }
    }

    private fun createFillingBottomFragment() {
        val fm = fragmentActivity.supportFragmentManager
        fragmentBottom = fm.findFragmentById(R.id.fillRecordContainerBottom) as FillRecordFragment?

        if (fragmentBottom == null) {
            fragmentBottom = FillRecordFragment.newInstance(fragmentActivity, countOfRecords, false)
            fm.beginTransaction()
                .add(R.id.fillRecordContainerBottom, fragmentBottom!!)
                .commit()
        }
    }
}