package me.joybar.customdialogfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import me.dialogfragment.library.CustomDialogFragment;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				final CustomDialogFragment.Builder  builder= new CustomDialogFragment.Builder(R.layout.dialog_view,getSupportFragmentManager());
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialogInterface) {
						Log.i(TAG,"DismissListener");
					}
				}).setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {
						Log.i(TAG,"onCancel");
					}
				}).setDialogLifecycle(new CustomDialogFragment.DialogLifecycle() {
					@Override
					public void onViewCreated() {
						Log.i(TAG,"onViewCreated1");
						builder.getViewById(R.id.imv_close).setVisibility(View.GONE);
						builder.getViewById(R.id.tv_purchase).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Log.i(TAG,"tv_purchase");
								builder.disMiss();
							}
						});
					}
				});
				builder.show();


			}
		});

	}
}
