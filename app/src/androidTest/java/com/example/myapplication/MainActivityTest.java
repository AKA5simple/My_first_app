package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.data.DataSaver;
import com.example.myapplication.data.ShopItem;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    DataSaver dataSaverBackup;
    ArrayList<ShopItem> shopItemsBackup;

    @Before
    public void setUp() throws Exception {
        dataSaverBackup=new DataSaver();
        Context targetContext= InstrumentationRegistry.getInstrumentation().getTargetContext();
        shopItemsBackup=dataSaverBackup.Load(targetContext);

        dataSaverBackup.Save(targetContext,new ArrayList<>());
    }

    @After
    public void tearDown() throws Exception {
        Context targetContext=InstrumentationRegistry.getInstrumentation().getTargetContext();
        dataSaverBackup.Save(targetContext,shopItemsBackup);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycleview),
                        childAtPosition(
                                withId(R.id.recyclerview_main),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, longClick()));

        ViewInteraction materialTextView = onView(
                allOf(withId(android.R.id.title), withText("Add0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edittext_shop_item_title), withText("Name"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Name123"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edittext_shop_item_title), withText("Name123"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edittext_shop_item_title_price), withText("1.0"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("234"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edittext_shop_item_title_price), withText("234"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.button_ok), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textview_item_caption), withText("Name123"),
                        withParent(allOf(withId(R.id.linearLayout3),
                                withParent(withId(R.id.linearLayout)))),
                        isDisplayed()));
        textView.check(matches(withText("Name123")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textview_item_price), withText("234.0"),
                        withParent(allOf(withId(R.id.linearLayout3),
                                withParent(withId(R.id.linearLayout)))),
                        isDisplayed()));
        textView2.check(matches(withText("234.0")));
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
