package com.github.kuweiguge.cleancodesweep.actions;

import com.github.kuweiguge.cleancodesweep.utils.FileUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * 去除文件中的空白行
 */
public class RemoveBlankLinesAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取当前选中的文件
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files != null) {
            for (VirtualFile file : files) {
                FileUtils.recursiveFileSearch(file, FileUtils::removeDocumentBlankLine);
            }
        }
    }
}