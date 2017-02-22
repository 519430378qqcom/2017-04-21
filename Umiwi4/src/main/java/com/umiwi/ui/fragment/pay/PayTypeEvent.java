package com.umiwi.ui.fragment.pay;

public enum PayTypeEvent {
	ALBUM(2),
	VIP(3),
	ZHUANTI(12),
	LECTURER(13);

	private int value;

	public int getValue() {
		return value;
	}

	PayTypeEvent(final int value) {
		this.value = value;
	}
	
	public String toString() {
		return String.valueOf(this.value);
	}

}
