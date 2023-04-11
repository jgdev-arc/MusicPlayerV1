package com.tlz.musicplayerv1

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // variables
    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0

    // Handler
    var handler: Handler = Handler()

    // Media Player
    var mediaPlayer = MediaPlayer()
    lateinit var timeTxt: TextView
    lateinit var seekBar: SeekBar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playBtn: Button = findViewById(R.id.playBtn)
        val stopBtn: Button = findViewById(R.id.pauseBtn)
        val forwardBtn: Button = findViewById(R.id.forwardBtn)
        val backBtn: Button = findViewById(R.id.backBtn)

        val titleTxt : TextView = findViewById(R.id.songTitle)
        timeTxt  =findViewById(R.id.timeLeftTv)

        seekBar  = findViewById(R.id.seekBar)



        // Media Player
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.xfl
        )

        seekBar.isClickable = false

        playBtn.setOnClickListener() {
            mediaPlayer.start()

            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.duration.toDouble()

            if (oneTimeOnly == 0) {
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            timeTxt.text = startTime.toString()
            seekBar.setProgress(startTime.toInt())

            handler.postDelayed(updateSongTime, 1)
        }

        titleTxt.text = "" + resources.getResourceEntryName(R.raw.xfl)

        stopBtn.setOnClickListener {
            mediaPlayer.pause()
        }

        forwardBtn.setOnClickListener() {
            var temp = startTime

            if ((temp + forwardTime) <= finalTime) {
                startTime = startTime + forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(this, "Cannot jump ahead of the track length", Toast.LENGTH_LONG).show()
            }
        }

        backBtn.setOnClickListener() {
            var temp = startTime.toInt()
            if ((temp - backwardTime) > 0) {
                startTime = startTime - backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(this, "Cannot jump backwards", Toast.LENGTH_LONG).show()
            }
        }


    }

    val updateSongTime: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            timeTxt.text = "" +
                    String.format(
                        "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()
                            - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    startTime.toLong()
                                )
                            ))
                    )

            seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 1)
        }
    }

}