package ca.ualberta.moodroid;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTest2 {

    /*
        This test works, like loginTest, but again it needs sleep to function, kind of like a person
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void loginTest2() {
        ViewInteraction supportVectorDrawablesButton = onView(
                allOf(withId(R.id.email_button), withText("Sign in with email"),
                        childAtPosition(
                                allOf(withId(R.id.btn_holder),
                                        childAtPosition(
                                                withId(R.id.container),
                                                0)),
                                0)));
        supportVectorDrawablesButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email_layout),
                                        0),
                                0)));
        textInputEditText.perform(scrollTo(), replaceText("test123@test.ca"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_next), withText("Next"),
                        childAtPosition(
                                allOf(withId(R.id.email_top_layout),
                                        childAtPosition(
                                                withClassName(is("android.widget.ScrollView")),
                                                0)),
                                2)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.password_layout),
                                        0),
                                0)));
        textInputEditText2.perform(scrollTo(), replaceText("test123"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button_done), withText("Sign in"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.ic_profile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomnav),
                                        0),
                                3),
                        isDisplayed()));
        //sleep so we don't crash
        sleep(1000);
        bottomNavigationItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.profile_user_name_text_view), withText("test2"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("test2")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
