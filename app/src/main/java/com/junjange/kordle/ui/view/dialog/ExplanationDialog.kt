package com.junjange.kordle.ui.view.dialog

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.junjange.kordle.R
import com.junjange.kordle.databinding.ActivityExplanationDialogBinding

class ExplanationDialog : AppCompatActivity() {
    private lateinit var binding: ActivityExplanationDialogBinding
    private var pagerAdapter: PagerAdapter? = null
    private lateinit var dots: Array<TextView?>
    private lateinit var layouts: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_explanation_dialog)
        setContentView(binding.root)
        binding.dialog = this
        binding.lifecycleOwner = this



        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게


        // 변화될 레이아웃들 주소
        // 원하는 경우 레이아웃을 몇 개 더 추가
        layouts = intArrayOf(
            R.layout.page0,
            R.layout.page1,
        )

        // 하단 점 추가
        addBottomDots(0)

        pagerAdapter = PagerAdapter()
        binding.viewPager.setAdapter(pagerAdapter)
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        binding.clear.setOnClickListener {
            finish()
        }




    }
    // 하단 점(선택된 점, 선택되지 않은 점) 구현
    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size) // 레이아웃 크기만큼 하단 점 배열에 추가
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        binding.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226;")
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(colorsInactive[currentPage])
            binding.layoutDots.addView(dots[i])
        }
        if (dots.size > 0) dots[currentPage]?.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(): Int {
        return binding.viewPager.currentItem + 1
    }

    // 뷰페이저 변경 리스너
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object :
        ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)

        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    // 알림 표시줄을 투명하게 만들기
    private fun changeStatusBarColor() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    // 호출기 어댑터
    inner class PagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}