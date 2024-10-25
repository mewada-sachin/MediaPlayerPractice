package com.example.mediaplayerpractice.ui.mediaplayer

import android.net.Uri
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mediaUrl = intent.getStringExtra("url").toString()
        initPlayer()
    }

    override fun onStop() {
        super.onStop()
        pause()
    }

    override fun onResume() {
        super.onResume()
        play()
        hideSystemUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onBackPressed() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            enterPictureInPictureMode()
            hideSystemUI()
        } else {
            super.onBackPressed()
        }
    }

    override fun onUserLeaveHint() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            enterPictureInPictureMode()
        }
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
                setMediaSource(getAssetMediaSource(assetVideoPath))
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
}