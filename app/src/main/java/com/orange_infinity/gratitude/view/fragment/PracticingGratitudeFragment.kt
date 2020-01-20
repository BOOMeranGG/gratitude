package com.orange_infinity.gratitude.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orange_infinity.gratitude.R
import kotlinx.android.synthetic.main.practicing_gratitude_fragment.*
import kotlinx.android.synthetic.main.practicing_gratitude_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*

class PracticingGratitudeFragment : Fragment() {

    private lateinit var fragmentActivity: FragmentActivity
    private var countOfRecords: Int = 1

    companion object {
        fun newInstance(activity: FragmentActivity, countOfRecords: Int): PracticingGratitudeFragment {
            val instance = PracticingGratitudeFragment()
            instance.fragmentActivity = activity
            instance.countOfRecords = countOfRecords

            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.practicing_gratitude_fragment, container, false)

        val formatForDateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val currentDate = formatForDateNow.format(Date())
        v.tvDate.text = currentDate

        createFillingTopFragment()
        createFillingBottomFragment()

        return v
    }

    private fun createFillingTopFragment() {
        val fm = fragmentActivity.supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fillRecordContainerTop)

        if (fragment == null) {
            fragment = FillRecordFragment.newInstance(fragmentActivity, countOfRecords, true)
            fm.beginTransaction()
                .add(R.id.fillRecordContainerTop, fragment)
                .commit()
        }
    }

    private fun createFillingBottomFragment() {
        val fm = fragmentActivity.supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fillRecordContainerBottom)

        if (fragment == null) {
            fragment = FillRecordFragment.newInstance(fragmentActivity, countOfRecords, false)
            fm.beginTransaction()
                .add(R.id.fillRecordContainerBottom, fragment)
                .commit()
        }
    }
}