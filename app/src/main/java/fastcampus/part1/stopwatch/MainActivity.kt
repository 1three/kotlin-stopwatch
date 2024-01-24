package fastcampus.part1.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fastcampus.part1.stopwatch.databinding.ActivityMainBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}