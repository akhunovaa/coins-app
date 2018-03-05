package ru.leon4uk.app.bot.impl.buffer;

import org.springframework.stereotype.Component;

@Component
public class BitsaneSellOrderBuffer {

    private String id;
    private String pair;
    private String side;
    private boolean status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public synchronized boolean isStatus() {
        return status;
    }

    public synchronized void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BitsaneSellOrderBuffer{" +
                "id='" + id + '\'' +
                ", pair='" + pair + '\'' +
                ", side='" + side + '\'' +
                ", status=" + status +
                '}';
    }
}
