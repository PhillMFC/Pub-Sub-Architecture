package com.example.pub_sub_arquitecture_calculator.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.example.pub_sub_arquitecture_calculator.DTO.MessageDTO;
import com.example.pub_sub_arquitecture_calculator.Service.TopicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;




@RestController
@RequestMapping("/api")
public class APIGateway{

    @Value("${server.address}")
    private String serverAddress;
    
    @Autowired
    private TopicService TopicContainer;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/post-message")
    public String postMessage(@RequestBody MessageDTO messageTopic) throws JsonProcessingException {
        System.out.println("\nNova consulta:\n");
        System.out.println("  Mensagem: " + messageTopic.getTopicMessage());
        System.out.println("  Tópico: " + messageTopic.getTopic());
        String subscriberURL;

        switch (messageTopic.getTopic()) {
            case "sum" -> subscriberURL = "http://" + serverAddress + ":50001/sum-subscriber";
            case "sub" -> subscriberURL = "http://" + serverAddress + ":50002/sub-subscriber";
            case "mul" -> subscriberURL = "http://" + serverAddress + ":50003/mul-subscriber";
            case "div" -> subscriberURL = "http://" + serverAddress + ":50004/div-subscriber";
            default -> throw new AssertionError("\nTópico desconhecido\n");
        }

        System.out.println("  Consulta de "  + "'" + messageTopic.getTopicMessage() + "'" + "para "  + subscriberURL);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("topicMessage", messageTopic.getTopicMessage());

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> subscriber = restTemplate.exchange(
                subscriberURL, HttpMethod.POST, 
                entity , String.class
            );

            System.out.println("  Resposta do subscriber: " + subscriber.getBody() + "\n" + subscriber.getHeaders() + " \n");

            return subscriber.getBody();
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString()).toString();
        }
    }

    @GetMapping("/sum")
    public ResponseEntity<Object[]> getSumMessages() {
        Object[] unreadMessages = TopicContainer.getSumTopic().toArray();
        TopicContainer.deleteSumTopicMessages();
        return ResponseEntity.ok(unreadMessages);
    }

    @GetMapping("/sub")
    public ResponseEntity<Object[]> getSubMessages() {
        Object[] unreadMessages = TopicContainer.getSubTopic().toArray();
        TopicContainer.deleteSubTopicMessages();
        return ResponseEntity.ok(unreadMessages);
    }

    @GetMapping("/mul")
    public ResponseEntity<Object[]> getMulMessages() {
        Object[] unreadMessages = TopicContainer.getMulTopic().toArray();
        TopicContainer.deleteMulTopicMessages();
        return ResponseEntity.ok(unreadMessages);
    }

    @GetMapping("/div")
    public ResponseEntity<Object[]> getDivMessages() {
        Object[] unreadMessages = TopicContainer.getDivTopic().toArray();
        TopicContainer.deleteDivTopicMessages();
        return ResponseEntity.ok(unreadMessages);
    }
}
