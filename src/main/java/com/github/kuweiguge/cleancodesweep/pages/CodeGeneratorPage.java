package com.github.kuweiguge.cleancodesweep.pages;

import com.github.kuweiguge.cleancodesweep.notifications.Notifier;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author zhengwei
 * @version 1.0
 * @since 2023/6/7 17:29
 */
public class CodeGeneratorPage {
    private JButton apiModelPropertyButton;
    private JButton excelPropertyButton;
    private JButton jsonPropertyButton;
    private JTextField prefix;
    private JTextField suffix;
    private JButton okButton;
    private JPanel rootPane;
    ResourceBundle notifyBundle = ResourceBundle.getBundle("messages.NotifyBundle", Locale.getDefault());

    public CodeGeneratorPage(Project project, Consumer<Void> apiModelPropertyButtonListener, Consumer<Void> excelPropertyButtonListener, Consumer<Void> jsonPropertyButtonListener, BiConsumer<String, String> okButtonListener) {
        apiModelPropertyButton.addActionListener(e -> apiModelPropertyButtonListener.accept(null));
        excelPropertyButton.addActionListener(e -> excelPropertyButtonListener.accept(null));
        jsonPropertyButton.addActionListener(e -> jsonPropertyButtonListener.accept(null));
        okButton.addActionListener(e -> {
            if (prefix.getText().isEmpty() && suffix.getText().isEmpty()) {
                Notifier.notifyWarning(project, notifyBundle.getString("prefixOrSuffixEmpty"));
            } else {
                okButtonListener.accept(prefix.getText(), suffix.getText());
            }
            SwingUtilities.getWindowAncestor(rootPane).dispose();
        });
    }

    public JPanel getRootPane() {
        return rootPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}