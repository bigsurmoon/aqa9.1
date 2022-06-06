package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.domain.RegistrationInfo;
import ru.netology.utils.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    RegistrationInfo client;


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        client = DataGenerator.Registration.generateInfo("RU");
        open("http://localhost:9999");
    }

    @Test
    void shouldOrderCardDelivery() throws InterruptedException {
        $("[data-test-id = 'city'] input").setValue(client.getCity());
        $("[data-test-id = 'date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id = 'date'] input").setValue(client.getFirstDate());
        $("[data-test-id = 'name'] input").setValue(client.getName());
        $("[data-test-id = 'phone'] input").setValue(client.getPhone());
        $("[data-test-id = 'agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id = 'success-notification']").shouldBe(appear);
        $("[data-test-id = 'success-notification']").shouldHave(text(client.getFirstDate()));
        $("[data-test-id = 'success-notification'] .icon_name_close").click();
        $("[data-test-id = 'date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id = 'date'] input").setValue(client.getSecondDate());
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id = 'replan-notification']").shouldBe(appear);
        $$("button").find(exactText("Перепланировать")).click();
        $("[data-test-id = 'success-notification']").shouldBe(appear);
        $("[data-test-id = 'success-notification']").shouldHave(text(client.getSecondDate()));
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
}