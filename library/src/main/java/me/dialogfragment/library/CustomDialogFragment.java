package me.dialogfragment.library;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by joybar on 2017/9/1.
 */

public class CustomDialogFragment extends DialogFragment {
	@LayoutRes
	private int layoutResID;
	private boolean isShowTitle = false;
	private DialogInterface.OnDismissListener onDismissListener;
	private DialogInterface.OnCancelListener onCancelListener;
	private DialogLifecycle dialogLifecycle;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
//		Window win = getDialog().getWindow();
//		win.setBackgroundDrawable( new ColorDrawable((Color.parseColor("#FFFFFF"))));
//		DisplayMetrics dm = new DisplayMetrics();
//		getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
//		WindowManager.LayoutParams params = win.getAttributes();
//		params.gravity = Gravity.CENTER;
//		params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
//		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//		win.setAttributes(params);

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setDialogDefaultTitle(isShowTitle);
		view = inflater.inflate(layoutResID, container, false);
		if (null != dialogLifecycle) {
			dialogLifecycle.onViewCreated();
		}
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

	//	private <T extends View> T findView(@IdRes int id) {
//		return (T) getDialog().findViewById(id);
//	}
	private <T extends View> T findView(@IdRes int id) {
		return (T) view.findViewById(id);
	}

	private <T extends View> T findView(@IdRes int id, View view) {
		return (T) view.findViewById(id);
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

	private void setDialogDefaultTitle(boolean isShowTitle) {
		if (!isShowTitle) {
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	}


	public void show(FragmentManager manager) {
		show(manager, "dingtoneDialog");
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
		this.isShowTitle = builder.isShowTitle;
		this.onDismissListener = builder.onDismissListener;
		this.onCancelListener = builder.onCancelListener;
		this.dialogLifecycle = builder.dialogLifecycle;

	}

	public static class Builder {
		@LayoutRes
		private final int layoutResID;
		private final FragmentManager fragmentManager;
		private CustomDialogFragment customDialog = null;
		private boolean cancelable = true;
		private boolean isShowTitle = false;

		private DialogInterface.OnDismissListener onDismissListener;
		private DialogInterface.OnCancelListener onCancelListener;
		private DialogLifecycle dialogLifecycle;

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

		public Builder setShowTitle(boolean showTitle) {
			isShowTitle = showTitle;
			return this;
		}


		public Builder setDialogLifecycle(DialogLifecycle dialogLifecycle) {
			this.dialogLifecycle = dialogLifecycle;
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

		public View getViewById(@IdRes int id) {
			return create().findView(id);
		}

		public void show() {
			CustomDialogFragment customDialogFragment = create();
			customDialogFragment.show(fragmentManager);
		}

		public void show(String tag) {
			CustomDialogFragment customDialogFragment = create();
			customDialogFragment.show(fragmentManager, tag);
		}

		public void disMiss() {
			if (null != customDialog) {
				customDialog.dismiss();
			}
		}

	}

	public interface DialogLifecycle {
		void onViewCreated();
	}

}
