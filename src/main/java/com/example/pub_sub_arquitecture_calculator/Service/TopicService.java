package com.example.pub_sub_arquitecture_calculator.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pub_sub_arquitecture_calculator.DTO.MessageDTO;

@Service
public class TopicService {

    private final List<String> sumTopic = new ArrayList<>();
    private final List<String> subTopic = new ArrayList<>();
    private final List<String> mulTopic = new ArrayList<>();
    private final List<String> divTopic = new ArrayList<>();

    public void appendMessageToTopic(MessageDTO message){
        String expression = message.getTopicMessage();
        String topic = message.getTopic();

        switch (topic) {
            case "sum" -> sumTopic.add(expression);
            case "sub" -> subTopic.add(expression);
            case "mul" -> mulTopic.add(expression);
            case "div" -> divTopic.add(expression);
            default -> throw new AssertionError("\nTópico desconhecido\n");
        }
    }

    public List<String> getSumTopic() {
        return sumTopic;
    }

    public List<String> getSubTopic() {
        return subTopic;
    }

    public List<String> getMulTopic() {
        return mulTopic;
    }

    public List<String> getDivTopic() {
        return divTopic;
    }

    public void deleteSumTopicMessages() {
        this.sumTopic.clear();
    }

    public void deleteSubTopicMessages() {
        this.subTopic.clear();
    }

    public void deleteMulTopicMessages() {
        this.mulTopic.clear();
    }

    public void deleteDivTopicMessages() {
        this.divTopic.clear();
    }
}
