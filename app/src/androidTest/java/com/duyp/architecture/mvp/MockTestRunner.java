package com.duyp.architecture.mvp;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by duypham on 8/31/17.
 * Custom test runner for using {@link TestApplication} instead of {@link com.duyp.architecture.mvp.app.MyApplication}
 * Be sure to update testInstrumentationRunner with your custom runner's name (in module level gradle file)
 */

public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestApplication.class.getName(), context);
    }
}
