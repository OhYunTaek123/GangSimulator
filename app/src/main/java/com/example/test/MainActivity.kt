package com.example.test

import LoadingDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import com.example.test.databinding.ActivityMainBinding
import kotlin.math.round
import kotlin.random.Random
import kotlin.math.*

class MainActivity : AppCompatActivity() {
    data class GanghwaResult(val average: Double, val averageboom: Double, val averageMeso: Double, val array: ArrayList<Int>, val arrayTryMeso: ArrayList<Int>) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readArrayList(Int::class.java.classLoader) as ArrayList<Int>,
            parcel.readArrayList(Int::class.java.classLoader) as ArrayList<Int>
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeDouble(average)
            parcel.writeDouble(averageboom)
            parcel.writeDouble(averageMeso)
            parcel.writeList(array)
            parcel.writeList(arrayTryMeso)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GanghwaResult> {
            override fun createFromParcel(parcel: Parcel): GanghwaResult {
                return GanghwaResult(parcel)
            }

            override fun newArray(size: Int): Array<GanghwaResult?> {
                return arrayOfNulls(size)
            }
        }
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.ganghwaFrom.clearFocus()
        binding.ganghwaAfter.clearFocus()
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var gangItemLevel = 130
        var gangEventSunday = 0
        var starCatchIsToggled = false
        var pabang15IsToggled = false
        var pabang16IsToggled = false
        var levelStat = 0
        val pabang15 = findViewById<Button>(R.id.pabang15)
        val pabang16 = findViewById<Button>(R.id.pabang16)

