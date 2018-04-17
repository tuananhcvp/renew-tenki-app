package com.example.tuananh.weatherforecast.utils.usecase;

import io.reactivex.Scheduler;

/**
 * Created by anh on 2018/04/16.
 */

public abstract class UseCase {

    protected Scheduler threadExecutor;
    protected Scheduler postExecutionThread;

    protected UseCase(Scheduler threadExecutor,
                      Scheduler postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }
}
