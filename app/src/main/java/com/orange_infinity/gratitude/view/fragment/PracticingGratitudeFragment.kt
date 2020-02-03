package com.orange_infinity.gratitude.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orange_infinity.gratitude.R
import com.r0adkll.slidr.model.SlidrInterface
import kotlinx.android.synthetic.main.level_noticing_fragment.view.*
import kotlinx.android.synthetic.main.practicing_gratitude_fragment.*
import kotlinx.android.synthetic.main.practicing_gratitude_fragment.view.*
import kotlinx.android.synthetic.main.practicing_gratitude_fragment.view.btnSave
import java.text.SimpleDateFormat
import java.util.*
import com.r0adkll.slidr.model.SlidrPosition
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.Slidr


class PracticingGratitudeFragment : Fragment() {

    private lateinit var fragmentActivity: FragmentActivity
    private var countOfRecords: Int = 1
    private var fragmentTop: FillRecordFragment? = null
    private var fragmentBottom: FillRecordFragment? = null
    var slidrInterface: SlidrInterface? = null
    private lateinit var tempView: View

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
        tempView = v

        val formatForDateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val currentDate = formatForDateNow.format(Date())
        v.tvDate.text = currentDate

        createFillingTopFragment()
        createFillingBottomFragment()

        v.btnSave.setOnClickListener {
            fragmentTop?.saveRecord()
            fragmentBottom?.saveRecord()
            activity?.finish()
        }

        return v
    }

//    override fun onResume() {
//        super.onResume()
//        if (slidrInterface == null)
//            slidrInterface = Slidr.replace(
//                tempView.findViewById(R.id.contentContainer),
//                SlidrConfig.Builder().position(SlidrPosition.LEFT).build()
//            )
//        println(2)
//    }


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