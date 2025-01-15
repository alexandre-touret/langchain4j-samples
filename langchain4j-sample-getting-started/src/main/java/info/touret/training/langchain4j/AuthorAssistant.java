package info.touret.training.langchain4j;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface AuthorAssistant {

    @SystemMessage("You are an expert in Science Fiction books.")
    @UserMessage("Write a short biography about {{author}}.")
    String getAuthorBiography(@V("author") String authorName);

}
