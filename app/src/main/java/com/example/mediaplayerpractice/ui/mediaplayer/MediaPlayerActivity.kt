package com.example.mediaplayerpractice.ui.mediaplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.mediaplayerpractice.databinding.ActivityMediaPlayerBinding

class MediaPlayerActivity : AppCompatActivity() {

    private var mediaUrl =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
    private val mediaUrlHls =
        "http://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8"
    private val assetVideoPath = "demo_video.mp4"

    private val binding by lazy {
        ActivityMediaPlayerBinding.inflate(layoutInflater)
    }

    private var player: ExoPlayer? = null
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    private var isInPictureInPictureMode = false

    companion object {
        var isActivityRunning = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isActivityRunning = true
        setContentView(binding.root)
        mediaUrl = intent.getStringExtra("url").toString()
        registerReceiver(
            pipReceiver, IntentFilter("com.example.mediaplayer.ACTION_STOP_PIP"),
            RECEIVER_NOT_EXPORTED
        )
        initPlayer()
    }

    override fun onUserLeaveHint() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            enterPictureInPictureMode()
        }
    }

    override fun onPictureInPictureModeChanged(isInPipMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPipMode)
        isInPictureInPictureMode = isInPipMode
        if (!isInPipMode && isFinishing) {
            releasePlayer()
        }
    }

    override fun onBackPressed() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            enterPictureInPictureMode()
            hideSystemUI()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        play()
    }

    override fun onStop() {
        super.onStop()
        pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        isActivityRunning = false
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(this)
            .build()
            .apply {
                val source = when {
                    assetVideoPath.isNotEmpty() -> getAssetMediaSource(assetVideoPath)
                    mediaUrl.contains("m3u8") -> getHlsMediaSource()
                    else -> getProgressiveMediaSource()
                }
                setMediaSource(getProgressiveMediaSource())
                prepare()
                addListener(playerListener)
            }
    }

    private fun getAssetMediaSource(assetPath: String): MediaSource {
        val assetDataSourceFactory = DataSource.Factory { AssetDataSource(this) }
        return ProgressiveMediaSource.Factory(assetDataSourceFactory)
            .createMediaSource(MediaItem.fromUri("asset:///$assetPath"))
    }

    private fun getHlsMediaSource(): MediaSource {
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(mediaUrlHls))
    }

    private fun getProgressiveMediaSource(): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(mediaUrl)))
    }

    private fun releasePlayer() {
        player?.apply {
            playWhenReady = false
            release()
        }
        player = null
    }

    private fun pause() {
        player?.playWhenReady = false
    }

    private fun play() {
        player?.playWhenReady = true
    }

    private fun restartPlayer() {
        player?.seekTo(0)
        player?.playWhenReady = true
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                Player.STATE_ENDED -> restartPlayer()
                Player.STATE_READY -> {
                    binding.playerView.player = player
                    play()
                }
            }
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE
                )
    }

    private val pipReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.mediaplayer.ACTION_STOP_PIP") {
                releasePlayer()

                mediaUrl = intent.getStringExtra("url").toString()
                initPlayer()
            }
        }
    }
}