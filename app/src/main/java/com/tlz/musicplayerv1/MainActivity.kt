package com.tlz.musicplayerv1

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
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
            R.raw.StandOut
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

            handler.postDelayed(UpdateSongTime, 1)
        }
    }

    val UpdateSongTime: Runnable = object : Runnable {
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