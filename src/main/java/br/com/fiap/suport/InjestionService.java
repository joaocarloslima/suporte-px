package br.com.fiap.suport;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InjestionService {

    private final PgVectorStore vectorStore;

    public InjestionService(PgVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    //@PostConstruct
    public void loadData(){
        String path = "classpath:docs/challenge.pdf";
        var pdfRead = new ParagraphPdfDocumentReader(path);
        TokenTextSplitter splitter = TokenTextSplitter.builder().build();
        List<Document> documents = splitter.apply(pdfRead.get());
        vectorStore.accept(documents);
    }

}
