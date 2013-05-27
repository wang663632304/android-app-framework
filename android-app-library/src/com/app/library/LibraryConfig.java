package com.app.library;

import android.content.Context;

import com.app.library.common.DebugLogger;
import com.app.library.common.ResourceUtils;

/**
 * 基本库配置工具类
 * 
 * @author savant-pan
 * 
 */
public final class LibraryConfig {

	/**
	 * 初始化基本库（建议在Application子类中进行）
	 * 
	 * @param context
	 *            上下文对象
	 * @param debug
	 *            调试开关标志
	 */
	public static void init(Context context, boolean debug) {
		DebugLogger.init(debug);
		ResourceUtils.init(context);
	}
}
