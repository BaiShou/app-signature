package com.arnold.channel.help

import com.arnold.signature.extend.execute
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException

fun channel(project: Project?,
            apkPath: String,
            channel: String,
            wallePath: String,
            successAction: (() -> Unit)? = null,
            failAction: ((String) -> Unit)? = null) {

    project?.asyncTask(hintText = "正在渠道打包", runAction = {


        val shell = "java -jar $wallePath batch -c $channel  ${apkPath}"
        val process = shell.execute()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw IOException("失败了")
        }
    }, successAction = {
        successAction?.invoke()
    }, failAction = {
        failAction?.invoke("签名失败")
    });

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