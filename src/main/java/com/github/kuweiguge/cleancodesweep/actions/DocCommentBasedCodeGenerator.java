package com.github.kuweiguge.cleancodesweep.actions;

import com.github.kuweiguge.cleancodesweep.notifications.Notifier;
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

/**
 * 根据文档注释添加一些代码
 *
 * @author zhengwei
 * @version 1.0
 * @since 2023/6/2 09:10
 */
public class DocCommentBasedCodeGenerator extends AnAction {
    ResourceBundle bundle = ResourceBundle.getBundle("messages.DocCommentBundle", Locale.getDefault());
    ResourceBundle notifyBundle = ResourceBundle.getBundle("messages.NotifyBundle", Locale.getDefault());
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        JPanel panel = createPanel(e, project);
        //创建一个
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

    @NotNull
    private JPanel createPanel(@NotNull AnActionEvent e, Project project) {
        JButton apiModelPropertyButton = new JButton(bundle.getString("addAp"));
        apiModelPropertyButton.addActionListener(e1 -> doAction(e, project, "@io.swagger.annotations.ApiModelProperty(value = ", ")"));
        JButton excelPropertyButton = new JButton(bundle.getString("addEx"));
        excelPropertyButton.addActionListener(e1 -> doAction(e, project, "@com.alibaba.excel.annotation.ExcelProperty(value = ", ")"));
        JButton jsonPropertyButton = new JButton(bundle.getString("addJs"));
        jsonPropertyButton.addActionListener(e1 -> doAction(e, project, "@com.fasterxml.jackson.annotation.JsonProperty(value = ", ")"));
        JLabel prefixLabel = new JLabel(bundle.getString("prefix"));
        prefixLabel.setToolTipText(bundle.getString("prefixTip"));
        JLabel suffixLabel = new JLabel(bundle.getString("suffix"));
        JTextField prefix = new JTextField(20);
        JTextField suffix = new JTextField(10);
        JButton okButton = new JButton(bundle.getString("ok"));
        okButton.addActionListener(e1 -> {
            if (prefix.getText().isEmpty() && suffix.getText().isEmpty()) {
                Notifier.notifyWarning(project, notifyBundle.getString("prefixOrSuffixEmpty"));
                return;
            }
            doAction(e, project, prefix.getText(), suffix.getText());
        });
        //三个按钮纵向排列，每个按钮占一行，剩下的俩个JLabel和两个文本框和按钮占一行
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel fixButtonPanel = new JPanel();
        fixButtonPanel.add(apiModelPropertyButton);
        fixButtonPanel.add(excelPropertyButton);
        fixButtonPanel.add(jsonPropertyButton);
        JPanel customPrefixPanel = new JPanel();
        customPrefixPanel.add(prefixLabel);
        customPrefixPanel.add(prefix);
        customPrefixPanel.add(suffixLabel);
        customPrefixPanel.add(suffix);
        customPrefixPanel.add(okButton);
        panel.add(fixButtonPanel);
        panel.add(customPrefixPanel);
        return panel;
    }

    public void doAction(AnActionEvent e, Project project, String prefix, String suffix) {
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
                    for (PsiField psiField : psiFields) {
                        //获取属性的文档注释
                        PsiDocComment docComment = psiField.getDocComment();
                        if (docComment == null) {
                            continue;
                        }
                        String commentText = docComment.getText();
                        //去文档注释的第二行，去掉前面的 *空格
                        String[] split = commentText.split("\n");
                        if (split.length < 2) {
                            continue;
                        }
                        String comment = split[1].split("\\*")[1].trim();
                        PsiAnnotation annotationFromText = elementFactory.createAnnotationFromText(prefix + "\"" + comment + "\"" + suffix, psiField);
                        //添加@ExcelProperty注解
                        WriteCommandAction.runWriteCommandAction(project, () -> {
                            psiField.addAfter(annotationFromText, psiField.getFirstChild());
                            JavaCodeStyleManager.getInstance(project).shortenClassReferences(javaFileClass);
                        });
                    }
                    WriteCommandAction.runWriteCommandAction(project, () -> {
                        CodeStyleManager.getInstance(project).reformat(javaFileClass);
                    });
                });
            }
        }
    }
}