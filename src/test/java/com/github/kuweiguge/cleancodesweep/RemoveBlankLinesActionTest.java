package com.github.kuweiguge.cleancodesweep;

import com.github.kuweiguge.cleancodesweep.actions.RemoveBlankLinesAction;
import com.intellij.testFramework.TestDataPath;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

/**
 * @author zhengwei
 * @version 1.0
 * @since 2023/5/31 16:42
 */
@TestDataPath("$CONTENT_ROOT/testData/remove_blank_lines")
public class RemoveBlankLinesActionTest extends BasePlatformTestCase {
    public void test() {
        myFixture.configureByFile("foo.xml");
        myFixture.testAction(new RemoveBlankLinesAction());
        myFixture.checkResultByFile("foo.xml", "foo_after.xml", true);
    }
}