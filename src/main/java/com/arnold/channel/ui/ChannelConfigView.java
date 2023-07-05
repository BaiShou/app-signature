package com.arnold.channel.ui;

import com.arnold.NotificationUtil;
import com.arnold.signature.config.Config;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.util.text.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChannelConfigView extends JDialog {
    private JPanel contentPane;
    private JButton save;
    private JTextArea etChannel;
    private JTextField etWallePath;
    private DialogCallback callback;

    public ChannelConfigView() {
        this(null);
    }

    public ChannelConfigView(DialogCallback callback) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(save);
        setTitle("渠道参数编辑");
        setLocation(400, 200);//距离屏幕左上角的其实位置
        setSize(500, 300);//对话框的长宽

        //绑定确定按钮事件
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });

        //绑定关闭按钮事件
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                if (callback != null) {
                    callback.onCancelBtnClicked();
                }
            }
        });

        initData();
    }


    private void initData() {

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String channel = propertiesComponent.getValue(Config.INSTANCE.getCHANNEL(), "");
        String walle_path = propertiesComponent.getValue(Config.INSTANCE.getWALLE_PATH(), "");
        etChannel.setText(channel);
        etWallePath.setText(walle_path);
    }


    private void submit() {
        String channel = etChannel.getText();
        String walle_path = etWallePath.getText();
        if (StringUtil.isEmpty(walle_path)){
            NotificationUtil.warning("瓦力路径不能为空");
            return;
        }
        if (StringUtil.isEmpty(channel)) {
            NotificationUtil.warning("渠道不能为空");
            return;
        }

        channel = channel.replace(System.lineSeparator(), ",");
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        propertiesComponent.setValue(Config.INSTANCE.getCHANNEL(), channel);
        propertiesComponent.setValue(Config.INSTANCE.getWALLE_PATH(), walle_path);

        NotificationUtil.info("保存成功");

        if (callback != null) {
            callback.onOkBtnClicked(channel,walle_path);
        }
        dispose();
    }

    public interface DialogCallback {
        void onOkBtnClicked(String channel,String walle_path);

        void onCancelBtnClicked();
    }

}
