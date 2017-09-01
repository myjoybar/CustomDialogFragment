package me.dialogfragment.library;

/**
 * Created by joybar on 2017/9/1.
 */


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

/**
 * Created by joybar on 2017/9/1.
 */

public class CustomDialogFragment extends DialogFragment {
	@LayoutRes
	private int layoutResID;
	private Map<Integer, View.OnLongClickListener> childrenIdLongClickListenerMap;
	private Map<Integer, View.OnClickListener> childrenIdClickListenerMap;
	private DialogInterface.OnDismissListener onDismissListener;
	private DialogInterface.OnCancelListener onCancelListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(layoutResID, container);
		loopAndSetListener(view);
		return view;
	}

	/**
	 * @param listenerInterface
	 * @param <T>
	 * @return
	 */
	private <T> T getDialogListener(Class<T> listenerInterface) {
		final Fragment targetFragment = getTargetFragment();
		if (targetFragment != null && listenerInterface.isAssignableFrom(targetFragment.getClass())) {
			return (T) targetFragment;
		}
		if (getActivity() != null && listenerInterface.isAssignableFrom(getActivity().getClass())) {
			return ((T) getActivity());
		}
		return null;
	}

	private <T extends View> T findView(@IdRes int id) {
		return (T) getDialog().findViewById(id);
	}
	private <T extends View> T findView(@IdRes int id,View view) {
		return (T) view.findViewById(id);
	}
	private void loopAndSetListener(View view){
		for (Integer id : childrenIdClickListenerMap.keySet()) {
			findView(id,view).setOnClickListener(childrenIdClickListenerMap.get(id));
		}
		for (Integer id : childrenIdLongClickListenerMap.keySet()) {
			findView(id,view).setOnLongClickListener(childrenIdLongClickListenerMap.get(id));
		}
	}


	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (onDismissListener != null) {
			onDismissListener.onDismiss(dialog);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		if (onCancelListener != null) {
			onCancelListener.onCancel(dialog);
		}
	}


	public void show(FragmentManager manager) {
		show(manager, "CustomDialogFragment");
	}
	@Override
	public void show(FragmentManager manager, String tag) {
		try {
			super.show(manager, tag);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void dismiss() {
		try {
			super.dismiss();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public CustomDialogFragment(Builder builder) {
		this.layoutResID = builder.layoutResID;
		this.onDismissListener = builder.onDismissListener;
		this.onCancelListener = builder.onCancelListener;
		this.childrenIdClickListenerMap = builder.childrenIdClickListenerMap;
		this.childrenIdLongClickListenerMap = builder.childrenIdLongClickListenerMap;
	}

	public static class Builder {
		@LayoutRes
		private final int layoutResID;
		private final FragmentManager fragmentManager;
		private CustomDialogFragment customDialog = null;
		private boolean cancelable = true;

		private DialogInterface.OnDismissListener onDismissListener;
		private DialogInterface.OnCancelListener onCancelListener;
		private Map<Integer, View.OnLongClickListener> childrenIdLongClickListenerMap = new ArrayMap<>();
		private Map<Integer, View.OnClickListener> childrenIdClickListenerMap = new ArrayMap<>();

		public Builder(int layoutResID, FragmentManager fragmentManager) {
			this.layoutResID = layoutResID;
			this.fragmentManager = fragmentManager;
		}

		public CustomDialogFragment create() {
			if (null == customDialog) {
				customDialog = new CustomDialogFragment(this);
				customDialog.setCancelable(cancelable);
			}
			return customDialog;
		}


		public Builder setCancelable(boolean cancelable) {
			this.cancelable = cancelable;
			return this;
		}

		public Builder setOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
			childrenIdClickListenerMap.put(id, onClickListener);
			return this;
		}

		public Builder setOnLongClickListener(@IdRes int id, View.OnLongClickListener onLongClickListener) {
			childrenIdLongClickListenerMap.put(id, onLongClickListener);
			return this;
		}

		public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
			this.onDismissListener = onDismissListener;
			return this;
		}

		public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
			this.onCancelListener = onCancelListener;
			return this;
		}

		public void show() {
			CustomDialogFragment customDialogFragment = create();
			customDialogFragment.show(fragmentManager);
		}

		public void show(String tag) {
			CustomDialogFragment customDialogFragment = create();
			customDialogFragment.show(fragmentManager, tag);
		}

		public void disMiss(){
			if(null!=customDialog){
				customDialog.dismiss();
			}
		}

	}

}
