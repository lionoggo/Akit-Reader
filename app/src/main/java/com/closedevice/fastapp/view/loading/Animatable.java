package com.closedevice.fastapp.view.loading;

/**
 * Interface that drawables supporting animations should implement.
 */
public interface Animatable extends android.graphics.drawable.Animatable {
    /**
     * This is drawable animation frame duration
     */
    int FRAME_DURATION = 16;

    /**
     * This is drawable animation duration
     */
    int ANIMATION_DURATION = 250;
}