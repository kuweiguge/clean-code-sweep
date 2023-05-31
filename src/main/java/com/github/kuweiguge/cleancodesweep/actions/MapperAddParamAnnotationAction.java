package com.github.kuweiguge.cleancodesweep.actions;

import com.github.kuweiguge.cleancodesweep.notifications.Notifier;
import com.github.kuweiguge.cleancodesweep.utils.FileUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 给Mapper的方法添加@Param注解
 *
 * @author zhengwei
 * @version 1.0
 * @since 2023/6/1 10:16
 */
public class MapperAddParamAnnotationAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String paramAnnotation = "@Param";
        Project project = e.getProject();
        // 获取当前选中的文件列表
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files != null && project != null) {
            for (VirtualFile file : files) {
                FileUtils.recursiveFileSearch(file, f -> {
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(f);
                    if (psiFile == null) {
                        return;
                    }
                    if (!psiFile.getName().endsWith("Mapper.java")) {
                        return;
                    }
                    //添加import org.apache.ibatis.annotations.Param;语句
                    PsiClass listClass = JavaPsiFacade.getInstance(project).findClass("org.apache.ibatis.annotations.Param", psiFile.getResolveScope());
                    if (listClass == null) {
                        //弹气泡框提示缺少Ibatis依赖
                        Notifier.notifyError(project, "Please add Ibatis dependency first!");
                        return;
                    }
                    // 获取当前文件的所有PsiClass
                    PsiClass[] psiClasses = PsiTreeUtil.getChildrenOfType(psiFile, PsiClass.class);
                    if (psiClasses == null) {
                        return;
                    }
                    //判断是否已经导入了org.apache.ibatis.annotations.Param
                    boolean hasImport = false;
                    PsiImportList psiImportList = PsiTreeUtil.getChildOfType(psiFile, PsiImportList.class);
                    if (psiImportList != null) {
                        PsiImportStatement[] psiImportStatements = psiImportList.getImportStatements();
                        for (PsiImportStatement psiImportStatement : psiImportStatements) {
                            if (psiImportStatement.getText().contains("org.apache.ibatis.annotations.Param")) {
                                hasImport = true;
                                break;
                            }
                        }
                    }
                    for (PsiClass psiClass : psiClasses) {
                        //跳过非接口Mapper.java
                        if (!psiClass.isInterface()) {
                            continue;
                        }
                        //如果没有导入，则添加
                        if (!hasImport) {
                            PsiImportStatement psiImportStatement = JavaPsiFacade.getElementFactory(project).createImportStatement(listClass);
                            WriteCommandAction.runWriteCommandAction(project, () -> {
                                psiFile.addAfter(psiImportStatement, psiFile.getFirstChild());
                            });
                        }
                        // 获取当前类的所有方法
                        PsiMethod[] psiMethods = psiClass.getMethods();
                        for (PsiMethod psiMethod : psiMethods) {
                            // 获取方法的所有参数
                            PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();
                            for (PsiParameter psiParameter : psiParameters) {
                                // 获取参数的注解列表
                                PsiAnnotation[] psiAnnotations = psiParameter.getAnnotations();
                                boolean hasParamAnnotation = false;
                                for (PsiAnnotation psiAnnotation : psiAnnotations) {
                                    // 判断是否有@Param注解
                                    if (psiAnnotation.getText() != null && psiAnnotation.getText().startsWith(paramAnnotation)) {
                                        hasParamAnnotation = true;
                                        break;
                                    }
                                }
                                // 如果没有@Param注解，则添加
                                if (!hasParamAnnotation) {
                                    PsiAnnotation psiAnnotation = JavaPsiFacade.getElementFactory(project).createAnnotationFromText(paramAnnotation + "(\"" + psiParameter.getName() + "\")", psiParameter);
                                    WriteCommandAction.runWriteCommandAction(project, () -> {
                                        psiParameter.addBefore(psiAnnotation, psiParameter.getFirstChild());
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}