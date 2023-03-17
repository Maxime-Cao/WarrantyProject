package com.warranty.warrantyproject

import android.content.Intent

import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class US1InstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun displayAddFragmentOnClick() {
        onView(withId(R.id.fragment_home)).check(matches(isDisplayed()))
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.fragment_add)).check(matches(isDisplayed()))
    }

    @Test
    fun displayErrorMessageOnRequiredFields() {
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.add_comp_add_screen)).perform(click())
        onView(withId(R.id.product_name_text)).check(matches(hasErrorText("Title required")))
        onView(withId(R.id.product_name_text)).perform(replaceText("Samsung S22"))
        onView(withId(R.id.add_comp_add_screen)).perform(click())
        onView(withId(R.id.product_price_value)).check(matches(hasErrorText("Price required")))
        onView(withId(R.id.product_price_value)).perform(replaceText("1250"))
        onView(withId(R.id.add_comp_add_screen)).perform(click())
        onView(withId(R.id.product_max_guarantee_date_edit_text)).check(matches(hasErrorText("Date required")))
        onView(withId(R.id.product_max_guarantee_date_edit_text)).perform(replaceText("25/06/2025"))

        // Ici il n'est pas possible de vérifier que le bouton permettant d'ajouter l'image de la garantie contient bien le message d'erreur "Image required", hasErrorText n'est pas applicable sur un bouton.
        // Comme je sais déjà que tous les autres champs obligatoires sont complétés dans ce test, je vais simplement tester que si j'appuie sur le bouton d'ajout, je ne suis pas redirigé vers le fragment home, ce qui prouve ici qu'il y a encore une erreur qui empêche cela (image de garantie requise)
        onView(withId(R.id.add_comp_add_screen)).perform(click())
        onView(withId(R.id.fragment_add)).check(matches(isDisplayed()))
    }

    @Test
    fun displayErrorMessageOnFuturePurchaseDate() {
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.product_name_text)).perform(replaceText("Samsung S22"))
        onView(withId(R.id.product_price_value)).perform(replaceText("1250"))
        onView(withId(R.id.product_date_of_purchase_edit_text)).perform(replaceText("17/03/2030"))
        onView(withId(R.id.add_comp_add_screen)).perform(click())
        onView(withId(R.id.product_date_of_purchase_edit_text)).check(matches(hasErrorText("Incorrect date")))
    }

    @Test
    fun displayErrorMessageWhenGreaterPurchaseDateThatExpiryDate() {
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.product_name_text)).perform(replaceText("Samsung S22"))
        onView(withId(R.id.product_price_value)).perform(replaceText("1250"))
        onView(withId(R.id.product_date_of_purchase_edit_text)).perform(replaceText("16/03/2023"))
        onView(withId(R.id.product_max_guarantee_date_edit_text)).perform(replaceText("15/03/2023"))
        onView(withId(R.id.add_comp_add_screen)).perform(click())
        onView(withId(R.id.product_date_of_purchase_edit_text)).check(matches(hasErrorText("Incorrect date")))

        // Ici on teste aussi indirectement le cas où une erreur est déclenchée lorsque la date d'expiration est inférieure à la date d'achat (voir US1)
    }

    @Test
    fun displayErrorMessageOnPastExpiryDate() {
            onView(withId(R.id.add_comp_home_screen)).perform(click())
            onView(withId(R.id.product_name_text)).perform(replaceText("Samsung S22"))
            onView(withId(R.id.product_price_value)).perform(replaceText("1250"))
            onView(withId(R.id.product_max_guarantee_date_edit_text)).perform(replaceText("15/03/2023"))
            onView(withId(R.id.add_comp_add_screen)).perform(click())
            onView(withId(R.id.product_max_guarantee_date_edit_text)).check(matches(hasErrorText("Incorrect date")))
    }

    @Test
    fun displayAlertDialogOnClickImageButton() {
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.button_add_image)).perform(click())
        onView(withText("Select an option")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText("Take a picture")).inRoot(isDialog()).check(matches(isDisplayed())); // Choix 1
        onView(withText("Open gallery")).inRoot(isDialog()).check(matches(isDisplayed())) // Choix 2
        onView(withText("Cancel")).inRoot(isDialog()).check(matches(isDisplayed())) // Choix 3
    }

    @Before
    fun grantPermission() {
        getInstrumentation().uiAutomation.executeShellCommand(
            "pm grant ${getInstrumentation().targetContext.packageName} " +
                    "android.permission.READ_EXTERNAL_STORAGE"
        )
    }
    @Test
    fun openGalleryOnOpenGalleryChoiceForImageProduct() {
        Intents.init()
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.button_add_image)).perform(click())

        onView(withText("Open gallery")).inRoot(isDialog()).perform(click());

        intended(hasAction(Intent.ACTION_OPEN_DOCUMENT))
        Intents.release()
    }

    @Test
    fun openCameraOnTakePictureChoiceForImageProduct() {
        Intents.init()
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.button_add_image)).perform(click())
        onView(withText("Take a picture")).inRoot(isDialog()).perform(click());
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
        Intents.release()
    }


    @Before
    fun grantPermission2() {
        getInstrumentation().uiAutomation.executeShellCommand(
            "pm grant ${getInstrumentation().targetContext.packageName} " +
                    "android.permission.READ_EXTERNAL_STORAGE"
        )
    }
    @Test
    fun openGalleryOnOpenGalleryChoiceForImageWarranty() {
        Intents.init()
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.warranty_button_add)).perform(click())

        onView(withText("Open gallery")).inRoot(isDialog()).perform(click());

        intended(hasAction(Intent.ACTION_OPEN_DOCUMENT))
        Intents.release()
    }

    @Test
    fun openCameraOnTakePictureChoiceForImageWarranty() {
        Intents.init()
        onView(withId(R.id.add_comp_home_screen)).perform(click())
        onView(withId(R.id.warranty_button_add)).perform(click())
        onView(withText("Take a picture")).inRoot(isDialog()).perform(click());
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
        Intents.release()
    }


}