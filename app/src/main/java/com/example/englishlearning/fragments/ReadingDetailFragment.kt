package com.example.englishlearning.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishlearning.R
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.CompletedReadings
import com.example.englishlearning.models.Readings
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReadingDetailFragment : Fragment() {

    private val args : ReadingDetailFragmentArgs by navArgs()
    private lateinit var progressBar :  ProgressBar
    private lateinit var readingNumb : TextView
    private lateinit var readingTask : TextView
    private lateinit var readingAnswer : EditText
    private lateinit var readingComplete : AppCompatButton
    private var answer = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.VISIBLE

        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuilder.getReading(args.idReading)
        requestCall.enqueue(object : Callback<Readings>{
            override fun onResponse(call: Call<Readings>, response: Response<Readings>) {
                if (response.isSuccessful){
                    readingNumb.text = "Задание № ${response.body()!!.id}"
                    readingTask.text = response.body()!!.task
                    answer = response.body()!!.answer
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Readings>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

        readingComplete.setOnClickListener {
            if (readingAnswer.text.toString() == answer){
                addComplReading(args.idReading)
            }else{
                Toast.makeText(context, "Задание выполнено неверно. Повторите попытку позже", Toast.LENGTH_SHORT).show()
                val action = ReadingDetailFragmentDirections.actionReadingDetailFragmentToReadingFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun addComplReading(idReading : Int) {
        val userId : PaperUserId = Paper.book().read("userId")
        val compReading = CompletedReadings(0, userId.userId, idReading, true, null)
        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuilder.completedReadingAdd(compReading)
        requestCall.enqueue(object : Callback<CompletedReadings>{
            override fun onResponse(
                call: Call<CompletedReadings>,
                response: Response<CompletedReadings>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(context, "Задание выполнено успешно", Toast.LENGTH_SHORT).show()
                    val action = ReadingDetailFragmentDirections.actionReadingDetailFragmentToReadingFragment()
                    findNavController().navigate(action)
                }
            }

            override fun onFailure(call: Call<CompletedReadings>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val readDetailFrag = inflater.inflate(R.layout.fragment_reading_detail, container, false)
        Paper.init(context)
        progressBar = readDetailFrag.findViewById(R.id.reading_detail_load)
        readingNumb = readDetailFrag.findViewById(R.id.number_reading)
        readingTask = readDetailFrag.findViewById(R.id.text_reading)
        readingAnswer = readDetailFrag.findViewById(R.id.reading_answer)
        readingComplete = readDetailFrag.findViewById(R.id.end_of_reading)
        return readDetailFrag
    }

}