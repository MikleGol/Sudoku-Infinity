package liber.app.sudoku


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import liber.app.sudoku.R

lateinit var btn_play : Button
lateinit var btn_complexity_medium : Button
lateinit var btn_complexity_easy : Button
lateinit var btn_complexity_hard : Button
lateinit var btn_easy : Button
lateinit var btn_medium : Button
lateinit var btn_hard : Button
lateinit var complexity : String
lateinit var dialog : Dialog
lateinit var w : Window
lateinit var txt_best : TextView

class HomeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setFullScreen()
        btn_play = findViewById(R.id.btn_play)
        btn_complexity_medium = findViewById(R.id.btn_complexity_medium)
        btn_complexity_easy = findViewById(R.id.btn_complexity_easy)
        btn_complexity_hard = findViewById(R.id.btn_complexity_hard)
        btn_easy = findViewById(R.id.btn_easy)
        btn_medium = findViewById(R.id.btn_medium)
        btn_hard = findViewById(R.id.btn_hard)
        txt_best = findViewById(R.id.txt_best)

        complexity = "medium"

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_view)
        dialog.setTitle("Theme")
        dialog.getWindow()?.decorView?.setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        btn_complexity_medium.isPressed = true

        val sharedPreferences = getSharedPreferences("Score", Context.MODE_PRIVATE)
        val bestInRow = sharedPreferences.getInt("bestInRow", 0)
        txt_best.setText("Best in a row: " + bestInRow)

        btn_play.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("complexity", complexity)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }


        btn_complexity_easy.setOnTouchListener(OnTouchListener { v, event -> // show interest in events resulting from ACTION_DOWN
            if (event.action == MotionEvent.ACTION_DOWN) return@OnTouchListener true
            if (event.action != MotionEvent.ACTION_UP) return@OnTouchListener false
            btn_complexity_medium.isPressed = false
            btn_complexity_hard.isPressed = false
            btn_easy.setBackgroundResource(R.drawable.button_green);
            btn_medium.setBackgroundResource(R.drawable.button_black);
            btn_hard.setBackgroundResource(R.drawable.button_black);
            complexity = "easy"
            btn_complexity_easy.setPressed(true)
            true
        })

        btn_complexity_medium.setOnTouchListener(OnTouchListener { v, event -> // show interest in events resulting from ACTION_DOWN
            if (event.action == MotionEvent.ACTION_DOWN) return@OnTouchListener true
            if (event.action != MotionEvent.ACTION_UP) return@OnTouchListener false
            btn_complexity_easy.isPressed = false
            btn_complexity_hard.isPressed = false
            btn_easy.setBackgroundResource(R.drawable.button_yellow);
            btn_medium.setBackgroundResource(R.drawable.button_yellow);
            btn_hard.setBackgroundResource(R.drawable.button_black);
            complexity = "medium"
            btn_complexity_medium.setPressed(true)
            true
        })

        btn_complexity_hard.setOnTouchListener(OnTouchListener { v, event -> // show interest in events resulting from ACTION_DOWN
            if (event.action == MotionEvent.ACTION_DOWN) return@OnTouchListener true
            if (event.action != MotionEvent.ACTION_UP) return@OnTouchListener false
            btn_complexity_easy.isPressed = false
            btn_complexity_medium.isPressed = false
            btn_easy.setBackgroundResource(R.drawable.button_red);
            btn_medium.setBackgroundResource(R.drawable.button_red);
            btn_hard.setBackgroundResource(R.drawable.button_red);
            complexity = "hard"
            btn_complexity_hard.setPressed(true)
            true
        })


        dialog.setOnDismissListener(
            object : DialogInterface.OnDismissListener {
                override fun onDismiss(dialogInterface: DialogInterface) {
                    setFullScreen()
                }
            })
    }

    private fun setFullScreen() {
        val decorView = window.decorView
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, insets ->
            val systemUiFlags = SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_HIDE_NAVIGATION
            decorView.systemUiVisibility = systemUiFlags
            insets
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }



}