package com.example.pub_sub_arquitecture_calculator.DTO;


public class MessageDTO {
    
    private String topicMessage;
    private String topic;

    public MessageDTO(){}

    public MessageDTO(String message, String topic){
        this.topicMessage = message;
        this.topic = topic;
    }

    public String getTopicMessage(){
        return this.topicMessage;
    }

    public String getTopic(){
        return this.topic;
    }
    
}
