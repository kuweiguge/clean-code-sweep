package com.github.kuweiguge.cleancodesweep.bundle

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

/**
 * @author zhengwei
 * @since 2023/6/9 10:29
 * @version 1.0
 */
@NonNls
private const val BUNDLE = "messages.DocCommentBundle"

object DocCommentBundle : DynamicBundle(BUNDLE) {
    @Suppress("SpreadOperator")
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getMessage(key, *params)

    @Suppress("SpreadOperator", "unused")
    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getLazyMessage(key, *params)
}