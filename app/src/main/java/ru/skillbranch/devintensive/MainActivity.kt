package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hidekeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity() , View.OnClickListener, TextView.OnEditorActionListener{

    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send
        sendBtn.setOnClickListener(this)
        messageEt.setOnEditorActionListener(this)


        val mStatus = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val mQuestion = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        Log.d("M_MainActivity", "onCreate $mStatus $mQuestion")

        benderObj = Bender(Bender.Status.valueOf(mStatus), Bender.Question.valueOf(mQuestion))

        textTxt.text = benderObj.askQuestion()

        val (r,g,b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)

    }


    override fun onRestart() {
        super.onRestart()

        Log.d("M_MainActivity", "onRestart")
    }

    override fun onStart() {
        super.onStart()

        Log.d("M_MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
        
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.status.name}  ${benderObj.question.name}")
    }


    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send){
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
            messageEt.setText("")
            val (r,g,b) = color
            benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
            if (this.isKeyboardOpen()){
                this.hidekeyboard()
            }
        }
    }

    override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return if (view?.id == R.id.et_message && actionId == EditorInfo.IME_ACTION_DONE) {
            sendBtn.performClick()
            true
        } else {
            false
        }
    }

}
