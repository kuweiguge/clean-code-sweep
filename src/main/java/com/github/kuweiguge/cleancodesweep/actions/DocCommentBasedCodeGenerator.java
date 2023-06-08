package com.github.kuweiguge.cleancodesweep.actions;

import com.github.kuweiguge.cleancodesweep.notifications.Notifier;
import com.github.kuweiguge.cleancodesweep.pages.CodeGeneratorPage;
import com.github.kuweiguge.cleancodesweep.utils.FileUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.github.kuweiguge.cleancodesweep.utils.FileUtils.operationField;

/**
 * 根据文档注释添加一些代码
 *
 * @author zhengwei
 * @version 1.0
 * @since 2023/6/2 09:10
 */
public class DocCommentBasedCodeGenerator extends AnAction {
    ResourceBundle bundle = ResourceBundle.getBundle("messages.DocCommentBundle", Locale.getDefault());
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        CodeGeneratorPage codeGeneratorPage = new CodeGeneratorPage(project,
                (Void) -> operationField(e, project, "@io.swagger.annotations.ApiModelProperty(value = ", ")", params -> consumer(params.project, params.prefix, params.suffix, params.elementFactory, params.psiField)),
                (Void) -> operationField(e, project, "@com.alibaba.excel.annotation.ExcelProperty(value = ", ")", params -> consumer(params.project, params.prefix, params.suffix, params.elementFactory, params.psiField)),
                (Void) -> operationField(e, project, "@com.fasterxml.jackson.annotation.JsonProperty(value = ", ")", params -> consumer(params.project, params.prefix, params.suffix, params.elementFactory, params.psiField)),
                (prefix, suffix) -> operationField(e, project, prefix, suffix, params -> consumer(params.project, params.prefix, params.suffix, params.elementFactory, params.psiField))
        );
        //创建一个
        JBPopupFactory.getInstance().createComponentPopupBuilder(codeGeneratorPage.getRootPane(), null)
                .setTitle(bundle.getString("title"))
                .setMovable(true)
                .setResizable(true)
                .setRequestFocus(true)
                .setNormalWindowLevel(false)
                .setCancelOnClickOutside(true)
                .setCancelKeyEnabled(true)
                .createPopup()
                .showCenteredInCurrentWindow(project);
    }

    public void consumer(Project project, String prefix, String suffix, PsiElementFactory elementFactory, PsiField psiField) {
        //判断字段是否已经添加了注解
        boolean hasAnnotation = psiField.hasAnnotation(FileUtils.getFqn(prefix));
        if (hasAnnotation) return;
        //获取属性的文档注释
        PsiDocComment docComment = psiField.getDocComment();
        if (docComment == null) return;
        String commentText = docComment.getText();
        //去文档注释的第二行，去掉前面的 *空格
        String[] split = commentText.split("\n");
        if (split.length < 2) return;
        String comment = split[1].split("\\*")[1].trim();
        PsiAnnotation annotation = elementFactory.createAnnotationFromText(prefix + "\"" + comment + "\"" + suffix, psiField);
        FileUtils.fieldAddModifier(project,psiField,annotation);
    }
}