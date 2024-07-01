package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    private static final Faker FAKER = new Faker(new Locale("en"));

    public static String validCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String generateValidCardMonth() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return formatter.format(date);
    }

    public static String generateValidCardYear() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");
        return formatter.format(date);
    }

    public static String generateValidCardOwner() {
        return FAKER.name().firstName().toUpperCase() + " " + FAKER.name().lastName().toUpperCase();
    }

    public static String generateValidCardCVC() {
        return FAKER.number().digits(3);
    }

    public static CardInfo getRandomValidCard() {
        return new CardInfo(validCardNumber(), generateValidCardMonth(), generateValidCardYear(), generateValidCardOwner(), generateValidCardCVC());
    }

    public static CardInfo getCardWithCustomCardNumber(String cardNumber) {
        return new CardInfo(cardNumber, generateValidCardMonth(), generateValidCardYear(), generateValidCardOwner(), generateValidCardCVC());
    }

    public static CardInfo getCardWithCustomMonth(String cardMonth) {
        return new CardInfo(validCardNumber(), cardMonth, generateValidCardYear(), generateValidCardOwner(), generateValidCardCVC());
    }

    public static CardInfo getCardWithCustomYear(String cardYear) {
        return new CardInfo(validCardNumber(), generateValidCardMonth(), cardYear, generateValidCardOwner(), generateValidCardCVC());
    }

    public static CardInfo getCardWithCustomOwner(String cardOwner) {
        return new CardInfo(validCardNumber(), generateValidCardMonth(), generateValidCardYear(), cardOwner, generateValidCardCVC());
    }

    public static CardInfo getCardWithCustomCVC(String cardCVC) {
        return new CardInfo(validCardNumber(), generateValidCardMonth(), generateValidCardYear(), generateValidCardOwner(), cardCVC);
    }

    @Value
    public static class CardInfo {
        String cardNumber;
        String cardMonth;
        String cardYear;
        String cardOwner;
        String cardCvc;
    }
}