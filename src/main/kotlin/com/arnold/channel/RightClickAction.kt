package com.arnold.channel

import com.arnold.channel.help.channel
import com.arnold.channel.ui.ChannelConfigView
import com.arnold.help.checkApkFile
import com.arnold.signature.config.Config
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile

class RightClickAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.checkApkFile(inexistAction = { hint ->
            Messages.showWarningDialog(hint, "来自渠道打包提示")
        }, existAction = { file ->
            checkChannelConfig(e, file)
        })

    }

    private fun checkChannelConfig(e: AnActionEvent, file: VirtualFile) {
        val propertiesComponent = PropertiesComponent.getInstance()
        val channel: String = propertiesComponent.getValue(Config.CHANNEL) ?: ""
        val wallePath: String = propertiesComponent.getValue(Config.WALLE_PATH) ?: ""
        checkChannelConfig(e, file, channel, wallePath)
    }

    private fun checkChannelConfig(e: AnActionEvent, file: VirtualFile, channel: String?, wallePath: String?) {
        if (StringUtil.isEmpty(wallePath)) {
            popupChannelConfigDialog(labelTitle = "瓦力路径不能为空", e, file)
            return
        }
        if (StringUtil.isEmpty(channel)) {
            popupChannelConfigDialog(labelTitle = "渠道不能为空", e, file)
            return
        }
        channel(project = getEventProject(e), file, channel!!, wallePath!!, successAction = {
            Messages.showWarningDialog("渠道打包成功", "来自渠道打包提示")
        }, failAction = { error ->
            Messages.showWarningDialog("渠道打包失败", "来自渠道打包提示")
        })
    }


    private fun popupChannelConfigDialog(labelTitle: String, e: AnActionEvent, file: VirtualFile) {
        Messages.showWarningDialog(labelTitle, "来自渠道打包提示")
        ChannelConfigView(object : ChannelConfigView.DialogCallback {
            override fun onOkBtnClicked(channel: String?, walle_path: String?) {
                checkChannelConfig(e, file, channel, walle_path)
            }

            override fun onCancelBtnClicked() {
                Messages.showWarningDialog("取消渠道配置", "来自渠道打包提示")
            }
        }).isVisible = true
    }

}