package com.github.kuweiguge.cleancodesweep.utils;

import com.github.kuweiguge.cleancodesweep.actions.BlankLinesRemover;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.function.Consumer;

/**
 * @author zhengwei
 * @version 1.0
 * @since 2023/6/1 10:18
 */
public class FileUtils {
    private static final Logger LOG = Logger.getInstance(BlankLinesRemover.class);

    /**
     * 递归方法，如果是文件夹就递归，如果是文件就执行文件操作
     */
    public static void recursiveFileSearch(VirtualFile file, Consumer<VirtualFile> consumer) {
        if (file == null || !file.exists()) {
            LOG.warn("BlankLinesRemover failed: file is null or not exists");
            return;
        }
        if (file.isDirectory()) {
            VirtualFile[] children = file.getChildren();
            if (children != null) {
                for (VirtualFile child : children) {
                    if (child.isDirectory()) {
                        recursiveFileSearch(child, consumer);
                    } else {
                        consumer.accept(child);
                    }
                }
            }
        } else {
            consumer.accept(file);
        }
    }

    /**
     * 去除文件中的空白行
     *
     * @param file 文件
     */
    public static void removeDocumentBlankLine(VirtualFile file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            LOG.warn("BlankLinesRemover failed: file is null or not exists or is directory");
            return;
        }
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) {
            // 获取文件内容
            String content = document.getText();
            // 去除文件空白行的逻辑
            String newContent = content.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
            //安全写入
            ApplicationManager.getApplication().runWriteAction(() -> document.setText(newContent));
        }
    }
}