package com.example.test

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ResultGanghwaBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.text.DecimalFormat


class ResultGanghwaActivity : AppCompatActivity() {
    private val binding by lazy { ResultGanghwaBinding.inflate(layoutInflater) }
    val dec_up = DecimalFormat("#,###")
    val dec_100million = DecimalFormat("#.#억")
    val dec_textPer = DecimalFormat("#회")

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.textPer.clearFocus()
        binding.textPerTry.clearFocus()
        binding.textTimeTry.clearFocus()

        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //이러한 형식의 디자인 패턴은 MVC(model-view-controller 라고 해야 할듯?)

        val ganghwaFrom = intent.getStringExtra("ganghwaFrom")
        val ganghwaAfter = intent.getStringExtra("ganghwaAfter")
        val juStat = intent.getStringExtra("juStat")
        val gongMa = intent.getStringExtra("gongMa")
        val ganghwaResult = intent.getParcelableExtra<MainActivity.GanghwaResult>("ganghwaResult")
        var average = 0.0
        var averageBoom = 0.0
        var averageMeso = 0.0
        val gab = ganghwaAfter?.toInt()?.minus(ganghwaFrom?.toInt()!!)
        var textPer0 = binding.textPer.text.toString()
        var textPerTry0 = binding.textPerTry.text.toString()

        if (ganghwaResult != null) {
            binding.startGang.text = ganghwaFrom.toString()
            binding.endGang.text = ganghwaAfter.toString()
            average = ganghwaResult.average
            averageBoom = ganghwaResult.averageboom
            averageMeso = ganghwaResult.averageMeso
            setData(binding.chart, ganghwaResult.array, average)
            binding.textAverage.text = average.toString()
            binding.textBoom.text = averageBoom.toString()
            binding.textMeso.text = dec_up.format(averageMeso).toString()
            binding.textMpd.text = (((gab)?.times(6))?.div(averageMeso)?.times(100000000)).toString()
            binding.textJuStat.text = juStat.toString()
            binding.textGongMa.text = gongMa.toString()
        }

        if (ganghwaResult != null) {
            var ganghwaTimes = 0
            var ganghwaHowMoney = 0.0
            var ganghwaTryTimes = 0
            var ganghwaTryHowTimes = 0.0
            for (i in ganghwaResult.arrayTryMeso) {
                ganghwaTimes += i
                ganghwaHowMoney += 1
                if (ganghwaTimes > textPer0.toInt() * 1000) {
                    break
                }
                Log.d(ganghwaHowMoney.toString(), i.toString())
            }
            for (i in ganghwaResult.array) {
                ganghwaTryTimes += i
                ganghwaTryHowTimes += 1
                if (ganghwaTryTimes > textPerTry0.toInt() * 1000) {
                    break
                }
            }
            Log.d("ganghwaTimes", ganghwaTimes.toString())
            binding.textHowMoney.text = dec_100million.format(ganghwaHowMoney / 10)
            binding.textTimeTry.text = dec_textPer.format(ganghwaTryHowTimes)
//          binding.textPer.text = dec_textPer.format(textPer0.toInt()) // EditText라서 안의 내용을 바꾸고싶은데 안되는듯;;.
        }

        if(binding.textPer != null){
            binding.textPer.addTextChangedListener(object : TextWatcher {
                var ganghwaTimes = 0
                var ganghwaHowMoney = 0.0
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    //입력하기 전
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    //EditText에 변화가 있을 때
                }

                override fun afterTextChanged(s: Editable) {
                    Log.d("s값은 ", s.toString())
                    if (ganghwaResult != null) {
                        if(s.toString() >= (100).toString()){
                            textPer0 = (100).toString()
                        }
                        if(s.isEmpty()){
                            textPer0 = (0).toString()
                            binding.textHowMoney.text = "0억"
                        }
                        else{
                            textPer0 = s.toString()
                            for (i in ganghwaResult.arrayTryMeso) {
                                ganghwaTimes += i
                                ganghwaHowMoney += 1
                                if (ganghwaTimes > textPer0.toInt() * 1000) {
                                    break
                                }
                            }
                            binding.textHowMoney.text = dec_100million.format(ganghwaHowMoney / 10)
                        }
                        Log.d("textPer값은 ", textPer0)
                        Log.d("ganghwaTimes", ganghwaTimes.toString())
                        Log.d("ganghwaHowMoney", ganghwaHowMoney.toString())
                        ganghwaTimes = 0
                        ganghwaHowMoney = 0.0
//                  binding.textPer.text = dec_textPer.format(textPer0.toInt()) // EditText라서 안의 내용을 바꾸고싶은데 안되는듯;;
                    }
                }
            });
        }
        if(binding.textPerTry != null) {
            binding.textPerTry.addTextChangedListener(object : TextWatcher {
                var ganghwaTryTimes = 0
                var ganghwaTryHowTimes = 0.0
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(s: Editable) {
                    Log.d("s값은 ", s.toString())
                    if (ganghwaResult != null) {
                        if (s.toString() >= (100).toString()) {
                            textPerTry0 = (100).toString()
                        }
                        if (s.isEmpty()) {
                            textPerTry0 = (0).toString()
                            binding.textTimeTry.text = "0회"
                        } else {
                            textPerTry0 = s.toString()
                            for (i in ganghwaResult.array) {
                                ganghwaTryTimes += i
                                ganghwaTryHowTimes += 1
                                if (ganghwaTryTimes > textPerTry0.toInt() * 1000) {
                                    break
                                }
                            }
                            binding.textTimeTry.text = dec_textPer.format(ganghwaTryHowTimes)
                        }
                        ganghwaTryTimes = 0
                        ganghwaTryHowTimes = 0.0
//                  binding.textPer.text = dec_textPer.format(textPer0.toInt()) // EditText라서 안의 내용을 바꾸고싶은데 안되는듯;;
                    }
                }
            })
        }

        setContentView(binding.root)
    }

    private fun setData(barChart: BarChart, array: ArrayList<Int>, average: Double) {
        // Zoom In / Out 가능 여부 설정
        barChart.setScaleEnabled(false)
        // pinch zoom 가능 (손가락으로 확대축소하는거)
        barChart.setPinchZoom(true)
        val valueList = ArrayList<BarEntry>()

        // 데이터 배열을 BarEntry로 변환
        for ((index, value) in array.withIndex()) {
            valueList.add(BarEntry(index.toFloat(), value.toFloat()))
        }

        val barDataSet = BarDataSet(valueList, null)
        barDataSet.setColors(Color.rgb(255, 145, 0))

        val data = BarData(barDataSet)

        val description = Description()
        description.isEnabled = false
        barChart.description = description

        val xAxis = barChart.xAxis
        xAxis.axisMaximum = average.toFloat() * 7f
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis = barChart.axisLeft
        leftAxis.axisMinimum = 0f // y축의 최소값을 0으로 설정
        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        val limitLine = LimitLine(average.toFloat(), "평균")
        limitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        limitLine.lineColor = Color.GRAY
        limitLine.lineWidth = 2f
        xAxis.addLimitLine(limitLine)

        barChart.data = data
        barChart.invalidate()
    }
}