package net.dreamfteam.quiznet.data.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;


@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class User {

    private String id;

    private String email;

    private String password;

    private String username;

    private Date creationDate;

    private Date lastTimeOnline;

    private String image;

    private String aboutMe;

    private String recoveryUrl;

    private String activationUrl;

    private Date recoverySentTime;

    private boolean online;

    private boolean activated;

    private boolean verified;

    private Role role;

    private boolean isFriend;

    private boolean outgoingRequest;

    private boolean incomingRequest;
}
