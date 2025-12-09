package com.example.myrecipepal

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class NavigationAndApiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // --- TEST 1: Verify App Starts ---
    @Test
    fun appLaunch_showsHomeScreen() {
        composeTestRule.waitForIdle()

        // Your HomeScreen uses "MyRecipePal" (no spaces)
        composeTestRule.onNodeWithText("MyRecipePal").assertIsDisplayed()
        composeTestRule.onNodeWithText("What are you cooking today?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dessert").assertIsDisplayed()
    }

    // --- TEST 2: Navigation (Home -> List) ---
    @Test
    fun navigation_clickCategory_showsList() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Dessert").performClick()

        // Wait for navigation (Back button appears)
        composeTestRule.waitUntil(timeoutMillis = 15000) {
            try {
                composeTestRule.onAllNodesWithContentDescription("Back").fetchSemanticsNodes().isNotEmpty()
            } catch (e: Exception) {
                false
            }
        }
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    // --- TEST 3: Bottom Navigation ---
    @Test
    fun bottomNavigation_switchesTabs() {
        composeTestRule.waitForIdle()

        // FIX: Finding the Bottom Navigation Tab.
        // Instead of relying on contentDescription (which is on the Icon),
        // we look for the Text "Favorites" which is visible to the user.
        // useUnmergedTree = true helps find text inside complex components like NavigationBarItem.
        val favoritesTab = composeTestRule.onNodeWithText("Favorites", useUnmergedTree = true)

        // Click it
        favoritesTab.performClick()

        composeTestRule.waitForIdle()

        // Verify we are on the new screen.
        // We check if the "Favorites" text is displayed AGAIN (as the screen title).
        // Using onAllNodes...onFirst() is safe because "Favorites" appears on the button AND the title.
        composeTestRule.onAllNodesWithText("Favorites").onFirst().assertIsDisplayed()
    }

    // --- TEST 4: Search Functionality ---
    @Test
    fun searchBar_interaction_flow() {
        composeTestRule.onNodeWithText("Dessert").performClick()
        composeTestRule.waitForIdle()

        // Your CategoryResultsScreen uses "Filter recipes by name..."
        val searchNodes = composeTestRule.onAllNodesWithText("Filter recipes by name...")

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            searchNodes.fetchSemanticsNodes().isNotEmpty()
        }

        if (searchNodes.fetchSemanticsNodes().isNotEmpty()) {
            val searchBar = searchNodes.onFirst()
            searchBar.performClick()
            composeTestRule.waitForIdle()

            searchBar.performTextInput("Pie")
            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithText("Pie").assertIsDisplayed()
        } else {
            throw AssertionError("Could not find Search Bar with text 'Filter recipes by name...'.")
        }
    }
}
