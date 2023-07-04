package com.arnold.channel

import com.arnold.channel.ui.ChannelConfigView
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class EditChannelAction  : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ChannelConfigView().setVisible(true);
    }
}