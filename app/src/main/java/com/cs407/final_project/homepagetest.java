package com.cs407.final_project;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class HomePageActivityTest {

    @Rule
    public ActivityTestRule<HomePageActivity> activityTestRule = new ActivityTestRule<>(HomePageActivity.class);

    @Test
    public void testHomePageDisplaysRecipes() {
        // Mock GPT response
        GptService mockGptService = Mockito.mock(GptService.class);
        when(mockGptService.generateRecipe()).thenReturn("Mocked Recipe Text");

        // Inject mock service into the presenter or repository responsible for GPT integration
        RecipePresenter presenter = new RecipePresenter(mockGptService);
        activityTestRule.getActivity().setPresenter(presenter);

        // Perform UI action (e.g., click on a button to generate a recipe)
        Espresso.onView(withId(R.id.generateRecipeButton)).perform(ViewActions.click());

        // Wait for GPT response (you may need to implement some form of synchronization or use IdlingResource)

        // Check if the generated recipe text is displayed on the UI
        Espresso.onView(withId(R.id.recipeTextView)).check(matches(isDisplayed()));
    }
    GptService provideGptService() {
        return new GptService(); // Assuming you have a GptService class
    }

    @Provides
    @Singleton
    RecipeRepository provideRecipeRepository(GptService gptService) {
        return new RecipeRepository(gptService); // Assuming you have a RecipeRepository class
    }

    @Provides
    @Singleton
    RecipePresenter provideRecipePresenter(RecipeRepository recipeRepository) {
        return new RecipePresenter(recipeRepository);
    }
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
    private GptService gptService;

    public RecipeRepository(GptService gptService) {
        this.gptService = gptService;
    }

    public String generateRecipe() {
        // Use the GPT service to generate a recipe
        return gptService.generateRecipe();
    }
    @Provides
    @Singleton
    RecipeRepository provideRecipeRepository(GptService gptService) {
        return new RecipeRepository(gptService); // Assuming you have a RecipeRepository class
    }

    @Provides
    @Singleton
    RecipePresenter provideRecipePresenter(RecipeRepository recipeRepository) {
        return new RecipePresenter(recipeRepository);
    }
    @Inject
    RecipePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Dagger injection
        MyApp.getAppComponent().inject(this);

        // Other initialization code
    }
    @Rule
    public ActivityTestRule<HomePageActivity> activityTestRule = new ActivityTestRule<>(HomePageActivity.class);

    @Test
    public void testHomePageDisplaysRecipes() {
        // Mock GPT response
        GptService mockGptService = Mockito.mock(GptService.class);
        when(mockGptService.generateRecipe()).thenReturn("Mocked Recipe Text");

        // Create Dagger test component
        TestAppComponent testAppComponent = DaggerTestAppComponent.builder()
                .appModule(new TestAppModule(mockGptService))
                .build();

        // Inject the mock GptService into the activity
        TestMyApp.setTestAppComponent(testAppComponent);
        activityTestRule.getActivity().setPresenter(testAppComponent.recipePresenter());

        // Perform UI action (e.g., click on a button to generate a recipe)
        Espresso.onView(withId(R.id.generateRecipeButton)).perform(ViewActions.click());

        // Wait for GPT response (you may need to implement some form of synchronization or use IdlingResource)

        // Check if the generated recipe text is displayed on the UI
        Espresso.onView(withId(R.id.recipeTextView)).check(matches(isDisplayed()));
    }

    private GptService gptService;

    public TestAppModule(GptService gptService) {
        this.gptService = gptService;
    }

    @Provides
    @Singleton
    GptService provideGptService() {
        return gptService;
    }

    @Provides
    @Singleton
    RecipeRepository provideRecipeRepository(GptService gptService) {
        return new RecipeRepository(gptService);
    }

    @Provides
    @Singleton
    RecipePresenter provideRecipePresenter(RecipeRepository recipeRepository) {
        return new RecipePresenter(recipeRepository);
    }
}


