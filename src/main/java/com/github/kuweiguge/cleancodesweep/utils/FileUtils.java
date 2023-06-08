package com.github.kuweiguge.cleancodesweep.utils;

import com.github.kuweiguge.cleancodesweep.actions.BlankLinesRemover;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

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

    /**
     * 对类字段进行操作
     *
     * @param e       事件
     * @param project 项目
     * @param prefix  前缀
     * @param suffix  后缀
     */
    public static void operationField(AnActionEvent e, Project project, String prefix, String suffix, Consumer<Params> consumer) {
        // 获取当前选中的文件
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files != null && project != null) {
            JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
            for (VirtualFile file : files) {
                FileUtils.recursiveFileSearch(file, f -> {
                    PsiJavaFile javaFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(f);
                    if (javaFile == null) return;
                    // 获取当前文件的所有PsiClass
                    PsiClass javaFileClass = facade.findClass(javaFile.getPackageName() + "." + javaFile.getName().replace(".java", ""), javaFile.getResolveScope());
                    if (javaFileClass == null) return;
                    //获取当前类的属性列表
                    PsiField[] psiFields = javaFileClass.getFields();
                    //获取字段上的单行注释
                    for (PsiField psiField : psiFields) {
                        Params params = new Params(project, prefix, suffix, elementFactory, psiField);
                        consumer.accept(params);
                    }
                    WriteCommandAction.runWriteCommandAction(project, () -> {
                        CodeStyleManager.getInstance(project).reformat(javaFileClass);
                    });
                });
            }
        }
    }

    public static class Params {
        public final Project project;
        public final String prefix;
        public final String suffix;
        public final PsiElementFactory elementFactory;
        public final PsiField psiField;

        Params(Project project, String prefix, String suffix, PsiElementFactory elementFactory, PsiField psiField) {
            this.project = project;
            this.prefix = prefix;
            this.suffix = suffix;
            this.elementFactory = elementFactory;
            this.psiField = psiField;
        }
    }

    /**
     * 获取注解的全限定名 从 @NotNull(message = "xxx") 中获取 NotNull
     *
     * @param anno 注解
     * @return 全限定名
     */
    public static String getFqn(String anno) {
        int beginIndex = anno.indexOf("@") + 1;
        int endIndex = anno.indexOf("(");
        return anno.substring(beginIndex, endIndex);
    }

    /**
     * 字段添加Modifier
     */
    public static void fieldAddModifier(Project project, PsiField psiField, PsiElement psiElement) {
        PsiModifierList modifierList = psiField.getModifierList();
        if (modifierList == null) return;
        //添加@ExcelProperty注解
        WriteCommandAction.runWriteCommandAction(project, () -> {
            modifierList.addBefore(psiElement, modifierList.getFirstChild());
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiField);
        });
    }

}