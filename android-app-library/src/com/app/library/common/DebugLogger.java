package com.app.library.common;

/**
 * 调试LOG
 */
public final class DebugLogger {
	private static boolean DEBUG = false;

	public static void init(boolean debug) {
		DEBUG = debug;
	}

	public static void d(String tag, String messag) {
		if (DEBUG) {
			android.util.Log.d(tag, messag);
		}
	}
}