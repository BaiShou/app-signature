package com.arnold.signature

import com.arnold.signature.ui.SignatureConfigView
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class EditConfigAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        SignatureConfigView().setVisible(true);
    }
}