        val itemLevel = resources.getStringArray(R.array.level_spinner_array)
        val eventSunday = resources.getStringArray(R.array.event_spinner_array)
        val levelSpinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemLevel)
        val eventSpinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventSunday)
        binding.levelSpinner.adapter = levelSpinnerAdapter
        binding.eventSpinner.adapter = eventSpinnerAdapter

        binding.levelSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0-> {
                        gangItemLevel = 130
                        levelStat = 0
                    }
                    1-> {
                        gangItemLevel = 135
                        levelStat = 0
                    }
                    2-> {
                        gangItemLevel = 140
                        levelStat = 1
                    }
                    3-> {
                        gangItemLevel = 150
                        levelStat = 2
                    }
                    4-> {
                        gangItemLevel = 160
                        levelStat = 3
                    }
                    5-> {
                        gangItemLevel = 200
                        levelStat = 5
                    }
                    6-> {
                        gangItemLevel = 250
                        levelStat = 7
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                gangItemLevel = 130
            }

        }

        binding.eventSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gangEventSunday = p2
                if(gangEventSunday == 1 || gangEventSunday == 3){
                    if(pabang15IsToggled){
                        pabang15IsToggled = !pabang15IsToggled
                        binding.pabang15.isChecked = false
                    }
                    pabang15.setEnabled(false)
                    Log.d("파방15", pabang15IsToggled.toString())
                    //
                }
                else{
                    pabang15.setEnabled(true)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                gangEventSunday = 0
            }

        }

        binding.doStarcatch.setOnClickListener{
            starCatchIsToggled = !starCatchIsToggled
        }
        pabang15.setOnClickListener{
            pabang15IsToggled = !pabang15IsToggled
        }
        pabang16.setOnClickListener{
            pabang16IsToggled = !pabang16IsToggled
        }
        //
        binding.calculStart.setOnClickListener{
            val intent = Intent(this, ResultGanghwaActivity::class.java)

            val ganghwaFrom0 = binding.ganghwaFrom.text.toString()
            val ganghwaAfter0 = binding.ganghwaAfter.text.toString()
            val ganghwaFrom1 = ganghwaFrom0.toInt()
            val ganghwaAfter1 = ganghwaAfter0.toInt()
            val resultCalcul = letsGanghwa(ganghwaFrom1, ganghwaAfter1, gangItemLevel, starCatchIsToggled, gangEventSunday, pabang15IsToggled, pabang16IsToggled)
            val (juStat, gongMa) = howMuchStrong(levelStat, ganghwaFrom1, ganghwaAfter1)
            intent.putExtra("ganghwaResult", resultCalcul)
            intent.putExtra("ganghwaFrom", binding.ganghwaFrom.text.toString())
            intent.putExtra("ganghwaAfter", binding.ganghwaAfter.text.toString())
            intent.putExtra("juStat", juStat.toString())
            intent.putExtra("gongMa", gongMa.toString())
            Log.d("주스텟", juStat.toString())
            Log.d("공마", gongMa.toString())

            startActivity(intent)
            //해당 작업을 비동기 처리로 작업하면 속도가 빨라질것 같은데 -> 이미 비동기 처리라고 하네;; intent를 통하여 보내는 데이터가 너무 많아서 느려지는듯
            //액티비티로 데이터를 보내지 말고 프래그먼트를 통하여 데이터를 보내면 속도가 빨라질 것 같다!!! jetpack navigation을 사용해 봐서 토이프로젝트 하나 해보자!
            //fragment간 데이터 전달 방법 1.jetpack navigation 2.result api 3.bundle을 이용
            //
        }
    }
    fun howMuchStrong(levelStat: Int, ganghwaFrom1: Int, ganghwaAfter1: Int): Pair<Int, Int>{
        val difFromAfter = ganghwaAfter1 - ganghwaFrom1
        var tempNowGang = ganghwaFrom1
        var juStat = 0
        var gongMa = 0
        repeat(difFromAfter){
            val (juStat1, gongMa1) = calStrong(levelStat, tempNowGang, juStat, gongMa)
            juStat = juStat1
            gongMa = gongMa1
            tempNowGang++
        }
        return Pair(juStat, gongMa)
    }

    fun calStrong(levelStat: Int, tempNowGang: Int, juStat: Int, gongMa: Int): Pair<Int, Int> {
        var juStatCal = juStat
        var gongMaCal = gongMa
        when(tempNowGang){
            12, 13, 14 -> {
                juStatCal += 3
            }
            15, 16, 17, 18, 19 -> {
                juStatCal += 7 + levelStat * 2
                gongMaCal += tempNowGang + levelStat - 8
                if(levelStat == 5 || levelStat == 7) juStatCal -= 2
            }
            20 -> {
                juStatCal += 7 + levelStat * 2
                gongMaCal += 12 + levelStat
            }
            21 -> {
                juStatCal += 7 + levelStat * 2
                gongMaCal += 14 + levelStat
            }
            22 -> {
                juStatCal += 7 + levelStat * 2
                gongMaCal += 16 + levelStat
            }
            23 -> {
                juStatCal += 7 + levelStat * 2
                gongMaCal += 18 + levelStat
            }
        }
        return Pair(juStatCal, gongMaCal)
    }

    fun letsGanghwa(now: Int, arr: Int, level: Int, star: Boolean, sunEvent: Int, pabang15: Boolean, pabang16: Boolean): GanghwaResult {
        val random = Random(System.currentTimeMillis())
        val array = ArrayList<Int>()
        val arrayTryMeso = ArrayList<Int>()
        var sum = 0
        var sumboom = 0
        var averageMeso = 0.0
        var summaMeso = 0

        repeat(100000) {
            val (trytime, boom, useMeso) = trygang(now, arr, random, level, star, sunEvent, pabang15, pabang16)
            summaMeso = (useMeso / 10000000).toInt()
            if(trytime>=array.size){
                for(i in array.size until trytime +1){
                    array.add(0)
                }
            }
            if(summaMeso>=arrayTryMeso.size){
                for(i in arrayTryMeso.size until summaMeso +1){
                    arrayTryMeso.add(0)
                }
            }
            //
            array[trytime]++
            arrayTryMeso[summaMeso]++
            sum += trytime
            sumboom += boom
            averageMeso += round(useMeso / 100000)
        }

        val average = round(sum.toDouble() / 1000) / 100
        val averageboom = round(sumboom.toDouble() / 1000) / 100

        return GanghwaResult(average, averageboom, averageMeso, array, arrayTryMeso)
    }

    fun trygang(now: Int, arr: Int, random: Random, level: Int, star: Boolean, sunEvent: Int, pabang15: Boolean, pabang16: Boolean): Triple<Int, Int, Double> {
        var current = now
        var usedMeso = 0.0
        var trytime = 0
        var boom = 0
        val levelpow = level.toDouble().pow(3)
        val failArray = ArrayDeque<Int>()
        failArray.addFirst(current)
        failArray.addFirst(current)

        while (current < arr) {
            trytime++
            val currentlevel = current +1
            val currentpow = round(currentlevel.toDouble().pow(2.7))
            when(sunEvent){
                1 -> {
                    if(current == 15){
                        usedMeso += round(1000 + (levelpow * currentpow / 200))
                        failArray.addFirst(12.toInt())
                        current++
                        continue
                    }
                }
                3 -> {
                    if(current == 15){
                        usedMeso += round(1000 + (levelpow * currentpow / 200)) * 1.43
                        failArray.addFirst(12.toInt())
                        current++
                        continue
                    }
                }
                else -> {

                }
            }
            when (current) {
                12 -> {
                    usedMeso += round(1000 + (levelpow * currentpow / 150))
                    if(star){
                        if (starGanghwa12(random.nextInt(100))) {
                            current++
                        }
                    } else {
                        if (ganghwa12(random.nextInt(100))) {
                            current++
                        }
                    }

                }
                13 -> {
                    usedMeso += round(1000 + (levelpow * currentpow / 110))
                    if(star){
                        if (starGanghwa13(random.nextInt(10000))) {
                            current++
                        }
                    } else {
                        if (ganghwa13(random.nextInt(100))) {
                            current++
                        }
                    }
                }
                14 -> {
                    usedMeso += round(1000 + (levelpow * currentpow / 75))
                    if(star){
                        if (starGanghwa(random.nextInt(1000))) {
                            current++
                        }
                    } else {
                        if (ganghwa(random.nextInt(100))) {
                            current++
                        }
                    }
                }
                15, 16, 17 -> {
                    usedMeso += round(1000 + (levelpow * currentpow / 200))
                    if(pabang15){
                        if(sunEvent == 2 || sunEvent == 3){
                            usedMeso += round(1000 + (levelpow * currentpow / 200)) * 1.42
                        } else {
                            usedMeso += round(1000 + (levelpow * currentpow / 200))
                        }
                    }
                    if(pabang16){
                        if(sunEvent == 2 || sunEvent == 3){
                            usedMeso += round(1000 + (levelpow * currentpow / 200)) * 1.42
                        } else {
                            usedMeso += round(1000 + (levelpow * currentpow / 200))
                        }
                    }
                    if(star){
                        if (starGanghwa(random.nextInt(1000))) {
                            current++
                        } else {
                            if(current != 15){
                                failArray.addFirst(current)
                            }

                            if (gangfail(random.nextInt(700))) {
                                if (current != 15) {
                                    current--
                                }
                            } else {
                                if(pabang15){
                                    if (current == 15) {
                                        continue
                                    }
                                }
                                if(pabang16){
                                    if (current == 16) {
                                        current--
                                        continue
                                    }
                                }
                                boom++
                                current = 12
                            }
                        }
                    } else {
                        if (ganghwa(random.nextInt(100))) {
                            current++
                        } else {
                            if(current != 15){
                                failArray.addFirst(current)
                            }
                            if (gangfail(random.nextInt(700))) {
                                if (current != 15) {
                                    current--
                                }
                            } else {
                                boom++
                                current = 12
                            }
                        }
                    }
                }
                18, 19 -> {
                    usedMeso += round(1000 + (levelpow * currentpow / 200))
                    if(star){
                        if (starGanghwa(random.nextInt(1000))) {
                            current++
                        } else {
                            failArray.addFirst(current)
                            if (gangfail18(random.nextInt(700))) {
                                current--
                            } else {
                                boom++
                                current = 12
                            }
                        }
                    } else {
                        if (ganghwa(random.nextInt(100))) {
                            current++
                        } else {
                            failArray.addFirst(current)
                            if (gangfail18(random.nextInt(700))) {
                                current--
                            } else {
                                boom++
                                current = 12
                            }
                        }
                    }
                }
                20, 21 -> {
                    usedMeso += round(1000 + (levelpow * currentpow / 200))
                    if(star){
                        if (starGanghwa(random.nextInt(1000))) {
                        current++
                    } else {
                            if(current != 20){
                                failArray.addFirst(current)
                            }
                        if (gangfail20(random.nextInt(700))) {
                            if (current != 20) {
                                current--
                            }
                        } else {
                            boom++
                            current = 12
                        }
                    }

                    } else {
                        if (ganghwa(random.nextInt(100))) {
                            current++
                        } else {
                            if(current != 20){
                                failArray.addFirst(current)
                            }
                            if (gangfail20(random.nextInt(700))) {
                                if (current != 20) {
                                    current--
                                }
                            } else {
                                boom++
                                current = 12
                            }
                        }
                    }
                }
                else -> {
                    usedMeso += round(1000 + (levelpow * currentpow / 200))
                    if(star){
                        if (starGanghwa22(random.nextInt(10000))) {
                            current++
                        } else {
                            failArray.addFirst(current)
                            if (gangfail22(random.nextInt(700))) {
                                current--
                            } else {
                                boom++
                                current = 12
                            }
                        }

                    } else {
                        if (ganghwa22(random.nextInt(100))) {
                            current++
                        } else {
                            failArray.addFirst(current)
                            if (gangfail22(random.nextInt(700))) {
                                current--
                            } else {
                                boom++
                                current = 12
                            }
                        }
                    }
                }
            }
            if(failArray.elementAt(0) == failArray.elementAt(1) - 1){
                current++
                usedMeso += round(1000 + (levelpow * currentpow / 200))
                failArray.clear()
                failArray.addFirst(current)
                failArray.addFirst(current)
            } // 찬스타임 로직
        }
        when(sunEvent){
            2, 3 -> {
                usedMeso *= 0.7
            }
            else -> {

            }
        }
        return Triple(trytime, boom, usedMeso)
    }

    fun ganghwa(nansu: Int): Boolean {
        return nansu < 30
    }
    fun starGanghwa(nansu: Int): Boolean {
        return nansu < 315
    }
    fun ganghwa1(nansu: Int): Boolean {
        return nansu < 95
    }
    fun starGanghwa1(nansu: Int): Boolean {
        return nansu < 9975
    }
    fun ganghwa2(nansu: Int): Boolean {
        return nansu < 90
    }
    fun starGanghwa2(nansu: Int): Boolean {
        return nansu < 945
    }
    fun ganghwa3(nansu: Int): Boolean {
        return nansu < 85
    }
    fun starGanghwa3(nansu: Int): Boolean {
        return nansu < 8925
    }
    fun ganghwa22(nansu: Int): Boolean {
        return nansu < 3
    }
    fun starGanghwa22(nansu: Int): Boolean {
        return nansu < 315
    }

    fun ganghwa13(nansu: Int): Boolean {
        return nansu < 35
    }
    fun starGanghwa13(nansu: Int): Boolean {
        return nansu < 3675
    }

    fun ganghwa12(nansu: Int): Boolean {
        return nansu < 40
    }
    fun starGanghwa12(nansu: Int): Boolean {
        return nansu < 42
    }

    fun gangfail(nansu: Int): Boolean {
        return nansu < 679
    }

    fun gangfail18(nansu: Int): Boolean {
        return nansu < 672
    }

    fun gangfail20(nansu: Int): Boolean {
        return nansu < 630
    }
    fun gangfail22(nansu: Int): Boolean {
        return nansu < 27
    }
}