// Generated code from Butter Knife. Do not modify!
package com.squalala.talkiewalkie.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squalala.talkiewalkie.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import mehdi.sakout.fancybuttons.FancyButton;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class TalkActivity_ViewBinding implements Unbinder {
  private TalkActivity target;

  private View view2131689621;

  private View view2131689606;

  @UiThread
  public TalkActivity_ViewBinding(TalkActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TalkActivity_ViewBinding(final TalkActivity target, View source) {
    this.target = target;

    View view;
    target.btnSpeak = Utils.findRequiredViewAsType(source, R.id.btnSpeak, "field 'btnSpeak'", AppCompatButton.class);
    target.mFlIndicator = Utils.findRequiredViewAsType(source, R.id.mFlIndicator, "field 'mFlIndicator'", FrameLayout.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerViewMessages, "field 'recyclerView'", RecyclerView.class);
    target.blanketView = Utils.findRequiredView(source, R.id.blanketView, "field 'blanketView'");
    target.mTvLog = Utils.findRequiredViewAsType(source, R.id.mTvLog, "field 'mTvLog'", TextView.class);
    target.txtWhoSpeak = Utils.findRequiredViewAsType(source, R.id.txtWhoSpeak, "field 'txtWhoSpeak'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_user_online, "field 'btnUserOnline' and method 'showConnectedUsers'");
    target.btnUserOnline = Utils.castView(view, R.id.btn_user_online, "field 'btnUserOnline'", FancyButton.class);
    view2131689621 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showConnectedUsers();
      }
    });
    target.txtWhatHappen = Utils.findRequiredViewAsType(source, R.id.txtWhatHappen, "field 'txtWhatHappen'", TextView.class);
    target.mTvRecordingHint = Utils.findRequiredViewAsType(source, R.id.mTvRecordingHint, "field 'mTvRecordingHint'", TextView.class);
    target.pulsator = Utils.findRequiredViewAsType(source, R.id.pulsator, "field 'pulsator'", PulsatorLayout.class);
    target.viewAvatar = Utils.findRequiredView(source, R.id.viewAvatar, "field 'viewAvatar'");
    target.imageUser = Utils.findRequiredViewAsType(source, R.id.image_user, "field 'imageUser'", SimpleDraweeView.class);
    target.progressView = Utils.findRequiredView(source, R.id.viewProgress, "field 'progressView'");
    view = Utils.findRequiredView(source, R.id.iconSettings, "field 'iconSettings' and method 'navigateToSettings'");
    target.iconSettings = Utils.castView(view, R.id.iconSettings, "field 'iconSettings'", ImageView.class);
    view2131689606 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.navigateToSettings();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    TalkActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnSpeak = null;
    target.mFlIndicator = null;
    target.recyclerView = null;
    target.blanketView = null;
    target.mTvLog = null;
    target.txtWhoSpeak = null;
    target.btnUserOnline = null;
    target.txtWhatHappen = null;
    target.mTvRecordingHint = null;
    target.pulsator = null;
    target.viewAvatar = null;
    target.imageUser = null;
    target.progressView = null;
    target.iconSettings = null;

    view2131689621.setOnClickListener(null);
    view2131689621 = null;
    view2131689606.setOnClickListener(null);
    view2131689606 = null;
  }
}
