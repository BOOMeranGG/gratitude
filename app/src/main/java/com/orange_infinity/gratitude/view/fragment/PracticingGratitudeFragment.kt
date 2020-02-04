package com.orange_infinity.gratitude.view.fragment

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.entities.Record
import com.orange_infinity.gratitude.useCase.RecordEntityManager
import com.r0adkll.slidr.model.SlidrInterface
import kotlinx.android.synthetic.main.practicing_gratitude_fragment.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.text.SimpleDateFormat
import java.util.*

class PracticingGratitudeFragment : Fragment(), KeyboardVisibilityEventListener {

    private lateinit var fragmentActivity: FragmentActivity
    private var countOfRecords: Int = 1
    private var fragmentTop: FillRecordFragment? = null
    private var fragmentBottom: FillRecordFragment? = null
    var slidrInterface: SlidrInterface? = null

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

        val display = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val height = size.y
        val mainLayout = v.findViewById<LinearLayout>(R.id.layoutMain)
        mainLayout.layoutParams.height =
            height - v.layoutDate.height - pxFromDp(mainLayout.marginTop.toFloat()).toInt() - getStatusBarHeight()


        createFillingTopFragment()
        createFillingBottomFragment()

        v.btnSave.setOnClickListener {
            val record1 = fragmentTop?.saveRecord()
            val record2 = fragmentBottom?.saveRecord()

            // TODO("Повтор, вынести в отдельный метод")
            if (record1 == null && record2 == null) {
                return@setOnClickListener
            }
            val saveRecord: Record = record1 ?: Record()
            saveRecord.date = record1?.date ?: record2?.date
            saveRecord.descriptionSecond = record2?.description ?: ""
            saveRecord.imageNameSecond = record2?.imageName
            saveRecord.soundNameSecond = record2?.soundName
            RecordEntityManager().save(activity!!, saveRecord)

            activity?.finish()
        }
        KeyboardVisibilityEvent.setEventListener(activity, this)

        return v
    }

    override fun onVisibilityChanged(isKeyboardOpen: Boolean) {
        if (isKeyboardOpen) {
//            scroll_view.scrollTo(0, scroll_view.bottom)
//            create_account_text.visibility = View.GONE
        } else {
//            scroll_view.scrollTo(0, scroll_view.top)
//            create_account_text.visibility = View.VISIBLE
        }
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
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

    private fun dpFromPx(px: Float): Float {
        return px / activity?.applicationContext?.resources?.displayMetrics?.density!!
    }

    private fun pxFromDp(dp: Float): Float {
        return dp * activity?.applicationContext?.resources?.displayMetrics?.density!!
    }
}