package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final SelenideElement checkoutButton = $$("[role=button]").findBy(exactText("Купить"));

    public PaymentPage goToPayment() {
        checkoutButton.click();
        return new PaymentPage();
    }
}
