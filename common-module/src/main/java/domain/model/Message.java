package domain.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Message {

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private ChatRoom chatRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  private String contents;
  private int type;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  private boolean isRead = false;
}
