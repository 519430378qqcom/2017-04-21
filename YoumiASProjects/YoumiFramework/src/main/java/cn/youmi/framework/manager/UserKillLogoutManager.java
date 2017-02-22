package cn.youmi.framework.manager;

import cn.youmi.framework.manager.UserKillLogoutManager.UserKillEvent;
import cn.youmi.framework.util.SingletonFactory;

/**
 * 
 * @author umiwi
 * 
 */
public class UserKillLogoutManager extends ModelManager<UserKillEvent, String> {

	public static UserKillLogoutManager getInstance() {
		return SingletonFactory.getInstance(UserKillLogoutManager.class);
	}

	public void setKillMessage(String message) {
		for (ModelManager.ModelStatusListener<UserKillEvent, String> l : listeners) {
			l.onModelUpdate(UserKillEvent.KILL_LOGOUT, message);
		}
	}

	public enum UserKillEvent {
		KILL_LOGOUT(1),

		DEFAULT_VALUE(99999);

		private int value;

		public int getValue() {
			return value;
		}

		UserKillEvent(final int value) {
			this.value = value;
		}
	}

}
