package com.example.englishlearning.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.style.TabStopSpan
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishlearning.R
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.Audition
import com.example.englishlearning.models.Auditions
import com.example.englishlearning.models.CompletedAuditions
import com.example.englishlearning.models.Links
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class AuditionDetailFragment : Fragment() {



    private val args : AuditionDetailFragmentArgs by navArgs()
    private lateinit var numberAud : TextView
    private lateinit var taskAud : TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var currentTime : TextView
    private lateinit var timeOfAudio : TextView
    private lateinit var playStopImage : ImageView
    private lateinit var seekBar : SeekBar
    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var auditionTask : ConstraintLayout
    private lateinit var answerField : EditText
    private lateinit var answerButton : AppCompatButton
    private var handler: Handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.VISIBLE
        auditionTask.visibility = View.INVISIBLE

        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuilder.getAudition(args.idAud)
        requestCall.enqueue(object : Callback<Auditions>{
            override fun onResponse(call: Call<Auditions>, response: Response<Auditions>) {
                if (response.isSuccessful){
                    numberAud.text = "Задание №${response.body()!!.id}"
                    taskAud.text = response.body()!!.taskText
                    //progressBar.visibility = View.INVISIBLE
                    getAudio(response.body()!!.id)
                }
            }

            override fun onFailure(call: Call<Auditions>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })


        playStopImage.setOnClickListener {
            if (mediaPlayer.isPlaying){
                handler.removeCallbacks(runnable)
                mediaPlayer.pause()
                playStopImage.setImageResource(R.drawable.ic_play)
            }else{
                mediaPlayer.start()
                playStopImage.setImageResource(R.drawable.ic_pause)
                updateSeekbar()
            }
        }

        answerButton.setOnClickListener {
            if (!answerField.text.isNullOrEmpty()){
                val servBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
                val reqCall = servBuilder.getAudition(args.idAud)
                reqCall.enqueue(object : Callback<Auditions>{
                    override fun onResponse(call: Call<Auditions>, response: Response<Auditions>) {
                        if (response.isSuccessful){
                            addCompl(response.body()!!.answer, response.body()!!.id)
                        }
                    }

                    override fun onFailure(call: Call<Auditions>, t: Throwable) {
                        Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }

    private fun addCompl(answer : String, id : Int){
        //Toast.makeText(context, "Вошли в функцию ${answerField.text.toString()} ${answer}", Toast.LENGTH_SHORT).show()
        if (answerField.text.toString() == answer){
            Log.d("answer", "${answerField.text}")
            val userId : PaperUserId = Paper.book().read("userId")
            val compAud = CompletedAuditions(id, 0, true ,userId.userId, Audition("","",0,"","",""))
            val sb = ServiceBuilder.buildServices(ApiRequest::class.java)
            val rq = sb.completedAuditionAdd(compAud)
            rq.enqueue(object : Callback<CompletedAuditions>{
                override fun onResponse(
                    call: Call<CompletedAuditions>,
                    response: Response<CompletedAuditions>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(context, "Задание успешно пройдено", Toast.LENGTH_LONG).show()
                        mediaPlayer.stop()
                        disposeLink(id)
                        val action = AuditionDetailFragmentDirections.actionAuditionDetailFragmentToAuditionFragment()
                        findNavController().navigate(action)
                    }
                }

                override fun onFailure(
                    call: Call<CompletedAuditions>,
                    t: Throwable
                ) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }else{
            Toast.makeText(context, "Задание не пройдено, повторите попытку позже", Toast.LENGTH_LONG).show()
            mediaPlayer.stop()
            disposeLink(id)
            val action = AuditionDetailFragmentDirections.actionAuditionDetailFragmentToAuditionFragment()
            findNavController().navigate(action)
        }
    }

    private fun disposeLink(id: Int) {
        val serviceBuild = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuild.disposeAudition(id)
        requestCall.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    d("er", "er")
                }else{
                    Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getAudio(id : Int){
        val serviceBuild = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuild.getAuditionTask(id)
        requestCall.enqueue(object : Callback<Links>{
            override fun onResponse(call: Call<Links>, response: Response<Links>) {
                if (response.isSuccessful){
                    prepareMediaPlayer(response.body()!!.link)
                    progressBar.visibility = View.INVISIBLE
                    auditionTask.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<Links>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun prepareMediaPlayer(urlAudio : String){
        try{

            mediaPlayer.setDataSource(urlAudio)
            mediaPlayer.prepare()
            timeOfAudio.text = millisecondsToTimer(mediaPlayer.duration.toLong())
        }catch (ex : Exception){
            Toast.makeText(context, "${ex.message}", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var audDetail = inflater.inflate(R.layout.fragment_audition_detail, container, false)
        Paper.init(context)
        numberAud = audDetail.findViewById(R.id.number_audition)
        taskAud = audDetail.findViewById(R.id.text_audition)
        progressBar = audDetail.findViewById(R.id.pbAudDetLoading)
        currentTime = audDetail.findViewById(R.id.current_time)
        timeOfAudio = audDetail.findViewById(R.id.time_of_audio)
        playStopImage = audDetail.findViewById(R.id.play_stop)
        seekBar = audDetail.findViewById(R.id.seek_bar)
        auditionTask = audDetail.findViewById(R.id.audio_player)
        answerField = audDetail.findViewById(R.id.audition_answer)
        answerButton = audDetail.findViewById(R.id.end_of_audition)
        mediaPlayer = MediaPlayer()
        seekBar.max = 100
        return audDetail
    }

    private fun millisecondsToTimer(milliseconds : Long) : String{
        var timerString = ""
        var secondsString : String

        var hours : Int = (milliseconds / (1000* 60 * 60)).toInt()
        var minutes : Int = (milliseconds / 1000 / 60).toInt()
        var seconds : Int = (milliseconds / 1000 % 60).toInt()

        if (hours > 0){
            timerString = "${hours}:"
        }
        if (seconds < 10){
            secondsString = "0${seconds}"
        }else{
            secondsString = "$seconds"
        }
        timerString = timerString +"${minutes}:" + secondsString
        return timerString
    }

    private fun updateSeekbar(){
        if (mediaPlayer.isPlaying){
            seekBar.progress = ((mediaPlayer.currentPosition.toFloat()/mediaPlayer.duration.toFloat()) * 100).toInt()
            handler.postDelayed(runnable, 1000)
        }
    }

    private var runnable : Runnable = Runnable {
        if (mediaPlayer.isPlaying){
            updateSeekbar()
            var currentDuration : Long = mediaPlayer.currentPosition.toLong()
            currentTime.text = millisecondsToTimer(currentDuration)
        }
    }



}