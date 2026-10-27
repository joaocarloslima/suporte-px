package br.com.fiap.suport;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final PgVectorStore vectorStore;
    private ChatClient chatClient;

    public ChatService(ChatClient.Builder builder, PgVectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore).build()
                )
                .build();
        this.vectorStore = vectorStore;
    }

    public Flux<String> sendMessagemToAI(String message) {
        var imageResource = new ClassPathResource("/docs/cronograma.png");

        return chatClient.prompt()
                .user(u -> u
                        .text(message)
                        .media(MimeTypeUtils.IMAGE_PNG, imageResource)
                )
                .stream()
                .content();
    }

}
