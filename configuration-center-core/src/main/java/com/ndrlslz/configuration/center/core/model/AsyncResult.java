//package com.ndrlslz.configuration.center.core.model;
//
//public class AsyncResult<T> {
//    private T result;
//    private Exception exception;
//
//    public void success(T object) {
//        this.result = object;
//    }
//
//    public void fail(Exception e) {
//        this.exception = e;
//    }
//
//    public boolean succeeded() {
//        return exception == null;
//    }
//
//    public boolean failed() {
//        return exception != null;
//    }
//
//    public T getResult() {
//        return result;
//    }
//
//    public void setResult(T result) {
//        this.result = result;
//    }
//
//    public Exception getException() {
//        return exception;
//    }
//
//    public void setException(Exception exception) {
//        this.exception = exception;
//    }
//}
