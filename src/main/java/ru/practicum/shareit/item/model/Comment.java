package ru.practicum.shareit.item.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content")
    private String text;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "created_by")
    private LocalDateTime createdBy;

    @ManyToOne(targetEntity = Item.class,
                fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    public Comment() {
    }

    public Comment(Long commentId, String text, String authorName, LocalDateTime createdBy, Item item) {
        this.commentId = commentId;
        this.text = text;
        this.authorName = authorName;
        this.createdBy = createdBy;
        this.item = item;
    }
}
