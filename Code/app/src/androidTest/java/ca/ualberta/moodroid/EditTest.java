package ca.ualberta.moodroid;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void editTest() {
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
        textInputEditText.perform(scrollTo(), replaceText("intent@test.ca"), closeSoftKeyboard());

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
        sleep(1000);
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.toolbar_button_left),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                0),
                        isDisplayed()));
        sleep(1000);
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.Annoyed), withText("\uD83D\uDE12"),
                        childAtPosition(
                                allOf(withId(R.id.wheel),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        sleep(1000);
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.centerButton), withText("\uD83D\uDE12"),
                        childAtPosition(
                                allOf(withId(R.id.wheel),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.mood_detail_reason),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("yes"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.add_detail_confirm_btn), withText("Add To History"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction cardView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.mood_list_view),
                                childAtPosition(
                                        withId(R.id.body),
                                        1)),
                        0),
                        isDisplayed()));
        sleep(1000);
        cardView.perform(longClick());


        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("Edit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        sleep(1000);
        appCompatButton6.perform(scrollTo(), click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.add_location_button), withText("add location"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1)));
        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction button = onView(
                allOf(withId(R.id.addLocationBtn), withText("Add Location"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        sleep(1000);
        button.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.social_situation),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(4);
        sleep(1000);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.add_detail_confirm_btn), withText("Add To History"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0)));
        appCompatButton8.perform(scrollTo(), click());

        ViewInteraction cardView3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.mood_list_view),
                                childAtPosition(
                                        withId(R.id.body),
                                        1)),
                        0),
                        isDisplayed()));
        sleep(1000);
        cardView3.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.detail_view_back_button),
                        childAtPosition(
                                allOf(withId(R.id.banner),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        sleep(1000);
        appCompatImageButton3.perform(click());

        ViewInteraction cardView10 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.mood_list_view),
                                childAtPosition(
                                        withId(R.id.body),
                                        1)),
                        0),
                        isDisplayed()));
        sleep(1000);
        cardView10.perform(longClick());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(android.R.id.button3), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        sleep(1000);
        appCompatButton11.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.ic_profile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomnav),
                                        0),
                                3),
                        isDisplayed()));
        sleep(1000);
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.logout_button), withText("Sign Out"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        sleep(1000);
        appCompatButton9.perform(click());
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
