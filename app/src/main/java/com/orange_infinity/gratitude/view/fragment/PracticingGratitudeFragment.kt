package com.orange_infinity.gratitude.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orange_infinity.gratitude.R
import kotlinx.android.synthetic.main.practicing_gratitude_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class PracticingGratitudeFragment : Fragment() {

    private lateinit var fragmentActivity: FragmentActivity

    companion object {
        fun newInstance(activity: FragmentActivity): PracticingGratitudeFragment {
            val instance = PracticingGratitudeFragment()
            instance.fragmentActivity = activity

            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.practicing_gratitude_fragment, container, false)

        val formatForDateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val currentDate = formatForDateNow.format(Date())
        tvDate.text = currentDate

        createFillingTopFragment()
        createFillingBottomFragment()

        return v
    }

    private fun createFillingTopFragment() {
        val fm = fragmentActivity.supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fillRecordContainerTop)

        if (fragment == null) {
            fragment = FillRecordFragment.newInstance(fragmentActivity)
            fm.beginTransaction()
                .add(R.id.fillRecordContainerTop, fragment)
                .commit()
        }
    }

    private fun createFillingBottomFragment() {
        val fm = fragmentActivity.supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fillRecordContainerBottom)

        if (fragment == null) {
            fragment = FillRecordFragment.newInstance(fragmentActivity)
            fm.beginTransaction()
                .add(R.id.fillRecordContainerBottom, fragment)
                .commit()
        }
    }
}