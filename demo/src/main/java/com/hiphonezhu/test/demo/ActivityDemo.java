package com.hiphonezhu.test.demo;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.task.TaskExecutor;
import com.android.baseline.framework.ui.BasicActivity;

/**
 * 网络请求测试
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo extends BasicActivity{
    private XLogic moduleLogic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_demo);

        moduleLogic = registLogic(new XLogic(this));

        // 网络请求
        findViewById(R.id.net_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                moduleLogic.getResult("18068440586");
            }
        });

        // 本地任务
        findViewById(R.id.task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                TaskExecutor.getInstance().execute(registTask(new ModuleTask(R.id.testTask, ActivityDemo.this)));
            }
        });
    }

    @Override
    protected void onCreateCustomToolBar(Toolbar toolbar) {
        // use custom Title Layout
        // super.onCreateCustomToolBar(toolbar);

        // use ToolBar
        toolbar.setTitle("标题");
        toolbar.setLogo(R.drawable.ic_launcher);
    }

    @Override
    protected boolean defaultTitleBarVisible() {
        return true;
    }

    @Override
    public void onResponse(Message msg) {
        super.onResponse(msg);
        switch (msg.what)
        {
            case R.id.mobilenumber:
                hideProgress();
                if (checkResponse(msg))
                {
                    InfoResult phoneResult = (InfoResult)msg.obj;
                    showToast(phoneResult.toString());
                }
                break;
            case R.id.testTask:
                hideProgress();
                if (checkResponse(msg))
                {
                    InfoResult infoResult = (InfoResult)msg.obj;
                    showToast(infoResult.toString());
                }
                break;
        }
    }
}