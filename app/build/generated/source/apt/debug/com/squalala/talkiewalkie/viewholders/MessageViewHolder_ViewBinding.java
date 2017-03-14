// Generated code from Butter Knife. Do not modify!
package com.squalala.talkiewalkie.viewholders;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squalala.talkiewalkie.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MessageViewHolder_ViewBinding implements Unbinder {
  private MessageViewHolder target;

  @UiThread
  public MessageViewHolder_ViewBinding(MessageViewHolder target, View source) {
    this.target = target;

    target.imageUser = Utils.findRequiredViewAsType(source, R.id.image_user_message, "field 'imageUser'", SimpleDraweeView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MessageViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageUser = null;
  }
}
