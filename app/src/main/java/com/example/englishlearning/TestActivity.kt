package com.example.englishlearning

import android.content.Intent
import android.graphics.Typeface
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.englishlearning.databinding.ActivityTestBinding
import com.example.englishlearning.helpers.PaperUserData
import com.example.englishlearning.models.TestQuestion
import com.example.englishlearning.models.Tests
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding : ActivityTestBinding
    private var currentSelect : Int = 0
    //private var currentSelectCorrect : Int = 0
    private var mSelectedAnswerId : Int = 0
    private var listQuestions : ArrayList<TestQuestion>? = null
    private var mCurrentPosition : Int = 1
    private var isSelected = false
    private var resultTest = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Paper.init(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        binding.pbLoading.visibility =  View.VISIBLE
        binding.cardTest.visibility = View.INVISIBLE
        binding.answer1.setOnClickListener(this)
        binding.answer2.setOnClickListener(this)
        binding.answer3.setOnClickListener(this)
        binding.answer4.setOnClickListener(this)
        binding.submitAnswer.setOnClickListener(this)

        getTest()
    }

    private fun setText(){
        val question = listQuestions!![mCurrentPosition-1]

        defaultAnswerView()

        if (mCurrentPosition == listQuestions!!.size){
            binding.submitAnswer.text = "Завершить тест"
        }else{
            binding.submitAnswer.text = "Подтвердить"
        }
        mSelectedAnswerId = 0

        binding.questionText.text = question.question.value
        binding.answer1.text = question.question.possibleAnswersInQuestions[0].possibleAnswer.value
        binding.answer2.text = question.question.possibleAnswersInQuestions[1].possibleAnswer.value
        binding.answer3.text = question.question.possibleAnswersInQuestions[2].possibleAnswer.value
        binding.answer4.text = question.question.possibleAnswersInQuestions[3].possibleAnswer.value
    }

    private fun getTest(){
        val requestBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = requestBuilder.getTestRandom()
        requestCall.enqueue(object : Callback<Tests>{
            override fun onResponse(call: Call<Tests>, response: Response<Tests>) {
                listQuestions = response.body()!!.testQuestions
                binding.pbLoading.visibility =  View.INVISIBLE
                binding.cardTest.visibility = View.VISIBLE
                setText()
            }

            override fun onFailure(call: Call<Tests>, t: Throwable) {
                Toast.makeText(this@TestActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun defaultAnswerView(){
        val answers = ArrayList<TextView>()
        answers.add(0, binding.answer1)
        answers.add(1, binding.answer2)
        answers.add(2, binding.answer3)
        answers.add(3, binding.answer4)

        for (answer in answers){
            answer.typeface = Typeface.DEFAULT
            answer.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_answer_field
            )
        }

    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.answer1->{
                currentSelect = 1
                selectedAnswerView(binding.answer1,currentSelect)
                mSelectedAnswerId = listQuestions!![mCurrentPosition-1].question.possibleAnswersInQuestions[0].possibleAnswerId
                isSelected = true
            }
            R.id.answer2->{
                currentSelect = 2
                selectedAnswerView(binding.answer2,currentSelect)
                mSelectedAnswerId = listQuestions!![mCurrentPosition-1].question.possibleAnswersInQuestions[1].possibleAnswerId
                isSelected = true
            }
            R.id.answer3->{
                currentSelect = 3
                selectedAnswerView(binding.answer3,currentSelect)
                mSelectedAnswerId = listQuestions!![mCurrentPosition-1].question.possibleAnswersInQuestions[2].possibleAnswerId
                isSelected = true
            }
            R.id.answer4->{
                currentSelect = 4
                selectedAnswerView(binding.answer4,currentSelect)
                mSelectedAnswerId = listQuestions!![mCurrentPosition-1].question.possibleAnswersInQuestions[3].possibleAnswerId
                isSelected = true
            }
            R.id.submit_answer ->{
                if (mSelectedAnswerId == 0){

                    when{
                        mCurrentPosition < listQuestions!!.size ->{
                            if (!isSelected){
                                Toast.makeText(this@TestActivity, "Выберите вариант ответа", Toast.LENGTH_SHORT).show()
                            }else{
                                mCurrentPosition++
                                setText()
                                isSelected = false
                            }
                        }else ->{
                        //Toast.makeText(this@TestActivity, "Вы прошли тест", Toast.LENGTH_SHORT).show()
                        finalDialogWindow(resultTest)
                        }
                    }
                }else{
                    val question = listQuestions?.get(mCurrentPosition-1)
                    if (question!!.question.truePossibleAnswerId != mSelectedAnswerId){
                        answerView(currentSelect, R.drawable.wrong_answer_bg)
                    }else{
                        resultTest++
                    }
                    val pos = question.question.possibleAnswersInQuestions.map {
                        x -> x.possibleAnswer
                    }.indexOf(question!!.question.truePossibleAnswer)
                    answerView(pos+1, R.drawable.correct_answer_bg)

                    if (mCurrentPosition == listQuestions!!.size){
                        binding.submitAnswer.text = "Завершить тест"
                    }else{
                        binding.submitAnswer.text = "Следующий вопрос"
                    }
                    mSelectedAnswerId = 0
                }
            }
        }
    }

    private fun answerView(answer : Int, drawableView: Int){
        when (answer){
            1 ->{
                binding.answer1.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            2 ->{
                binding.answer2.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            3 ->{
                binding.answer3.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            4 ->{
                binding.answer4.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }

    private fun finalDialogWindow(result : Int){
        var textResult = ""
        when (result){
            in 0..4 ->{
                textResult = "A1"
            }
            in 5..9 -> {
                textResult = "A2"
            }
            in 10..14 ->{
                textResult = "B1"
            }
            in 15..16->{
                textResult = "B2"
            }
        }
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.result_dialog, null)
        val mDialogBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mDialogBuilder.show()
        val textResultView = mAlertDialog.findViewById<TextView>(R.id.result_test)
        textResultView!!.text = textResult
        val buttonOk = mAlertDialog.findViewById<Button>(R.id.registration_go_button)
        buttonOk!!.setOnClickListener {
            //Toast.makeText(this, "Диалог", Toast.LENGTH_SHORT).show()
            val userData = PaperUserData(textResult, listQuestions!!.last().testId)
            Paper.book().write("userData", userData)
            mAlertDialog.cancel()
            val intent = Intent(this@TestActivity, RegistrationActivity::class.java)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            startActivity(intent)
            finish()
        }
    }

    private fun selectedAnswerView(tv : TextView, selectedAnswerNum : Int){
        defaultAnswerView()
        mSelectedAnswerId = selectedAnswerNum

        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_answer_field
        )
    }
}