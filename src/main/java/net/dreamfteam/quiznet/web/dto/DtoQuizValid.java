package net.dreamfteam.quiznet.web.dto;

import lombok.*;

import java.util.Date;
import java.util.List;


@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class DtoQuizValid {

    private String id;

    private String title;

    private String description;

    private Date creationDate;

    private String creatorId;

    private String username;

    private String language;

    private String adminComment;

    private byte[] imageContent;

    private boolean published;

    private boolean activated;
}
