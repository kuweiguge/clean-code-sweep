package com.github.kuweiguge.cleancodesweep.actions;

import com.github.kuweiguge.cleancodesweep.utils.FileUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

/**
 * 单行注释转多行注释
 *
 * @author zhengwei
 * @version 1.0
 * @since 2023/6/2 09:10
 */
public class LineCommentToggler extends AnAction {
    public static final String LINE_BREAK = "\n";
    public static final String JAVA_DOC_START = "/**";
    public static final String JAVA_DOC_END = "*/";
    public static final String SPACE = " * ";
    public static final String LINE_COMMENT_START = "//";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        // 获取当前选中的文件或文件夹列表
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files != null && project != null) {
            for (VirtualFile file : files) {
                JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
                FileUtils.recursiveFileSearch(file, f -> {
                    PsiJavaFile javaFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(f);
                    if (javaFile == null) return;
                    PsiClass javaFileClass = facade.findClass(javaFile.getPackageName() + "." + javaFile.getName().replace(".java", ""), javaFile.getResolveScope());
                    if (javaFileClass == null) return;
                    //获取当前类的属性列表
                    PsiField[] psiFields = javaFileClass.getFields();
                    //获取字段上的单行注释
                    for (PsiField psiField : psiFields) {
                        PsiDocComment docComment = psiField.getDocComment();
                        if (docComment != null) return;
                        PsiElement[] children = psiField.getChildren();
                        StringBuilder commentTextBuilder = new StringBuilder();
                        //收集所有单行注释
                        for (PsiElement child : children) {
                            if (child instanceof PsiComment) {
                                PsiComment comment = (PsiComment) child;
                                String commentText = comment.getText();
                                if (commentText.contains(LINE_COMMENT_START)) {
                                    commentText = commentText.replace(LINE_COMMENT_START, "").trim();
                                    commentTextBuilder.append(SPACE).append(commentText).append(LINE_BREAK);
                                    //删除单行注释
                                    WriteCommandAction.runWriteCommandAction(project, comment::delete);
                                }
                            }
                        }
                        String commentText = commentTextBuilder.toString();
                        //拼接JavaDoc注释
                        String finalReplace = JAVA_DOC_START + LINE_BREAK + commentText + JAVA_DOC_END;
                        //添加JavaDoc注释
                        WriteCommandAction.runWriteCommandAction(project, () -> {
                            PsiComment commentFromText = JavaPsiFacade.getElementFactory(project).createCommentFromText(finalReplace, psiField);
                            psiField.addBefore(commentFromText, psiField.getFirstChild());
                        });

                    }
                });
            }
        }
    }
}