package domain.model;

import domain.status.ChatRoomStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.ORDINAL)
  private ChatRoomStatus status = ChatRoomStatus.ACTIVE;

  @ManyToOne(fetch = FetchType.LAZY)
  private User host;

  @ManyToOne(fetch = FetchType.LAZY)
  private User guest;

  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
  private List<Message> messages = new ArrayList<>();
}
