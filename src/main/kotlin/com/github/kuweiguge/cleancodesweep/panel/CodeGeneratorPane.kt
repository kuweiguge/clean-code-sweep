package com.github.kuweiguge.cleancodesweep.panel

import com.github.kuweiguge.cleancodesweep.bundle.DocCommentBundle
import com.github.kuweiguge.cleancodesweep.dsl.bindText
import com.github.kuweiguge.cleancodesweep.dsl.editorTextField
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.panel
import org.jetbrains.annotations.ApiStatus
import java.awt.event.ActionEvent
import java.util.function.Consumer

@ApiStatus.Internal
internal data class CodeGeneratorModel(
    var customAnnotation: String = "",
)

/**
 * 生成字段注解的面板
 */
fun codeGeneratorPane(
    event: AnActionEvent,
    addApActionPerformed: Consumer<ActionEvent>,
    addJsActionPerformed: Consumer<ActionEvent>,
    addExActionPerformed: Consumer<ActionEvent>,
    okConsumer: Consumer<String>
): DialogPanel {
    lateinit var panel: DialogPanel
    val model = CodeGeneratorModel()
    panel = panel {
        group(DocCommentBundle.message("commonGroup")) {
            row {
                button(DocCommentBundle.message("addAp")) {
                    addApActionPerformed.accept(it)
                }
            }
            row {
                button(DocCommentBundle.message("addJs")) {
                    addJsActionPerformed.accept(it)
                }
            }
            row {
                button(DocCommentBundle.message("addEx")) {
                    addExActionPerformed.accept(it)
                }
            }
        }
        group(DocCommentBundle.message("customGroup")) {
            row {
                editorTextField(event, 200)
                    .bindText(model::customAnnotation)
                    .comment(DocCommentBundle.message("tip"))
            }
            row {
                button(DocCommentBundle.message("ok")) {
                    //只有调用了apply方法，才会将数据绑定到model中
                    panel.apply()
                    //如果model.customAnnotation第一位不是@，则添加@，如果不包含(则在末尾添加(,如果包含)就去掉
                    if (!model.customAnnotation.startsWith("@")) {
                        model.customAnnotation = "@${model.customAnnotation}"
                    }
                    if (!model.customAnnotation.contains("(")) {
                        model.customAnnotation = "${model.customAnnotation}("
                    }
                    if (model.customAnnotation.contains(")")) {
                        model.customAnnotation = model.customAnnotation.replace(")", "")
                    }
                    okConsumer.accept(model.customAnnotation)
                }
            }
        }
    }
    return panel
}