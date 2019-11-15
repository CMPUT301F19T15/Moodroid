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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SendFollowRequestAndTryToFollowAgainAfterTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sendFollowRequestAndTryToFollowAgainAfterTest() {
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
        textInputEditText.perform(scrollTo(), replaceText("test4@test.ca"), closeSoftKeyboard());

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
                allOf(withId(R.id.ic_friends),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomnav),
                                        0),
                                2),
                        isDisplayed()));
        //sleep to load
        sleep(1000);
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.toolbar_button_left),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                0),
                        isDisplayed()));
        //sleep to load
        sleep(1000);
        appCompatImageButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("test test"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.send), withText("Send"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        //sleep to load
        sleep(1000);
        appCompatButton3.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.toolbar_button_left),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.toolbar_button_left),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                0),
                        isDisplayed()));
        //sleep to load
        sleep(1000);
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test test"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.send), withText("Send"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        //sleep to load
        sleep(1000);
        appCompatButton4.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.toolbar_button_left),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                0),
                        isDisplayed()));
        //sleep to load
        sleep(1000);
        appCompatImageButton4.perform(click());
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
