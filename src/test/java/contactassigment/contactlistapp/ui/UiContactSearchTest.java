package contactassigment.contactlistapp.ui;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UiContactSearchTest {

    @LocalServerPort
    int randomPort;

    // Shared between all tests in this class.
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    BrowserContext context;
    Page page;

    private static File targetClassesDir = new File(UiContactSearchTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    private static Path path = Paths.get(targetClassesDir.getParent() + "/site/");

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @DisplayName("Test to check home page is displayed ")
    @Test
    void shouldShowWelcomePageOnStartUp() {
        page.navigate("http://localhost:"+randomPort);
        assertEquals("http://localhost:" + randomPort +"/", page.url());
    }

    @DisplayName("Test to check contact is displayed with Edit button for search result with single contact ")
    @Test
    void shouldDisplayContactsForMatchingSearchCriteriaAndEdit() {
        page.navigate("http://localhost:"+randomPort+"/contacts");
        page.getByLabel("FirstName").fill("Sophie");
        page.getByText("Search").click();
        assertEquals("Sophie", page.getByLabel("FirstName").inputValue());
        assertEquals("Klein", page.getByLabel("LastName").inputValue());
        assertEquals("Australian Agent for International Students", page.getByLabel("Organisation Name").inputValue());
        assertEquals("11111111111", page.getByLabel("Organisation ABN").inputValue());
        assertEquals("31/12/2022 00:00:00", page.getByLabel("Created").inputValue());
        assertThat(page.getByRole(AriaRole.BUTTON).and (page.getByTitle("Edit")));
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path + "/singleContact.png")));
    }

    @DisplayName("Test to check multiple contact is displayed for search result with more than one contact ")
    @Test
    void shouldDisplayContactsForMatchingSearchCriteria() {
        page.navigate("http://localhost:"+ randomPort + "/contacts");
        page.getByLabel("LastName").fill("B");
        page.getByText("Search").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path + "/multipleContact.png")));
        assertThat(page.getByRole(AriaRole.BUTTON).and (page.getByTitle("View")));
    }


}
