package com.itep.mt.common.view;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.itep.mt.common.R;


/**
 * 通用提示框
 */
public class GeneralDialog {
	Dialog dialog;
	private Button sure, cancel;
	private View line2;

	public Dialog showDialog(Context context) {
		dialog = new Dialog(context,R.style.CustomDialog);
		dialog.setContentView(R.layout.dialog_general);
		((TextView) dialog.findViewById(R.id.dwp_txt_content))
				.setText("是否要提交签名吗？");
		dialog.show();
		sure = (Button) dialog.findViewById(R.id.dwp_btn_queren);
		cancel = (Button) dialog.findViewById(R.id.dwp_btn_quxiao);
		line2 = dialog.findViewById(R.id.line2);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}

	/**
	 * @param context
	 * @param details
	 *            内容
	 * @return
	 */

	public Dialog showDialog(Context context, String details) {
		dialog = new Dialog(context, R.style.CustomDialog);
		dialog.setContentView(R.layout.dialog_general);
		((TextView) dialog.findViewById(R.id.dwp_txt_content)).setText(details);
		line2 = dialog.findViewById(R.id.line2);
		sure = (Button) dialog.findViewById(R.id.dwp_btn_queren);
		cancel = (Button) dialog.findViewById(R.id.dwp_btn_quxiao);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
		return dialog;
	}

	/**
	 * @param context
	 * @param title
	 *            标题
	 * @param details
	 *            内容
	 * @return
	 */
	public Dialog showDialog(Context context, String title, String details) {
		dialog = new Dialog(context, R.style.CustomDialog);
		dialog.setContentView(R.layout.dialog_general);
		line2 = dialog.findViewById(R.id.line2);
		((TextView) dialog.findViewById(R.id.dwp_txt_title)).setText(title);
		((TextView) dialog.findViewById(R.id.dwp_txt_content)).setText(details);
		dialog.show();
		sure = (Button) dialog.findViewById(R.id.dwp_btn_queren);
		cancel = (Button) dialog.findViewById(R.id.dwp_btn_quxiao);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}

	public void setOneButton(String str) {
		cancel.setText(str);
		sure.setVisibility(View.GONE);
		line2.setVisibility(View.INVISIBLE);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 确认按钮内容
	 *
	 * @param str
	 */
	public void setSureText(String str) {
		sure.setText(str);
	}

	/**
	 * 取消按钮
	 *
	 * @param str
	 */
	public void setCancelText(String str) {
		cancel.setText(str);
	}

	/**
	 * 确认键监听
	 *
	 * @param l
	 */
	public void sure(OnClickListener l) {
		sure.setOnClickListener(l);
	}

	/**
	 * 取消键监听
	 *
	 * @param l
	 */
	public void cancel(OnClickListener l) {
		cancel.setOnClickListener(l);
	}

	/**
	 * 关闭dialog
	 */
	public void dismiss() {
		dialog.dismiss();
	}

	public void setCancel(boolean bo) {
		dialog.setCancelable(bo);
	}

	public void setTitleVisible(int visibility) {
		((TextView) dialog.findViewById(R.id.dwp_txt_title))
				.setVisibility(visibility);
	}
}
