// Generated code from Butter Knife. Do not modify!
package com.squalala.talkiewalkie.viewholders;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squalala.talkiewalkie.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UserViewHolder_ViewBinding implements Unbinder {
  private UserViewHolder target;

  @UiThread
  public UserViewHolder_ViewBinding(UserViewHolder target, View source) {
    this.target = target;

    target.imageUser = Utils.findRequiredViewAsType(source, R.id.image_user, "field 'imageUser'", SimpleDraweeView.class);
    target.txtUsername = Utils.findRequiredViewAsType(source, R.id.txtUsername, "field 'txtUsername'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UserViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageUser = null;
    target.txtUsername = null;
  }
}
