package me.dialogfragment.library;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.Map;

/**
 * Created by joybar on 2017/9/1.
 */

public class CustomDialogFragment extends DialogFragment {
	@LayoutRes
	private int layoutResID;
	private boolean isShowTitle = false;
	private Map<Integer, View.OnLongClickListener> childrenIdLongClickListenerMap;
	private Map<Integer, View.OnClickListener> childrenIdClickListenerMap;
	private Map<Integer, Integer> invisibleViewMap;
	private Map<Integer, Integer> visibleViewMap;
	private Map<Integer, Integer> goneViewMap;
	private DialogInterface.OnDismissListener onDismissListener;
	private DialogInterface.OnCancelListener onCancelListener;

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
		View view = inflater.inflate(layoutResID, container, false);
		initView(view);
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

	private <T extends View> T findView(@IdRes int id, View view) {
		return (T) view.findViewById(id);
	}

	private void initView(View view) {
		for (Integer id : childrenIdClickListenerMap.keySet()) {
			findView(id, view).setOnClickListener(childrenIdClickListenerMap.get(id));
		}
		for (Integer id : childrenIdLongClickListenerMap.keySet()) {
			findView(id, view).setOnLongClickListener(childrenIdLongClickListenerMap.get(id));
		}
		for (Integer id : invisibleViewMap.keySet()) {
			findView(id, view).setVisibility(View.INVISIBLE);
		}
		for (Integer id : visibleViewMap.keySet()) {
			findView(id, view).setVisibility(View.VISIBLE);
		}
		for (Integer id : goneViewMap.keySet()) {
			findView(id, view).setVisibility(View.GONE);
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
		this.childrenIdClickListenerMap = builder.childrenIdClickListenerMap;
		this.childrenIdLongClickListenerMap = builder.childrenIdLongClickListenerMap;
		this.invisibleViewMap = builder.invisibleViewMap;
		this.visibleViewMap = builder.visibleViewMap;
		this.goneViewMap = builder.goneViewMap;
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
		private Map<Integer, View.OnLongClickListener> childrenIdLongClickListenerMap = new ArrayMap<>();
		private Map<Integer, View.OnClickListener> childrenIdClickListenerMap = new ArrayMap<>();
		private Map<Integer, Integer> invisibleViewMap = new ArrayMap<>();
		private Map<Integer, Integer> visibleViewMap = new ArrayMap<>();
		private Map<Integer, Integer> goneViewMap = new ArrayMap<>();

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

		public Builder setOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
			childrenIdClickListenerMap.put(id, onClickListener);
			return this;
		}

		public Builder setVisibleStatus(@IdRes int id, int visibleStatus) {
			if (visibleStatus == View.GONE) {
				goneViewMap.put(id, visibleStatus);
			} else if (visibleStatus == View.INVISIBLE) {
				invisibleViewMap.put(id, visibleStatus);
			} else if (visibleStatus == View.VISIBLE) {
				visibleViewMap.put(id, visibleStatus);
			}
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

		public void disMiss() {
			if (null != customDialog) {
				customDialog.dismiss();
			}
		}

	}

}
