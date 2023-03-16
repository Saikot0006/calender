package com.example.myapplication

import android.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityMainBinding
    val events = mutableListOf<EventDay>()
    private var selectedDate : Long  = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val calender = Calendar.getInstance();
        selectedDate = System.currentTimeMillis()



        EventDB.getDB(application).getDao().getAllData().observe(this) {
            val str = StringBuilder()
            it.forEach {
                Log.e("name", "onCreate: "+it.name )
                str.append(it.name+"\n")

                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date
                events.add(EventDay(cal, R.drawable.alert_dark_frame))

            }
          //  calender.set(2023,2,22)
          //  calender.set(2023,2,20)
           // events.add(EventDay(calender, R.drawable.alert_dark_frame))
            Log.e("eventsSize", "onCreate: "+events.size )
            binding.calendarView.setEvents(events)
            binding.calenderTV.text = str
        }

        binding.buttonSave.setOnClickListener {
            var eventName = binding.editText.text.toString()
            var evenModel = EvenModel(name = eventName, date = selectedDate)
            Log.e("eventName", "onDayClick: "+evenModel.toString() )

            CoroutineScope(Dispatchers.Main).launch {
                EventDB.getDB(application).getDao().insertEvent(evenModel)

            }
        }


        binding.calendarView.setOnDayClickListener(object : OnDayClickListener{
            override fun onDayClick(eventDay: EventDay) {
                binding.calendarView.setDate(eventDay.calendar)
                //calendarView.setCurrentDate(CalendarDay.from(year, month, day), true);
                Log.e("calendar", "onDayClick: "+eventDay.calendar.timeInMillis )
                selectedDate = eventDay.calendar.timeInMillis
            }
        })


        //calendar.set(2023, 3, 5)
       // calendar.timeInMillis = System.currentTimeMillis()

      //  events.add(EventDay(calendar, R.drawable.alert_dark_frame))

      //  binding.calendarView.setEvents(events)

        setContentView(binding.root)

    }


}