/*
 * Copyright (C) 2017 Fayçal Kaddouri <powervlagos@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.squalala.talkiewalkie.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAmplitude;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squalala.talkiewalkie.App;
import com.squalala.talkiewalkie.OnClickMessageListener;
import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.Session;
import com.squalala.talkiewalkie.adapters.MessageAdapter;
import com.squalala.talkiewalkie.ui.fragments.ConnectedUserFragment;
import com.squalala.talkiewalkie.ui.fragments.SettingsFragment;
import com.squalala.talkiewalkie.models.Message;
import com.squalala.talkiewalkie.models.User;
import com.squalala.talkiewalkie.utils.FileUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import mehdi.sakout.fancybuttons.FancyButton;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.squalala.talkiewalkie.utils.FileUtils.humanReadableByteCount;
import static com.squalala.talkiewalkie.utils.FileUtils.readFile;

/**
 * Created by Fayçal KADDOURI on 16/02/17.
 */

public class TalkActivity extends RxAppCompatActivity
        implements AudioRecorder.OnErrorListener, SettingsFragment.FragmentListener {

    private static final String KEY_JOIN_ROOM = "joinRoom";
    private static final String KEY_LEFT_ROOM = "leftRoom";
    private static final String KEY_ADD_USER = "add username";
    private static final String KEY_ADD_URL_AVATAR = "add url_avatar";
    private static final String KEY_NEW_MESSAGE = "new message";
    private static final String KEY_NUMBER_USERS = "numberUsers";
    private static final String KEY_CONNECTED_USERS = "onConnectedUsers";

    public static final int MIN_AUDIO_LENGTH_SECONDS = 1;

    public static final int PRE_LIMIT = 7;
    public static final int MAX_LENGTH_AUDIO = 10;

    public static final int SHOW_INDICATOR_DELAY_MILLIS = 300;

    private static final String TAG = TalkActivity.class.getSimpleName();

    private static final ButterKnife.Action<View> INVISIBLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.INVISIBLE);
        }
    };

    private static final ButterKnife.Action<View> VISIBLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    private int lenghtAudio;

    @BindView(R.id.btnSpeak)
    AppCompatButton btnSpeak;

    @BindView(R.id.mFlIndicator)
    FrameLayout mFlIndicator;

    @BindView(R.id.recyclerViewMessages)
    RecyclerView recyclerView;

    @BindView(R.id.blanketView)
    View blanketView;

   // @BindView(R.id.mTvPressToSay)
   // TextView mTvPressToSay;

    @BindView(R.id.mTvLog)
    TextView mTvLog;

  //  @BindView(R.id.txtHowManyPerson)
   // TextView txtNumberUsers;

    @BindView(R.id.txtWhoSpeak)
    TextView txtWhoSpeak;

    @BindView(R.id.btn_user_online)
    FancyButton btnUserOnline;

    @BindView(R.id.txtWhatHappen)
    TextView txtWhatHappen;

    @BindView(R.id.mTvRecordingHint)
    TextView mTvRecordingHint;

    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;

    @BindView(R.id.viewAvatar)
    View viewAvatar;

    @BindView(R.id.image_user)
    SimpleDraweeView imageUser;

    @BindView(R.id.viewProgress)
    View progressView;

