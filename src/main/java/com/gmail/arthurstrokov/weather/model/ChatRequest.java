package com.gmail.arthurstrokov.weather.model;


import java.util.List;

public record ChatRequest(String model, List<ChatMessage> messages, Boolean stream) {
}
