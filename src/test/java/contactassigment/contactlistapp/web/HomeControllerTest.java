package contactassigment.contactlistapp.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {

    @InjectMocks
    HomeController homeController;

    @DisplayName("Junit test to check home is returned ")
    @Test
    public void testReturnHome()
    {
        // given click on home , default home page /

        // when
        String returnValue = homeController.home();

        // then
        assertEquals("/home", returnValue);
    }
}