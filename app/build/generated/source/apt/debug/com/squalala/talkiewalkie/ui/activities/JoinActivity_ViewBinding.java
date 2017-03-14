// Generated code from Butter Knife. Do not modify!
package com.squalala.talkiewalkie.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.squalala.talkiewalkie.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class JoinActivity_ViewBinding implements Unbinder {
  private JoinActivity target;

  private View view2131689606;

  private View view2131689605;

  @UiThread
  public JoinActivity_ViewBinding(JoinActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public JoinActivity_ViewBinding(final JoinActivity target, View source) {
    this.target = target;

    View view;
    target.editRoomName = Utils.findRequiredViewAsType(source, R.id.editRoomName, "field 'editRoomName'", EditText.class);
    view = Utils.findRequiredView(source, R.id.iconSettings, "method 'navitageToSettings'");
    view2131689606 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.navitageToSettings();
      }
    });
    view = Utils.findRequiredView(source, R.id.btnJoinRoom, "method 'navigateToJoinActivity'");
    view2131689605 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.navigateToJoinActivity();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    JoinActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.editRoomName = null;

    view2131689606.setOnClickListener(null);
    view2131689606 = null;
    view2131689605.setOnClickListener(null);
    view2131689605 = null;
  }
}
