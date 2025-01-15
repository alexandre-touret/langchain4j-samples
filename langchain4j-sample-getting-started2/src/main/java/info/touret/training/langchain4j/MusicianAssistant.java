package info.touret.training.langchain4j;

// tag::adocHeader[]

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;

import java.util.List;

import static dev.langchain4j.model.azure.AzureOpenAiChatModelName.GPT_4_O;
import static java.lang.System.exit;
import static java.time.Duration.ofSeconds;

public class MusicianAssistant {

    private static final String AZURE_OPENAI_KEY = System.getenv("AZURE_OPENAI_KEY");
    private static final String AZURE_OPENAI_ENDPOINT = System.getenv("AZURE_OPENAI_ENDPOINT");

    public static void main(String[] args) {
        String name = args[0];

        ChatLanguageModel model = AzureOpenAiChatModel.builder()
                .apiKey(AZURE_OPENAI_KEY)
                .endpoint(AZURE_OPENAI_ENDPOINT)
                .deploymentName(GPT_4_O.modelName())
                .temperature(0.3)
                .timeout(ofSeconds(60))
                .logRequestsAndResponses(true)
                .build();

        Musician musician = new MusicianAssistant().generateTopThreeAlbums(model, name);

        System.out.println(musician);
        exit(0);
    }

   public Musician generateTopThreeAlbums(ChatLanguageModel model, String name) {

        SystemMessage systemMsg = SystemMessage.from("""
                You are an expert in Jazz music.
                Reply with only the names of the artists, albums, etc.
                Be very concise.
                If a list is given, separate the items with commas.
                """);
        UserMessage userMsg = UserMessage.from(
                String.format("Only list the top 3 albums of %s", name)
        );
        List<ChatMessage> messages = List.of(systemMsg, userMsg);

        Response<AiMessage> albums = model.generate(messages);
        String topThreeAlbums = albums.content().text();

        return new Musician(name, topThreeAlbums);
    }
}