//    @BindView(R.id.iconBeep)
  //  ImageView iconBeep;

    @BindView(R.id.iconSettings)
    ImageView iconSettings;

    List<ImageView> mIvVoiceIndicators;

    private boolean isTalking;

    private AudioRecorder mAudioRecorder;

    private RxAudioPlayer mRxAudioPlayer;

    private Session session;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MediaPlayer mediaPlayerForBeep = new MediaPlayer();

    private Socket mSocket;

    private File mAudioFile;

    private Subscription mRecordSubscription;

    private Queue<File> mAudioFiles = new LinkedList<>();

    private Queue<Message> mMessages = new LinkedList<>();

    private List<Message> messages = new ArrayList<>();
    private MessageAdapter messageAdapter;

    private LinearLayoutManager linearLayoutManager;

    private ConnectedUserFragment connectedUserFragment;
    private SettingsFragment settingsFragment;

    private AudioManager amanager;

    private List<User> users;



    @Override
    public void onHideFragment() {
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, session.getSoundVolume(amanager), 0);
        blanketView.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_user_online)
    void showConnectedUsers() {
        if (users != null && users.size() > 1) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, connectedUserFragment)
                    .addToBackStack("connectedUsers");
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);

        connectedUserFragment = new ConnectedUserFragment();
        settingsFragment = new SettingsFragment();

        messageAdapter = new MessageAdapter(messages, new OnClickMessageListener() {
            @Override
            public void onClickMessage(Message message) {
                message.setRead(false);
                recyclerView.getAdapter().notifyDataSetChanged();
                mMessages.add(message);
                playAudios();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);


        progressView.setVisibility(View.VISIBLE);
        btnUserOnline.setVisibility(View.GONE);
        txtWhatHappen.setVisibility(View.GONE);
    //    txtNumberUsers.setVisibility(View.GONE);
        txtWhoSpeak.setVisibility(View.GONE);

        session = ((App) getApplication()).getSession();
        mSocket = ((App) getApplication()).getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(KEY_NEW_MESSAGE, onNewMessage);
        mSocket.on(KEY_JOIN_ROOM, onJoinRoom);
        mSocket.on(KEY_LEFT_ROOM, onLeftRoom);
        mSocket.on(KEY_NUMBER_USERS, onNumberUsers);
        mSocket.on(KEY_CONNECTED_USERS, onConnectedUsers);

        mSocket.connect();
/*
        if (session.isBeep())
            iconBeep.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_up_black_48dp));
        else
            iconBeep.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_off_black_48dp));  */


        amanager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

     //   int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

      //  System.out.println("Max volume " + maxVolume);
      //  System.out.println("session Max volume " + session.getSoundVolume());
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, session.getSoundVolume(amanager), 0);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayerForBeep.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @DebugLog
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                viewAvatar.setVisibility(View.GONE);
              //  pulsator.stop();

                if (session.isAutomaticPlay())
                    playAudios();
            }
        });


        mIvVoiceIndicators = new ArrayList<>();
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator1));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator2));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator3));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator4));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator5));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator6));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator7));

        mAudioRecorder = AudioRecorder.getInstance();
        mRxAudioPlayer = RxAudioPlayer.getInstance();
        mAudioRecorder.setOnErrorListener(this);

        btnSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        press2Record();
                        break;
                    case MotionEvent.ACTION_UP:
                        release2Send();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        release2Send();
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    @DebugLog
    public int getMaxVolume() {
        return amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    @OnClick(R.id.iconSettings)
    void navigateToSettings() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content, settingsFragment)
                .addToBackStack("settingsFragment");
        fragmentTransaction.commit();
        blanketView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
     //   finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

 /*   @OnClick(R.id.iconBeep)
    void changeBeepState() {
        if (session.isBeep()){
          //  iconBeep.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_off_black_48dp));
            session.setBeep(false);
            Toast.makeText(this, getString(R.string.beep_disabled), Toast.LENGTH_LONG).show();
        }
        else {
          //  iconBeep.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_up_black_48dp));
            session.setBeep(true);
            Toast.makeText(this, getString(R.string.beep_enabled), Toast.LENGTH_LONG).show();
        }
    }  */

    @Override
    protected void onDestroy() {
        if (mRxAudioPlayer != null) {
            mRxAudioPlayer.stopPlay();
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(KEY_NEW_MESSAGE, onNewMessage);
        mSocket.off(KEY_JOIN_ROOM, onJoinRoom);
        mSocket.off(KEY_LEFT_ROOM, onLeftRoom);
        mSocket.off(KEY_NUMBER_USERS, onNumberUsers);
        mSocket.off(KEY_CONNECTED_USERS, onConnectedUsers);

        super.onDestroy();
    }


    private void press2Record() {
        //btnSpeak.setBackgroundResource(R.drawable.button_press_to_say_pressed_bg);
     //   mTvRecordingHint.setText(R.string.voice_msg_input_hint_speaking);

        boolean isPermissionsGranted = RxPermissions.getInstance(getApplicationContext())
                .isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) && RxPermissions.getInstance(
                getApplicationContext()).isGranted(Manifest.permission.RECORD_AUDIO);
        if (!isPermissionsGranted) {
            RxPermissions.getInstance(getApplicationContext())
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            // not record first time to request permission
                            if (granted) {
                                Toast.makeText(getApplicationContext(), "Permission granted",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Permission not granted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } else {
            recordAfterPermissionGranted();
        }
    }

    private void recordAfterPermissionGranted() {
        mRecordSubscription = Single.just(true)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Boolean, Single<Boolean>>() {
                    @Override
                    public Single<Boolean> call(Boolean aBoolean) {
                        Log.d(TAG, "to play audio_record_start: " + R.raw.audio_record_start);
                        return mRxAudioPlayer.play(
                                PlayConfig.res(getApplicationContext(), R.raw.audio_record_start)
                                        .build());
                    }
                })
                .doOnSuccess(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.d(TAG, "audio_record_start play finished");
                    }
                })
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        mAudioFile = new File(
                                Environment.getExternalStorageDirectory().getAbsolutePath() +
                                        File.separator + System.nanoTime() + ".file.m4a");
                        Log.d(TAG, "to prepare record");
                        return mAudioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC,
                                MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
                                192000, 192000, mAudioFile);
                    }
                })
                .doOnSuccess(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.d(TAG, "prepareRecord success");
                    }
                })
                .flatMap(new Func1<Boolean, Single<Boolean>>() {
                    @Override
                    public Single<Boolean> call(Boolean aBoolean) {
                        Log.d(TAG, "to play audio_record_ready: " + R.raw.audio_record_ready);
                        return mRxAudioPlayer.play(
                                PlayConfig.res(getApplicationContext(), R.raw.audio_record_ready)
                                        .build());
                    }
                })
                .doOnSuccess(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.d(TAG, "audio_record_ready play finished");
                    }
                })
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        // TODO why need delay?
                        mFlIndicator.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mFlIndicator.setVisibility(View.VISIBLE);
                            }
                        }, SHOW_INDICATOR_DELAY_MILLIS);
                        return mAudioRecorder.startRecord();
                    }
                })
                .doOnSuccess(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.d(TAG, "startRecord success " + mediaPlayer.isPlaying());

                        isTalking = true;

                        mediaPlayer.pause();
                        lenghtAudio = mediaPlayer.getCurrentPosition();
                    }
                })
                .toObservable()
                .flatMap(new Func1<Boolean, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Boolean aBoolean) {
                        return RxAmplitude.from(mAudioRecorder);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer level) {
                        int progress = mAudioRecorder.progress();
                        Log.d(TAG, "amplitude: " + level + ", progress: " + progress);

                        refreshAudioAmplitudeView(level);

                        if (progress >= PRE_LIMIT) {
                            mTvRecordingHint.setText(String.format(
                                    getString(R.string.voice_msg_input_hint_time_limited_formatter),
                                    MAX_LENGTH_AUDIO - progress));
                            if (progress == MAX_LENGTH_AUDIO) {
                                release2Send();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void release2Send() {

       // btnSpeak.setBackgroundResource(R.drawable.button_press_to_say_bg);
        mFlIndicator.setVisibility(View.GONE);

        if (mRecordSubscription != null && !mRecordSubscription.isUnsubscribed()) {
            mRecordSubscription.unsubscribe();
            mRecordSubscription = null;
        }

        Log.d(TAG, "to play audio_record_end: " + R.raw.audio_record_end);
        mRxAudioPlayer.play(PlayConfig.res(getApplicationContext(), R.raw.audio_record_end).build())
                .doOnSuccess(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.d(TAG, "audio_record_end play finished");
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        int seconds = mAudioRecorder.stopRecord();
                        if (seconds >= MIN_AUDIO_LENGTH_SECONDS) {
                            mAudioFiles.offer(mAudioFile);
                            return true;
                        }
                        return false;
                    }
                })
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean added) {
                        if (added) {

                            System.out.println(humanReadableByteCount(mAudioFile.length(), false));
                            System.out.println(humanReadableByteCount(mAudioFile.length(), true));


                         /*       Message message = new Message(session.getUsername(), session.getUrlAvatar(), bytes);
                                String json = new Gson().toJson(message);
                                System.out.println(json);
                                System.out.println(message.toString());  */

                            if (session.isMessageConfirmation()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(TalkActivity.this);
                                builder.setMessage(getString(R.string.send_the_message))
                                        .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                try {
                                                    byte[] bytes = readFile(mAudioFile);
                                                    mSocket.emit(KEY_NEW_MESSAGE, bytes);
                                                    Toast.makeText(TalkActivity.this, getString(R.string.messaged_sended),
                                                            Toast.LENGTH_SHORT).show();
                                                    Answers.getInstance().logCustom(new CustomEvent("Messaging")
                                                            .putCustomAttribute("Send", 1));
                                                    dialogInterface.dismiss();

                                                    mAudioFile.delete();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        })
                                        .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                mAudioFile.delete();
                                            }
                                        }).show();
                            }
                            else {
                                try {
                                    byte[] bytes = readFile(mAudioFile);
                                    mSocket.emit(KEY_NEW_MESSAGE, bytes);
                                    Toast.makeText(TalkActivity.this, getString(R.string.messaged_sended),
                                            Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Answers.getInstance().logCustom(new CustomEvent("Messaging")
                                        .putCustomAttribute("Send", 1));

                                mAudioFile.delete();
                            }


                            isTalking = false;

                            if (lenghtAudio > 0 ) {
                                mediaPlayer.start();
                                mediaPlayer.seekTo(lenghtAudio);
                            }

                            lenghtAudio = 0;

                            if (session.isAutomaticPlay())
                                playAudios();

                            mTvLog.setText(
                                    mTvLog.getText() + "\n" + "audio file " + mAudioFile.getName() +
                                            " added");

                   //         mAudioFile.delete();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }



    private void refreshAudioAmplitudeView(int level) {
        int end = level < mIvVoiceIndicators.size() ? level : mIvVoiceIndicators.size();
        ButterKnife.apply(mIvVoiceIndicators.subList(0, end), VISIBLE);
        ButterKnife.apply(mIvVoiceIndicators.subList(end, mIvVoiceIndicators.size()), INVISIBLE);
    }


    @Override
    public void onError(int error) {
        Toast.makeText(this, "Error code: " + error, Toast.LENGTH_SHORT).show();
    }

    private void playMp3(byte[] mp3SoundByteArray) {
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("test", "m4a", getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();
            // In case you run into issues with threading consider new instance like:
            // MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    private void playAlert(byte[] mp3SoundByteArray) {

        if (session.isBeep()) {

            try {
                // create temp file that will hold byte array
                File tempMp3 = File.createTempFile("test0", "m4a", getCacheDir());
                tempMp3.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempMp3);
                fos.write(mp3SoundByteArray);
                fos.close();

                // resetting mediaplayer instance to evade problems
                mediaPlayerForBeep.reset();

                // In case you run into issues with threading consider new instance like:
                // MediaPlayer mediaPlayer = new MediaPlayer();

                // Tried passing path directly, but kept getting
                // "Prepare failed.: status=0x1"
                // so using file descriptor instead
                FileInputStream fis = new FileInputStream(tempMp3);
                mediaPlayerForBeep.setDataSource(fis.getFD());

                mediaPlayerForBeep.prepare();
                mediaPlayerForBeep.start();
            } catch (IOException ex) {
                String s = ex.toString();
                ex.printStackTrace();
            }
        }

    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("onConnect");
                    mSocket.emit(KEY_ADD_USER, session.getUsername());
                    mSocket.emit(KEY_ADD_URL_AVATAR, session.getUrlAvatar());
                    mSocket.emit(KEY_JOIN_ROOM, session.getRoom());

                    Answers.getInstance().logCustom(new CustomEvent("Room joined")
                            .putCustomAttribute("roomId", session.getRoom()));
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Disconnect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Arrays.toString(args));
                    Toast.makeText(TalkActivity.this, getString(R.string.error_connection),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectedUsers = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("onConnectedUsers");
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<User>>(){}.getType();
                    users = gson.fromJson(args[0].toString(), type);
                    connectedUserFragment.updateUsersList(users);
                }
            });
        }
    };


    private Emitter.Listener onNumberUsers = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("onNumberUsers");
                    JSONObject data = (JSONObject) args[0];
                    String numberUsers;
                    try {
                        numberUsers = data.getString("numberOfUsers");
                        btnUserOnline.setText(numberUsers);
                        btnUserOnline.setVisibility(View.VISIBLE);
                     //   txtNumberUsers.setText(getString(R.string.number_user, numberUsers));
                      //  txtNumberUsers.setVisibility(View.VISIBLE);
                        progressView.setVisibility(View.GONE);
                        Toast.makeText(TalkActivity.this, getString(R.string.you_are_connected), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onLeftRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("onLeftRoom");
                    JSONObject data = (JSONObject) args[0];
                    String numberUsers;
                    String username;
                    try {
                        numberUsers = data.getString("numberOfUsers");
                        username = data.getString("username");
                        btnUserOnline.setText(numberUsers);
                        btnUserOnline.setVisibility(View.VISIBLE);
                   //     txtNumberUsers.setText(getString(R.string.number_user, numberUsers));
                     //   txtNumberUsers.setVisibility(View.VISIBLE);

                        txtWhatHappen.setText(getString(R.string.left_room, username));
                        txtWhatHappen.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtWhatHappen.setVisibility(View.GONE);
                            }
                        }, 4000);
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onJoinRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String numberUsers;
                    try {
                        username = data.getString("username");
                        numberUsers = data.getString("numberOfUsers");
                        txtWhatHappen.setText(getString(R.string.join_room, username));
                        txtWhatHappen.setVisibility(View.VISIBLE);

                        btnUserOnline.setText(numberUsers);
                        btnUserOnline.setVisibility(View.VISIBLE);
                       // txtNumberUsers.setText(getString(R.string.number_user, numberUsers));
                       // txtNumberUsers.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtWhatHappen.setVisibility(View.GONE);
                            }
                        }, 4000);
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("onNewMessage");
                    System.out.println(Arrays.toString(args));

                    JSONObject data = (JSONObject) args[0];
                    String buffer;
                    String username;
                    String urlAvatar;
                    try {
                        buffer = data.getString("buffer");
                        username = data.getString("username");
                        urlAvatar = data.getString("url_avatar");

                        byte[] decodedString = Base64.decode(buffer, Base64.DEFAULT);

                        Message message = new Message(username, urlAvatar, decodedString);

                        messages.add(message);

                        recyclerView.getAdapter().notifyItemInserted(messages.size() - 1);

                        System.out.println("DB essage size : "+messages.size());
                        System.out.println("DB findLastVisibleItemPosition : "+linearLayoutManager.findLastVisibleItemPosition());

                        if (linearLayoutManager.findLastVisibleItemPosition() == messages.size() - 2) {
                            System.out.println("DB CONDITION OK");
                            recyclerView.scrollToPosition(messages.size() - 1);
                        }

                        mMessages.add(message);

                        Answers.getInstance().logCustom(new CustomEvent("Messaging")
                                .putCustomAttribute("Receive", 1));

                        if (session.isAutomaticPlay())
                            playAudios();

                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };

    private void playBip() {
        InputStream ins = getResources().openRawResource(
                getResources().getIdentifier("tilt",
                        "raw", getPackageName()));

        try {
            playAlert(FileUtils.readBytes(ins));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @DebugLog
    private void playAudios() {

        if (!mMessages.isEmpty() && !mediaPlayer.isPlaying() && !isTalking) {

            Message message = mMessages.poll();

            message.setRead(true);

            viewAvatar.setVisibility(View.VISIBLE);

            imageUser.setImageURI(Uri.parse(message.getUrlAvatar()));
            txtWhoSpeak.setText(getString(R.string.is_talking, message.getUsername()));
            txtWhoSpeak.setVisibility(View.VISIBLE);


         /*   ImagePipeline imagePipeline = Fresco.getImagePipeline();

            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(message.getUrlAvatar()))
                    .setRequestPriority(Priority.HIGH)
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .build();

            DataSource<CloseableReference<CloseableImage>> dataSource =
                    imagePipeline.fetchDecodedImage(imageRequest, this);

            try {
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @DebugLog
                    @Override
                    public void onNewResultImpl(@Nullable Bitmap bitmap) {
                        if (bitmap == null) {
                            Log.d(TAG, "Bitmap data source returned success, but bitmap null.");
                            return;
                        }
                        // The bitmap provided to this method is only guaranteed to be around
                        // for the lifespan of this method. The image pipeline frees the
                        // bitmap's memory after this method has completed.
                        //
                        // This is fine when passing the bitmap to a system process as
                        // Android automatically creates a copy.
                        //
                        // If you need to keep the bitmap around, look into using a
                        // BaseDataSubscriber instead of a BaseBitmapDataSubscriber.
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                Palette.Swatch vibrant = palette.getVibrantSwatch();
                                System.out.println(palette.getLightVibrantColor(vibrant.getRgb()));

                                // tabHost.setAccentColor(palette.getDarkVibrantColor(defaultColor));

                            }
                        });
                    }

                    @DebugLog
                    @Override
                    public void onFailureImpl(DataSource dataSource) {
                        // No cleanup required here
                    }
                }, CallerThreadExecutor.getInstance());
            } finally {
                if (dataSource != null) {
                    dataSource.close();
                }
            }

  */
            pulsator.setDuration(3000);
            pulsator.setCount(4);
            pulsator.start();

            recyclerView.getAdapter().notifyDataSetChanged();

            playBip();

            playMp3(message.getAudio());
        }
        else {
          //  viewAvatar.setVisibility(View.GONE);
          //  pulsator.stop();
        }

    }


}
