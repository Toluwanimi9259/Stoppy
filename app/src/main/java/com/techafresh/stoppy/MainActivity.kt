package com.techafresh.stoppy

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var timeSelected : Int = 0
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffSet: Long = 0
    private var isStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addBtn: ImageView = findViewById(R.id.btnAdd)
        addBtn.setOnClickListener {
            setTimeFunction()
        }
        val startBtn:Button = findViewById(R.id.btnPlayPause)
        startBtn.setOnClickListener {
            startTimerSetup()
        }

        val stopBtn:Button = findViewById(R.id.btnStop)
        stopBtn.setOnClickListener {
            resetTime()
        }

        val holdBtn : Button = findViewById(R.id.holdBtn)
        holdBtn.setOnClickListener {
            timePause()
        }

        val addTimeTv:TextView = findViewById(R.id.tv_addTime)
        addTimeTv.setOnClickListener {
            addExtraTime()
        }
    }

    private fun addExtraTime()
    {
        val progressBar :ProgressBar = findViewById(R.id.pbTimer)
        if (timeSelected!=0)
        {
            timeSelected += 15
            progressBar.max = timeSelected
            timePause()
            startTimer(pauseOffSet)
            Toast.makeText(this,"15 sec added",Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetTime()
    {
        if (timeCountDown!=null)
        {
            timeCountDown!!.cancel()
            timeProgress=0
            timeSelected=0
            pauseOffSet=0
            timeCountDown=null
            isStart = true
            val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
            progressBar.progress = 0
            val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
            timeLeftTv.text = "0s"
        }
    }

    private fun timePause()
    {
        if (timeCountDown!=null)
        {
            timeCountDown!!.cancel()
        }
    }

    private fun startTimerSetup()
    {
        val startBtn: Button = findViewById(R.id.btnPlayPause)
        if (timeSelected>timeProgress)
        {
//            startBtn.text = "Pause"
            startTimer(pauseOffSet)
        }
        else
        {
            Toast.makeText(this,"Enter Time",Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(pauseOffSetL: Long)
    {
        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
        progressBar.progress = timeProgress
        timeCountDown = object :CountDownTimer(
            (timeSelected*1000).toLong() - pauseOffSetL*1000, 1000)
        {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffSet = timeSelected.toLong()- p0/1000
                progressBar.progress = timeSelected-timeProgress
                val timeLeftTv:TextView = findViewById(R.id.tvTimeLeft)
                timeLeftTv.text = (timeSelected - timeProgress).toString()+ "s"
            }

            override fun onFinish() {
                resetTime()
                Toast.makeText(this@MainActivity,"Times Up!", Toast.LENGTH_SHORT).show()
            }

        }.start()
    }


    private fun setTimeFunction()
    {
        val timeDialog = Dialog(this)
        timeDialog.setContentView(R.layout.add_dialog)
        val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
        val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
        timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            if (timeSet.text.isEmpty())
            {
                Toast.makeText(this,"Enter Time Duration",Toast.LENGTH_SHORT).show()
            }
            else
            {
                resetTime()
                timeLeftTv.text = timeSet.text
                timeSelected = timeSet.text.toString().toInt()
                progressBar.max = timeSelected
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(timeCountDown!=null)
        {
            timeCountDown?.cancel()
            timeProgress=0
        }

    }
}