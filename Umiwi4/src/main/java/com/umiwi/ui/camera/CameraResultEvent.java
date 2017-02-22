package com.umiwi.ui.camera;

public enum CameraResultEvent {

	SUCC(1),
	
	MULTI_SUCC(2),
	
	SAVE(3),

	/** onError */
	ERROR(503),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	CameraResultEvent(final int value) {
		this.value = value;
	}

}
