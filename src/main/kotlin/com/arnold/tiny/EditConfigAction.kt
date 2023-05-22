package com.arnold.tiny

import com.arnold.tiny.ui.TinyConfigView
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class EditConfigAction  : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        TinyConfigView().setVisible(true);
    }
}