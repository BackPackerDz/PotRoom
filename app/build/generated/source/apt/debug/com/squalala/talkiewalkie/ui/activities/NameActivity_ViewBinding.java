// Generated code from Butter Knife. Do not modify!
package com.squalala.talkiewalkie.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squalala.talkiewalkie.R;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NameActivity_ViewBinding implements Unbinder {
  private NameActivity target;

  private View view2131689614;

  private View view2131689616;

  @UiThread
  public NameActivity_ViewBinding(NameActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NameActivity_ViewBinding(final NameActivity target, View source) {
    this.target = target;

    View view;
    target.editUsername = Utils.findRequiredViewAsType(source, R.id.editUsername, "field 'editUsername'", EditText.class);
    view = Utils.findRequiredView(source, R.id.backdrop, "field 'profileImage' and method 'selectImage'");
    target.profileImage = Utils.castView(view, R.id.backdrop, "field 'profileImage'", SimpleDraweeView.class);
    view2131689614 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.selectImage();
      }
    });
    target.progresView = Utils.findRequiredViewAsType(source, R.id.IndicatorView, "field 'progresView'", AVLoadingIndicatorView.class);
    view = Utils.findRequiredView(source, R.id.btnRecordUsername, "method 'checkUsername'");
    view2131689616 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.checkUsername();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    NameActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.editUsername = null;
    target.profileImage = null;
    target.progresView = null;

    view2131689614.setOnClickListener(null);
    view2131689614 = null;
    view2131689616.setOnClickListener(null);
    view2131689616 = null;
  }
}
