package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {
    private final SelenideElement cardInput = $x("//span[contains(text(),'Номер карты')]/following-sibling::span[@class='input__box']/input[@class='input__control']");
    private final SelenideElement monthInput = $x("//span[contains(text(),'Месяц')]/following-sibling::span[@class='input__box']/input[@class='input__control']");

    private final SelenideElement yearInput = $x("//span[contains(text(),'Год')]/following-sibling::span[@class='input__box']/input[@class='input__control']");


    private final SelenideElement ownerInput = $x("//span[contains(text(),'Владелец')]/following-sibling::span[@class='input__box']/input[@class='input__control']");


    private final SelenideElement cvcInput = $x("//span[contains(text(),'CVC/CVV')]/following-sibling::span[@class='input__box']/input[@class='input__control']");


    private final SelenideElement next = $$("[role=button]").findBy(exactText("Продолжить"));
    private final SelenideElement resultNotification = $(".notification__content");
    private final SelenideElement errorNotification = $(".input__sub");

    public void fillInTheForm(DataHelper.CardInfo card) {
        cardInput.setValue(card.getCardNumber());
        monthInput.setValue(card.getCardMonth());
        yearInput.setValue(card.getCardYear());
        ownerInput.setValue(card.getCardOwner());
        cvcInput.setValue(card.getCardCvc());
    }

    public void verifySuccessMessage(String message) {
        next.click();
        resultNotification.shouldHave(Condition.exactText(message), Duration.ofSeconds(20)).shouldBe(visible);
    }

    public void verifyFailMessage(String message) {
        next.click();
        resultNotification.shouldHave(Condition.exactText(message), Duration.ofSeconds(20)).shouldBe(visible);
    }

    public void verifyErrorMessage(String message) {
        next.click();
        errorNotification.shouldHave(Condition.exactText(message)).shouldBe(visible);
    }
}
