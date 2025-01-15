package info.touret.training.langchain4j;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        String authorName = args[0];
        ChatLanguageModel model = AzureOpenAiChatModel.builder()
                .apiKey(System.getenv("AZURE_OPENAI_KEY"))
                .deploymentName("gpt-4o")
                .endpoint(System.getenv("AZURE_OPENAI_ENDPOINT")).build();

        AuthorAssistant assistant = AiServices.create(AuthorAssistant.class, model);

        String answer = assistant.getAuthorBiography(authorName);
        System.out.println(answer);
        exit(0);

    }
}
