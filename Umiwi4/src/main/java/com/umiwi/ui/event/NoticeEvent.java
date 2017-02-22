package com.umiwi.ui.event;

/**
 * Created by txy on 15/11/4.
 */
public enum NoticeEvent {

    COUPON(10),
    MESSAGE(11),
    COIN_GOODS(12),
    ACTIVITY(13),
    SHAKE(14),

    ALL(500),

    /***/
    DEFAULT_VALUE(99999);

    private int value;

    public int getValue() {
        return value;
    }

    NoticeEvent(final int value) {
        this.value = value;
    }
}
