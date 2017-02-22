package cn.youmi.framework.main;

/**
 * 不同项目之间需要，同一个引用需要不同常量的定义，比如CookieOrigin
 * @author tangxiyong
 *
 */
public abstract class ConstantProvider {

	private static ConstantProvider sInstance;

	public static ConstantProvider getInstance() {
		return sInstance;
	}

	public static void setInstance(ConstantProvider um) {
		sInstance = um;
	}

	public abstract String setCookieOrigin();
}
