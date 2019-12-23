package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hashtags",
    "user_mentions"
})
public class Entities {
  @JsonProperty("hashtags")
  private Hashtag[] hashTag;

  @JsonProperty("user_mentions")
  private UserMention[] userMentions;

  public Hashtag[] getHashTag() {
    return hashTag;
  }

  public void setHashTag(Hashtag[] hashTag) {
    this.hashTag = hashTag;
  }

  public UserMention[] getUserMentions() {
    return userMentions;
  }

  public void setUserMentions(UserMention[] userMentions) {
    this.userMentions = userMentions;
  }

  @Override
  public String toString() {
    return "Entities{" +
        "hashTag=" + Arrays.toString(hashTag) +
        ", userMentions=" + Arrays.toString(userMentions) +
        '}';
  }
}
