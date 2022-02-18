package com.example.spokenwapp.events;


public class SpokenEvents {

    public SpokenEvents() {
    }

    public static class Message{
        private String message;
        private long currentDuration;
        private long totalDuration;

        public Message(String message, long currentDuration, long totalDuration) {
            this.message = message;
            this.currentDuration = currentDuration;
            this.totalDuration = totalDuration;
        }

        public String getMessage() {
            return message;
        }

        public long getCurrentDuration() {
            return currentDuration;
        }

        public long getTotalDuration() {
            return totalDuration;
        }
    }
}
