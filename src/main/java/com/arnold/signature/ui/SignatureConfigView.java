package com.arnold.signature.ui;

import com.arnold.NotificationUtil;
import com.arnold.signature.config.Config;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.util.text.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SignatureConfigView extends JDialog {
    private JTextField buildTextField;
    private JTextField keyStoreTextField;
    private JTextField passwordTextField;
    private JTextField aliasTextField;
    private JTextField aliasPasswordTextField;
    private JButton submit;
    private JPanel contentPane;
    private SignatureConfigView.DialogCallback callback;

    public SignatureConfigView() {
        this(null);
    }

    public SignatureConfigView(SignatureConfigView.DialogCallback callback) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(submit);
        setTitle("签名参数编辑");
        setLocation(400, 200);//距离屏幕左上角的其实位置
        setSize(500, 350);//对话框的长宽

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
                if (callback != null) {
                    callback.onCancelBtnClicked();
                }
            }
        });

    }

    private void initData() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String buildToolsPath = propertiesComponent.getValue(Config.INSTANCE.getBUILD_TOOLS_PATH(), "");
        String keyStorePath = propertiesComponent.getValue(Config.INSTANCE.getKEY_STORE_PATH(), "");
        String password = propertiesComponent.getValue(Config.INSTANCE.getPASSWORD(), "");
        String alias = propertiesComponent.getValue(Config.INSTANCE.getALIAS(), "");
        String aliasPassword = propertiesComponent.getValue(Config.INSTANCE.getALIASPASSWORD(), "");
        buildTextField.setText(buildToolsPath);
        keyStoreTextField.setText(keyStorePath);
        passwordTextField.setText(password);
        aliasTextField.setText(alias);
        aliasPasswordTextField.setText(aliasPassword);
    }

    private void submit() {
        String buildToolsPath = buildTextField.getText();
        String keyStorePath = keyStoreTextField.getText();
        String password = passwordTextField.getText();
        String alias = aliasTextField.getText();
        String aliasPassword = aliasPasswordTextField.getText();

        if (StringUtil.isEmpty(buildToolsPath) ||
                StringUtil.isEmpty(keyStorePath) ||
                StringUtil.isEmpty(password) ||
                StringUtil.isEmpty(alias) ||
                StringUtil.isEmpty(aliasPassword)
        ) {
            NotificationUtil.warning("参数不能为空");
            return;
        }

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        propertiesComponent.setValue(Config.INSTANCE.getBUILD_TOOLS_PATH(), buildToolsPath);
        propertiesComponent.setValue(Config.INSTANCE.getKEY_STORE_PATH(), keyStorePath);
        propertiesComponent.setValue(Config.INSTANCE.getPASSWORD(), password);
        propertiesComponent.setValue(Config.INSTANCE.getALIAS(), alias);
        propertiesComponent.setValue(Config.INSTANCE.getALIASPASSWORD(), aliasPassword);
        NotificationUtil.info("保存成功");
        if (callback != null) {
            callback.onOkBtnClicked();
        }

        dispose();
    }

    public interface DialogCallback {
        void onOkBtnClicked();

        void onCancelBtnClicked();
    }
}
