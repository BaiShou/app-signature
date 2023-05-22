package com.arnold.tiny.ui;

import com.arnold.signature.config.Config;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.util.text.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TinyConfigView extends JDialog {

    private JTextField apiKeyEdit;
    private JButton submit;
    private JPanel contentPane;
    private DialogCallback callback;

    public TinyConfigView() {
        this(null);
    }

    public TinyConfigView(DialogCallback callback) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(submit);
        setTitle("签名参数编辑");
        setLocation(400, 200);//距离屏幕左上角的其实位置
        setSize(500, 200);//对话框的长宽

        initData();

        //绑定确定按钮事件
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });

        //绑定关闭按钮事件
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                if (callback!=null){
                    callback.onCancelBtnClicked();
                }
            }
        });
    }


    private void initData() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String apiKey = propertiesComponent.getValue(Config.INSTANCE.getTINY_API_KEY(), "");
        apiKeyEdit.setText(apiKey);
    }

    private void submit() {
        String apiKey = apiKeyEdit.getText();
        if (StringUtil.isEmpty(apiKey)) {
            Notifications.Bus.notify(
                    new Notification("ProjectViewPopupMenu", "警告", "key不能为空", NotificationType.INFORMATION)
            );
            return;
        }
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        propertiesComponent.setValue(Config.INSTANCE.getTINY_API_KEY(), apiKey);
        Notifications.Bus.notify(
                new Notification("ProjectViewPopupMenu", "提示", "保存成功", NotificationType.INFORMATION)
        );
        if (callback!=null){
            callback.onOkBtnClicked(apiKey);
        }
        dispose();
    }

    public interface DialogCallback {
        void onOkBtnClicked(String tinyPngKey);

        void onCancelBtnClicked();
    }
}
