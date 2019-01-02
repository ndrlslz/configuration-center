//package com.ndrlslz.configuration.center.core.util;
//
//import com.ndrlslz.configuration.center.core.model.AsyncResult;
//import io.reactivex.Single;
//import io.reactivex.SingleOnSubscribe;
//
//import java.util.concurrent.Callable;
//
//import static io.reactivex.schedulers.Schedulers.io;
//
//public class AsyncHelper {
//    public static <T> AsyncResult<T> async(Callable<T> callable) {
//        return Single
//                .create((SingleOnSubscribe<AsyncResult<T>>) emitter -> {
//                    AsyncResult<T> asyncResult = new AsyncResult<>();
//                    try {
//                        T result = callable.call();
//                        asyncResult.success(result);
//                    } catch (Exception e) {
//                        asyncResult.fail(e);
//                    }
//                    emitter.onSuccess(asyncResult);
//                })
//                .subscribeOn(io())
//                .blockingGet();
//    }
//}