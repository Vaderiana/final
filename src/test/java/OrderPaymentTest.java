import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderPaymentTest {
    public static final String SUCCEED_MESSAGE = "Операция одобрена Банком.";
    public static final String FAILED_MESSAGE = "Ошибка! Банк отказал в проведении операции.";
    public static final String APPROVED_STATUS = "APPROVED";
    public static final String WRONG_CARD_EXPIRATION = "Неверно указан срок действия карты";
    public static final String CARD_EXPIRED = "Истёк срок действия карты";
    public static final String EMPTY_FIELD = "Поле обязательно для заполнения";
    public static final String WRONG_FORMAT = "Неверный формат";
    private MainPage mainPage;
    DataHelper.CardInfo card;

    @BeforeEach
    void setup() {
        mainPage = open("http://localhost:8080", MainPage.class);
    }

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void cleanupAll() {
        SelenideLogger.removeListener("allure");
        SQLHelper.cleanupDB();
    }
    
    @Test
    @DisplayName("Should successful complete order")
    void successfulOrderTest() {
        var paymentPage = mainPage.goToPayment();
        card = getRandomValidCard();
        paymentPage.fillInTheForm(card);
        paymentPage.verifySuccessMessage(SUCCEED_MESSAGE);
        String actualStatusPayment = SQLHelper.getPaymentStatus();
        boolean expectedStatusOrder = true;
        boolean actualStatusOrder = SQLHelper.isOrderSuccessful();
        assertEquals(APPROVED_STATUS, actualStatusPayment);
        assertEquals(expectedStatusOrder, actualStatusOrder);
    }

    @ParameterizedTest
    @DisplayName("Should successful complete order - positive - different month")
    @ValueSource(strings = {"07", "08", "09", "10", "11", "12"})
    void successfulOrderTestCorrectMonth(String month) {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomMonth(month);
        paymentPage.fillInTheForm(card);
        paymentPage.verifySuccessMessage(SUCCEED_MESSAGE);
        String actualStatusPayment = SQLHelper.getPaymentStatus();
        boolean expectedStatusOrder = true;
        boolean actualStatusOrder = SQLHelper.isOrderSuccessful();
        assertEquals(APPROVED_STATUS, actualStatusPayment);
        assertEquals(expectedStatusOrder, actualStatusOrder);
    }

    @Test
    @DisplayName("Should not complete order - invalid card")
    void successfulOrderTestInvalidCard() {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomCardNumber("1234 1234 1234 1234");
        paymentPage.fillInTheForm(card);
        paymentPage.verifyFailMessage(FAILED_MESSAGE);
    }

    @ParameterizedTest
    @DisplayName("Should not complete order - negative month (before or after current)")
    @ValueSource(strings = {"00", "01", "02", "03", "04", "05", "13"})
    void successfulOrderTestIncorrectMonthBeforeAfter(String month) {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomMonth(month);
        paymentPage.fillInTheForm(card);
        paymentPage.verifyErrorMessage(WRONG_CARD_EXPIRATION);
    }

    @ParameterizedTest
    @DisplayName("Should not complete order - negative month (wrong format)")
    @ValueSource(strings = {"  ", "qw", "1"})
    void successfulOrderTestIncorrectMonth(String month) {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomMonth(month);
        paymentPage.fillInTheForm(card);
        paymentPage.verifyErrorMessage(WRONG_FORMAT);
    }

    @ParameterizedTest
    @DisplayName("Should not complete order - negative year (expired)")
    @ValueSource(strings = {"00", "22", "23"})
    void successfulOrderTestIncorrectYearBefore(String year) {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomYear(year);
        paymentPage.fillInTheForm(card);
        paymentPage.verifyErrorMessage(CARD_EXPIRED);
    }

    @ParameterizedTest
    @DisplayName("Should not complete order - negative year")
    @ValueSource(strings = {"30", "33"})
    void successfulOrderTestIncorrectYear(String year) {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomYear(year);
        paymentPage.fillInTheForm(card);
        paymentPage.verifyErrorMessage(WRONG_CARD_EXPIRATION);
    }

    @DisplayName("Should not complete order - negative year (wrong format)")
    @Test
    void successfulOrderTestIncorrectFormatYear() {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomYear("5");
        paymentPage.fillInTheForm(card);
        paymentPage.verifyErrorMessage(WRONG_FORMAT);
    }

    @DisplayName("Should not complete order - incorrect owner")
    @ParameterizedTest
    @ValueSource(strings = {"Василий Пупкин", "123", "SAM"})
    void successfulOrderTestIncorrectOwner() {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomOwner("");
        paymentPage.fillInTheForm(card);
        paymentPage.verifyErrorMessage(WRONG_FORMAT);
    }

    @ParameterizedTest
    @DisplayName("Should not complete order - negative CVC")
    @ValueSource(strings = {"1", "22"})
    void successfulOrderTestIncorrectCVC(String cvc) {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomCVC(cvc);
        paymentPage.fillInTheForm(card);
        paymentPage.verifyErrorMessage(WRONG_FORMAT);
    }

    @Test
    @DisplayName("Should not complete order - negative CVC")
    void successfulOrderTestEmptyCVC() {
        var paymentPage = mainPage.goToPayment();
        card = getCardWithCustomCVC("");
        paymentPage.fillInTheForm(card);
        paymentPage.verifyErrorMessage(EMPTY_FIELD);
    }
}