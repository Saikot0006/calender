package com.example.myapplication

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityMainBinding
    val events = mutableListOf<EventDay>()
    private var selectedDate : Long  = 0
    private lateinit var eventList : ArrayList<EvenModel>
    private lateinit var caledarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        selectedDate = System.currentTimeMillis()
        autoSelectDate(Calendar.getInstance())
        eventList = ArrayList()
        caledarAdapter = CalendarAdapter()

        EventDB.getDB(application).getDao().getAllData().observe(this) {
            it.forEach {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date
                events.add(EventDay(cal, R.drawable.alert_dark_frame))
            }
            binding.calendarView.setEvents(events)

        }

        binding.calendarRV.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = caledarAdapter
        }

        binding.buttonSave.setOnClickListener {
            var eventName = binding.editText.text.toString()
            var evenModel = EvenModel(name = eventName, date = selectedDate)

            CoroutineScope(Dispatchers.Main).launch {
                EventDB.getDB(application).getDao().insertEvent(evenModel)

            }
        }

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener{
            override fun onDayClick(eventDay: EventDay) {
                binding.calendarView.setDate(eventDay.calendar)
                selectedDate = eventDay.calendar.timeInMillis
                val cal = Calendar.getInstance()
                cal.timeInMillis = selectedDate
                Log.e("selectedDate", "onDayClick: "+selectedDate )


                EventDB.getDB(applicationContext).getDao().getDataByDate(selectedDate).observe(this@MainActivity) {
                    if(it.size > 0){
                        caledarAdapter.submitList(it)
                        Log.e("event", "onDayClick: No Event" )
                        binding.calendarRV.visibility = View.VISIBLE
                        binding.calenderTV.visibility = View.GONE

                    }else{
                        binding.calendarRV.visibility = View.GONE
                        binding.calenderTV.visibility = View.VISIBLE
                        Log.e("event", "onDayClick: No Event" )
                    }

                   // eventList = it
                    Log.e("selectedDate", "onDayClick: "+it.size.toString() )
                }

                autoSelectDate(cal)

            }
        })



        setContentView(binding.root)

    }

    private fun autoSelectDate(cal: Calendar) {
        val list = listOf(
            CalendarDay(cal).apply {
                labelColor = R.color.black
                backgroundResource  = R.color.holo_blue_light

            }
        )

        binding.calendarView.setCalendarDays(list)
    }

}