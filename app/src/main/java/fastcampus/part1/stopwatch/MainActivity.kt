package fastcampus.part1.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import fastcampus.part1.stopwatch.databinding.ActivityMainBinding
import fastcampus.part1.stopwatch.databinding.DialogCountdownSettingBinding

/**
 * 스톱워치 앱
 *
 * 1. count down
 *    사용자 설정 가능
 *    progress bar
 *    시작 3초 전 알림음
 * 2. 0.1초마다 update
 * 3. start, pause, stop, lap time button
 * 4. stop 시 alert dialog (종료 확인 dialog)
 * */

/**
 * UI 스레드
 * AlertDialog
 * Thread
 * runOnUiThread
 * ToneGenerator
 * addView
 * */

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var countdownSecond = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startButton = binding.startButton
        val stopButton = binding.stopButton
        val pauseButton = binding.pauseButton
        val lapTimeButton = binding.lapTimeButton

        // 카운트다운 설정 버튼
        binding.countdownTextView.setOnClickListener {
            showCountdownSettingDialog()
        }

        // 시작 버튼
        startButton.setOnClickListener {
            start()
            startButton.isVisible = false
            stopButton.isVisible = false
            pauseButton.isVisible = true
            lapTimeButton.isVisible = true
        }

        // 중지 버튼
        stopButton.setOnClickListener {
            showAlertDialog()
        }

        // 일시정지 버튼
        pauseButton.setOnClickListener {
            pause()
            startButton.isVisible = true
            stopButton.isVisible = true
            pauseButton.isVisible = false
            lapTimeButton.isVisible = false
        }

        // 랩 타임 버튼
        lapTimeButton.setOnClickListener {
            lapTime()
        }
    }

    private fun start() {

    }

    private fun stop() {

    }

    private fun pause() {

    }

    private fun lapTime() {

    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("스톱워치를 종료할까요?")
            setPositiveButton("네") { _, _ ->
                stop()
            }
            setNegativeButton("아니요", null)
        }.show()
    }

    private fun showCountdownSettingDialog() {
        AlertDialog.Builder(this).apply {
            val dialogBinding = DialogCountdownSettingBinding.inflate(layoutInflater)
            with(dialogBinding.countdownSecondPicker) {
                maxValue = 20
                minValue = 0
                value = countdownSecond
            }
            setView(dialogBinding.root) // setContentView (액티비티 화면 구성), setView (다이얼로그, 팝업 창 구성)

            setTitle("카운트다운 설정")
            setPositiveButton("확인") { _, _ ->
                countdownSecond = dialogBinding.countdownSecondPicker.value
                binding.countdownTextView.text =
                    String.format("%02d", countdownSecond) // String formatting
            }
            setNegativeButton("취소", null)
        }.show()
    }
}