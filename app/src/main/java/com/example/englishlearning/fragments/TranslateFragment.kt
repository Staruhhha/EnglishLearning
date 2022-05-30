package com.example.englishlearning.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.arrayMapOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.englishlearning.R
import com.example.englishlearning.databinding.FragmentTranslateBinding
import com.example.englishlearning.models.Minicard
import com.example.englishlearning.services.DictionaryRequest
import com.example.englishlearning.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class TranslateFragment : Fragment() {

    lateinit var binding : FragmentTranslateBinding
    private var authToken : String = "Basic MTE3M2IxZjAtNDFmOS00NzkyLTgzNmYtOWFmNjA3OTg5NDA2OjgzYmMwYTBkNmY0YzRhOGNiZjFiMGNkZTgzYTg3OWE3"
    private var authBearedToken : String = ""
    private var minicard  = Minicard()
    private var mediaPlayer = MediaPlayer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notFound.visibility = View.INVISIBLE
        binding.translateConstraint.visibility = View.INVISIBLE

        authAction()

        binding.searchButton.setOnClickListener {
            if (!binding.word.text.toString().isNullOrEmpty()){
                when (binding.languageList.selectedItem.toString()){
                    "EN->RU" -> translateWord(binding.word.text.toString(), "1033", "1049")
                    "RU->EN" -> translateWord(binding.word.text.toString(), "1049", "1033")
                }
            }
        }

        binding.playWord.setOnClickListener {
            playAudio(minicard)
        }

        //authAction()

    }

    private fun mp3File(byteArray: ByteArray){
        try {
            var tempFile = File.createTempFile("trans", ".mp3", context?.cacheDir)
            tempFile.deleteOnExit()
            var fos = FileOutputStream(tempFile)
            fos.write(byteArray)
            fos.close()

            mediaPlayer.reset()

            var fis = FileInputStream(tempFile)
            mediaPlayer.setDataSource(fis.getFD())
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (ex : IOException){
            //var s = ex.message
            ex.printStackTrace()
        }
    }

    private fun playAudio(minicard: Minicard) {
        val dictionaryName = minicard.translation?.dictionaryName
        val audioFile = minicard.translation?.soundName
        val serviceBuilder = ServiceBuilder.buildServicesDictionary(DictionaryRequest::class.java)
        val map = arrayMapOf<String, String>()
        map["Authorization"] = authBearedToken
        val requestCall = serviceBuilder.getTranscription(dictionaryName!!, audioFile!!, map)
        requestCall.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    val byteArray = response.body()!!.toByteArray()
                    mp3File(byteArray)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun translateWord(word: String, srcLang : String, dstLang : String) {
        val serviceBuilder = ServiceBuilder.buildServicesDictionary(DictionaryRequest::class.java)
        val mapAuth = arrayMapOf<String, String>()
        mapAuth["Authorization"] = authBearedToken
        val requestCall = serviceBuilder.wordTranslate(word,srcLang, dstLang, mapAuth)
        requestCall.enqueue(object:Callback<Minicard>{
            override fun onResponse(call: Call<Minicard>, response: Response<Minicard>) {
                if (response.isSuccessful){
                    binding.notFound.visibility = View.INVISIBLE
                    binding.translateConstraint.visibility = View.VISIBLE
                    minicard = response.body()!!
                    setWord(response.body())

                }else{
                    binding.notFound.visibility = View.VISIBLE
                    binding.notFound.text = "Перевод слова '$word' не найден"
                    binding.translateConstraint.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<Minicard>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setWord(minicard: Minicard?) {
        binding.wordEntered.text = minicard!!.translation!!.heading
        binding.wordTranslate.text = minicard!!.translation!!.translation
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_translate, container, false)
        return binding.root
    }

    private fun authAction(){
        val serviceBuilder = ServiceBuilder.buildServicesDictionary(DictionaryRequest::class.java)
        val map = arrayMapOf<String, String>()
        map["Authorization"] = authToken
        val requestCall = serviceBuilder.auth(map)
        requestCall.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    authBearedToken = "Bearer ${response.body()}"
                    Log.d("bearer", authBearedToken)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

}