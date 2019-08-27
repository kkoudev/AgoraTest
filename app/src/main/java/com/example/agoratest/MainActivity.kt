package com.example.agoratest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import java.lang.Exception

/**
 * AgoraTest Main activity.
 */
class MainActivity : AppCompatActivity() {

    companion object {

        /**
         * Logging tag name
         */
        const val TAG = "AgoraTest"

        /**
         * Agora app id TODO : Please set your app id.
         */
        const val AGORA_APP_ID = ""

        /**
         * Agora joining channel name TODO : Please set your channel name.
         */
        const val AGORA_CHANNEL_NAME = "ExamplePlayEffect"

        /**
         * Agora play effect sound id
         */
        const val AGORA_PLAY_EFFECT_SOUND_ID = 100

        /**
         * Agora play effect file path
         */
        const val AGORA_PLAY_EFFECT_FILE_PATH = "/assets/sample.m4a"

    }

    /**
     * RtcEngine
     */
    private var rtcEngine: RtcEngine? = null

    /**
     * Joined channel name
     */
    private var joinedChannelName: String? = null


    /**
     * {@inheritDoc}
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRtcEngine()
        initButtonEvents()

    }

    /**
     * Initialize rtc engine.
     */
    private fun initRtcEngine() {

        try {

            rtcEngine = RtcEngine.create(
                this,
                AGORA_APP_ID,
                object : IRtcEngineEventHandler() {

                    override fun onJoinChannelSuccess(
                        channel: String?,
                        uid: Int,
                        elapsed: Int
                    ) {

                        super.onJoinChannelSuccess(channel, uid, elapsed)

                        // Set joined channel name
                        this@MainActivity.joinedChannelName = channel

                        Log.d(TAG, "agoraEngine : onJoinChannelSuccess")

                        this@MainActivity.runOnUiThread {

                            Toast.makeText(
                                this@MainActivity,
                                "agoraEngine : onJoinChannelSuccess",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                    override fun onLeaveChannel(stats: RtcStats?) {

                        super.onLeaveChannel(stats)

                        // Clear joined channel name
                        this@MainActivity.joinedChannelName = null

                        Log.d(TAG, "agoraEngine : onLeaveChannel")

                        this@MainActivity.runOnUiThread {

                            Toast.makeText(
                                this@MainActivity,
                                "agoraEngine : onLeaveChannel",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                    override fun onAudioEffectFinished(soundId: Int) {

                        super.onAudioEffectFinished(soundId)

                        Log.d(TAG, "agoraEngine : onAudioEffectFinished")

                        this@MainActivity.runOnUiThread {

                            Toast.makeText(
                                this@MainActivity,
                                "agoraEngine : onAudioEffectFinished",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                    override fun onError(err: Int) {

                        super.onError(err)

                        Log.d(TAG, "agoraEngine : onError")

                        this@MainActivity.runOnUiThread {

                            Toast.makeText(
                                this@MainActivity,
                                "agoraEngine : onError [err = $err]",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                }
            )

        } catch (e: Exception) {

            Log.e(TAG, "Error create RtcEngine!!!", e)

        }

    }

    /**
     * Initialize button events.
     */
    private fun initButtonEvents() {

        // Join Channel Button event
        findViewById<Button>(R.id.btn_join_channel).setOnClickListener {

            val useRtcEngine = rtcEngine ?: return@setOnClickListener

            useRtcEngine.joinChannel(null, AGORA_CHANNEL_NAME, null, 0)

        }

        // Play Effect Button event
        findViewById<Button>(R.id.btn_play_effect).setOnClickListener {

            val useRtcEngine = rtcEngine ?: return@setOnClickListener

            // play effect
            val result = useRtcEngine.audioEffectManager.playEffect(
                AGORA_PLAY_EFFECT_SOUND_ID,
                AGORA_PLAY_EFFECT_FILE_PATH,
                0,
                1.0,
                0.0,
                100.0,
                false)

            if (result < 0) {

                Toast.makeText(
                    this@MainActivity,
                    "Error playEffect [result = $result]",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

        // Leave Channel Button event
        findViewById<Button>(R.id.btn_leave_channel).setOnClickListener {

            val useRtcEngine = rtcEngine ?: return@setOnClickListener

            // not join channel
            if (joinedChannelName == null) {

                return@setOnClickListener

            }

            useRtcEngine.leaveChannel()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
