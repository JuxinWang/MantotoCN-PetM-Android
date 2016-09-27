package com.petm.property.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.petm.property.R;
import com.petm.property.views.TasksCompletedView;

@SuppressLint("NewApi")
public class ProgressFragment extends DialogFragment {
	private TextView vLoading_text;
	private String mMsg = "正在加载···";
	private TasksCompletedView completedView;
	private int mTotalProgress;
	private int mCurrentProgress;
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_loading, null);
		initVariable();
		completedView = (TasksCompletedView) view.findViewById(R.id.progress);
		vLoading_text = (TextView) view.findViewById(R.id.loading_text);
		vLoading_text.setText(mMsg);
		completedView.setProgress(mCurrentProgress);
		Dialog dialog = new Dialog(getActivity(), R.style.MyLoadDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		return dialog;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	public void setMsg(String msg) {
		if (msg != null) {
			this.mMsg = msg;
		}
	}
	private void initVariable() {
		mTotalProgress = 100;
		mCurrentProgress = 0;
	}
	public void setProgress(int mCurrent){
		mCurrentProgress = mCurrent;
	}

}
