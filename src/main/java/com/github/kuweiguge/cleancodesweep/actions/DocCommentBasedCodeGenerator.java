package com.github.kuweiguge.cleancodesweep.actions;

import com.github.kuweiguge.cleancodesweep.utils.FileUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogPanel;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.github.kuweiguge.cleancodesweep.panel.CodeGeneratorPaneKt.codeGeneratorPane;
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
        DialogPanel panel = codeGeneratorPane(e,
                addApEvent -> operationField(e, project, "@io.swagger.annotations.ApiModelProperty(value = ", ")", params -> consumer(params.project, params.prefix, params.suffix, params.elementFactory, params.psiField)),
                addExEvent -> operationField(e, project, "@com.fasterxml.jackson.annotation.JsonProperty(value = ", ")", params -> consumer(params.project, params.prefix, params.suffix, params.elementFactory, params.psiField)),
                addJsEvent -> operationField(e, project, "@com.alibaba.excel.annotation.ExcelProperty(value = ", ")", params -> consumer(params.project, params.prefix, params.suffix, params.elementFactory, params.psiField)),
                prefix -> operationField(e, project, prefix, ")", params -> consumer(params.project, params.prefix, params.suffix, params.elementFactory, params.psiField)));
        JBPopupFactory.getInstance().createComponentPopupBuilder(panel, null)
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