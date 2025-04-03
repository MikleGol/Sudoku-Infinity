package liber.app.sudoku

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import liber.app.sudoku.R
import liber.app.sudoku.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt


var loses = 0
var wins = 0
var status = 0
var rightAnswers = 0
val allButtons = arrayListOf<Button>()
var inRow = 0
var bestInRow = 0
private lateinit var binding: ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen()
        val complexity : String = intent.getStringExtra("complexity").toString()
        allButtons.clear()
        addAllButtons()
        board()

        val sharedPreferences = getSharedPreferences("Score", Context.MODE_PRIVATE)
        bestInRow = sharedPreferences.getInt("bestInRow", 0)

        loses = 0
        wins = 0
        status = 0
        rightAnswers = 0
        inRow = 0
        binding.txtComplexity.setText(complexity)

        binding.btnNew.setOnClickListener { board() }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }

    }

    private fun setFullScreen() {
        val decorView = window.decorView
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, insets ->
            val systemUiFlags = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            decorView.systemUiVisibility = systemUiFlags
            insets
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun board() {

        val d = Random.nextInt(1..9)
        val e = Random.nextInt(1..9)
        val f = Random.nextInt(1..9)

        val i = Random.nextInt(1..9)
        val j = Random.nextInt(1..9)
        val k = Random.nextInt(1..9)

        val l = Random.nextInt(1..9)
        val m = Random.nextInt(1..9)
        val n = Random.nextInt(1..9)

        val buttonUse = arrayListOf<Button>(binding.defaultButton)
        val buttonUsed = arrayListOf<Button>(binding.defaultButton)

        val start = 0
        val buttonColor = arrayListOf<Int>()
        var attempts = 3
        var mistakes = 0
        rightAnswers = 0


        status = 0
        var a = 0;

        for(button1 : Button in allButtons){
            button1.setBackgroundResource(R.drawable.button_beige_block)
        }

        fun finishGame() {
            if(status == 0){
                for(button1 : Button in allButtons){
                    if(!button1.text.equals(button1.tag)){
                        button1.setBackgroundResource(R.drawable.selected)
                        button1.text = "" + button1.tag
                    }
                }
                loses++
                inRow = 0
                binding.count.setText("WIN: " + wins + "        LOSE: " + loses)
                status = -1

                val customToastLayout = layoutInflater.inflate(R.layout.custom_toast_finish,null)
                val customToast = Toast(this)
                customToast.view = customToastLayout
                customToast.setGravity(Gravity.BOTTOM,0,0)
                customToast.duration = Toast.LENGTH_LONG
                customToast.show()
            }
        }




        fun checkAnswer(button: Button, answer: Int, useHint: Boolean) {
            if(button.tag.toString().equals(answer.toString())){
                //Toast.makeText(this, "Right!", Toast.LENGTH_SHORT).show()

                if(useHint){
                    button.setBackgroundResource(R.drawable.button_beige_block_hint)
                } else{
                    button.setBackgroundResource(R.drawable.right_answer)
                }

                rightAnswers = 0

                for(button1 : Button in allButtons){
                    if(button1.text.equals(button1.tag)){
                        rightAnswers++
                    }
                }

                if(rightAnswers == 81 && status == 0){
                    wins++
                    inRow++
                    binding.count.setText("WIN: " + wins + "        LOSE: " + loses)
                    status = 1

                    if(inRow > bestInRow){
                        bestInRow = inRow
                        val sharedPreferences = getSharedPreferences("Score", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("bestInRow", bestInRow)
                        editor.apply()
                    }

                    val customToastLayout = layoutInflater.inflate(R.layout.custom_toast_win,null)
                    val customToast = Toast(this)
                    customToast.view = customToastLayout
                    customToast.setGravity(Gravity.BOTTOM,0,0)
                    customToast.duration = Toast.LENGTH_LONG
                    customToast.show()
                }
                rightAnswers = 0
            } else {
                //Toast.makeText(this, "Wrong! Need to be: " + button.tag, Toast.LENGTH_SHORT).show()
                button.setBackgroundResource(R.drawable.selected)
                if(mistakes >= 2){
                    finishGame()
                }
                mistakes++
            }
        }

        fun keys(btnSelected : Button){
            var trats = 0
            var buttonColorSize = buttonColor.size.toInt()
            var hintText = btnSelected.text



            btnSelected.setOnClickListener {

                for(btn in allButtons){
                    btn.setBackgroundResource(R.drawable.button_beige_block)
                }

                buttonUse.add(btnSelected)

                var selectedButtonIndex = allButtons.indexOf(btnSelected)
                var middleCellStartIndex = (selectedButtonIndex / 9) * 9

                val selectedMiddleCellRow = middleCellStartIndex / 9

                var rowStartIndex = middleCellStartIndex - (middleCellStartIndex % 3)
                var rowEndIndex = rowStartIndex
                var b = selectedButtonIndex%3

                if(b == 0){
                    rowStartIndex = selectedButtonIndex
                    rowEndIndex = selectedButtonIndex + 2
                } else if(b == 1){
                    rowStartIndex = selectedButtonIndex - 1
                    rowEndIndex = selectedButtonIndex + 1
                } else{
                    rowStartIndex = selectedButtonIndex - 2
                    rowEndIndex = selectedButtonIndex
                }

                var buttonsInSameRow = ArrayList<Button>()


                for (i in rowStartIndex..rowEndIndex) {
                    if (i != selectedButtonIndex) {
                        buttonsInSameRow.add(allButtons[i])
                        allButtons[i].setBackgroundResource(R.drawable.button_beige_block_selected)
                    }
                }

                var rowStart1 = selectedButtonIndex
                var rowStart2 = selectedButtonIndex
                var rowEnd1 = selectedButtonIndex
                var rowEnd2 = selectedButtonIndex

                var colStartArr = ArrayList<Int>()
                var colStart = selectedButtonIndex
                var colEnd = selectedButtonIndex
                var colIndex = 0

                var bb = selectedMiddleCellRow%3
                if(bb == 0){
                    if(b == 0){
                        rowStart1 = selectedButtonIndex + 9
                        rowEnd1 = selectedButtonIndex + 11
                        rowStart2 = selectedButtonIndex + 18
                        rowEnd2 = selectedButtonIndex + 20
                        colIndex = 0
                    } else if(b == 1){
                        rowStart1 = selectedButtonIndex + 8
                        rowEnd1 = selectedButtonIndex + 10
                        rowStart2 = selectedButtonIndex + 17
                        rowEnd2 = selectedButtonIndex + 19
                        colIndex = 1
                    } else{
                        rowStart1 = selectedButtonIndex + 7
                        rowEnd1 = selectedButtonIndex + 9
                        rowStart2 = selectedButtonIndex + 16
                        rowEnd2 = selectedButtonIndex + 18
                        colIndex = 2
                    }
                } else if(bb == 1){
                    if(b == 0){
                        rowStart1 = selectedButtonIndex - 9
                        rowEnd1 = selectedButtonIndex - 7
                        rowStart2 = selectedButtonIndex + 9
                        rowEnd2 = selectedButtonIndex + 11
                        colIndex = 9
                    } else if(b == 1){
                        rowStart1 = selectedButtonIndex - 10
                        rowEnd1 = selectedButtonIndex - 8
                        rowStart2 = selectedButtonIndex + 8
                        rowEnd2 = selectedButtonIndex + 10
                        colIndex = 10
                    } else{
                        rowStart1 = selectedButtonIndex - 11
                        rowEnd1 = selectedButtonIndex - 9
                        rowStart2 = selectedButtonIndex + 7
                        rowEnd2 = selectedButtonIndex + 9
                        colIndex = 11
                    }
                } else{
                    if(b == 0){
                        rowStart1 = selectedButtonIndex -18
                        rowEnd1 = selectedButtonIndex -16
                        rowStart2 = selectedButtonIndex -9
                        rowEnd2 = selectedButtonIndex -7
                        colIndex = 18
                    } else if(b == 1){
                        rowStart1 = selectedButtonIndex -19
                        rowEnd1 = selectedButtonIndex - 17
                        rowStart2 = selectedButtonIndex -10
                        rowEnd2 = selectedButtonIndex - 8
                        colIndex = 19
                    } else{
                        rowStart1 = selectedButtonIndex -20
                        rowEnd1 = selectedButtonIndex  -18
                        rowStart2 = selectedButtonIndex -11
                        rowEnd2 = selectedButtonIndex -9
                        colIndex = 20
                    }
                }



                for(j in 1..3){
                    for(i in 1..3){
                        allButtons[colIndex].setBackgroundResource(R.drawable.button_beige_block_selected)
                        colIndex += 3
                    }
                    colIndex += 18
                }

                for (i in rowStart1..rowEnd1) {
                    buttonsInSameRow.add(allButtons[i])
                    allButtons[i].setBackgroundResource(R.drawable.button_beige_block_selected)
                }

                for(i in rowStart2 .. rowEnd2){
                    buttonsInSameRow.add(allButtons[i])
                    allButtons[i].setBackgroundResource(R.drawable.button_beige_block_selected)
                }


                //Toast.makeText(this, "" + btnSelected.id,Toast.LENGTH_SHORT).show()

                //Log.d("123123", buttonUse[0].background.toString())
                //Log.d("123123",R.drawable.selected.toString())
                buttonUse.remove(buttonUse[0])
                btnSelected.setBackgroundResource(R.drawable.button_beige_block_selected_main)

                //Toast.makeText(this, "" + buttonUsed.size,Toast.LENGTH_SHORT).show()

                binding.one.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "1"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 1, false)}
                binding.two.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "2"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 2, false)}
                binding.three.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "3"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 3, false)}
                binding.four.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "4"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 4, false)}
                binding.five.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "5"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 5, false)}
                binding.six.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "6"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 6, false)}
                binding.seven.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "7"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 7, false)}
                binding.eight.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "8"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 8, false)}
                binding.nine.setOnClickListener { buttonUsed.add(btnSelected); btnSelected.text = "9"; btnSelected.setBackgroundResource(
                    R.drawable.button_beige_block
                ); checkAnswer(btnSelected, 9, false)}

                binding.btnHint.setOnClickListener {
                    if(attempts <= 0){
                        //Toast.makeText(this, "No hints left!", Toast.LENGTH_SHORT).show();
                        val customToastLayout = layoutInflater.inflate(R.layout.custom_toast_hint,null)
                        val customToast = Toast(this)
                        var txtDescriprionToast = customToastLayout.findViewById<TextView>(R.id.custom_toast_text)
                        txtDescriprionToast.setText("No hints left!")
                        customToast.view = customToastLayout
                        customToast.setGravity(Gravity.BOTTOM,0,0)
                        customToast.duration = Toast.LENGTH_LONG
                        customToast.show()
                    } else{
                        buttonUsed.add(btnSelected);
                        btnSelected.text = "" + btnSelected.tag;
                        //btnSelected.setBackgroundResource(R.drawable.button_beige_block_hint)

                        val customToastLayout = layoutInflater.inflate(R.layout.custom_toast_hint,null)
                        val customToast = Toast(this)
                        var txtDescriprionToast = customToastLayout.findViewById<TextView>(R.id.custom_toast_description)
                        txtDescriprionToast.setText("Remains: " + (attempts-1))
                        customToast.view = customToastLayout
                        customToast.setGravity(Gravity.BOTTOM,0,0)
                        customToast.duration = Toast.LENGTH_LONG
                        customToast.show()
                        attempts--;
                        checkAnswer(btnSelected,Integer.parseInt(btnSelected.text.toString()), true)
                    }
                }

                binding.btnFinish.setOnClickListener {
                    finishGame()
                }

                binding.btnDelete.setOnClickListener{
                    if(buttonUsed.size > 1 && status == 0){
                        buttonUsed[buttonUsed.size-1].text = ""
                        buttonUsed.remove(buttonUsed[buttonUsed.size-1])
                    }

                }
            }
        }

        if(d!=e && d!=f && e!=f){
            if(i!=j && i!=k && j!=k && i!=d && i!=e && i!=f && j!=d && j!=e && j!=f && k!=d && k!=e && k!=f){
                if(l!=m && l!=n && m!=n && l!=d && l!=e && l!=f && l!=i && l!=j && l!=k && m!=d && m!=e && m!=f && m!=i && m!=j && m!=k && n!=d && n!=e && n!=f && n!=i && n!=j && n!=k){
                    binding.b1.text = "$d"; binding.b1.tag="$d"
                    binding.b1.text = "$d"; binding.b1.tag = "$d"
                    binding.b2.text = "$f"; binding.b2.tag = "$f"
                    binding.b3.text = "$k"; binding.b3.tag = "$k"
                    binding.b4.text = "$e"; binding.b4.tag = "$e"
                    binding.b5.text = "$n"; binding.b5.tag = "$n"
                    binding.b6.text = "$l"; binding.b6.tag = "$l"
                    binding.b7.text = "$m"; binding.b7.tag = "$m"
                    binding.b8.text = "$i"; binding.b8.tag = "$i"
                    binding.b9.text = "$j"; binding.b9.tag = "$j"

                    binding.b10.text = "$i"; binding.b10.tag = "$i"
                    binding.b11.text = "$l"; binding.b11.tag = "$l"
                    binding.b12.text = "$m"; binding.b12.tag = "$m"
                    binding.b13.text = "$d"; binding.b13.tag = "$d"
                    binding.b14.text = "$k"; binding.b14.tag = "$k"
                    binding.b15.text = "$j"; binding.b15.tag = "$j"
                    binding.b16.text = "$f"; binding.b16.tag = "$f"
                    binding.b17.text = "$n"; binding.b17.tag = "$n"
                    binding.b18.text = "$e"; binding.b18.tag = "$e"

                    binding.b19.text = "$j"; binding.b19.tag = "$j"
                    binding.b20.text = "$e"; binding.b20.tag = "$e"
                    binding.b21.text = "$n"; binding.b21.tag = "$n"
                    binding.b22.text = "$i"; binding.b22.tag = "$i"
                    binding.b23.text = "$m"; binding.b23.tag = "$m"
                    binding.b24.text = "$f"; binding.b24.tag = "$f"
                    binding.b25.text = "$k"; binding.b25.tag = "$k"
                    binding.b26.text = "$d"; binding.b26.tag = "$d"
                    binding.b27.text = "$l"; binding.b27.tag = "$l"

                    binding.b28.text = "$k"; binding.b28.tag = "$k"
                    binding.b29.text = "$m"; binding.b29.tag = "$m"
                    binding.b30.text = "$f"; binding.b30.tag = "$f"
                    binding.b31.text = "$i"; binding.b31.tag = "$i"
                    binding.b32.text = "$j"; binding.b32.tag = "$j"
                    binding.b33.text = "$n"; binding.b33.tag = "$n"
                    binding.b34.text = "$l"; binding.b34.tag = "$l"
                    binding.b35.text = "$e"; binding.b35.tag = "$e"
                    binding.b36.text = "$d"; binding.b36.tag = "$d"

                    binding.b37.text = "$l"; binding.b37.tag = "$l"
                    binding.b38.text = "$e"; binding.b38.tag = "$e"
                    binding.b39.text = "$n"; binding.b39.tag = "$n"
                    binding.b40.text = "$k"; binding.b40.tag = "$k"
                    binding.b41.text = "$d"; binding.b41.tag = "$d"
                    binding.b42.text = "$f"; binding.b42.tag = "$f"
                    binding.b43.text = "$m"; binding.b43.tag = "$m"
                    binding.b44.text = "$j"; binding.b44.tag = "$j"
                    binding.b45.text = "$i"; binding.b45.tag = "$i"

                    binding.b46.text = "$d"; binding.b46.tag = "$d"
                    binding.b47.text = "$i"; binding.b47.tag = "$i"
                    binding.b48.text = "$j"; binding.b48.tag = "$j"
                    binding.b49.text = "$e"; binding.b49.tag = "$e"
                    binding.b50.text = "$l"; binding.b50.tag = "$l"
                    binding.b51.text = "$m"; binding.b51.tag = "$m"
                    binding.b52.text = "$f"; binding.b52.tag = "$f"
                    binding.b53.text = "$n"; binding.b53.tag = "$n"
                    binding.b54.text = "$k"; binding.b54.tag = "$k"

                    binding.b55.text = "$j"; binding.b55.tag = "$j"
                    binding.b56.text = "$d"; binding.b56.tag = "$d"
                    binding.b57.text = "$i"; binding.b57.tag = "$i"
                    binding.b58.text = "$f"; binding.b58.tag = "$f"
                    binding.b59.text = "$l"; binding.b59.tag = "$l"
                    binding.b60.text = "$m"; binding.b60.tag = "$m"
                    binding.b61.text = "$n"; binding.b61.tag = "$n"
                    binding.b62.text = "$k"; binding.b62.tag = "$k"
                    binding.b63.text = "$e"; binding.b63.tag = "$e"

                    binding.b64.text = "$n"; binding.b64.tag = "$n"
                    binding.b65.text = "$f"; binding.b65.tag = "$f"
                    binding.b66.text = "$l"; binding.b66.tag = "$l"
                    binding.b67.text = "$e"; binding.b67.tag = "$e"
                    binding.b68.text = "$i"; binding.b68.tag = "$i"
                    binding.b69.text = "$k"; binding.b69.tag = "$k"
                    binding.b70.text = "$j"; binding.b70.tag = "$j"
                    binding.b71.text = "$m"; binding.b71.tag = "$m"
                    binding.b72.text = "$d"; binding.b72.tag = "$d"

                    binding.b73.text = "$m"; binding.b73.tag = "$m"
                    binding.b74.text = "$k"; binding.b74.tag = "$k"
                    binding.b75.text = "$e"; binding.b75.tag = "$e"
                    binding.b76.text = "$n"; binding.b76.tag = "$n"
                    binding.b77.text = "$j"; binding.b77.tag = "$j"
                    binding.b78.text = "$d"; binding.b78.tag = "$d"
                    binding.b79.text = "$l"; binding.b79.tag = "$l"
                    binding.b80.text = "$f"; binding.b80.tag = "$f"
                    binding.b81.text = "$i"; binding.b81.tag = "$i"

                    var random = Random.nextInt(50..53)

                    if(complexity.equals("easy")){
                        random = Random.nextInt(20..40)
                    } else if(complexity.equals("medium")){
                        random = Random.nextInt(40..60)
                    } else if(complexity.equals("hard")){
                        random = Random.nextInt(60..70)
                    }

                    var a = 0
                    while (a <= random) {
                        val r = Random.nextInt(1..81)
                        val button = allButtons[r - 1]  // -1, так как индексы списка начинаются с 0
                        button.text = ""
                        keys(button)
                        a++
                    }
                }else{
                    board()
                }
            }else{
                board()
            }
        } else{
            board()
        }
    }

    private fun addAllButtons() {
        for (i in 1..81) {
            val buttonId = "b$i"
            val button = binding::class.java.getDeclaredField(buttonId).get(binding) as Button
            allButtons.add(button)
        }
    }
}