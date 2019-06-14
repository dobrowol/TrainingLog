package com.dobrowol.traininglog.util;

public class Event<T> {
    private T content;

    Event(T content) {
        this.content = content;
    }

    boolean hasBeenHandled = false;

    private void set(T content) {
    } // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    T peekContent() {

        return content;
    }
}