package info.touret.training.langchain4j.test;

import com.github.dockerjava.api.model.Image;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import info.touret.training.langchain4j.Musician;
import info.touret.training.langchain4j.MusicianAssistant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class MusicianAssistantTest {

  static String MODEL_NAME = "tinyllama";

  @Test
  public void shouldGenerateMusicianTopThreeAlbums() throws IOException, InterruptedException {

    OllamaContainer ollamaContainer = createOllamaContainer();
    ollamaContainer.start();

    ChatLanguageModel model = OllamaChatModel.builder()
      .baseUrl(String.format("http://%s:%d", ollamaContainer.getHost(), ollamaContainer.getFirstMappedPort()))
      .modelName(MODEL_NAME)
      .temperature(0.0)
      .timeout(Duration.ofMinutes(5))
      .build();

    Musician musician = new MusicianAssistant().generateTopThreeAlbums(model, "Miles Davis");

    assertTrue(musician.albums().contains("Kind of Blue"));
  }

  private OllamaContainer createOllamaContainer() throws IOException, InterruptedException {
    // Check if the Ollama Docker image exists already
    List<Image> listImagesCmd = DockerClientFactory.lazyClient()
      .listImagesCmd()
      .withImageNameFilter(MODEL_NAME)
      .exec();

    if (listImagesCmd.isEmpty()) {
      System.out.println("Creating a new Ollama container with the model image...");
      OllamaContainer ollama = new OllamaContainer("ollama/ollama:latest");
      ollama.start();
      ollama.execInContainer("ollama", "pull", MODEL_NAME);
      ollama.commitToImage(MODEL_NAME);
      return ollama;
    } else {
      System.out.println("Using existing Ollama container with model image...");
      return new OllamaContainer(
        DockerImageName.parse(MODEL_NAME)
          .asCompatibleSubstituteFor("ollama/ollama"));
    }
  }
}
