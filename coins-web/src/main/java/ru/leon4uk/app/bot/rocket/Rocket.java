package ru.leon4uk.app.bot.rocket;

import org.springframework.stereotype.Component;
import ru.futuredreams.rocketchat.api.client.RocketChatApiClientFactory;
import ru.futuredreams.rocketchat.api.client.RocketChatApiRestClient;
import ru.futuredreams.rocketchat.api.client.domain.miscellaneousInfo.Account;
import ru.futuredreams.rocketchat.api.client.domain.miscellaneousInfo.AnswerPostRocketMessage;
import ru.futuredreams.rocketchat.api.client.domain.miscellaneousInfo.Login;
import ru.futuredreams.rocketchat.api.client.domain.miscellaneousInfo.PostRocketMessage;

import javax.annotation.PostConstruct;

@Component
public class Rocket {

    private RocketChatApiRestClient clientAuth;

    @PostConstruct
    public void init() {

        RocketChatApiClientFactory factory = RocketChatApiClientFactory.newInstance();
        RocketChatApiRestClient client = factory.newRestClient();

        Login login = client.makeLogin( new Account("javabot", "javabot123456"));

        RocketChatApiClientFactory factoryAuth = RocketChatApiClientFactory.newInstance(login.getData().getAuthToken(), login.getData().getUserId());
        clientAuth = factoryAuth.newRestClient();

    }

    public void sendMessage(String message) {
        AnswerPostRocketMessage answer = clientAuth.postMessage(new PostRocketMessage("c6HdWYc67xr8vHaxs", "#TestChannel", message, "JavaBot"));
    }
}