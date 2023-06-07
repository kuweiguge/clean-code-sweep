package com.github.kuweiguge.cleancodesweep.actions;

import com.github.kuweiguge.cleancodesweep.utils.FileUtils;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.jetbrains.annotations.NotNull;

/**
 * 给Mapper的方法添加@Param注解
 *
 * @author zhengwei
 * @version 1.0
 * @since 2023/6/1 10:16
 */
public class ParamAnnotationMapper extends AnAction {

    public static final String MAPPER_JAVA = "Mapper.java";
    public static final String AT = "@";
    public static final String LEFT_PAR = "(\"";
    public static final String RIGHT_PAR = "\")";
    private final String FULL_QUALIFIED_NAME = "org.apache.ibatis.annotations.Param";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        // 获取当前选中的文件或文件夹列表
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files != null && project != null) {
            JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
            for (VirtualFile file : files) {
                FileUtils.recursiveFileSearch(file, f -> {
                    PsiJavaFile javaFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(f);
                    //判断是否是Mapper文件
                    if (javaFile == null || !javaFile.getName().endsWith(MAPPER_JAVA)) return;
                    PsiClass javaFileClass = facade.findClass(javaFile.getPackageName() + "." + javaFile.getName().replace(".java", ""), javaFile.getResolveScope());
                    //跳过非接口的Mapper.java
                    if (javaFileClass == null || !javaFileClass.isInterface()) return;
                    // 获取当前类的所有方法
                    PsiMethod[] psiMethods = javaFileClass.getMethods();
                    for (PsiMethod psiMethod : psiMethods) {
                        // 获取方法的所有参数
                        PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();
                        for (PsiParameter psiParameter : psiParameters) {
                            // 判断当前参数是否有@Param注解
                            PsiAnnotation methodParamAnno = AnnotationUtil.findAnnotation(psiParameter, FULL_QUALIFIED_NAME);
                            // 如果没有@Param注解，则添加
                            if (methodParamAnno == null) {
                                PsiAnnotation psiAnnotation = JavaPsiFacade.getElementFactory(project).createAnnotationFromText(AT + FULL_QUALIFIED_NAME + LEFT_PAR + psiParameter.getName() + RIGHT_PAR, psiParameter);
                                WriteCommandAction.runWriteCommandAction(project, () -> {
                                    psiParameter.addBefore(psiAnnotation, psiParameter.getFirstChild());
                                    // 添加注解时，使用完全限定的名称，之后调用此方法，会自动导包，并且将注解替换为短名称
                                    JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiMethod);
                                });
                            }
                        }
                        WriteCommandAction.runWriteCommandAction(project, () -> {
                            CodeStyleManager.getInstance(project).reformat(psiMethod);
                        });
                    }
                });
            }
        }
    }
}