package com.github.kuweiguge.cleancodesweep.dsl

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.JavaCodeFragmentFactory
import com.intellij.psi.PsiDocumentManager
import com.intellij.ui.EditorTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.toMutableProperty
import kotlin.reflect.KMutableProperty0

/**
 * @author zhengwei
 * @since 2023/6/9 16:48
 * @version 1.0
 */
fun Row.editorTextField(event: AnActionEvent, width :Int): Cell<EditorTextField> {
    val editor = event.getData(CommonDataKeys.EDITOR)
    val psiFile = PsiDocumentManager.getInstance(event.project!!).getPsiFile(editor?.document!!)
    val element = psiFile?.findElementAt(editor.caretModel.offset)
    //isClassesAccepted参数为true时，会拿到类的全限定名称
    val code = JavaCodeFragmentFactory.getInstance(event.project).createReferenceCodeFragment("", element, true, true)
    val document = PsiDocumentManager.getInstance(event.project!!).getDocument(code)
    val editorTextField = EditorTextField(document, event.project, JavaFileType.INSTANCE)
    editorTextField.setPreferredWidth(width)
    editorTextField.setOneLineMode(true)
    return cell(editorTextField)
}

fun <T : EditorTextField> Cell<T>.bindText(prop: KMutableProperty0<String>): Cell<T> {
    return bind(EditorTextField::getText, EditorTextField::setText,prop.toMutableProperty())
}