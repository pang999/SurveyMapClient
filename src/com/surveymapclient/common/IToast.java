package com.surveymapclient.common;

import android.content.Context;
import android.widget.Toast;

public class IToast {
	public static void show(Context context, int resId) {
		show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, int duration) {
		show(context, context.getResources().getText(resId), duration);
	}

	public static void show(Context context, CharSequence text) {
		show(context, text, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, Object... args) {
		show(context,
				String.format(context.getResources().getString(resId), args),
				Toast.LENGTH_SHORT);
	}

	public static void show(Context context, String format, Object... args) {
		show(context, String.format(format, args), Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, int duration,
			Object... args) {
		show(context,
				String.format(context.getResources().getString(resId), args),
				duration);
	}

	public static void show(Context context, String format, int duration,
			Object... args) {
		show(context, String.format(format, args), duration);
	}

	public static void show(Context context, CharSequence text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

//	public static void centerShow(Context context, String message) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View view = inflater.inflate(R.layout.toast_info, null);
//		TextView toastInfo = (TextView) view.findViewById(R.id.toast_info);
//		toastInfo.setText(message);
//		Toast toast = new Toast(context);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.setView(view);
//		toast.setDuration(1000);
//		toast.show();
//	}

	/** 请求处理结果，Toast消息通知 */
	public static void showToast(Context context, int toastId) {
		String toastContent = "处理错误";
		switch (toastId) {
		case -1:
			toastContent = "请先登录!";
			break;

		case 234:
			toastContent = "验证码校验失败";
			break;
		}
		Toast.makeText(context, toastContent, Toast.LENGTH_SHORT).show();
	}
}
