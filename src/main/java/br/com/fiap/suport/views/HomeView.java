package br.com.fiap.suport.views;

import br.com.fiap.suport.ChatService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.Instant;

@Route("")
public class HomeView extends VerticalLayout {

    private final ChatService chatService;
    MessageList list = new MessageList();

    public HomeView(ChatService chatService) {

        add(new H1("Suporte FIAP"));
        add(new Paragraph("Tire todas as suas dúvidas"));

        var input = new MessageInput();
        input.setWidthFull();
        input.addSubmitListener(submitEvent -> sendMessage(submitEvent.getValue(), "Você"));

        //sendMessage("O que vamos aprender hoje?", UserType.PROFESSOR);

        add(input);
        add(list);
        this.chatService = chatService;
    }

    private void sendMessage(String messageText, String username){
        MessageListItem userMessageItem = new MessageListItem(messageText, Instant.now(), username);
        userMessageItem.setUserColorIndex(1);
        list.addItem(userMessageItem);

        var iaMessageItem = new MessageListItem("", Instant.now(), "Suporte");
        iaMessageItem.setUserColorIndex(2);
        list.addItem(iaMessageItem);

        chatService.sendMessagemToAI(messageText).subscribe(partialResponse -> {
            getUI().ifPresent(ui -> ui.access(() -> {
                iaMessageItem.appendText(partialResponse);
            }));
        });
    }
}