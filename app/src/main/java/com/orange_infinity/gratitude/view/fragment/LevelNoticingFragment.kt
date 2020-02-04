package com.orange_infinity.gratitude.view.fragment

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orange_infinity.gratitude.R
import kotlinx.android.synthetic.main.level_noticing_fragment.view.*
import com.r0adkll.slidr.model.SlidrInterface
import kotlinx.android.synthetic.main.level_noticing_fragment.*
import kotlinx.android.synthetic.main.level_noticing_fragment.view.btnSave
import kotlinx.android.synthetic.main.practicing_gratitude_fragment.view.*

class LevelNoticingFragment : Fragment() {

    private lateinit var fragmentActivity: FragmentActivity
    private var countOfRecords: Int = 1
    private var fragmentTop: FillRecordFragment? = null
    private var fragmentBottom: FillRecordFragment? = null
    var slidrInterface: SlidrInterface? = null

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

        val display = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val height = size.y
        val mainLayout = v.findViewById<LinearLayout>(R.id.layoutMain)
        mainLayout.layoutParams.height =
            height - v.layoutTop.height - pxFromDp(mainLayout.marginTop.toFloat()).toInt() -
                    pxFromDp(v.layoutTop.marginTop.toFloat()).toInt() - getStatusBarHeight()

        createFillingTopFragment()
        createFillingBottomFragment()

        v.btnSave.setOnClickListener {
            fragmentTop?.saveRecord()
            fragmentBottom?.saveRecord()
            activity?.finish()
        }

        return v
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
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

    private fun dpFromPx(px: Float): Float {
        return px / activity?.applicationContext?.resources?.displayMetrics?.density!!
    }

    private fun pxFromDp(dp: Float): Float {
        return dp * activity?.applicationContext?.resources?.displayMetrics?.density!!
    }
}