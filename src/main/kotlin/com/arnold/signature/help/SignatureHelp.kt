package com.arnold.signature.help

import com.arnold.signature.extend.execute
import com.arnold.signature.extend.text
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException

fun signature(
        project: Project?,
        apkPath: String,
        buildToolsPath: String,
        keyStorePath: String,
        password: String,
        alias: String,
        aliasPassword: String,
        successAction: (() -> Unit)? = null,
        failAction: ((String) -> Unit)? = null
) {
    project?.asyncTask(hintText = "正在签名", runAction = {

        val signedApkPath = apkPath.substring(0, apkPath.length - 4) + "_signed.apk"
        //使用Jar命令进行签名
        val shell = "${buildToolsPath}/apksigner sign --ks $keyStorePath --v3-signing-enabled false --v4-signing-enabled false --ks-key-alias $alias --ks-pass pass:${password} --key-pass pass:${aliasPassword} --out $signedApkPath ${apkPath}"
        val process = shell.execute()
        val text = process.text()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw IOException(text)
        }
    }, successAction = {
        successAction?.invoke()
    }, failAction = {
        failAction?.invoke("签名失败")
    })
}

//创建后台异步任务的Project的扩展函数asyncTask
private fun Project.asyncTask(
        hintText: String,
        runAction: (ProgressIndicator) -> Unit,
        successAction: (() -> Unit)? = null,
        failAction: ((Throwable) -> Unit)? = null,
        finishAction: (() -> Unit)? = null
) {
    object : Task.Backgroundable(this, hintText) {
        override fun run(p0: ProgressIndicator) {
            runAction.invoke(p0)
        }

        override fun onSuccess() {
            successAction?.invoke()
        }

        override fun onThrowable(error: Throwable) {
            failAction?.invoke(error)
        }

        override fun onFinished() {
            finishAction?.invoke()
        }
    }.queue()
}