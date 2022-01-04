package com.example.healthappdemo;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;




import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import static java.util.EnumSet.allOf;

import static dagger.internal.Preconditions.checkNotNull;

import com.google.common.collect.Iterables;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

//    @Rule
//    public ActivityScenarioRule<RegisterActivity> activityRule =
//            new ActivityScenarioRule<>(RegisterActivity.class);

    @Rule
    public ActivityScenarioRule<EligibilityTestFirstActivity> activityRule_1 =
            new ActivityScenarioRule<>(EligibilityTestFirstActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.healthappdemo", appContext.getPackageName());
    }

//    @Test
//    public void user_can_register(){
//        onView(withId(R.id.user_register_email)).perform(typeText("test1@gmail.com"));
//        onView(withId(R.id.user_register_password)).perform(typeText("123456789"));
//        onView(withId(R.id.user_register_confirm_pass)).perform(typeText("123456789"));
//
//        onView(withId(R.id.LinearLayout_Register)).perform(click());
//        //onView(withText("Hello Steve!")).check(matches(isDisplayed()));
//
//        // An intent is fired to launch a different Activity. Robolectric doesn't currently
//        // support launching a new Activity, so use Espresso Intents to verify intent was sent
//
//    }

    @Test
    public void user_can_first_eligibility_form(){
        onView(withId(R.id.user_register_firstName)).perform(typeText("test 1 FName"));
        onView(withId(R.id.user_register_LastName)).perform(typeText("Test2 LName"));
    }


    @Test
    public void user_can_first_eligibility_age() {

        onView(withId(R.id.user_register_DOB)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("19-9-2021")));
    }

    @Test
    public void user_can_first_eligibility_gender() {

        onView(withId(R.id.sp_new_user_register_gender)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("Male")));

    }


    private Matcher<? extends Object> allOf(Matcher<Object> objectMatcher, Matcher<String> male) {
        return new TypeSafeMatcher<Object>() {
            @Override
            protected boolean matchesSafely(Object item) {
                return objectMatcher.matches(male);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }


}