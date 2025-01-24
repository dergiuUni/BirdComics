package com.birdcomics.GestioneCarrello;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/CarrelloTest")
public class CarrelloTest {

    // Mappa per memorizzare le sessioni degli utenti con l'ID utente come chiave
    private static Map<String, Session> userSessions = new ConcurrentHashMap<>();

    // Quando un client si connette, memorizziamo la sua sessione
    @OnOpen
    public void onOpen(Session session) {
        String userId = getUserIdFromSession(session); // Recupera l'ID utente (dovrai implementarlo in base alla tua logica di autenticazione)
        userSessions.put(userId, session);
        System.out.println("Utente con ID " + userId + " connesso.");
    }

    // Quando un client si disconnette, rimuoviamo la sua sessione
    @OnClose
    public void onClose(Session session) {
        String userId = getUserIdFromSession(session); // Recupera l'ID utente
        userSessions.remove(userId);
        System.out.println("Utente con ID " + userId + " disconnesso.");
    }

    // Quando un messaggio viene ricevuto dal client (opzionale)
    @OnMessage
    public void onMessage(String message, Session session) {
        String userId = getUserIdFromSession(session); // Recupera l'ID utente
        System.out.println("Messaggio ricevuto da " + userId + ": " + message);
    }

    // Metodo per inviare un messaggio a uno specifico utente
    public static void sendMessageToUser(String userId, String message) {
        Session userSession = userSessions.get(userId);
        if (userSession != null) {
            try {
                userSession.getBasicRemote().sendText(message);
                System.out.println("Messaggio inviato a " + userId + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Utente con ID " + userId + " non trovato.");
        }
    }

    // Metodo per recuperare l'ID utente dalla sessione (personalizza secondo la tua logica)
    private String getUserIdFromSession(Session session) {
        // Qui puoi implementare la logica per identificare l'utente, ad esempio usando un attributo della sessione HTTP
        // O un token di autenticazione passato durante la connessione WebSocket.
        return session.getId(); // Questo è solo un esempio, dovrai sostituirlo con il tuo metodo di identificazione dell'utente
    }
}
