package com.arnold.tiny.help

import com.arnold.signature.config.Config
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.util.text.StringUtil

//检查ApiKey文件是否存在
fun checkApiKeyFile(inexistAction: ((String) -> Unit)? = null, existAction: ((String) -> Unit)? = null) = with(PropertiesComponent.getInstance().getValue(Config.TINY_API_KEY)) {
    if (StringUtil.isEmpty(this)) {//保存API_KEY的文件不存在, 或者读取的文件内容为空，提示用户输入(一般为第一次)
        return@with inexistAction?.invoke("请输入TinyPng Key, 请往TinyPng官网申请")
    } else {//为了减少不必要API KEY检查次数，此处改为只要文件存在，直接读取文件中的API_KEY
        return@with existAction?.invoke(this!!)//(注意:此时API_KEY有可能不合法，此处先不做检验，而是去捕获认证异常，提示用户重新键入API_KEY)
    }
}