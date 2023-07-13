package com.arnold.channel

import com.arnold.NotificationUtil
import com.arnold.channel.help.channel
import com.arnold.channel.ui.ChannelConfigView
import com.arnold.channel.ui.SelectApkChannelDialogView
import com.arnold.signature.config.Config
import com.arnold.signature.help.signature
import com.arnold.signature.ui.SignatureConfigView
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.text.StringUtil

/**
 * 选择Apk进行渠道打包
 */
class SelectApkChannelAction : AnAction(), SelectApkChannelDialogView.DialogCallback {

    var e: AnActionEvent? = null;
    override fun actionPerformed(e: AnActionEvent) {
        this.e = e
        SelectApkChannelDialogView(this).isVisible = true
    }

    override fun onOkBtnClicked(apkPath: String) {
        checkChannelConfig(e!!, apkPath);
    }

    override fun onCancelBtnClicked() {

    }

    private fun checkChannelConfig(e: AnActionEvent, apkPath: String) {
        val propertiesComponent = PropertiesComponent.getInstance()
        val channel: String = propertiesComponent.getValue(Config.CHANNEL) ?: ""
        val wallePath: String = propertiesComponent.getValue(Config.WALLE_PATH) ?: ""

        if (StringUtil.isEmpty(wallePath)) {
            popupChannelConfigDialog(labelTitle = "瓦力路径不能为空", e, apkPath)
            return
        }
        if (StringUtil.isEmpty(channel)) {
            popupChannelConfigDialog(labelTitle = "渠道不能为空", e, apkPath)
            return
        }

        channel(project = getEventProject(e), apkPath, channel, wallePath, successAction = {
            Messages.showWarningDialog("渠道打包成功", "来自渠道打包提示")
        }, failAction = { error ->
            Messages.showWarningDialog("渠道打包失败", "来自渠道打包提示")
        })
    }

    private fun popupChannelConfigDialog(labelTitle: String, e: AnActionEvent, apkPath: String) {
        Messages.showWarningDialog(labelTitle, "来自渠道打包提示")
        ChannelConfigView(object : ChannelConfigView.DialogCallback {

            override fun onOkBtnClicked(channel: String?, walle_path: String?) {
                checkChannelConfig(e, apkPath)
            }

            override fun onCancelBtnClicked() {
                Messages.showWarningDialog("取消渠道打包", "来自渠道打包提示")
            }
        }).isVisible = true

    }
}