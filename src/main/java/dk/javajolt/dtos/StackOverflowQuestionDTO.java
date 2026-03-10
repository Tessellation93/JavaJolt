package dk.javajolt.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StackOverflowQuestionDTO {

    @JsonProperty("question_id")
    private Long questionId;

    private String title;

    private String body;

    private Integer score;

    @JsonProperty("answer_count")
    private Integer answerCount;

    private String link;

    private List<String> tags;

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getAnswerCount() { return answerCount; }
    public void setAnswerCount(Integer answerCount) { this.answerCount = answerCount; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}