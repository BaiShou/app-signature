package com.arnold.help

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile

//检查是否选中的是APK
fun AnActionEvent.checkApkFile(inexistAction: ((String) -> Unit)? = null, existAction: ((VirtualFile) -> Unit)? = null) = with(CommonDataKeys.VIRTUAL_FILE.getData(this.dataContext)) {
    if (this?.exists() == false || this?.name?.endsWith("apk") == false) {
        return@with inexistAction?.invoke("只能选择.apk文件")
    } else {
        return@with existAction?.invoke(this!!)
    }
}