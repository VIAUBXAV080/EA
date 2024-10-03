package hu.bme.aut.composetestingdemo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.composetestingdemo.ui.theme.ComposeTestingDemoTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDemoWorks() {
        // Given
        // Start the app
        composeTestRule.setContent {
            ComposeTestingDemoTheme {
                DemoScreen()
            }
        }

        // When
        composeTestRule.onNodeWithText("Show demo").performClick()

        // Then
        composeTestRule.onNodeWithText(
            text = "Demo", substring = true).assertIsDisplayed()
    }
}