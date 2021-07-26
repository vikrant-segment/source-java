package com.example.source;

import com.segment.analytics.Analytics;
import com.segment.analytics.Log;
import com.segment.analytics.messages.GroupMessage;
import com.segment.analytics.messages.IdentifyMessage;
import com.segment.analytics.messages.PageMessage;
import com.segment.analytics.messages.ScreenMessage;
import com.segment.analytics.messages.TrackMessage;
import java.util.Map;

public class Main {

  public static final String WRITE_KEY = "CYOZjJXPPhAfSo7yS9EcLTpc35iIgPWX";
  public static final String USER_ID = "123456abcdef";

  public static Log STDOUT = new Log() {
    @Override
    public void print(Level level, String format, Object... args) {
      System.out.println(level + ":\t" + String.format(format, args));
    }

    @Override
    public void print(Level level, Throwable error, String format, Object... args) {
      System.out.println(level + ":\t" + String.format(format, args));
      error.printStackTrace();
    }
  };
  
  public static void main(String[] args) throws InterruptedException {
    Analytics analytics = Analytics.builder(WRITE_KEY).endpoint("https://api.segment.build")
        .log(STDOUT).build();

    System.out.println("IdentifyMessage");
    analytics.enqueue(IdentifyMessage.builder()
        .userId(USER_ID)
        .traits(Map.ofEntries(
            Map.entry("name", "Firstname Lastname"),
            Map.entry("email", "firstname.lastname@segment.com")
        )));

    for (int i = 0; i < 10; i++) {
      System.out.println("TrackMessage");
      analytics.enqueue(TrackMessage.builder("Item purchased")
          .userId(USER_ID)
          .properties(Map.ofEntries(
              Map.entry("revenue", 39.95),
              Map.entry("shipping", "2-day"),
              Map.entry("index", i)
          )));
    }

    System.out.println("ScreenMessage");
    analytics.enqueue(ScreenMessage.builder("Schedule")
        .userId(USER_ID)
        .properties(Map.ofEntries(
            Map.entry("category", "Sports"),
            Map.entry("path", "/sports/schedule")
        )));

    System.out.println("PageMessage");
    analytics.enqueue(PageMessage.builder("Schedule")
        .userId(USER_ID)
        .properties(Map.ofEntries(
            Map.entry("category", "Sports"),
            Map.entry("path", "/sports/schedule")
        )));

    System.out.println("GroupMessage");
    analytics.enqueue(GroupMessage.builder("segment-group-1")
        .userId(USER_ID)
        .traits(Map.ofEntries(
            Map.entry("name", "Segment"),
            Map.entry("size", 50)
        )));
    analytics.flush();

    Thread.sleep(5000);
    analytics.shutdown();
  }
}
