package com.arnold.signature

import com.arnold.help.checkApkFile
import com.arnold.signature.config.Config
import com.arnold.signature.help.signature
import com.arnold.signature.ui.SignatureConfigView
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile


class RightClickAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {

        e.checkApkFile(inexistAction = { hint ->
            Messages.showWarningDialog(hint, "来自签名提示")
        }, existAction = { file ->
            checkSignatureConfig(e, file)
        })

    }

    private fun checkSignatureConfig(e: AnActionEvent, file: VirtualFile) {
        val propertiesComponent = PropertiesComponent.getInstance()
        val buildToolsPath: String = propertiesComponent.getValue(Config.BUILD_TOOLS_PATH) ?: ""
        val keyStorePath: String = propertiesComponent.getValue(Config.KEY_STORE_PATH) ?: ""
        val password: String = propertiesComponent.getValue(Config.PASSWORD) ?: ""
        val alias: String = propertiesComponent.getValue(Config.ALIAS) ?: ""
        val aliasPassword: String = propertiesComponent.getValue(Config.ALIASPASSWORD) ?: ""

        if (StringUtil.isEmpty(buildToolsPath) || StringUtil.isEmpty(keyStorePath) ||
                StringUtil.isEmpty(password) || StringUtil.isEmpty(alias) || StringUtil.isEmpty(aliasPassword)) {
            popupSignatureConfigDialog(labelTitle = "参数不能为空", e, file)
            return
        }

        signature(project = getEventProject(e), file.path, buildToolsPath, keyStorePath, password, alias, aliasPassword, successAction = {
            Messages.showWarningDialog("签名成功", "来自签名提示")
        }, failAction = { error ->
            Messages.showWarningDialog("签名失败", "来自签名提示:\n${error}")
        })
    }


    private fun popupSignatureConfigDialog(labelTitle: String, e: AnActionEvent, file: VirtualFile) {


        Messages.showWarningDialog(labelTitle, "来自签名提示")

        SignatureConfigView(object : SignatureConfigView.DialogCallback {
            override fun onOkBtnClicked() {
                checkSignatureConfig(e, file)
            }

            override fun onCancelBtnClicked() {
                Messages.showWarningDialog("取消签名", "来自签名提示")
            }
        }).isVisible = true

    }

}