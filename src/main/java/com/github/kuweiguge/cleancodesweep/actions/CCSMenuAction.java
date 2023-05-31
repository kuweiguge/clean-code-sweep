package com.github.kuweiguge.cleancodesweep.actions;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import org.jetbrains.annotations.NotNull;

/**
 * 弹出菜单框
 *
 * @author zhengwei
 * @version 1.0
 * @since 2023/6/1 16:15
 */
public class CCSMenuAction extends AnAction {

    /**
     * 菜单项
     */
    public static final String[] ACTIONS = {
            "com.github.kuweiguge.cleancodesweep.actions.RemoveBlankLinesAction",
            "com.github.kuweiguge.cleancodesweep.actions.MapperAddParamAnnotationAction",
            "com.github.kuweiguge.cleancodesweep.actions.ToggleLineCommentAction",
            "com.github.kuweiguge.cleancodesweep.actions.AddCodeByDocCommentAction",
            "com.github.kuweiguge.cleancodesweep.actions.AddPropertyAction",
            "com.github.kuweiguge.cleancodesweep.actions.PathToFormAction"};

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            ActionGroup actionGroup = ActionUtil.getActionGroup(ACTIONS);
            if (actionGroup == null) {
                return;
            }
            ListPopup listPopup = JBPopupFactory.getInstance().createActionGroupPopup(
                    "Clean Code Sweep",
                    actionGroup,
                    e.getDataContext(),
                    JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                    true
            );
            listPopup.showCenteredInCurrentWindow(project);
        }
    }
}