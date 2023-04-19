package com.arnold.signature

import com.arnold.signature.config.Config
import com.arnold.signature.extend.execute
import com.arnold.signature.extend.text
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys


class RightClickAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext)

        if (file?.exists() == false || file?.name?.endsWith("apk") == false) {

            Notifications.Bus.notify(
                    Notification("ProjectViewPopupMenu", "警告", "文件只能选择.apk", NotificationType.INFORMATION)
            )
            return
        }
        val propertiesComponent = PropertiesComponent.getInstance()
        val buildToolsPath: String = propertiesComponent.getValue(Config.BUILD_TOOLS_PATH)?:""
        val keyStorePath: String = propertiesComponent.getValue(Config.KEY_STORE_PATH)?:""
        val password: String =propertiesComponent.getValue(Config.PASSWORD)?:""
        val alias: String = propertiesComponent.getValue(Config.ALIAS)?:""
        val aliasPassword: String = propertiesComponent.getValue(Config.ALIASPASSWORD)?:""


        val signedApkPath = file!!.path.substring(0, file.path.length - 4) + "_signed.apk"

        //使用Jar命令进行签名
        val shell = "${buildToolsPath}/apksigner sign --ks ${keyStorePath} --v3-signing-enabled false --v4-signing-enabled false --ks-key-alias ${alias} --ks-pass pass:${password} --key-pass pass:${aliasPassword} --out $signedApkPath ${file.path}"

        val process = shell.execute()
        val exitCode = process.waitFor()
        val text = process.text()

        Notifications.Bus.notify(
                Notification("ProjectViewPopupMenu", "签名结果", "${if (exitCode == 0) "成功" else "失败"}", NotificationType.INFORMATION)
        )
    }
}