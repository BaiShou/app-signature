package com.arnold.signature.help

import com.arnold.signature.extend.execute
import com.arnold.signature.extend.text
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
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
//    project?.asyncTask(hintText = "正在签名", runAction = {
//        val signedApkPath = apkPath.substring(0, apkPath.length - 4) + "_signed.apk"
//        val apkSignerPath = buildToolsPath + System.getProperty("file.separator") + "apksigner"
//        // 构建shell命令
//        val shell =
//            "$apkSignerPath sign --ks $keyStorePath --v3-signing-enabled false --v4-signing-enabled false --ks-key-alias $alias --ks-pass pass:${password} --key-pass pass:${aliasPassword} --out $signedApkPath $apkPath"
//
//        val process = shell.execute()
//        val text = process.text()
//        val exitCode = process.waitFor()
//        if (exitCode != 0) {
//            throw IOException(text)
//        }
//
//    }, successAction = {
//        successAction?.invoke()
//    }, failAction = { throwable ->
//        failAction?.invoke("签名失败:$throwable")
//    })

    project?.asyncTask(hintText = "正在签名", runAction = {
        val signedApkPath = apkPath.substring(0, apkPath.length - 4) + "_signed.apk"
        val apkSignerPath = buildToolsPath + File.separator + "apksigner"

        // 确定操作系统
        val isWindows = System.getProperty("os.name").startsWith("Windows")

        // 构建命令列表
        val command = if (isWindows) {
            listOf(
                "cmd.exe", "/C",
                apkSignerPath,
                "sign",
                "--ks", keyStorePath,
                "--v3-signing-enabled", "false",
                "--v4-signing-enabled", "false",
                "--ks-key-alias", alias,
                "--ks-pass", "pass:${password}",
                "--key-pass", "pass:${aliasPassword}",
                "--out", signedApkPath,
                apkPath
            )
        } else {
            listOf(
                "bash", "-c",
                """
            $apkSignerPath sign \
            --ks "$keyStorePath" \
            --v3-signing-enabled false \
            --v4-signing-enabled false \
            --ks-key-alias "$alias" \
            --ks-pass pass:${password} \
            --key-pass pass:${aliasPassword} \
            --out "$signedApkPath" \
            "$apkPath"
            """.trimIndent()
            )
        }

        println("Executing command: ${command.joinToString(" ")}") // 打印命令用于调试
        try {
            // 使用 ProcessBuilder 执行命令
            val processBuilder = ProcessBuilder(command)
            processBuilder.redirectErrorStream(true) // 合并标准错误流和标准输出流
            val process = processBuilder.start()

            // 获取输出
            val text = process.inputStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()

            if (exitCode != 0) {
                throw IOException(text) // 如果返回码不为0，则表示命令执行失败
            }
        } catch (throwable: Throwable) {
            println("${throwable}")
            throw IOException(" ${throwable.message}")
        }

    }, successAction = {
        successAction?.invoke()
    }, failAction = { throwable ->
        failAction?.invoke("签名失败: $throwable")
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