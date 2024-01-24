package fastcampus.part1.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import fastcampus.part1.stopwatch.databinding.ActivityMainBinding
import fastcampus.part1.stopwatch.databinding.DialogCountdownSettingBinding
import java.util.Timer
import kotlin.concurrent.timer

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

// 0.1초마다 시간 update 작업 → Worker Thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var countdownSecond = 10
    private var currentCountdownDeciSecond = countdownSecond * 10 // 진행 중인 countdown 수, -1 = -0.1s
    private var currentDesiSecond = 0 // 진행 중인 stopwatch 수 , +1 = +0.1s
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startButton = binding.startButton
        val stopButton = binding.stopButton
        val pauseButton = binding.pauseButton
        val lapTimeButton = binding.lapTimeButton

        // 카운트다운 초기화
        initCountdownViews()

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

    private fun initCountdownViews() {
        binding.countdownTextView.text = String.format("%02d", countdownSecond)
        binding.countdownProgressBar.progress = 100
    }

    // timer 생성 ≒ Worker Thread 생성
    private fun start() {
        timer = timer(initialDelay = 0, period = 100) {// period = 100ms(0.1s)마다 작업, 1000ms = 1s
            if (currentCountdownDeciSecond == 0) {
                currentDesiSecond += 1

                val minutes = currentDesiSecond / 10 / 60
                val seconds = currentDesiSecond / 10 % 60
                val deciSeconds = currentDesiSecond % 10

                // Worker Thread UI 조작 1
                runOnUiThread {
                    binding.countdownGroup.isVisible = false
                    binding.timeTextView.text = String.format("%02d:%02d", minutes, seconds)
                    binding.tickTextView.text = deciSeconds.toString()
                }
            } else {
                currentCountdownDeciSecond -= 1

                val seconds = currentCountdownDeciSecond / 10
                val progress = (currentCountdownDeciSecond / (countdownSecond * 10f)) * 100

                // Worker Thread UI 조작 2
                binding.root.post {
                    binding.countdownTextView.text = String.format("%02d", seconds)
                    binding.countdownProgressBar.progress = progress.toInt()
                }
            }

            // Worder Thread는 UI 접근 불가
            // binding.timeTextView.text = String.format("%02d:%02d", minutes, seconds)
            // binding.tickTextView.text = deciSeconds.toString()
        }
    }

    private fun stop() {
        currentDesiSecond = 0
        binding.timeTextView.text = "00:00"
        binding.tickTextView.text = "0"

        // countdown
        binding.countdownGroup.isVisible = true
        initCountdownViews()
    }

    private fun pause() {
        timer?.cancel()
        timer = null
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
                currentCountdownDeciSecond = countdownSecond * 10
                binding.countdownTextView.text =
                    String.format("%02d", countdownSecond) // String formatting
            }
            setNegativeButton("취소", null)
        }.show()
    }
}